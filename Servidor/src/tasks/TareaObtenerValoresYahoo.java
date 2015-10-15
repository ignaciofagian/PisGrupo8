package tasks;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;

import org.jboss.security.plugins.TmpFilePassword;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import ejb.EjbPaquete;
import ejb.EjbServidor;
import ejb.IEjbPaquete;
import ejb.IEjbServidor;
import model.Accion;
import model.Historico;
import model.JPAProveedorValor;
import sun.util.resources.CalendarData_el_CY;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

@Singleton
@Startup
public class TareaObtenerValoresYahoo {
	
	private String nombreTimer = "EjecucionPedidoValoresYahoo";
	
	@EJB
	private IEjbPaquete ejbPaquete;
	
	@EJB private IEjbServidor ejbServidor;
	
	
	@Resource
	private SessionContext context;

	private long intervaloActualDeEjecucion;
	
    public void stop(String timerName) {

    }
    
    private boolean cargoDatosHistoricos = true;

	@PostConstruct
	public void initialize() 
	{
		System.out.println("**** Desactivando timers anteriores... ****");
	    for(Object obj : context.getTimerService().getTimers()) {
	    	
	        Timer t = (Timer)obj;
	        if (t.getInfo().equals(nombreTimer)) {
	        	System.out.println("**** Se borro timer anterior " + nombreTimer + " ****");
	        	t.cancel();
	        }
	    }
		
		this.intervaloActualDeEjecucion = obtenerPeriodoDeEjecucionDeSolicitud();
	
		crearTimer(5000);
		}
	
	
	private void crearTimer(long proximaEjecucion){
		TimerConfig config = new TimerConfig();
		config.setPersistent(false);
		config.setInfo(nombreTimer);
		Date proxima = new Date(new Date().getTime()  + proximaEjecucion);
		context.getTimerService().createIntervalTimer(proxima, this.intervaloActualDeEjecucion,
				config);
	}

	@Timeout
	public void onEventoTimer(Timer timer) 
	{
		
		//System.out.println("Se ejecuta actualizacion de acciones Yahoo!");
		pedidoDeValoresYahoo();
		long valorPeriodoProperties = obtenerPeriodoDeEjecucionDeSolicitud();
		//System.out.println("este es el valor del timer: ->>" + valorPeriodoProperties);
		if (valorPeriodoProperties != this.intervaloActualDeEjecucion) {
			// Cambiaron el tiempo de ejecucion
			timer.cancel();
			this.intervaloActualDeEjecucion = valorPeriodoProperties;

			crearTimer(this.intervaloActualDeEjecucion);
		}
	}

	
	
	public void pedidoDeValoresYahoo() {
		boolean simularSiCerrado = true;
		List<Accion> accionesDelSistema = ejbPaquete.obtenerConjuntoAccionesUsadasPorPaquetes();
		int minutos = 0;
		if (!ejbServidor.estaAbiertoStockExchangeAhora() && simularSiCerrado) {
			minutos = Calendar.getInstance().get(Calendar.MINUTE);

		}
		// La idea es el ultimo de dato obtenido por yahoo se actualizan en la
		// tabla de Accion
		// El valor anterior que tenia la tabla Accion, se pasa para Historico
		// con fecha = fechaActual - TiempoDeTimer

		List<String> symbols = new ArrayList<String>();
		Map<String, Accion> mapAcciones = new HashMap<String, Accion>();
		for (Accion acc : accionesDelSistema) {
			symbols.add(acc.getSymbol());
			mapAcciones.put(acc.getSymbol(), acc);
		}

		// Obtengo el valor actual de Yahoo en batchs de 200 elementos
		Map<String, Stock> yahooStocks = new HashMap<String, Stock>();
		try {
			for (int i = 0; i < symbols.size(); i += 200) {
				int tope = Math.min(i + 200, symbols.size());
				List<String> sublista = symbols.subList(i, tope);
				String[] pedido = new String[sublista.size()];
				sublista.toArray(pedido);
				Map<String, Stock> respuesta;
				if (cargoDatosHistoricos) {
					Calendar from = new GregorianCalendar(2015, Calendar.OCTOBER, 7);
					respuesta = YahooFinance.get(pedido, from, Interval.DAILY);
				} else {
					respuesta = YahooFinance.get(pedido);
				}
				yahooStocks.putAll(respuesta);
			}

		} catch (IOException e) {
			System.out.println("Error: Al obtener datos de yahoo");
			e.printStackTrace();
		}
		double simulacion = 1 + (minutos % 60) / 50.0; // 100% - 220% del valor
														// real
		System.out.println("Simulacion x" + simulacion);

		for (String symbol : yahooStocks.keySet()) {
			// Dejo que el ejb se encarge de tocar la BD
			Accion acc = mapAcciones.get(symbol);
			Stock stock = yahooStocks.get(symbol);
			List<Historico> antiguos = new ArrayList<Historico>();
			if (cargoDatosHistoricos) {
				try {
					for (HistoricalQuote hq : stock.getHistory()) {
						Historico h = new Historico(acc, hq.getAdjClose().doubleValue(), hq.getClose().doubleValue(),
								hq.getDate().getTime());
						antiguos.add(h);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			ejbPaquete.actualizarAccionDesdeYahooYAgregarHistorico(acc,
					stock.getQuote().getPrice().doubleValue() * simulacion, antiguos);

		}

		ejbPaquete.actualizarValorPaquetesAlgoritmicos();
		ejbServidor.actualizarSaldosAll();
		cargoDatosHistoricos = false;
	}
	
	public long obtenerPeriodoDeEjecucionDeSolicitud() {
		Properties props = new Properties();
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("servidor.properties");

		if (inputStream == null) 
		{
		//	System.out.println("Error al leer periodo de ejecucion de PROPERTIES");
		} else 
		{
			try 
			{
				props.load(inputStream);
				String peridoString = props.getProperty("periodoActualizacionYahoo");
				return Long.parseLong(peridoString);
				
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		return 60000 * 5; // 5 minutos por defecto si falla la lectura dle archivo.. no deberia suceder
	}
	
	

		
		
	

}

package tasks;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jboss.security.plugins.TmpFilePassword;


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
	@Resource
	private SessionContext context;

	@EJB
	private IEjbPaquete ejbPaquete;
	
	@EJB private IEjbServidor ejbServidor;
	
	@PersistenceContext(unitName = "Servidor")
	private EntityManager em;
	
	
	private final String nombreTimer = "EjecucionPedidoValoresYahoo";
	
	private Logger log = LogManager.getLogger(nombreTimer);
	private boolean corriendo ;

	private long intervaloActualDeEjecucion;
	private static Calendar fechaDatosHistoricos = new GregorianCalendar(2010, Calendar.JANUARY,1);
	
	
	
    public static Calendar getFechaDatosHistoricos() {
		return fechaDatosHistoricos;
	}



	public void stop(String timerName) {

    }
    
  

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
		corriendo = false;
		this.intervaloActualDeEjecucion = obtenerPeriodoDeEjecucionDeSolicitud();
		this.fechaDatosHistoricos = obtenerFechaDatosHistoricos();
	
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
		if (!corriendo){
			corriendo = true;
			try {
				log.info("Se ejecuta actualizacion de acciones Yahoo!");
				ejbPaquete.pedidoDeValoresYahoo(true,fechaDatosHistoricos);
				long valorPeriodoProperties = obtenerPeriodoDeEjecucionDeSolicitud();
				//System.out.println("este es el valor del timer: ->>" + valorPeriodoProperties);
				if (valorPeriodoProperties != this.intervaloActualDeEjecucion) {
					// Cambiaron el tiempo de ejecucion
					timer.cancel();
					this.intervaloActualDeEjecucion = valorPeriodoProperties;

					crearTimer(this.intervaloActualDeEjecucion);
				} 
			} finally {
				corriendo = false;
			}
		}
		else{
			log.warn("Todavia esta corriendo la tarea anterior!!!");
		}
		
	}

	private Set<String> obtenerAccionesHistorico(Calendar desde){
		// mejorar, asumo que si no hay datos entre la fecha de inicio y 4 disas depsues tengo que pedirlo
		Calendar hasta = (Calendar)desde.clone();
		hasta.add(Calendar.DAY_OF_YEAR, 4);
		return ejbPaquete.symAccionesEnHistorico(desde, hasta);
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
	
	
	private Calendar obtenerFechaDatosHistoricos() {
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
				String peridoString = props.getProperty("fechaInicioDatosHistoricos");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date d = sdf.parse(peridoString);
				GregorianCalendar cal = new GregorianCalendar();
				cal.setTime(d);
				return  cal;
				
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		return new GregorianCalendar(2010,Calendar.JANUARY,4);
	}


		
		
	

}

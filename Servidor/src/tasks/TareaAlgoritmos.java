package tasks;


import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import ejb.IEjbAlgoritmo;
import ejb.IEjbCalculoSaldoHist;
import ejb.IEjbCliente;
import ejb.IEjbPaquete;
import interprete.IInterpreter;
import interprete.InterpreterController;
import model.ClientePaquete;
import model.PaqueteAlgoritmico;

@Singleton
@Startup
public class TareaAlgoritmos {
	
	private String nombreTimer  ="EjecucionAlgoritmos";

	@EJB
	private IEjbPaquete ejbPaquete;

	@EJB
	private IEjbCliente ejbCliente;
	
	@EJB
	private IEjbCalculoSaldoHist ejbSH;
	

	@EJB
	private IEjbAlgoritmo ejbAlgoritmo;
	
	@Resource
	private SessionContext context;

	private long intervaloActualDeEjecucion;

	@PostConstruct
	public void initialize() {
		System.out.println("**** Desactivando timers anteriores... ****");
	    for(Object obj : context.getTimerService().getTimers()) {
	        Timer t = (Timer)obj;
	        if (t.getInfo().equals(nombreTimer)) {
	        	System.out.println("**** Se borro timer anterior " + nombreTimer + " ****");
	        	t.cancel();
	        }
	    }

		this.intervaloActualDeEjecucion = obtenerPeriodoDeEjecucionAlgoritmos();
		crearTimer(20000);
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
	public void onEventoTimer(Timer timer) {
		ejecutarAlgoritmos();
		long valorPeriodoProperties = obtenerPeriodoDeEjecucionAlgoritmos();
		if (valorPeriodoProperties != this.intervaloActualDeEjecucion) {
		
			// Cambiaron el tiempo de ejecucion
			timer.cancel();
		    this.intervaloActualDeEjecucion = valorPeriodoProperties;
		    crearTimer( this.intervaloActualDeEjecucion );
		    }
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void ejecutarAlgoritmos() {
		List<PaqueteAlgoritmico> paquetes = ejbPaquete.obtenerPaquetesAlgoritmicos();
		System.out.println("Timer: Ejecutando algoritmos");
		for (PaqueteAlgoritmico pa : paquetes) {
			long idPaquete = pa.getId();
		    String algorithm = pa.getAlgoritmo();
		    if (pa.isRecalcular()){
		    	ejbSH.crearHistoriaDiariaAlgoritmo(pa);
		    	ejbSH.calcularEvolucionAlgoritmo(pa);
		    }
		    
		    IInterpreter inter = new InterpreterController();
		    inter.setEJBAlgoritmo(ejbAlgoritmo);
		    try{
			    inter.evaluate(idPaquete, algorithm);
		    }
		    catch(Exception e){
		    	
			    System.out.println("TareaAlgo " + e.getMessage());
			    System.out.println("TareaAlgo " +"Error en algoritmo " + pa.getNombre() + " algo: " + pa.getAlgoritmo());
			    
		  }	
		}
		
	}

	public void Algoritmo1(PaqueteAlgoritmico pa) {
		//pegunto si el paquete tuvo una variacion de +10% con respecto a ayer
		//  ===> Vendo 50 
		//pregunto si el paquete tuvo una variacion de -5% con respecto a 2 dias anteriores
		// ===> compro 40
		Calendar fechaActual = Calendar.getInstance();
		Calendar fechaDiaAnterior = Calendar.getInstance();
		Calendar fechaDosDiasAnterior = Calendar.getInstance();
		
		fechaDiaAnterior.add(Calendar.DAY_OF_YEAR, -1);
		fechaDosDiasAnterior.add(Calendar.DAY_OF_YEAR, -2);
		
		boolean deboVender50 =  ejbPaquete.obtenerVariacionPaquete(fechaDiaAnterior, 
				fechaActual, pa) >= 1.1;
	
		boolean deboComprar40 =  ejbPaquete.obtenerVariacionPaquete(fechaDiaAnterior, 
				fechaActual, pa) <= 0.95;		
		
		if (deboVender50){
			pa.vender(50);
		}
		if(deboComprar40){
			pa.comprar(40);
		}
		
		
		
		
		//ACA EXPLOTAAAAAAA
		/*List<ClientePaquete> clientesPaquetes = ejbCliente.obtenerClientesConPaqueteAlgoritmico(pa.getId());
		
		for (ClientePaquete cp : clientesPaquetes )
		{
			
			//Pregunto si se cumple la condicion variacion de +10%
			if (deboVender50)
			{
				//Controlo si tiene menos de 50 invertido vendo lo que tenga
				double cantidadVendido = Math.min(50, cp.getInversion());
				
				cp.setInversion(cp.getInversion() - cantidadVendido);
				cp.setSaldoDisponible(cp.getSaldoDisponible() + cantidadVendido);
			}
			
			if (deboComprar40)	
			{
				//Controlo si tiene menos de 40 disponible sino compro lo que tenga
				double cantdadAComprar = Math.min(cp.getSaldoDisponible(), 40);
				
				cp.setInversion(cp.getInversion() + cantdadAComprar);
				cp.setSaldoDisponible(cp.getSaldoDisponible() - cantdadAComprar);
			}
		}*/
	}

	public void Algoritmo2(PaqueteAlgoritmico pa) {
		System.out.println("Se ejecuto Algoritmo 2");
	}

	public long obtenerPeriodoDeEjecucionAlgoritmos() {
		Properties props = new Properties();
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("servidor.properties");

		if (inputStream == null) {
			System.out.println("Error al leer periodo de ejecucion de PROPERTIES");
		} else {
			try {
				props.load(inputStream);
				String peridoString = props.getProperty("periodoEejecucionAlgoritmos");
				return Long.parseLong(peridoString);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return 60000 * 5; // 5 minutos por defecto si falla la lectura dle archivo.. no deberia suceder
	}
}

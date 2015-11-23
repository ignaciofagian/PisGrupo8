package tasks;

import java.util.Calendar;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
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
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import model.Portafolio;

@Singleton
public class TareaDesactivarInactivos {
	private final String nombreTimer = "DesactivarInactivos";
	private Logger log = LogManager.getLogger(nombreTimer);
	private long intervaloActualDeEjecucion;
	private int diasLimite = 7;
	
	@PersistenceContext(unitName = "Servidor")
	private EntityManager em;
	
	@Resource
	private SessionContext context;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void desactivarClientesInactivos(){
		Calendar limite = Calendar.getInstance();
		limite.add(Calendar.DATE, -1 * diasLimite);
		TypedQuery<Portafolio> q = em.createQuery("Select p from Portafolio p Where p.ultimoAcceso < :lim",Portafolio.class);
		q.setParameter("lim", limite,TemporalType.TIMESTAMP);
		for(Portafolio p : q.getResultList()){
			if (p.getUltimoAcceso() != null) {
				p.setActivo(false);
				log.info("Se desactiva idPort="+ p.getId() + " ua=" + p.getUltimoAcceso().getTime().toString());
			}
		}
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
		
		this.intervaloActualDeEjecucion = obtenerPeriodoDeEjecucionDeSolicitud();
		this.diasLimite = obtenerDesactivarLimite();
	
		crearTimer(60000);
		}
	
	private void crearTimer(long proximaEjecucion){
		TimerConfig config = new TimerConfig();
		config.setPersistent(false);
		config.setInfo(nombreTimer);
		Date proxima = new Date(new Date().getTime()  + proximaEjecucion);
		context.getTimerService().createIntervalTimer(proxima, this.intervaloActualDeEjecucion,
				config);
	}

	
	public long obtenerPeriodoDeEjecucionDeSolicitud() {
		Properties props = new Properties();
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("servidor.properties");

		if (inputStream != null) 
		{
			try 
			{
				props.load(inputStream);
				String periodoString = props.getProperty("periodoDesactivarUsuarios");
				return Long.parseLong(periodoString);
				
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		return 7 * 24 * 3600 * 1000L;
	}
	
	private int obtenerDesactivarLimite() {
		Properties props = new Properties();
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("servidor.properties");

		if (inputStream != null) 
		{
			try 
			{
				props.load(inputStream);
				String peridoString = props.getProperty("limiteDias");
				return Integer.parseInt(peridoString);
			} catch (Exception e) { }
		}
		return 7;
	}
	
	@Timeout
	public void onEventoTimer(Timer timer) 
	{
		try {
			log.info("Se desactivan clientes inactivos");
			 desactivarClientesInactivos();
			long valorPeriodoProperties = obtenerPeriodoDeEjecucionDeSolicitud();
			if (valorPeriodoProperties != this.intervaloActualDeEjecucion) {
				// Cambiaron el tiempo de ejecucion
				timer.cancel();
				this.intervaloActualDeEjecucion = valorPeriodoProperties;
				crearTimer(this.intervaloActualDeEjecucion);
			} 
		}
		catch(Exception ex){
			
		}
	}

		
	
	
}

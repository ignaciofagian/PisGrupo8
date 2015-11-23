package ejb;



import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import interprete.Category;
import log.LogConfig;
import model.Accion;
import model.Cliente;
import model.ClientePaquete;
import model.Historico;
import model.JPAProveedorValor;
import model.Paquete;
import model.PaqueteAccion;
import model.PaqueteAlgoritmico;
import model.PaqueteCategoria;
import model.PaqueteIndice;
import model.Portafolio;
import model.PreguntaEspecifica;
import model.PreguntaGeneral;
import model.RespuestaGeneral;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

@Startup
@Singleton
public class EjbInit implements IEjbPrueba {

	@PersistenceContext(unitName = "Servidor")
	private EntityManager em;	
	
	@EJB
	private IEjbCliente ejbCliente;
	
	@EJB
	private IEjbServidor ejbServidor;
	
	
	@EJB
	private IEjbPaquete ejbPaquete;
	
	
	
	/*public void paquete3grandes(){
		//http://www.thestreet.com/story/13165126/2/3-high-yield-low-risk-dividend-stocks-for-extreme-safety.html
		String[] symbols = new String[]{
				"PEP",// PepsiCo 
				"JNJ", //Johnson & Johnson
				"KMB" // Kimberly-Clark aka Huggies
		};
		try {
			long id3 = 10003;
			if (em.find(Paquete.class, id3) == null){
			
			Accion pepsi = cargarAccion("PEP");
			Accion jnj = cargarAccion("JNJ");
			Accion kmb = cargarAccion("KMB");
			Paquete paq = new PaqueteIndice();
			paq.agregarAccion(pepsi, 15);
			paq.agregarAccion(jnj, 25);
			paq.agregarAccion(kmb, 15);
			paq.setId(id3);
			
			em.persist(paq);
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	private void reiniciarClaveSerialDB(String tabla,String columna){
		// magia
		// http://stackoverflow.com/questions/244243/how-to-reset-postgres-primary-key-sequence-when-it-falls-out-of-sync
		Query nativeQ = em.createNativeQuery("SELECT setval(pg_get_serial_sequence(:tabla, :col), coalesce(max("+columna+"),0) + 1, false) FROM " + tabla + ";");
		nativeQ.setParameter("tabla", tabla);
		nativeQ.setParameter("col",columna);
		nativeQ.getResultList();
	}
	
	private PaqueteCategoria categoriaDesdeBD(String categoria){
		int intentos = 0;
		PaqueteCategoria c = null;
		while (intentos < 2){
			try {
				List<PaqueteCategoria> lst = em
						.createQuery("Select c from PaqueteCategoria c Where c.categoria = :c", PaqueteCategoria.class)
						.setParameter("c", categoria).getResultList();
				if (lst.isEmpty()) {
					c = new PaqueteCategoria();
					c.setCategoria(categoria);
					c.setDescripcion(categoria);
					em.persist(c);
					System.out.println("Categoria creada con id="+c.getId() + " : " + categoria);
				} else {
					c = lst.get(0);
				} 
				intentos = Integer.MAX_VALUE;
			} catch (Exception e) {
				System.out.println("ERROR al crear la categoria: " + categoria);
				reiniciarClaveSerialDB("paquetecategoria","idpaqcategoria");
				intentos++;
			}
		}
		return c;
	}

	@SuppressWarnings("unchecked")	
	private void crearPreguntasGenerales()
	{
		//Paquetes
		//    Dow Jones  (bajoRiesgo)
		//    Nasdaq Composite (bajoRiesgo)
		//    Penny Stocks (altoRiesgo)
		//    Health Stocks (altoRiesgo)
		//    Automobile Stocks (estarAtento, altoRiesgo)
		
		
		List<PreguntaGeneral> res = em.createQuery("Select preg from PreguntaGeneral preg").getResultList();
		if (!res.isEmpty()) return;
		
		PreguntaGeneral pregGeneral1 = new PreguntaGeneral();
		PreguntaGeneral pregGeneral2 = new PreguntaGeneral();
		
		pregGeneral1.setSentenciaESP("G1  ¿Está interesado en el riesgo?");
		pregGeneral1.setSentenciaENG("G1  Are you a risk taker?");
		pregGeneral1.agregarRespuestaGeneral("Yes", "Sí", altoRiesgo());
		pregGeneral1.agregarRespuestaGeneral("No", "No", bajoRiesgo());
		
		pregGeneral2.setSentenciaESP("G2  ¿Considera que estará atento a la aplicación?");
		pregGeneral2.setSentenciaENG("G2  Are you going to pay attention to this app?");
		pregGeneral2.agregarRespuestaGeneral("Yes", "Sí", estarAtento());
		pregGeneral2.agregarRespuestaGeneral("No", "No", bajoRiesgo());
		
		em.persist(pregGeneral1);
		em.persist(pregGeneral2);
		
		System.out.println(" **** Preguntas generales creadas ****");
	}
		
	private PaqueteCategoria altoRiesgo(){
		return categoriaDesdeBD("Risky");
	}
	
	private PaqueteCategoria bajoRiesgo(){
		return categoriaDesdeBD("LowRisk");
	}
	
	private PaqueteCategoria estarAtento()
	{
		return categoriaDesdeBD("RequiresAttention");
	}
	
	@Override
	public Accion buscarCrearAccion(String symbol){
		List<Accion> list = em.createQuery("Select a from Accion a Where a.symbol = :s",Accion.class).setParameter("s", symbol).getResultList();
		if (list.isEmpty()){
			Stock s;
			try {
				s = YahooFinance.get(symbol);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			Accion a = new Accion();
			a.setSymbol(s.getSymbol());
			a.setNombre(s.getName());
			a.setValorActual(s.getQuote().getPrice().doubleValue());
			em.persist(a);
			em.flush();
			return a;
		}
		else{
			return list.get(0);
		}
	}
	
	public void crearSP500(){
		String nombre = "S&P500";
		if (existePaquetePorNombre(nombre)) return;
		
		Accion dj = buscarCrearAccion("^GSPC");
		PreguntaEspecifica preg = new PreguntaEspecifica();
		preg.setSentenciaESP("¿Cuánto cree en las grandes empresas?");
		preg.setSentenciaENG("How much do you believe in large companies?");
		preg.agregarRespuestaEspecifica(10, "Much", "Mucho");
		preg.agregarRespuestaEspecifica(5, "Little", "Poco");
		preg.agregarRespuestaEspecifica(0, "None", "Nada");
		
		PaqueteIndice dow = new PaqueteIndice();
		preg.setPaquete(dow);
		dow.setNombre(nombre);
		dow.agregarAccion(dj, 100);
		dow.addCategorias(bajoRiesgo());
		dow.getPreguntaEspecificas().add(preg);
		dow.validar();
		em.persist(dow);
		em.flush();
		System.out.println(" **** Paquete " + nombre + " creado ****");
	}
	
	public void crearNasdaq(){
		
		String nombre = "Nasdaq Composite";
		if (existePaquetePorNombre(nombre)) return;
		
		Accion ixic = buscarCrearAccion("^IXIC");
		PreguntaEspecifica preg = new PreguntaEspecifica();
		preg.setSentenciaESP("¿Qué pasará con el sector de la tecnología?");
		preg.setSentenciaENG("What will happen with the tech sector?");
		preg.agregarRespuestaEspecifica(10, "It will improve", "Mejorará");
		preg.agregarRespuestaEspecifica(2, "It will remain stable", "Se mantendrá estable");
		preg.agregarRespuestaEspecifica(0, "It will decrease", "Bajará");
		
		PaqueteIndice nasd = new PaqueteIndice();
		preg.setPaquete(nasd);
		nasd.setNombre(nombre);
		nasd.agregarAccion(ixic, 100);
		nasd.addCategorias(bajoRiesgo());
		nasd.getPreguntaEspecificas().add(preg);
		nasd.validar();
		em.persist(nasd);
		em.flush();
		System.out.println(" **** Paquete " + nombre + " creado ****");
	}
	
	
	public void crearPaqueteAlgoritmoSalud()
	{
		String nombre = "Health Stocks";
		if (existePaquetePorNombre(nombre)) return;
		
		Accion accionHNT = buscarCrearAccion("HNT");
		Accion accionMOH = buscarCrearAccion("MOH");
		Accion accionWCG = buscarCrearAccion("WCG");
		
		PreguntaEspecifica preg = new PreguntaEspecifica();
		preg.setSentenciaESP("¿Cuándo esperas que el avance de la tecnología tenga un impacto importante en la medicina?");
		preg.setSentenciaENG("When do you expect the advance of technology to have a major impact on medicine?");
		preg.agregarRespuestaEspecifica(10, "Soon", "Pronto");
		preg.agregarRespuestaEspecifica(5, "in 10 years", "en 10 años");
		preg.agregarRespuestaEspecifica(1, "we are far from it", "estamos lejos de eso");
		
		PaqueteAlgoritmico paqueteHealth = new PaqueteAlgoritmico();
		preg.setPaquete(paqueteHealth);
		paqueteHealth.setNombre("Health Stocks");
		paqueteHealth.setAlgoritmo("BUY(10)");
		paqueteHealth.agregarAccion(accionHNT, 40);
		paqueteHealth.agregarAccion(accionMOH, 35);
		paqueteHealth.agregarAccion(accionWCG, 25);
		paqueteHealth.getPreguntaEspecificas().add(preg);
		paqueteHealth.addCategorias(altoRiesgo());
		paqueteHealth.setRecalcular(true);
		paqueteHealth.validar();
		
		em.persist(paqueteHealth);
		em.flush();
		System.out.println(" **** Paquete " + nombre + " creado ****");
		
	}
	
	public void crearPaqueteAlgoritmoAutomotriz()
	{
		String nombre = "Automobile Stocks";
		if (existePaquetePorNombre(nombre)) return;
		
		Accion accionTSLA = buscarCrearAccion("TSLA");
		Accion accionFORD = buscarCrearAccion("F");
		
		PreguntaEspecifica preg = new PreguntaEspecifica();
		preg.setSentenciaESP("¿Piensa que el futuro de la industria automotriz se encuentra en los autos electricos?");
		preg.setSentenciaENG("Do you think the future of the auto industry lies on electric cars?");
		preg.agregarRespuestaEspecifica(8, "Yes", "Sí");
		preg.agregarRespuestaEspecifica(5, "Maybe", "Tal vez");
		preg.agregarRespuestaEspecifica(2, "No", "No");
		
		PaqueteAlgoritmico paqueteAutomobile = new PaqueteAlgoritmico();
		preg.setPaquete(paqueteAutomobile);
		paqueteAutomobile.setNombre("Automobile Stocks");
		paqueteAutomobile.setAlgoritmo("BUY(10)");
		paqueteAutomobile.agregarAccion(accionTSLA, 75);
		paqueteAutomobile.agregarAccion(accionFORD, 25);
		
		paqueteAutomobile.getPreguntaEspecificas().add(preg);
		paqueteAutomobile.addCategorias(estarAtento());
		paqueteAutomobile.addCategorias(altoRiesgo());
		paqueteAutomobile.setRecalcular(true);
		paqueteAutomobile.validar();
		
		em.persist(paqueteAutomobile);
		em.flush();
		
		System.out.println(" **** Paquete " + nombre + " creado ****");
	}
	
	@SuppressWarnings("rawtypes")
	public boolean existePaquetePorNombre(String nombre){
		List res = em.createQuery("Select p.id from Paquete p Where p.nombre = :n").setParameter("n", nombre).setMaxResults(1).getResultList();
		if (!res.isEmpty()) System.out.println("*** Se encontro " + nombre + "! ***");
		return !res.isEmpty();
	
	}
	
	
	final static String nombre_penny =  "Penny Stocks";
	final static String nombre_nasdaq = "Nasdaq Composite";
	
	public void crearPennyStock(){
		

			String nombre = nombre_penny;
			if (existePaquetePorNombre(nombre)) return;
			
			Accion siri =  buscarCrearAccion("SIRI"); // Sirius XM Radio Inc. (SIRI)
			Accion amd =  buscarCrearAccion("AMD");// la gran competencia de Intel y creadora de la arquitectura x64 reducida a un Penny Stock... todo por culpa del core i7
			Accion grpn =  buscarCrearAccion("GRPN"); //  Groupon Inc. 
			Accion plug =  buscarCrearAccion("PLUG");//Plug Power Inc. (PLUG)
			Accion xoma =  buscarCrearAccion("XOMA"); // XOMA Corporation
			Accion ocle =  buscarCrearAccion("OCLR");//Oclaro Inc (OCLR)
			PaqueteIndice penny = new PaqueteIndice();
			penny.setNombre("Penny Stocks");
				
			penny.agregarAccion(siri, 15);
			penny.agregarAccion(amd, 25);
			penny.agregarAccion(grpn, 15);
			penny.agregarAccion(plug, 15);
			penny.agregarAccion(xoma, 15);
			penny.agregarAccion(ocle,15);	

			PreguntaEspecifica preg = new PreguntaEspecifica();
			preg.setSentenciaENG("Do you believe that small companies will prosper?");
			preg.setSentenciaESP("¿Cree que las empresas pequeñas mejorarán?");
			preg.agregarRespuestaEspecifica(10, "Much", "Mucho");
			preg.agregarRespuestaEspecifica(5, "A little", "Un poco");
			preg.agregarRespuestaEspecifica(0, "They won't", "No lo harán");
			preg.setPaquete(penny);

			// establecer condiciones
			penny.addCategorias(altoRiesgo());
			penny.addCategorias(estarAtento());
			penny.getPreguntaEspecificas().add(preg);
			penny.validar();    
			em.persist(penny);
			em.flush();
			System.out.println(" **** Paquete " + nombre + " creado ****");
		
	}

	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override
	public void borrarClientesPrueba(){
		// borro los clientes anteriores
				String identificador = "prueba_volumen_";
				TypedQuery<Cliente> q = em.createQuery("Select c from Cliente c Where c.idTelefono LIKE :id", Cliente.class);
				q.setParameter("id", identificador + "%" );
				for(Cliente c : q.getResultList()){
					ejbCliente.borrarSaldoHistorico(c.getPortafolio());
					em.remove(c);
				}
				em.flush();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void crearClientesPrueba(int numero){
		String identificador = "prueba_volumen_";
		Paquete nasdaq = ejbPaquete.paquetePorNombre(nombre_nasdaq);
		Paquete penny = ejbPaquete.paquetePorNombre(nombre_penny);
		
		// creo los clientes nuevos
		for(int i = 1; i <= numero; i++){
			Cliente nuevoCliente = new Cliente();
			nuevoCliente.setIdTelefono(identificador + i);
			nuevoCliente.getPortafolio().asignarSaldoInicial();
			nuevoCliente.getPortafolio().agregarPaquete(penny, 50);
			nuevoCliente.getPortafolio().agregarPaquete(nasdaq, 100);
			em.persist(nuevoCliente);
		}
	}

	

		
    @PostConstruct
    public void initialize() {
    	YahooFinance.logger.setLevel(Level.OFF);
		LogConfig.getInstance().mostrarLogInfo();


		try{
			crearPreguntasGenerales();
	    	System.out.println(" **** Creando paquetes por defecto... " + "****");
	    	crearSP500();
	    	crearNasdaq();
	    	crearPennyStock();
	    	//crearPaqueteAlgoritmoAutomotriz();
	    	//crearPaqueteAlgoritmoSalud();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
    	

    }
    
    private void BorrarDatosSimulados(Date inicio,Date fin){
    	TypedQuery<Historico> q  = em.createQuery("Select h from Historico h Where h.fecha BETWEEN :ini AND :fin",Historico.class);
    	q.setParameter("ini",inicio,TemporalType.TIMESTAMP);
    	q.setParameter("fin",fin,TemporalType.TIMESTAMP);
    	Calendar c = new GregorianCalendar();
    	for(Historico h : q.getResultList()){
    		c.setTime(h.getFecha());
    		if (ejbServidor.estaAbiertoStockExchange(c)){
    			em.remove(h);
    		}
    	}
    }
    
    @PreDestroy
    public void preDestroy(){
    	LogConfig.getInstance().shutdown(); // escribe los logs que hayan quedado en el buffer
    }

}


package ejb;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import auxiliar.CacheHistPorAccion;
import auxiliar.CacheHistPorFecha2;
import auxiliar.CacheTreeMap;
import auxiliar.ProvFromMap;
import datatypes.DTEstado;
import datatypes.DTSaldo;
import interprete.IInterpreter;
import interprete.InterpreterController;
import model.Cliente;
import model.ClientePaquete;
import model.Historico;
import model.JPAProveedorValor;
import model.Paquete;
import model.PaqueteAlgoritmico;
import model.Portafolio;
import model.SaldoHistorico;

@Stateless
public class EjbCalculoSaldo implements IEjbCalculoSaldoHist {
	SimpleDateFormat ymd =  new SimpleDateFormat("yyyy-MM-dd");
	
	@EJB
	private IEjbServidor ejbServidor;
	
	@EJB
	private IEjbPaquete ejbPaquete;
	
	@EJB
	private IEjbCliente ejbCliente;
	
	@EJB
	private IEjbAlgoritmo ejbAlgoritmo;
	
	@PersistenceContext(unitName = "Servidor")
	private EntityManager em;
	
	// inicio de intervalo para tiempos  de tiempo por dia
	Calendar fin_solo_historico = new GregorianCalendar(2015,Calendar.OCTOBER,1);

	
	public Cliente obtenerCliente(String idCliente){
		try{
		Query q1 = em.createQuery("select c from Cliente c where c.idTelefono=:id");
		q1.setParameter("id", idCliente);
		return (Cliente)q1.getSingleResult();
		}
		catch(NoResultException ex){
			System.out.println("ejbCalc Error no se encontro cliente con iden=" + idCliente);
			return null;
		}
	}
    

	/* (non-Javadoc)
	 * @see ejb.IEjbCalculoSaldoHist#pruebaDatosHistoricos(java.lang.String)
	 */
	@Override
    public void pruebaDatosHistoricos(String id){
    	
    	ejbCliente.borrarCliente(id);
    	ejbCliente.registrarUsuario(id);
    	em.flush();
    	Cliente c = obtenerCliente(id);
    	Portafolio p = c.getPortafolio();
    	p.setSinInvertir(200);
    	em.flush();
    	JPAProveedorValor val = new JPAProveedorValor(em);
    	//Paquete nasdaq = ejbPaquete.paquetePorNombre("Nasdaq Composite");
    	//Paquete penny = ejbPaquete.paquetePorNombre("Penny Stocks");
    	Paquete winners = ejbPaquete.paquetePorNombre("Winners");
    	Calendar inicio = new GregorianCalendar(2014,Calendar.JANUARY,7);
    	p.setUltimaFechaSaldo(inicio);
    	p.agregarPaquete(winners, 100,val);
    	//p.agregarPaquete(penny, 50,val);
    	//ClientePaquete.generar(c, penny, 100);
    	
    	Calendar fin = new GregorianCalendar(2015, Calendar.OCTOBER, 28);
    	em.flush();
    	calcularSaldoHistoricoContinuo(inicio, fin, p);
    }
	
	
	
	public void calcularSaldoHistoricoContinuo(Calendar desde,Calendar hasta, Portafolio p){
		long nano1 = System.nanoTime();
		Calendar ufs = p.getUltimaFechaSaldo(); 
		ArrayList<Long> acciones = new ArrayList<Long>(p.idAccionesEnPortafolio());
		System.out.println((( System.nanoTime() - nano1) / 1000000) + " ms");
		List<Historico> res = ejbServidor.obtenerHistorico(desde, hasta, acciones);
		CacheHistPorFecha2 cache = new CacheHistPorFecha2();
		cache.setFallback(new JPAProveedorValor(em));
		cache.setearHistoricos(res, true);
		if (!cache.getFechas().isEmpty()){
		p.setUltimaFechaSaldo(cache.getFechas().get(0));
		System.out.println((( System.nanoTime() - nano1) / 1000000) + " ms");
		for(Calendar c : cache.getFechas()){
			try{
				em.persist(p.calcularSaldo(c, cache));
			}
			catch(Exception ex){
				System.out.println(ex.getMessage());
				System.out.println("Date=" + c.getTime().toString());
			}
			
		}
		p.setUltimaFechaSaldo(ufs);
		System.out.println((( System.nanoTime() - nano1) / 1000000) + " ms");
		em.flush();
		}
	
		System.out.println((( System.nanoTime() - nano1) / 1000000) + " ms");
	}
	
	/**
	 * 
	 * @param p
	 * @param list_h Lista de historicos ordenada ASC 
	 */
	public boolean calcularSaldoHistorico(Portafolio p,List<Historico> list_h){
		long nano1 = System.nanoTime();
		boolean perdio = false;
		System.out.println((( System.nanoTime() - nano1) / 1000000) + " ms");
		//List<Historico> res = ejbServidor.obtenerHistorico(desde, hasta, acciones);
		CacheHistPorFecha2 cache = new CacheHistPorFecha2();
		cache.setFallback(new JPAProveedorValor(em));
		cache.setearHistoricos(list_h, true);
		if (!cache.getFechas().isEmpty()){
			p.setUltimaFechaSaldo(cache.getFechas().get(0));
			System.out.println((( System.nanoTime() - nano1) / 1000000) + " ms");
			for(Calendar c : cache.getFechas()){
				try{
					em.persist(p.calcularSaldo(c, cache));
					perdio = p.isLost() || p.getUltimoSaldo() <= 0;
					p.setLost(perdio);
					// no necesito seguir calculando el saldo si perdio
					if (perdio) break; 
				}
				catch(Exception ex){
					System.out.println(ex.getMessage());
					System.out.println("Date=" + c.getTime().toString());
				}
				
		}
		System.out.println((( System.nanoTime() - nano1) / 1000000) + " ms");
		
		}
		em.flush();
		System.out.println((( System.nanoTime() - nano1) / 1000000) + " ms");
		return perdio;
	}


	
	enum Resolucion {DIA,MIN_20,MAX,BAJA,MIN};
	private class RangoResolucion{
		Resolucion resolucion;
		Calendar inicio;
		Calendar fin;
		public RangoResolucion(Resolucion resolucion, Calendar inicio, Calendar fin) {
			super();
			this.resolucion = resolucion;
			this.inicio = inicio;
			this.fin = fin;
		}
		
	}
	
	
	
	private Calendar max(Calendar t1,Calendar t2){
		return t2.after(t1) ? t2 : t1;
	}
	
	private Calendar min(Calendar t1,Calendar t2){
		return t2.before(t1) ? t2 : t1;
	}
	
	public List<RangoResolucion> obtenerResoluciones(Calendar inicio,Calendar fin){
		if (fin.before(inicio)){
			System.out.println("obtenerResoluciones fin < inicio");
			return new ArrayList<EjbCalculoSaldo.RangoResolucion>();
		}
		
		// copia defensiva
		inicio = (Calendar)inicio.clone();
		fin = (Calendar)fin.clone();
		
		// inicio de intervalo para tiempos de a 2 minutos
		int diaSem = fin.get(Calendar.DAY_OF_WEEK);
		int diasQuitar = 1;
		if (diaSem == Calendar.SUNDAY ){
			 diasQuitar = 2; // desde el inicio del viernes
		}
		Calendar tiempoReal_ini = new GregorianCalendar(fin.get(Calendar.YEAR),fin.get(Calendar.MONTH),fin.get(Calendar.DATE));
		tiempoReal_ini.add(Calendar.DATE, -diasQuitar); 
		
		// inicio de intervalo para tiempos de a 20 minutos
		Calendar min_20_ini = new GregorianCalendar(fin.get(Calendar.YEAR),fin.get(Calendar.MONTH),fin.get(Calendar.DATE));
		min_20_ini.add(Calendar.DATE, -7); // una semana
		
		Calendar min_diario = (Calendar)min_20_ini.clone();
		min_diario.add(Calendar.YEAR, -2);
		

		
		ArrayList<RangoResolucion> rangos = new ArrayList<RangoResolucion>();
		
		if (inicio.before(fin_solo_historico)){ // solo tengo historicos 
			// para usar consulta optimizada para rango continuo			
			while(inicio.before(min_diario)){
				// divido en intervalos de 2 years para no sobrecargar el sistema con estructuras temporales muy grandes
				Calendar fin_iter = (Calendar)inicio.clone();
				fin_iter.add(Calendar.YEAR,2);
				fin_iter = min(min(fin,min_diario),fin_iter);
				RangoResolucion r = new RangoResolucion(Resolucion.MIN, inicio, fin_iter);
				rangos.add(r);
				inicio = fin_iter;
			}
			
			RangoResolucion r = new RangoResolucion(Resolucion.MAX, inicio, min(fin,fin_solo_historico));
			rangos.add(r);
			inicio = fin_solo_historico;
		}
		if (fin.after(fin_solo_historico)){
			if (min_20_ini.after(inicio) ){ // pido por dia
				RangoResolucion r = new RangoResolucion(Resolucion.DIA, inicio,min(fin,min_20_ini));
				rangos.add(r);
				inicio = min_20_ini;
			}
			if (tiempoReal_ini.after(inicio) ){ // pido cada 20 minutos
				RangoResolucion r = new RangoResolucion(Resolucion.MIN_20, inicio,min(fin,tiempoReal_ini));
				rangos.add(r);
				inicio = tiempoReal_ini; 
			}
			if (tiempoReal_ini.after(inicio)){ 
				RangoResolucion r = new RangoResolucion(Resolucion.MAX, inicio,fin);
				rangos.add(r);
			}			
		}
		return rangos;
	}
	
	/**
	 * Consulta que obtiene el ultimo historico de un intervalo, 
	 * @param inicio
	 * @param fin
	 * @param idAcciones
	 * @param segundosIntervalo debe caber un numero entero de veces dentro de 24 horas para obtener resultados razonables
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Historico> consultaUltimoHistoricoPorIntervalo(Calendar inicio,Calendar fin,List<Long> idAcciones,long segundosIntervalo){
		if (idAcciones.isEmpty()) return new ArrayList<Historico>();
		StringBuilder sb = new StringBuilder();
		// creo lista de acciones separada por comas
		for(Long id : idAcciones) sb.append(Long.toString(id)).append(",");
		sb.setLength(sb.length() - 1);  // quito la ultima coma
		String listaId = sb.toString();
		// ?1 es fecha inicio, ?2 es fecha fin, ?3 es lista de acciones, ?4 es intervaloEnSegundos
		// http://stackoverflow.com/questions/2411559/how-do-i-query-sql-for-a-latest-record-date-for-each-user
		// http://stackoverflow.com/questions/4342370/grouping-into-interval-of-5-minutes-within-a-time-range
		String consulta_interna = "select idaccion,max(fecha) as MaxDate from Historico  WHERE fecha BETWEEN ?1 AND ?2 AND idaccion IN ("+listaId+") GROUP BY idaccion,round(extract('epoch' from fecha) / ?3)";
		String consulta =
				" SELECT h.fecha, h.adjclose,h.close,h.idaccion From Historico h inner join (" + consulta_interna + ") h2 ON h.idaccion = h2.idaccion AND h.fecha = h2.MaxDate ORDER BY h.fecha";
		Query q = em.createNativeQuery(consulta, Historico.class);
		q.setParameter(1, inicio.getTime(),TemporalType.TIMESTAMP);
		q.setParameter(2, fin.getTime(),TemporalType.TIMESTAMP);
		q.setParameter(3, segundosIntervalo);
		return (List<Historico>)q.getResultList();
	}
	
	@Override
	public List<Historico> consultaUltimoHistoricoPorIntervaloDia(Calendar inicio,Calendar fin,List<Long> idAcciones){
		return consultaUltimoHistoricoPorIntervalo(inicio, fin, idAcciones, TimeUnit.SECONDS.convert(1, TimeUnit.SECONDS));
	}
	
	private void rellenarUltimoSaldo(Portafolio p, Calendar fin,int intervaloDias){
		Calendar f = (Calendar)p.getUltimaFechaSaldo().clone();
		f.add(Calendar.DATE,intervaloDias);
		SaldoHistorico sh;
		while(f.before(fin)){
			f = (Calendar)f.clone();
			sh = new SaldoHistorico(p, f, p.getUltimoSaldo());
			em.persist(sh);
			f = (Calendar)f.clone();
			f.add(Calendar.DATE,intervaloDias);
		}
		p.setUltimaFechaSaldo(f);
	}
	
	public void calcularSaldoHistoricoUsuario(Portafolio p,Calendar fin){
		List<RangoResolucion> rangos = obtenerResoluciones(p.getUltimaFechaSaldo(),fin);
		long nano1 = System.nanoTime();
 
		ArrayList<Long> acciones = new ArrayList<Long>(p.idAccionesEnPortafolio());
		if (acciones.isEmpty()){
			rellenarUltimoSaldo(p, fin,1);
		}
		else{
			for(RangoResolucion r :rangos){
				List<Historico> list_h = null;
				if (r.resolucion == Resolucion.MAX){
					list_h = ejbServidor.obtenerHistorico(r.inicio, r.fin, acciones);
					
				}
				else if (r.resolucion == Resolucion.DIA){
					long segundos = TimeUnit.DAYS.toSeconds(1); // 1 dia
					 list_h = consultaUltimoHistoricoPorIntervalo(r.inicio, r.fin, acciones, segundos);
					
				}
				else if (r.resolucion == Resolucion.MIN_20){
					long segundos = TimeUnit.MINUTES.toSeconds(20); // 20 minutos
					list_h = consultaUltimoHistoricoPorIntervalo(r.inicio, r.fin, acciones, segundos);
					
				}
				else if (r.resolucion == Resolucion.BAJA){
					long segundos = TimeUnit.DAYS.toSeconds(2); // 2 dias
					list_h = consultaUltimoHistoricoPorIntervalo(r.inicio, r.fin, acciones, segundos);
				}
				else {// if (r.resolucion == Resolucion.MIN)
					long segundos = TimeUnit.DAYS.toSeconds(5); // 5 dias
					list_h = consultaUltimoHistoricoPorIntervalo(r.inicio, r.fin, acciones, segundos);
				}
				calcularSaldoHistorico(p,list_h);
			}
		}
		p.setUltimaFechaSaldo(fin);
		desactivarHistoricoHoy(p, fin);
		
		System.out.println((( System.nanoTime() - nano1) / 1000000) + " ms (calcSaldosHistUsu)");
 	}
	
	
	public void desactivarHistoricoHoy(Portafolio p,Calendar proxFecha){
		Calendar ayer = Calendar.getInstance();
		ayer.set(Calendar.HOUR_OF_DAY, 23);
		ayer.set(Calendar.MINUTE, 59);
		ayer.add(Calendar.DATE,-1);
		if (proxFecha.after(ayer)){
			p.setHistorico(false);
		}
	}
	
	
	public void configSaldoHistorico(String identificador,Calendar fecha){
		ejbCliente.resetearTodasLasPreguntas(identificador);
		Cliente c = obtenerCliente(identificador);
		if (c == null) return;
		Portafolio p = c.getPortafolio();
		em.createQuery("Delete from SaldoHistorico s Where s.portafolio.id = :id").setParameter("id",p.getId()).executeUpdate();
		em.flush();
		p.setLost(false);
		p.setHistorico(true);
		p.asignarSaldoInicial();

		p.setUltimaFechaSaldo(fecha);
		SaldoHistorico sh = new SaldoHistorico(p, p.getUltimaFechaSaldo(),p.getUltimoSaldo());
		em.persist(sh);
		// los paquetes por la fecha los pido en el ajustar paquetes segun preg generales
	}
	
	private List<DTSaldo> lstInvalida(){
		return new ArrayList<DTSaldo>(Arrays.asList(new DTSaldo("incorrect id",0)));
	}
	
	@Override
	public List<DTSaldo> avanzarProximaFecha(String identificador,String prox){

		Cliente c = obtenerCliente(identificador);
		if (c == null) return lstInvalida();
		Portafolio p = c.getPortafolio();
		long inicio = p.getUltimaFechaSaldo().getTimeInMillis();
		Date d;
		try {
			d = ymd.parse(prox);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			d = new Date(inicio);
		}
		Calendar proxFecha = new GregorianCalendar();
		proxFecha.setTime(d);
		
		Calendar now = Calendar.getInstance();
		if (proxFecha.after(now)) proxFecha = now;
		calcularSaldoHistoricoUsuario(p, proxFecha);
		em.flush();
		return ejbServidor.obtenerSaldoHistorico(identificador, inicio, p.getUltimaFechaSaldo().getTimeInMillis());
	}
	
	// TODO: Terminar
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void crearHistoriaDiariaAlgoritmo(PaqueteAlgoritmico p){
		boolean deprecated = p.isDeprecated();
		if (p.getPrimerFecha() == null){
			p.setPrimerFecha(ejbPaquete.getPrimerFechaPaquete(p));
		}
		p.setDeprecated(true); // para que no ofrezca el paquete mientras estamos calculando
		p.setInversion(100);
		p.setNominal(100.0);
		p.setDisponible(0);
		
		ArrayList<Long> borro = new ArrayList<Long>();
		if (p.getAccionHistoria() != null && p.getAccionHistoria().getId() != null){
			borro.add(p.getAccionHistoria().getId() );
		}
		if (p.getAccionEquivalente() != null && p.getAccionEquivalente().getId() != null){
			borro.add(p.getAccionEquivalente().getId() );
		}
		if (!borro.isEmpty()){ // hibernate da error si la lista en IN es vacia
			Query borrar = em.createQuery("DELETE From Historico h WHERE h.accion.id IN :lista");
			borrar.setParameter("lista",borro);
			borrar.executeUpdate();
		}
		em.flush();
		try {
			Calendar inicio = p.getPrimerFecha();
			Calendar fin = Calendar.getInstance();
			List<Long> idAcciones = p.getIdAcciones();
			List<Historico> historicos = consultaUltimoHistoricoPorIntervalo(inicio, fin, idAcciones,
					TimeUnit.DAYS.toSeconds(1));
			CacheHistPorFecha2 cache = new CacheHistPorFecha2();
			cache.setFallback(new JPAProveedorValor(em));
			//ProvFromMap val = new ProvFromMap();
			cache.setearHistoricos(historicos, true);
			p.setFechaCalculo(inicio);
			for (Calendar c : cache.getFechas()) {
				//val.getM().putAll(cache.getMap(c));
				p.calcularValorPorVarMercado(cache, c);
			}
			p.setDeprecated(deprecated);
		} catch(RuntimeException ex) {
			System.out.println("Error al calcular historia diaria algo " + p.getId());
			ex.printStackTrace();
			em.refresh(p);
			p.setDeprecated(deprecated);
			em.flush();
			
			throw ex;
		}
		
	}
	
	@Override
	
	public void  calcularHistoriaAlgoritmos(){
		System.out.println("Inicia calculo historia algo");
		for(PaqueteAlgoritmico pa : ejbPaquete.obtenerPaquetesAlgoritmicos()){
			if (pa.getPrimerFecha() == null){
				pa.setPrimerFecha(ejbPaquete.getPrimerFechaPaquete(pa));
			}
			if (pa.checkearAcciones()){
				em.flush();
			}
			Calendar fhis = ejbPaquete.getPrimerFechaHistoria(pa);
			if (fhis == null || fhis.get(Calendar.YEAR) != pa.getPrimerFecha().get(Calendar.YEAR) ){
				crearHistoriaDiariaAlgoritmo(pa);
				em.flush();
			}
		}
		System.out.println("termina calculo historia algo");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void  calcularEvolucionAlgoritmo(PaqueteAlgoritmico pa){
		//PaqueteAlgoritmico pa = paq;//em.find(PaqueteAlgoritmico.class, paq.getId());
		System.out.println("Inicia calculo evolucion algo " + pa.getId());
		long nanosInicio = System.nanoTime();
		CacheTreeMap ch = new CacheTreeMap();
		ch.setFallback(new JPAProveedorValor(em));
		ch.agregarList(consultaUltimoHistoricoPorIntervaloDia(pa.getFechaCalculo(), Calendar.getInstance(), Arrays.asList(pa.getAccionHistoria().getId())));
		
		IInterpreter inter = new InterpreterController();
	
		ejbAlgoritmo.setCache(ch);
		inter.setEJBAlgoritmo(ejbAlgoritmo);
		Query q = em.createQuery("Select h.fecha FROM Historico h Where h.accion.id = :id ORDER BY h.fecha ASC");
		q.setParameter("id", pa.getAccionHistoria().getId());
		List<Date> listaFechas = q.getResultList();
		
		for(Date d : listaFechas){
			Calendar c = new GregorianCalendar();
			c.setTime(d);
			inter.evaluate(pa.getId(), pa.getAlgoritmo(),c);
		}
		pa.setRecalcular(false);
		ejbAlgoritmo.setCache(null);
		System.out.println("termina calculo evolucion algo "  + pa.getId() + " t=" + (System.nanoTime() - nanosInicio)/ (1000* 1000) + " ms");
	}
	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public DTEstado maquinaTiempo(String iden,String fecha) {
		DTEstado estado = new DTEstado();
		try {
			TimeZone tz = TimeZone.getTimeZone("GMT");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			Date d;
			
			d = sdf.parse(fecha);
		
			Calendar cal = GregorianCalendar.getInstance();
			cal.setTimeZone(tz);
			cal.setTime(d);
			cal.set(Calendar.HOUR_OF_DAY, 23); // poner fecha mercado
			configSaldoHistorico(iden, cal);
			estado.setEstado("ok");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			estado.setEstado("Invalid Date");
		}
		return estado;
		
	}
	
}

package ejb;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import ejb.remote.IEjbServidorRemote;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.persistence.annotations.ReadOnly;

import auxiliar.CacheHistPorAccion;
import auxiliar.CacheHistPorFecha;
import auxiliar.CacheHistPorFecha2;
import datatypes.DTEstado;
import datatypes.DTPregunta;
import datatypes.DTPreguntaGeneral;
import datatypes.DTRespuesta;
import datatypes.DTRespuestaGeneral;
import datatypes.DTSaldo;
import ejb.remote.IEjbAdminRemote;
import interprete.Category;
import model.Accion;
import model.Cliente;
import model.ClienteAccion;
import model.ClientePaquete;
import model.Historico;
import model.JPAProveedorValor;
import model.Paquete;

import model.PaqueteAccion;

import model.PaqueteCategoria;

import model.PaqueteIndice;
import model.Portafolio;
import model.PreguntaGeneral;
import model.Respuesta;
import model.RespuestaGeneral;
import model.SaldoHistorico;

@Local(IEjbServidor.class)
@Stateless
public class EjbServidor implements IEjbServidor, IEjbServidorRemote {

	@PersistenceContext(unitName = "Servidor")
	private EntityManager em;	

	@EJB
	private IEjbPaquete ejbPaquete;
	
	private Cliente cargarClienteConPortafolio(String identificador){
		TypedQuery<Cliente> q = em.createQuery("Select c from Cliente c where c.idTelefono = :iden",Cliente.class);
		q.setHint("org.hibernate.cacheable", true);
		q = q.setParameter("iden", identificador);
		List<Cliente> lst =  q.getResultList();
		Cliente c = lst.isEmpty() ? null : lst.get(0);
		return c;
		
	}
	
	
	

	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void actualizarSaldosAll(){
		actualizarSaldoNoHistoricos();
	
	}
	
	// lo hago fuera de transaccion porque no necesito atributos adicionales (es mas rapido)
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Historico> obtenerHistorico(Calendar desde, Calendar hasta, List<Long> acciones) {
		if (acciones.isEmpty()) return new ArrayList<Historico>();
		String jql = "Select h FROM Historico h WHERE h.accion.id IN :lista AND h.fecha BETWEEN :desde AND :hasta ORDER BY h.fecha";
		TypedQuery<Historico> query = em.createQuery(jql,Historico.class);
		query.setParameter("lista", acciones);
		query.setParameter("desde", desde.getTime(),TemporalType.TIMESTAMP);
		query.setParameter("hasta", hasta.getTime(),TemporalType.TIMESTAMP);
		List<Historico> res = query.getResultList();
		return res;
	}
	
	@EJB IEjbServidor ejbServidor;
	

	

	
	/*public void actualizarSaldoIndividual(long portId,Calendar ahora){
		TypedQuery<Portafolio> q =  em.createQuery("Select p from Portafolio p LEFT JOIN FETCH  p.paquetes pcp LEFT JOIN FETCH pcp.acciones WHERE p.id=:id",Portafolio.class);
		q.setParameter("id", portId);
		List<Portafolio> res = q.getResultList();
		if (res.isEmpty()){
			return;
		}
		actualizarSaldo2(ahora, res.get(0));
		
	}*/
	
	public void actualizarSaldoNoHistoricos(){
		//TypedQuery<Portafolio> q =  em.createQuery("Select p from Portafolio p LEFT JOIN FETCH  p.paquetes pcp LEFT JOIN FETCH pcp.acciones WHERE p.historico = :h",Portafolio.class);
		System.out.println("INICIO ACTUALIZACION SALDOS");
		Map<Long,Map<Long,Double>> m = this.pedirSumaCaBD();
		TypedQuery<Portafolio> q =  em.createQuery("Select p from Portafolio p WHERE p.historico = :h AND p.activo = :a",Portafolio.class);
		q.setLockMode(LockModeType.PESSIMISTIC_READ);
		q.setParameter("h", Boolean.FALSE);
		q.setParameter("a", Boolean.TRUE);
		Calendar ahora = GregorianCalendar.getInstance();
		long nano = System.nanoTime();
		long ini = nano;
		for(Portafolio p : q.getResultList()){

			SaldoHistorico s = p.calcularSaldoOptimizado(ahora,m.get(p.getId()));// m);
			em.persist(s);

		}
		em.flush();
		System.out.println("FIN ACTUALIZACION SALDOS");
		System.out.println("Total = " + ((System.nanoTime() - ini)/1000000));
	}
	
	@SuppressWarnings("unchecked")
	public Map<Long,Double> pedirSumaCaBDInd(Long idPort){
		Map<Long,Double> res = new HashMap<Long,Double>();
		Query sql_query = em.createNativeQuery("SELECT ca.acc_idportafolio,ca.acc_idpaq,SUM(ca.cantidad * a.valoractual) as suma FROM ClienteAccion ca JOIN Accion a ON ca.idaccion = a.idaccion WHERE ca.acc_idportafolio = ?1 GROUP BY ca.acc_idportafolio,ca.acc_idpaq");
		sql_query.setParameter(1, idPort);
		List<Object[]> resumen = sql_query.getResultList();
		for(Object[] arr : resumen){
			Long paq = ((BigInteger)arr[1]).longValue();
			Double suma = ((Double)arr[2]);
			res.put(paq, suma);
		}
		return res;
	}
	
	
	@SuppressWarnings("unchecked")
	public Map<Long,Map<Long,Double>> pedirSumaCaBD(){
		Map<Long,Map<Long,Double>> res = new HashMap<Long, Map<Long,Double>>();
		Query sql_query = em.createNativeQuery("SELECT ca.acc_idportafolio,ca.acc_idpaq,SUM(ca.cantidad * a.valoractual) as suma FROM ClienteAccion ca JOIN Accion a ON ca.idaccion = a.idaccion JOIN Portafolio p ON ca.acc_idportafolio = p.idportafolio WHERE p.historico = 'f' GROUP BY ca.acc_idportafolio,ca.acc_idpaq");
		List<Object[]> resumen = sql_query.getResultList();
		for(Object[] arr : resumen){
			Long port = ((BigInteger)arr[0]).longValue();
			Long paq = ((BigInteger)arr[1]).longValue();
			Double suma = ((Double)arr[2]);
			if (!res.containsKey(port)) res.put(port, new HashMap<Long, Double>());
			res.get(port).put(paq, suma);
		}
		return res;
	}
	

	/*private void actualizarSaldo2(Calendar ahora,Portafolio p) {
		JPAProveedorValor jpa = new JPAProveedorValor(em);
		boolean exito = false;

		while(!exito){
			try {
				p.calcularSaldo(ahora, jpa);
				em.lock(p, LockModeType.OPTIMISTIC);
				exito = true;
				em.flush();
			} catch (OptimisticLockException e) {
				em.refresh(p);
			}
		}
	}*/
	
	@Override
	public List<DTSaldo> obtenerSaldoHistorico2(String identificador, String desdeFecha, String hastaFecha) {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		try {
			Date desde;
			Date hasta;
			Calendar hastaC = new GregorianCalendar();
			try {
				hasta = sdf2.parse(hastaFecha);
				desde = sdf2.parse(hastaFecha);
				hastaC.setTime(hasta);
			} catch (ParseException e) {
				desde = sdf1.parse(desdeFecha);
				hasta = sdf1.parse(hastaFecha);
				hastaC.setTime(hasta);
				hastaC.add(Calendar.DAY_OF_YEAR, 1);
				hastaC.add(Calendar.SECOND, -1); // fin del dia indicado (es
													// inclusivo)
			}
			return obtenerSaldoHistorico(identificador, desde.getTime(), hastaC.getTimeInMillis());
		} catch (ParseException e) {
			return Arrays.asList(new DTSaldo("Invalid date format, must be yyyy-MM-dd", 0));
		}
	}
	
	//@EJB IEjbCalculoSaldoHist ejbSH;
	
	public List<DTSaldo> obtenerSaldoHistorico(String identificador, long desdeFecha, long hastaFecha) {
		try {
			if (hastaFecha <= 0) {
				hastaFecha = Calendar.getInstance().getTimeInMillis();
			}
			Query q = em.createQuery("Select c.portafolio from Cliente c Where c.idTelefono = :iden");
			Portafolio p = (Portafolio)q.setParameter("iden", identificador).getSingleResult();
			actualizarFechaAcceso(p);
			Long id = (Long) p.getId();
			TypedQuery<SaldoHistorico> qs = em.createQuery(
					"Select s from SaldoHistorico s where s.portafolio.id = :id and s.fecha Between :desde And :hasta Order By s.fecha Asc",
					SaldoHistorico.class);


			Calendar desde = Calendar.getInstance();
			desde.setTimeInMillis(desdeFecha);
			Calendar hasta = Calendar.getInstance();
			hasta.setTimeInMillis(hastaFecha);
			qs.setParameter("id", id);
			qs.setParameter("desde", desde, TemporalType.TIMESTAMP);
			qs.setParameter("hasta", hasta, TemporalType.TIMESTAMP);
			List<DTSaldo> res = new ArrayList<DTSaldo>();
			for (SaldoHistorico s : qs.getResultList()) {
				DTSaldo dt = new DTSaldo(s.getFecha(), (int) Math.round(s.getValor()));
				dt.setL(p.isLost());
				res.add(dt);
			}
			return res;
		} catch (NoResultException ex) {
			return new ArrayList<DTSaldo>(Arrays.asList(Invalido()));
		}
	}
		
	public  DTSaldo Invalido(){
		return new DTSaldo("invalid",0);
	}
	
	
	//  porque un int??
	
	public DTSaldo calcularSaldo(String identificador) {
		// o deberiamos recibir el id de cliente o deberiamos hacer el id de telef clave primaria
		//TypedQuery<Cliente> q = em.createQuery("Select c from Cliente c where c.idTelefono = :id",Cliente.class);
		try{ 
			JPAProveedorValor prov = new JPAProveedorValor(em);
			Cliente c = cargarClienteConPortafolio(identificador);//c.getPortafolio();
			if (c == null) return Invalido();
			Portafolio p = c.getPortafolio();
			
			em.persist(p.calcularSaldoOptimizado(Calendar.getInstance(), null));
			em.merge(p);
			int s= (int)Math.round(p.getUltimoSaldo());
			return  new DTSaldo(p.getUltimaFechaSaldo(),s);
			
			
		}
		catch(NoResultException ex){
			// tengo que hacer algo mejor si el dispositivo no existe...
			DTSaldo err = Invalido();
			
			return err;
		}
	}
	
	 void actualizarFechaAcceso(Portafolio p){
		 if (p.getUltimoAcceso() == null){
			 p.setUltimoAcceso(Calendar.getInstance());
		 }
		 else{
			 Calendar ua_plus1 = (Calendar)p.getUltimoAcceso().clone();
			 ua_plus1.add(Calendar.HOUR_OF_DAY, 1);
			 if (p.getUltimoAcceso().after(p.getUltimoAcceso())){
				 p.setUltimoAcceso(Calendar.getInstance());
			 }
		 }
	 }
	
	@Override
	public DTSaldo obtenerSaldo(String identificador) {
		// o deberiamos recibir el id de cliente o deberiamos hacer el id de telef clave primaria
		//TypedQuery<Cliente> q = em.createQuery("Select c from Cliente c where c.idTelefono = :id",Cliente.class);
		try{ 
			Cliente c = cargarClienteConPortafolio(identificador);//c.getPortafolio();
			if (c == null) return Invalido();
			Portafolio p = c.getPortafolio();
			actualizarFechaAcceso(p);
			double saldo = p.getUltimoSaldo();
			int s= (int)Math.round(saldo);
			DTSaldo d =  new DTSaldo(p.getUltimaFechaSaldo(),s);
			d.setL(p.isLost());
			return d;
		}
		catch(NoResultException ex){
			// tengo que hacer algo mejor si el dispositivo no existe...
			DTSaldo err = Invalido();
			
			return err;
		}
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<DTPregunta> obtenerPreguntasGenerales() {
		Query q = em.createQuery("select p from PreguntaGeneral p where p.deprecated = false");
		ArrayList<PreguntaGeneral> preguntas =(ArrayList<PreguntaGeneral>)  q.getResultList();
		ArrayList<DTPregunta> preguntasDT = new ArrayList<DTPregunta>();
		for (PreguntaGeneral pg : preguntas)
		{
			ArrayList<DTRespuesta> dtRespuestas = new ArrayList<DTRespuesta>();
			for (Respuesta r : pg.getRespuestas())
			{
				DTRespuesta dtr = new DTRespuesta(r.getId(),r.getSentenciaENG(),r.getSentenciaESP());
				dtRespuestas.add(dtr);
			}
			DTPregunta dtpg = new  DTPregunta(pg.getId(),
					pg.getSentenciaENG(),pg.getSentenciaESP(), dtRespuestas);
						
			preguntasDT.add(dtpg);		
		}
		
		return  preguntasDT;
	}

	@Override
	public  ArrayList<DTPreguntaGeneral> obtenerPreguntasGeneralesWeb()
	{
		Query q = em.createQuery("select p from PreguntaGeneral p where p.deprecated = false");
		ArrayList<PreguntaGeneral> preguntas =(ArrayList<PreguntaGeneral>)  q.getResultList();
		ArrayList<DTPreguntaGeneral> preguntasDT = new ArrayList<DTPreguntaGeneral>();
		
		for (PreguntaGeneral pg : preguntas)
		{
			ArrayList<DTRespuestaGeneral> dtRespuestas = new ArrayList<DTRespuestaGeneral>();
			for (Respuesta r : pg.getRespuestas())
			{
				RespuestaGeneral rg = (RespuestaGeneral)r;
				
				String categoriasTexto = "";
				
				for (PaqueteCategoria pc : rg.getCategorias())
				{
					categoriasTexto += ", " + pc.getCategoria();
				}
				
				if (categoriasTexto.length() >= 2) categoriasTexto = categoriasTexto.substring(2);
				
				DTRespuestaGeneral dtr = new DTRespuestaGeneral();
				dtr.setCategoriasTexto(categoriasTexto);
				dtr.setEng(rg.getSentenciaENG());
				dtr.setEsp(rg.getSentenciaESP());
				
				dtRespuestas.add(dtr);
			}
			
			DTPreguntaGeneral dtpg = new  DTPreguntaGeneral(pg.getId(), pg.getSentenciaENG(),pg.getSentenciaESP(), dtRespuestas);
						
			preguntasDT.add(dtpg);		
		}
		
		return  preguntasDT;
	}
	
	@Override
	public void persistirArbol(Category c) {
		// TODO Auto-generated method stub
		em.persist(c);
	}
	
	

	@Override public Calendar horaCierreMercado(String ymd){

		try {
			TimeZone newYorkTZ = TimeZone.getTimeZone("America/New_York");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sdf.setTimeZone( newYorkTZ);
			Date fecha;
			fecha = sdf.parse(ymd);
			Calendar c = GregorianCalendar.getInstance(newYorkTZ); 
			c.setTime(fecha);
			c.set(Calendar.HOUR_OF_DAY, 16);
			return c;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	
	@Override
	public boolean abreStockExchangeDia(Calendar c){
		int y = c.get(Calendar.YEAR);
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
		String fecha = sdf.format(c.getTime());

		// feriados 2015 y  2016
		if (Arrays.asList("01-01","07-04","12-25").contains(fecha)) return false; // feriados fijos
		if (y == 2015 && Arrays.asList("01-19","02-16","04-03","05-25","07-03","09-07","11-26").contains(fecha)) return false; // feriados 2015
		if (y == 2016 && Arrays.asList("01-18","02-15","03-25","05-30","09-05","11-24","12-26").contains(fecha)) return false; // feriados 2016
		int diaSemana = c.get(Calendar.DAY_OF_WEEK);
		return (diaSemana >= Calendar.MONDAY && diaSemana <= Calendar.FRIDAY); // NYSE abren de lunes a viernes
	}
	
	
	public boolean abreStockExchange(int y,int m,int d){
		GregorianCalendar c = new GregorianCalendar(y,m,d);
		return abreStockExchangeDia(c);
	}
	
	@Override
	public boolean estaAbiertoStockExchange(Calendar ahora){
		// https://en.wikipedia.org/wiki/List_of_stock_exchange_opening_times
		// http://stackoverflow.com/questions/9863625/difference-between-est-and-america-new-york-time-zones
		//https://www.nyse.com/markets/hours-calendars#holidays
		TimeZone newYorkTZ = TimeZone.getTimeZone("America/New_York");
		ahora = (Calendar)ahora.clone();
		ahora.setTimeZone(newYorkTZ);
	
		if (abreStockExchangeDia(ahora)){
			int minutosNY = 60 *  ahora.get(Calendar.HOUR_OF_DAY) + ahora.get(Calendar.MINUTE);
			return (minutosNY >= 9 * 60 + 30 && minutosNY <= 16 * 60); // de 09:30 a 16:00
		}
		return false;
	}
	
	@Override
	public boolean estaAbiertoStockExchangeAhora(){
		return estaAbiertoStockExchange(Calendar.getInstance());
	}
	
	public String obtenerCantidadUsuariosPaquetesAcciones()
	{
		Query q1 = em.createQuery("select count(acc.id) from Accion acc");
		Query q2 = em.createQuery("select count(paq.id) from Paquete paq where paq.deprecated = false");
		Query q3 = em.createQuery("select count(cli.id) from Cliente cli");
		
		int cantidadAcciones = ((Long)q1.getSingleResult()).intValue();
		int cantidadPaquetes = ((Long)q2.getSingleResult()).intValue();
		int cantidadClientes = ((Long)q3.getSingleResult()).intValue();
		
		return "{ \"actionCount\": " + cantidadAcciones + "," +
				 "\"packetCount\": " + cantidadPaquetes + "," +
				 "\"clientCount\": " + cantidadClientes + "}";

	}
	

	@Override
	public void agregarNuevaPreguntaGeneral(DTPreguntaGeneral dtpg){
		
		
		PreguntaGeneral nuevaPregunta = new PreguntaGeneral();
		
		nuevaPregunta.setSentenciaENG(dtpg.getEng());
		nuevaPregunta.setSentenciaESP(dtpg.getEsp());
		
		for (DTRespuestaGeneral dtrg: dtpg.getRespuestas())
		{
			Query q2 = em.createQuery("select c from PaqueteCategoria c where c.id IN :listaCategoria");
			q2.setParameter("listaCategoria",dtrg.getCategorias());
			List<PaqueteCategoria> categorias = (List<PaqueteCategoria>) q2.getResultList();
			
			nuevaPregunta.agregarRespuestaGeneral(dtrg.getEng(), dtrg.getEsp(), categorias.toArray(new PaqueteCategoria[categorias.size()]));
		}
		
		em.persist(nuevaPregunta);
		em.flush();
	}


	@Override
	public void intentarBorrarPreguntasGeneralesDeprecated() {
		// Borro las preguntas generales que tienen deprecated en true y no
		// tiene asociado ningun cliente

		// Primero obtengo todas las generales con deprecated = true y que no esten asociadas con clientes
		Query q2 = em.createQuery("select pg "
								+ "from PreguntaGeneral pg "
								+ "where pg.deprecated = true"
									+ " and not exists (select r "
													 + "from Cliente c JOIN c.respuestas r "
													 + "where r.pregunta.id = pg.id)");

		List<PreguntaGeneral> preguntasABorrar = (List<PreguntaGeneral>) q2.getResultList();

		for (PreguntaGeneral pg : preguntasABorrar) {
			em.remove(em.contains(pg) ? pg : em.merge(pg));
		}
		em.flush();
	}

	@Override
	public void borrarPreguntaGeneral(long idPregunta)
	{
		Query q = em.createQuery("select p from Pregunta p where p.id = :idPregunta").setParameter("idPregunta",
				idPregunta);
		
		PreguntaGeneral pregunta = (PreguntaGeneral) q.getSingleResult();
		
		pregunta.setDeprecated(true);
		
		intentarBorrarPreguntasGeneralesDeprecated();
	}
}


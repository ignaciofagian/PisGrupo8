package ejb;

import java.io.IOException;
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
import java.util.TreeSet;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import datatypes.DTCategoria;
import datatypes.DTPaquete;
import datatypes.DTPaqueteAccion;
import datatypes.DTPaqueteDetalle;
import datatypes.DTPaqueteIdNombre;
import datatypes.DTPaquetePregunta;
import datatypes.DTPaqueteRespuesta;
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
import model.PreguntaEspecifica;
import model.Respuesta;
import model.RespuestaEspecifica;
import tasks.TareaObtenerValoresYahoo;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

@Stateless
public class EjbPaquete implements IEjbPaquete {
	
	
	@PersistenceContext(unitName = "Servidor")
	private EntityManager em;
	
	@EJB
	IEjbPrueba ejbPrueba;
	
	/*@Override
	public ArrayList<Paquete> obtenerPaquetesPorCategoria(ArrayList<PaqueteCategoria> categorias)
	{	
		
		List<Long> ids = new ArrayList<Long>();
		for(int i = 0;i < categorias.size();i++)
		{
		   ids.add(categorias.get(i).getId());
		}
				
		Query q = em.createQuery("select paq from Paquete paq join paq.categorias cat where paq.deprecated = false and cat.id IN :listaIdCat");
		q.setParameter("listaIdCat", ids);
			
		ArrayList<Paquete> listaPaquetes = (ArrayList<Paquete>) q.getResultList();
		
		//Retorno todos los paquetes que cumplen con las categorias pasadas como parametro
		return listaPaquetes;
	}*/
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	/**
	 * Obtienes los paquetes disponibles en esa fecha que este dentro de alguna de la categorias indicadas
	 */
	public ArrayList<Paquete> obtenerPaquetesPorCategoria(ArrayList<PaqueteCategoria> categorias,Calendar fecha)
	{	
		if (categorias.isEmpty()) return new ArrayList<Paquete>();
		List<Long> ids = new ArrayList<Long>();
		for(int i = 0;i < categorias.size();i++)
		{
		   ids.add(categorias.get(i).getId());
		}
		// me da unexpected end 		
		Query q = em.createQuery("select paq from Paquete paq join paq.categorias cat where paq.deprecated = false and paq.primerFecha <= :fecha and cat.id IN :listaIdCat ");
		q.setParameter("fecha", fecha,TemporalType.DATE);
		q.setParameter("listaIdCat", ids);
			
		ArrayList<Paquete> listaPaquetes = (ArrayList<Paquete>) q.getResultList();
		
		//Retorno todos los paquetes que cumplen con las categorias pasadas como parametro
		return listaPaquetes;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<PaqueteAlgoritmico> obtenerPaquetesAlgoritmicos()
	{
		Query q = em.createQuery("select pa from PaqueteAlgoritmico pa");
		ArrayList<PaqueteAlgoritmico> listaPaquetes = (ArrayList<PaqueteAlgoritmico>) q.getResultList();
		
		return listaPaquetes;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Accion> obtenerConjuntoAccionesUsadasPorPaquetes()
	{
		Query q = em.createQuery("select a from Accion a Where a.equivalente = false");
		
		ArrayList<Accion> accionesDelSistema = (ArrayList<Accion>) q.getResultList();
		
		return accionesDelSistema;	
	}

	
	public Historico actualizarAccionDesdeYahoo(Accion accion, double valor,Date ahora) {
		
		Historico historicoAccion;
		
		//Agrego al historico este valor
		historicoAccion = new Historico(accion, valor, valor, ahora);
		//Actualizo el valor de la accion
		
		accion.setValorActual(valor);
		em.flush();
	//	em.merge(accion);
		return historicoAccion;
				
	}
	
	@Override
	public double obtenerVariacionPaquete(Calendar t1, Calendar t2, Paquete paquete)
	{
		JPAProveedorValor jpa = new JPAProveedorValor(em);
		return paquete.getValorRatio(t1, t2, jpa);
	}
	
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public void actualizarValorPaquetesAlgoritmicos(){
		try {
			Calendar ahora = new GregorianCalendar();
			List<PaqueteAlgoritmico> todos = obtenerPaquetesAlgoritmicos();
			JPAProveedorValor jpa = new JPAProveedorValor(em);
			for(PaqueteAlgoritmico pa : todos){
				pa.calcularValorPorVarMercado(jpa, ahora);

				em.flush();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
	}
	
	@Override
	public void setInversionTodosLosPaquetes(String idCliente,int inversion){
		Query q = em.createQuery("select c from Cliente c where c.idTelefono = :idTel").setParameter("idTel",
				idCliente);
		Cliente c = (Cliente) q.getSingleResult();
		for(ClientePaquete p : c.getPortafolio().getPaquetes()){
			p.asignarInversion(inversion);
		}
		c.getPortafolio().setSinInvertir(0);
	}
	


	@Override
	public void actualizarAccionDesdeYahooYAgregarHistorico(Accion accion,Date ahora,double valor, List<Historico> antiguos) {
		Historico ha = actualizarAccionDesdeYahoo(accion, valor,ahora);
		for(Historico h : antiguos){
			em.merge(h);
		}
		em.merge(ha);
		
	}


	@Override @SuppressWarnings({ "unchecked", "rawtypes" })
	public  Set<String> symAccionesEnHistorico(Calendar desde,Calendar hasta){
		Query query = em.createQuery("Select Distinct(h.accion.symbol) From Historico h Where h.fecha BETWEEN :desde AND :hasta");
		query.setParameter("desde", desde.getTime(),TemporalType.TIMESTAMP);
		query.setParameter("hasta", hasta.getTime(),TemporalType.TIMESTAMP);
		return new TreeSet(query.getResultList());
	}
	
	@EJB
	private IEjbServidor ejbServidor;
	
	public Paquete paquetePorNombre(String nombre){
		List<Paquete> res = em.createQuery("Select p from Paquete p Where p.nombre = :n",Paquete.class).setParameter("n", nombre).setMaxResults(1).getResultList();
		return (res.isEmpty()) ? null :  res.get(0);
	}
	

	public Calendar getPrimerFechaAcciones(List<Long> idAcciones){
		TypedQuery<Date> q = em.createQuery("Select MIN(h.fecha) FROM Historico h WHERE h.accion.id IN (:lista) GROUP BY h.accion.id",Date.class);  
		q.setParameter("lista",idAcciones);
		List<Date> x = q.getResultList();
		if (x.isEmpty() || x.get(0) == null) {
			return null;
		}
		else{
			java.util.Collections.sort(x);
			Calendar cal = new GregorianCalendar();
			cal.setTime((Date)x.get(x.size() - 1));
			return cal;
		}
	}
	

	@Override
	public Calendar getPrimerFechaPaquete(Paquete p){
		List<Long> idAcciones = p.getIdAcciones();
		return getPrimerFechaAcciones(idAcciones);
	}
	
	
	@Override
	public Calendar getPrimerFechaHistoria(PaqueteAlgoritmico p){
		if (p.getAccionHistoria() == null) return  null;
		return getPrimerFechaAcciones(Arrays.asList(p.getAccionHistoria().getId()));
	}
	
	
	@Override
	public void calcularFechaPaquetes(){
		TypedQuery<Paquete> q = em.createQuery("Select p from Paquete p",Paquete.class);
		for(Paquete p : q.getResultList()){
			Calendar c = getPrimerFechaPaquete(p);
			p.setPrimerFecha(c);
		}
	}
	
	

	@Override
	public ArrayList<PaqueteCategoria> obtenerCategoriasDePaquetes()
	{
		List<PaqueteCategoria> lst = em.createQuery("Select c from PaqueteCategoria c",PaqueteCategoria.class).getResultList();
		 
		return (ArrayList<PaqueteCategoria>) lst;
	}
	
	@Override
	public void crearPaquete(DTPaquete dtPaquete)
	{
		
		Paquete nuevoPaquete;	
		
		if (dtPaquete.getTipo() == 0)
		{
			nuevoPaquete = new PaqueteIndice();
		}else
		{
			nuevoPaquete = new PaqueteAlgoritmico();
			((PaqueteAlgoritmico)nuevoPaquete).setAlgoritmo(dtPaquete.getAlgoritmo());
		}
		
		nuevoPaquete.setNombre(dtPaquete.getNombre());
		
		for (DTPaquetePregunta dtPaqPreg : dtPaquete.getPreguntas())
		{
			PreguntaEspecifica pregEsp = new PreguntaEspecifica();
			pregEsp.setSentenciaENG(dtPaqPreg.getEng());
			pregEsp.setSentenciaESP(dtPaqPreg.getEsp());
			pregEsp.setPaquete(nuevoPaquete);
			
			for (DTPaqueteRespuesta respEsp : dtPaqPreg.getRespuestas())
			{
				pregEsp.agregarRespuestaEspecifica(respEsp.getPuntaje(), 
						respEsp.getEng(), respEsp.getEsp());
														
			}
			nuevoPaquete.getPreguntaEspecificas().add(pregEsp);
		}
		
		for (DTPaqueteAccion dtPaqAcc : dtPaquete.getAcciones())
		{
			Accion nuevaOExistenteAccion = ejbPrueba.buscarCrearAccion(dtPaqAcc.getSimbolo());
			nuevoPaquete.agregarAccion(nuevaOExistenteAccion, dtPaqAcc.getPorcentaje());
		}
		
		//Categorias
		List<Long> ids = new ArrayList<Long>();
		for(int i = 0;i < dtPaquete.getCategorias().size();i++)
		{
		   ids.add(dtPaquete.getCategorias().get(i).getId());
		}
		
		Query q2 = em.createQuery("select c from PaqueteCategoria c where c.id IN :listaCategoria");
		q2.setParameter("listaCategoria",ids );
		@SuppressWarnings("unchecked")
		List<PaqueteCategoria> categorias = (List<PaqueteCategoria>) q2.getResultList();
		
		nuevoPaquete.setCategorias(categorias);
		
		nuevoPaquete.validar();
		
		em.persist(nuevoPaquete);
		em.flush();
		this.pedidoDeValoresYahoo(false,TareaObtenerValoresYahoo.getFechaDatosHistoricos());
		System.out.println("############# Paquete - " + dtPaquete.getNombre() + " creado con exito #########");
		if (dtPaquete.getTipo() != 0){
			PaqueteAlgoritmico pa = (PaqueteAlgoritmico)nuevoPaquete;
			ejbSH.crearHistoriaDiariaAlgoritmo(pa);
			em.flush();
			ejbSH.calcularEvolucionAlgoritmo(pa);
		}
	}
	
@Override
public void pedidoDeValoresYahoo(boolean calcularSaldos,Calendar fechaDatosHistoricos) {
	    Logger log = LogManager.getLogger("ejbPaq-Yahoo");
		boolean simularSiCerrado = false;
		List<Accion> accionesDelSistema = this.obtenerConjuntoAccionesUsadasPorPaquetes();
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
		Set<String> pedirHistorico = new HashSet<String>();
		for (Accion acc : accionesDelSistema) {
			symbols.add(acc.getSymbol());
			mapAcciones.put(acc.getSymbol(), acc);
			if (acc.isPedirHistorico()) pedirHistorico.add(acc.getSymbol());
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
				respuesta = YahooFinance.get(pedido);
				// pido datos historicos si ya no los tengo:
				for (Stock y : respuesta.values()){
					if ( pedirHistorico.contains(y.getSymbol())){
						Calendar from = fechaDatosHistoricos;
						try{
							log.debug("Pido hist " + y.getSymbol() + " desde " + from.getTime().toString());
							y.getHistory(from,Interval.DAILY);
							mapAcciones.get(y.getSymbol()).setPedirHistorico(false); // para no pedir de vuelta
						}
						catch (IOException e) {
							System.out.println("Error de Red al obtener datos historicos de yahoo para " + y.getSymbol() + " - " + e.getMessage());
						}
					}
				}
				yahooStocks.putAll(respuesta);
			}

		} catch (IOException e) {
			System.out.println("Error de red al obtener datos historicos de yahoo - " + e.getMessage());
		}
		double simulacion = 1 + (minutos % 60) / 50.0; // 100% - 220% del valor
														// real
		System.out.println("Simulacion x" + simulacion);
		Date ahora = Calendar.getInstance().getTime();
		
		
		for (String symbol : yahooStocks.keySet()) {
			// Dejo que el ejb se encarge de tocar la BD
			Accion acc = mapAcciones.get(symbol);
			Stock stock = yahooStocks.get(symbol);
			List<Historico> antiguos = new ArrayList<Historico>();
			if (pedirHistorico.contains(symbol)) {
				try {
					for (HistoricalQuote hq : stock.getHistory()) {
						Historico h = new Historico(acc, hq.getAdjClose().doubleValue(), hq.getClose().doubleValue(),
								hq.getDate().getTime());
						antiguos.add(h);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("Error al agregar datos historicos: " + e.getMessage());
				}
			}
			
			this.actualizarAccionDesdeYahooYAgregarHistorico(acc,ahora,
					stock.getQuote().getPrice().doubleValue() * simulacion, antiguos);

		}

		
		
		this.calcularFechaPaquetes();
			if (calcularSaldos){
			this.actualizarValorPaquetesAlgoritmicos();
			ejbServidor.actualizarSaldosAll();
		}
	}
	
	@EJB IEjbCalculoSaldoHist ejbSH;
	
	@Override
	public DTPaquete obtenerPaquete(long idPaquete)
	{
		Query q = em.createQuery("select p from Paquete p where p.id = :idPaquete").setParameter("idPaquete",
				idPaquete);
		
		Paquete paquete = (Paquete) q.getSingleResult();
		
		DTPaquete dtPaquete = new DTPaquete();
		List<DTCategoria> categorias = new ArrayList<DTCategoria>();
		List<DTPaqueteAccion> accionesPaquete = new ArrayList<DTPaqueteAccion>();
		List<DTPaquetePregunta> preguntasPaquete = new ArrayList<DTPaquetePregunta>();
		
		dtPaquete.setNombre(paquete.getNombre());
		
		if (paquete instanceof PaqueteAlgoritmico)
		{
			dtPaquete.setTipo(1);
			dtPaquete.setAlgoritmo(((PaqueteAlgoritmico)paquete).getAlgoritmo());
		}else
			dtPaquete.setTipo(0);
		
		for (PreguntaEspecifica pregEsp : paquete.getPreguntaEspecificas())
		{
			List<DTPaqueteRespuesta> respuestas = new ArrayList<DTPaqueteRespuesta>();
			for (Respuesta respEsp :  pregEsp.getRespuestas())
			{
				DTPaqueteRespuesta dtPaqResp = new DTPaqueteRespuesta(respEsp.getSentenciaENG(),
						respEsp.getSentenciaESP(),((RespuestaEspecifica)respEsp).getPuntaje());
				
				respuestas.add(dtPaqResp);
			}
			DTPaquetePregunta dtPaqPreg = new DTPaquetePregunta(pregEsp.getSentenciaENG(),
					pregEsp.getSentenciaESP(), respuestas);
			
			preguntasPaquete.add(dtPaqPreg);
		}
		
		for (PaqueteCategoria paqueteCategorias : paquete.getCategorias())
		{
			categorias.add(new DTCategoria(paqueteCategorias.getId(), paqueteCategorias.getCategoria()));
		}
		
		for (PaqueteAccion paqAccion : paquete.getAccionesPaquete())
		{
			accionesPaquete.add(new DTPaqueteAccion(paqAccion.getAccion().getSymbol(),
					paqAccion.getAccion().getNombre(), paqAccion.getInversion()));
		}
		dtPaquete.setAcciones(accionesPaquete);
		dtPaquete.setCategorias(categorias);
		dtPaquete.setPreguntas(preguntasPaquete);
		
		return dtPaquete;

	}

	public ArrayList<DTPaqueteIdNombre> obtenerPaquetesConIdNombre()
	{
		 TypedQuery<DTPaqueteIdNombre> query = em.createQuery(
			      "select NEW datatypes.DTPaqueteIdNombre(p.id, p.nombre) FROM Paquete p where p.deprecated = false", DTPaqueteIdNombre.class);
			  List<DTPaqueteIdNombre> results = query.getResultList();
		return (ArrayList<DTPaqueteIdNombre>) results;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<DTPaqueteDetalle> obtenerPaquetesConDetalle()
	{
		Query q = em.createQuery("select p from Paquete p where p.deprecated = false");
		List<Paquete> paquetes = q.getResultList();
		
		List<DTPaqueteDetalle> detalles = new ArrayList<DTPaqueteDetalle>();
				
		for (Paquete p : paquetes)
		{
			List<DTPaqueteAccion> listaDtAccionesPaquete = new ArrayList<DTPaqueteAccion>();
			
			//Obtengo la cantidad de usuarios que usan ese paquete
			Query q2 = em.createQuery("select count(DISTINCT pa.portafolio.id) from ClientePaquete pa where pa.paquete.id = :idPaquete").setParameter("idPaquete",
					p.getId());
			
			int cantidadUsuarios = ((Long)q2.getSingleResult()).intValue();
			
			for (PaqueteAccion pa : p.getAccionesPaquete())
			{
				DTPaqueteAccion dtPaqueteAccion = new DTPaqueteAccion(pa.getAccion().getSymbol(), 
						pa.getAccion().getNombre(), pa.getInversion());
				
				listaDtAccionesPaquete.add(dtPaqueteAccion);
			}
			
			DTPaqueteDetalle paqueteDetalle = new DTPaqueteDetalle(p.getId(),p.getNombre(),p.getTipoPaquete(),
					listaDtAccionesPaquete,cantidadUsuarios);
			detalles.add(paqueteDetalle);
		}
		
		return (ArrayList<DTPaqueteDetalle>) detalles;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void intentarBorrarPaquetesDeprecated() {
		// Borro los paquetes que tienen deprecated en true y no
		// tiene asociado ningun cliente

		Query q2 = em.createQuery("select paq "
								+ "from Paquete paq "
								+ "where paq.deprecated = true"
									+ " and not exists (select c "
													 + "from Cliente c JOIN c.portafolio.paquetes cp  "
													 + "where cp.paquete.id = paq.id)");

		List<Paquete> paquetesABorrar = (List<Paquete>) q2.getResultList();

		for (Paquete paq : paquetesABorrar) {
			em.remove(em.contains(paq) ? paq : em.merge(paq));
		}
		
		em.flush();
	}
	
	@Override	
	public void borrarPaquete(long idPaquete)
	{
		Query q = em.createQuery("select p from Paquete p where p.id = :idPaquete").setParameter("idPaquete",
				idPaquete);
		
		Paquete paquete = (Paquete) q.getSingleResult();
		
		paquete.setDeprecated(true);
		
		intentarBorrarPaquetesDeprecated();
	}
	


}


package ejb;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.xml.datatype.XMLGregorianCalendar;

import datatypes.DTEstado;
import datatypes.DTPregunta;
import datatypes.DTRespuesta;
import datatypes.DTSaldo;
import interprete.Category;
import model.Accion;
import model.Cliente;
import model.ClienteAccion;
import model.ClientePaquete;
import model.JPAProveedorValor;
import model.PaqueteIndice;
import model.Portafolio;
import model.PreguntaGeneral;
import model.Respuesta;
import model.SaldoHistorico;

@Stateless
public class EjbServidor implements IEjbServidor {

	@PersistenceContext(unitName = "Servidor")
	private EntityManager em;	

	private Cliente cargarClienteConPortafolio(String identificador){
		//TypedQuery<Cliente> q = em.createQuery("Select c from Cliente c Left Join Fetch c.portafolio  p Left Join Fetch p.paquetes pq Left Join Fetch pq.acciones where c.idTelefono = :iden",Cliente.class);
		//TypedQuery<Cliente> q = em.createQuery("Select c from Cliente c Left Join Fetch c.portafolio pq where c.idTelefono = :iden",Cliente.class);
		TypedQuery<Cliente> q = em.createQuery("Select c from Cliente c where c.idTelefono = :iden",Cliente.class);
		q = q.setParameter("iden", identificador);
		Cliente c = q.getResultList().get(0);
		//Query q_acciones = em.createNativeQuery("Select * from ClienteAccion ca where ca.acc_idportafolio = ?",ClienteAccion.class);
		//q_acciones.setParameter(1,c.getPortafolio().getId());

		return c;
		
	}
	
	public void actualizarSaldosAll(){
		TypedQuery<String> q = em.createQuery("Select c.idTelefono from Cliente c",String.class);
		for(String iden : q.getResultList()){
			calcularSaldo(iden);
		}
	}
	
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
	
	public List<DTSaldo> obtenerSaldoHistorico(String identificador, long desdeFecha, long hastaFecha) {
		try {
			if (hastaFecha <= 0) {
				hastaFecha = Calendar.getInstance().getTimeInMillis();
			}
			Query q = em.createQuery("Select c.portafolio.id from Cliente c Where c.idTelefono = :iden");
			Long id = (Long) q.setParameter("iden", identificador).getSingleResult();
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
			//Cliente c = q.setParameter("id", identificador).getSingleResult();
			JPAProveedorValor prov = new JPAProveedorValor(em);
			Cliente c = cargarClienteConPortafolio(identificador);//c.getPortafolio();
			Portafolio p = c.getPortafolio();
			//Calendar actual = new GregorianCalendar();
			//Calendar ultimo = p.getUltimaFechaSaldo();
			// si se calculo hace poco deberia obtener el ultimo saldo guardado
			//long diff = ultimo.getTimeInMillis() - actual.getTimeInMillis();
			
			double saldo = p.calcularSaldo(new GregorianCalendar(), prov);
			em.merge(p);
			int s= (int)Math.round(saldo);
			return  new DTSaldo(p.getUltimaFechaSaldo(),s);
			
			
		}
		catch(NoResultException ex){
			// tengo que hacer algo mejor si el dispositivo no existe...
			DTSaldo err = Invalido();
			
			return err;
		}
	}
	
	@Override
	public DTSaldo obtenerSaldo(String identificador) {
		// o deberiamos recibir el id de cliente o deberiamos hacer el id de telef clave primaria
		//TypedQuery<Cliente> q = em.createQuery("Select c from Cliente c where c.idTelefono = :id",Cliente.class);
		try{ 
			Cliente c = cargarClienteConPortafolio(identificador);//c.getPortafolio();
			Portafolio p = c.getPortafolio();

			double saldo = p.getUltimoSaldo();
			int s= (int)Math.round(saldo);
			return  new DTSaldo(p.getUltimaFechaSaldo(),s);
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
		Query q = em.createQuery("select p from PreguntaGeneral p");
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
	public void persistirArbol(Category c) {
		// TODO Auto-generated method stub
		em.persist(c);
	}
	
	
	
	public boolean estaAbiertoStockExchange(Calendar ahora){
		// https://en.wikipedia.org/wiki/List_of_stock_exchange_opening_times
		// http://stackoverflow.com/questions/9863625/difference-between-est-and-america-new-york-time-zones
		//https://www.nyse.com/markets/hours-calendars#holidays
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
		String fecha = sdf.format(ahora.getTime());
		int y = ahora.get(Calendar.YEAR);
		
		// feriados 2015 y  2016
		if (Arrays.asList("01-01","07-04","12-25").contains(fecha)) return false; // feriados fijos
		if (y == 2015 && Arrays.asList("01-19","02-16","04-03","05-25","07-03","09-07","11-26").contains(fecha)) return false; // feriados 2015
		if (y == 2016 && Arrays.asList("01-18","02-15","03-25","05-30","09-05","11-24","12-26").contains(fecha)) return false; // feriados 2016
		
		TimeZone newYorkTZ = TimeZone.getTimeZone("America/New_York");
		ahora = (Calendar)ahora.clone();
		ahora.setTimeZone(newYorkTZ);
		int diaSemanaNY = ahora.get(Calendar.DAY_OF_WEEK);
		int minutosNY = 60 *  ahora.get(Calendar.HOUR_OF_DAY) + ahora.get(Calendar.MINUTE);
		if (diaSemanaNY >= Calendar.MONDAY && diaSemanaNY <= Calendar.FRIDAY){ // NYSE abren de lunes a viernes
			if (minutosNY >= 9 * 60 + 30 && minutosNY <= 16 * 60){ // de 09:30 a 16:00
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean estaAbiertoStockExchangeAhora(){
		return estaAbiertoStockExchange(Calendar.getInstance());
	}
	
	

}

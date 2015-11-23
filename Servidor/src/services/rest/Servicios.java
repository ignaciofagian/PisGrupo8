package services.rest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.datatype.XMLGregorianCalendar;

import datatypes.DTEstado;
import datatypes.DTPregunta;
import datatypes.DTPreguntaConRespuesta;
import datatypes.DTRespuesta;
import datatypes.DTSaldo;
import model.Cliente;
import model.Pregunta;
import model.PreguntaEspecifica;
import model.PreguntaGeneral;
import model.RespuestaGeneral;
import ejb.EjbCalculoSaldo;
import ejb.EjbCliente;
import ejb.EjbPaquete;
import ejb.EjbServidor;
import ejb.IEjbCalculoSaldoHist;
import ejb.IEjbCliente;
import ejb.IEjbPaquete;
import ejb.IEjbPrueba;
import ejb.IEjbServidor;

@Path("/app")
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class Servicios {

	@EJB
	private IEjbServidor ejb;
	@EJB
	private IEjbCliente ejbCliente;
	@EJB
	private IEjbPaquete ejbPaquete;
	
	@EJB
	private IEjbCalculoSaldoHist ejbCalcSaldo;
	
	@EJB
	private IEjbPrueba ejbPrueba;

	@GET
	@Path("registrar")
	@Produces(MediaType.APPLICATION_JSON)
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public DTEstado registrar(@QueryParam("id") String idvalue) {
		DTEstado dtEstado = new DTEstado();
		dtEstado.setEstado(ejbCliente.registrarUsuario(idvalue));
		return dtEstado;
	}

	@GET
	@Path("saldo")
	@Produces(MediaType.APPLICATION_JSON)
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public DTSaldo getSaldo(@QueryParam("id") String idvalue) {
		//System.out.println("Saldo de usuario con id=" + idvalue + " : $1200");
		DTSaldo saldo = ejb.obtenerSaldo(idvalue);
		
		return saldo;//"Saldo de usuario con id=" + idvalue + " : $" + saldo;
	}
	
	@GET
	@Path("saldos")
	@Produces(MediaType.APPLICATION_JSON)
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<DTSaldo> getSaldos(@QueryParam("id") String idvalue,@QueryParam("desde") long desde,@QueryParam("hasta") long hasta) {
		//System.out.println("Saldo de usuario con id=" + idvalue + " : $1200");
		List<DTSaldo> saldos = ejb.obtenerSaldoHistorico(idvalue, desde, hasta);
		
		return saldos;//"Saldo de usuario con id=" + idvalue + " : $" + saldo;
	}
	
	@GET
	@Path("saldos2")
	@Produces(MediaType.APPLICATION_JSON)
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<DTSaldo> getSaldos2(@QueryParam("id") String idvalue,@QueryParam("desde") String desde,@QueryParam("hasta") String hasta) {
		//System.out.println("Saldo de usuario con id=" + idvalue + " : $1200");
		List<DTSaldo> saldos = ejb.obtenerSaldoHistorico2(idvalue, desde, hasta);
		
		return saldos;//"Saldo de usuario con id=" + idvalue + " : $" + saldo;
	}


	@GET
	@Path("preguntas/obtenerPreguntasGenerales")
	@Produces(MediaType.APPLICATION_JSON)
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public ArrayList<DTPregunta> obtenerPreguntasGenerales() {
		System.out.println("entro a obtener preguntas generales");
		ArrayList<DTPregunta> preguntas = ejb.obtenerPreguntasGenerales();
		return preguntas;
	}
	
	@GET
	@Path("preguntas/registrarRespuestasGenerales")
	@Produces(MediaType.APPLICATION_JSON)
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public DTEstado registrarRespuestasGenerales(@QueryParam("id") String idCliente, @QueryParam("resp") String respuestas) {		
		System.out.println("entro a registrar preguntas generales");
		return ejbCliente.ajustarPaquetesEnBaseRespGenerales(idCliente, respuestas);
	}
	
	@GET
	@Path("preguntas/obtenerPreguntasEspecificas")
	@Produces(MediaType.APPLICATION_JSON)
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public ArrayList<DTPregunta> obtenerPreguntasEspecificas(@QueryParam("id") String idCliente) {		
		System.out.println("entro a obtener preguntas especificas");
		return ejbCliente.obtenerPreguntasEspecificas(idCliente);
	}
	
	@GET
	@Path("preguntas/registrarRespuestasEspecificas")
	@Produces(MediaType.APPLICATION_JSON)
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public DTEstado registrarRespuestasEspecificas(@QueryParam("id") String idCliente, @QueryParam("resp") String respuestas) {		
		System.out.println("entro a registrar preguntas especificas");
		return ejbCliente.ajustarInversionPaquetesEnBaseRespEspecificas(idCliente, respuestas);
	}
	
	@GET
	@Path("preguntas/obtenerPreguntasRespondidas")
	@Produces(MediaType.APPLICATION_JSON)
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public ArrayList<DTPreguntaConRespuesta> obtenerPreguntasRespondidas(@QueryParam("id") String idCliente) {		
		System.out.println("entro a obtener preguntas respondidas");
		ArrayList<DTPreguntaConRespuesta> dtPreguntasConResp = ejbCliente.obtenerPreguntasRespondidasPorCliente(idCliente);
		
		return dtPreguntasConResp;
	}

	@GET
	@Path("prueba")
	@Produces(MediaType.APPLICATION_JSON)
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void prueba(@QueryParam("id") long idPaquete) {		
		System.out.println("entro a obtener preguntas respondidas");
		ejbCliente.obtenerClientesConPaqueteAlgoritmico(idPaquete);
	}
	
	@GET
	@Path("pruebaCliente")
	@Produces(MediaType.APPLICATION_JSON)
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public DTSaldo pruebaCliente() {
		System.out.println("pruebaCliente");
		String idCliente = "prueba5555";
		ejbCliente.registrarUsuario(idCliente);
		ArrayList<Long> respuestasEspecificas = new ArrayList<Long>();
		for (DTPregunta esp : ejbCliente.obtenerPreguntasEspecificas(idCliente)) {
			for (DTRespuesta r : esp.getResp()) {
				if (esp.getIng() == "How much do you belive that industry will improve?" && r.getIng() == "Little") {
					respuestasEspecificas.add( r.getId());
				}

				if (esp.getIng() == "Do you believe that small companies will prosper?" && r.getIng() == "Much") {
					respuestasEspecificas.add( r.getId());
				}
				
				if (esp.getIng() == "Do you think the future of the auto industry lies on electric cars?" && r.getIng() == "Yes") {
					respuestasEspecificas.add( r.getId());
				}
			}
		}
		Long[] esp = new Long[3];
		respuestasEspecificas.toArray(esp);
		ejbCliente.ajustarInversionPaquetesEnBaseRespEspecificas(idCliente, esp[0]+"-"+esp[1]+esp[2]);
		return ejb.obtenerSaldo(idCliente);
	}

	

	@GET
	@Path("mercadoAbierto")
	@Produces(MediaType.APPLICATION_JSON)
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public DTEstado mercadoAbierto() {		
		boolean open = ejb.estaAbiertoStockExchangeAhora();
		DTEstado estado = new DTEstado();
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		sdf2.setTimeZone(TimeZone.getTimeZone("GMT"));
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		Date ahora = Calendar.getInstance().getTime();
		estado.setEstado(open ? "open" : "closed" + " Local: " + sdf1.format(ahora) + " UTC: " + sdf2.format(ahora) );
		return estado;
	}
	
	@GET
	@Path("paquetes/setInvAllDebug")
	@Produces(MediaType.APPLICATION_JSON)
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public DTSaldo setInvAllDebug(@QueryParam("id") String idvalue,@QueryParam("val") int val) {
		//System.out.println("Saldo de usuario con id=" + idvalue + " : $1200");
		ejbPaquete.setInversionTodosLosPaquetes(idvalue, val);
		DTSaldo saldo = ejb.obtenerSaldo(idvalue);
		
		return saldo;//"Saldo de usuario con id=" + idvalue + " : $" + saldo;
	}
	
	@GET
	@Path("preguntas/resetearTodasLasPreguntas")
	@Produces(MediaType.APPLICATION_JSON)
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public DTEstado resetearTodasLasPreguntas(@QueryParam("id") String idCliente) {
		System.out.println("entro a resetear todas las preguntas");
		return ejbCliente.resetearTodasLasPreguntas(idCliente);
	}
	
	@GET
	@Path("borrarCliente")
	@Produces(MediaType.APPLICATION_JSON)
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public DTEstado borrarCliente(@QueryParam("id") String idCliente) {
		return ejbCliente.borrarCliente(idCliente);
	}
	
	@GET
	@Path("probarH")
	@Produces(MediaType.APPLICATION_JSON)
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public DTEstado probarDatosHistoricos(@QueryParam("id") String idCliente) {
		ejbCalcSaldo.pruebaDatosHistoricos(idCliente);
		DTEstado estado = new DTEstado();
		estado.setEstado("ok");
		return estado;
	}
	
	
	
	@GET
	@Path("crearClientes")
	@Produces(MediaType.APPLICATION_JSON)
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public DTEstado probarDatosHistoricos(@QueryParam("cantidad") int cantidad) {
		ejbPrueba.borrarClientesPrueba();
		ejbPrueba.crearClientesPrueba(cantidad);
		DTEstado estado = new DTEstado();
		estado.setEstado("ok");
		return estado;
	}
	

	
	@GET // borrar una vez que se cambie
	@Path("maquinatiempo")
	@Produces(MediaType.APPLICATION_JSON)
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public DTEstado maquinaTiempo(@QueryParam("iden") String iden,@QueryParam("date") String fecha) {
		return ejbCalcSaldo.maquinaTiempo(iden, fecha);
	}
	
	@GET
	@Path("avanzar")
	@Produces(MediaType.APPLICATION_JSON)
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<DTSaldo> avanzar(@QueryParam("id") String iden,@QueryParam("date") String fecha) {
		return ejbCalcSaldo.avanzarProximaFecha(iden, fecha);
	}
}



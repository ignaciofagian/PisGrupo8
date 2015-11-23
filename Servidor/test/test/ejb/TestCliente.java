package test.ejb;

import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import model.ClientePaquete;

import org.junit.Test;

import datatypes.DTEstado;
import datatypes.DTPregunta;
import datatypes.DTPreguntaConRespuesta;
import ejb.remote.IEjbClienteRemote;

public class TestCliente {
	
	private IEjbClienteRemote obtenterServicio() throws NamingException {

		final InitialContext context = TestUtils.createContext("localhost:4447");
		//final InitialContext context = TestUtils.createContext("localhost:4447");
		final String solrEjbService = "Servidor/EjbCliente!" + IEjbClienteRemote.class.getName();
		System.out.println("context.lookup.("+solrEjbService+")");
		IEjbClienteRemote remote = (IEjbClienteRemote) context.lookup(solrEjbService);
		System.out.println("Servicio inicializado");
		return remote;
		// }
	}
	
	@Test
	public void registrarUsuario(){
		try {
			IEjbClienteRemote ejb = obtenterServicio();
			System.out.println("Se ejecuta el test...");
			ejb.registrarUsuario("1111");
			assert(true);
		} catch (NamingException e) {
			System.out.println("ERROR obetner servicio : " + e.getMessage());
			e.printStackTrace();
		}		
	}
	
	@Test
	public void ajustarPaquetesEnBaseRespGenerales(){
		try {
			IEjbClienteRemote ejb = obtenterServicio();
			System.out.println("Se ejecuta el test...");
			DTEstado e = ejb.ajustarPaquetesEnBaseRespGenerales("1111", "1-3");
			assert("ok".equals(e.getEstado()));
		} catch (NamingException e) {
			System.out.println("ERROR obetner servicio : " + e.getMessage());
			e.printStackTrace();
		}		
	}
	
	@Test
	public void obtenerPreguntasRespondidasPorCliente(){
		try {
			IEjbClienteRemote ejb = obtenterServicio();
			System.out.println("Se ejecuta el test...");
			ArrayList<DTPreguntaConRespuesta> p = ejb.obtenerPreguntasRespondidasPorCliente("1111");
			assert(p!=null);
		} catch (NamingException e) {
			System.out.println("ERROR obetner servicio : " + e.getMessage());
			e.printStackTrace();
		}		
	}
	
	@Test
	public void obtenerPreguntasEspecificas(){
		try {
			IEjbClienteRemote ejb = obtenterServicio();
			System.out.println("Se ejecuta el test...");
			ArrayList<DTPregunta> p = ejb.obtenerPreguntasEspecificas("1111");
			assert(p!=null);
		} catch (NamingException e) {
			System.out.println("ERROR obetner servicio : " + e.getMessage());
			e.printStackTrace();
		}		
	}
	
	@Test
	public void ajustarInversionPaquetesEnBaseRespEspecificas(){
		try {
			IEjbClienteRemote ejb = obtenterServicio();
			System.out.println("Se ejecuta el test...");
			DTEstado e = ejb.ajustarInversionPaquetesEnBaseRespEspecificas("1111", "1-3");
			assert("ok".equals(e.getEstado()));
		} catch (NamingException e) {
			System.out.println("ERROR obetner servicio : " + e.getMessage());
			e.printStackTrace();
		}		
	}
	
	@Test
	public void obtenerClientesConPaqueteAlgoritmico(){
		try {
			IEjbClienteRemote ejb = obtenterServicio();
			System.out.println("Se ejecuta el test...");
			ArrayList<ClientePaquete> cp = ejb.obtenerClientesConPaqueteAlgoritmico(1);
			assert(cp.size() > 0);
		} catch (NamingException e) {
			System.out.println("ERROR obetner servicio : " + e.getMessage());
			e.printStackTrace();
		}		
	}
	
	@Test
	public void resetear(){
		try {
			IEjbClienteRemote ejb = obtenterServicio();
			System.out.println("Se ejecuta el test...");
			DTEstado e = ejb.resetearTodasLasPreguntas("1111");
			assert("ok".equals(e.getEstado()));
		} catch (NamingException e) {
			System.out.println("ERROR obetner servicio : " + e.getMessage());
			e.printStackTrace();
		}		
	}
	
	@Test
	public void eliminarCliente(){
		try {
			IEjbClienteRemote ejb = obtenterServicio();
			System.out.println("Se ejecuta el test...");
			DTEstado e = ejb.borrarCliente("1111");
			assert("ok cliente borrado".equals(e.getEstado()));
		} catch (NamingException e) {
			System.out.println("ERROR obetner servicio : " + e.getMessage());
			e.printStackTrace();
		}		
	}
	
}

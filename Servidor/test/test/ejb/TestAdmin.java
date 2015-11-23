package test.ejb;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.Test;

import ejb.remote.IEjbAdminRemote;

public class TestAdmin {
	
	private IEjbAdminRemote obtenterServicio() throws NamingException {

		final InitialContext context = TestUtils.createContext("localhost:4447");
		//final InitialContext context = TestUtils.createContext("localhost:4447");
		final String solrEjbService = "Servidor/EjbAdmin!" + IEjbAdminRemote.class.getName();
		System.out.println("context.lookup.("+solrEjbService+")");
		IEjbAdminRemote remote = (IEjbAdminRemote) context.lookup(solrEjbService);
		System.out.println("Servicio inicializado");
		return remote;
		// }
	}
	
	@Test
	public void loginAdmin(){
		try {
			IEjbAdminRemote ejb = obtenterServicio();
			System.out.println("Se ejecuta el test...");
			ejb.CrearAdmin("admin", "admin");
			assert(ejb.existeUsuario("admin") & ejb.esValidaPassword("admin", "amdin"));
		} catch (NamingException e) {
			System.out.println("ERROR obetner servicio : " + e.getMessage());
			e.printStackTrace();
		}
		
	}
	

}

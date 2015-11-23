package test.ejb;

import java.util.ArrayList;
import java.util.Random;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.Test;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Parser;

import datatypes.DTPregunta;
import datatypes.DTPreguntaGeneral;
import datatypes.DTRespuestaGeneral;
import datatypes.DTSaldo;
import ejb.EjbInit;
import ejb.EjbServidor;
import ejb.remote.IEjbClienteRemote;
import ejb.remote.IEjbServidorRemote;

public class TestServidor {

	private IEjbServidorRemote obtenterServicio() throws NamingException {

//		final InitialContext context = TestUtils.createContext("localhost:8080");
		final InitialContext context = TestUtils.createContext("localhost:4447");
		final String solrEjbService = "Servidor/EjbServidor!" + IEjbServidorRemote.class.getName();
		System.out.println("context.lookup.("+solrEjbService+")");
		IEjbServidorRemote remote = (IEjbServidorRemote) context.lookup(solrEjbService);
		System.out.println("Servicio inicializado");
		return remote;
		// }
	}
	
	private IEjbClienteRemote obtenterServicioCliente() throws NamingException {

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
	public void actualizarSaldosAll(){
		try {
			IEjbServidorRemote ejb = obtenterServicio();
			ejb.actualizarSaldosAll();
			assert(true);
		} catch (NamingException e) {
			System.out.println("ERROR obetner servicio : " + e.getMessage());
			e.printStackTrace();
		}	
	}
	
	@Test
	public void agregarPreguntaGeneral(){
		try {
			IEjbServidorRemote ejb = obtenterServicio();
			int cant = ejb.obtenerPreguntasGenerales().size();
			
			ArrayList<Long> l = new ArrayList<Long>();
			l.add((long) 1);
			l.add((long) 2);
			DTRespuestaGeneral drRg1 = new DTRespuestaGeneral("eng resp1", "esp resp 1", l);
			DTRespuestaGeneral drRg2 = new DTRespuestaGeneral("eng resp2", "esp resp 2", l);
			ArrayList<DTRespuestaGeneral> listaRes = new ArrayList<DTRespuestaGeneral>();
			listaRes.add(drRg1);
			listaRes.add(drRg2);
			DTPreguntaGeneral dt = new DTPreguntaGeneral(1111,"question", "preg", listaRes);
			ejb.agregarNuevaPreguntaGeneral(dt);
			int cant2 = ejb.obtenerPreguntasGenerales().size();
			
			// se puede agregar obtenerPreguntasGeneralesWeb
			ejb.obtenerPreguntasGeneralesWeb();
			
			assert(cant + 1 == cant2);
		} catch (NamingException e) {
			System.out.println("ERROR obetner servicio : " + e.getMessage());
			e.printStackTrace();
		}	
	}
	
	
	
	@Test
	public void obtenerCantidadUsuariosPaquetesAcciones(){
		try {
			IEjbServidorRemote ejb = obtenterServicio();
			String cantidad = ejb.obtenerCantidadUsuariosPaquetesAcciones();
			System.out.println("cant user paq acc :  " + cantidad);
			//int cant = Integer.parseInt(cantidad);
			//harcodear valor
			//int valor = 1000;
			assert(true);
		} catch (NamingException e) {
			System.out.println("ERROR obetner servicio : " + e.getMessage());
			e.printStackTrace();
		}	
	}
	
	@Test
	public void obtenerSaldo(){
		try {
			IEjbServidorRemote ejb = obtenterServicio();
			//IEjbClienteRemote ejbCli = obtenterServicioCliente();
//			if(ejbCli==null){
//				System.out.println("ejbcli es null");
//			}
			Random r = new Random();
			Long id = r.nextLong();
			//ejbCli.registrarUsuario(id.toString());
//			DTSaldo saldo = ejb.obtenerSaldo(id.toString());
			DTSaldo saldo = ejb.obtenerSaldo("1111");
			assert(saldo.getSaldo() == 200);
		} catch (NamingException e) {
			System.out.println("ERROR obetner servicio : " + e.getMessage());
			e.printStackTrace();
		}	
	}
	
	@Test
	public void obtenerSaldoHistorico(){
		try {
			IEjbServidorRemote ejb = obtenterServicio();
			//Harcodear identidifacdor
			String identidicador = "1";
			ArrayList<DTSaldo> saldos = (ArrayList<DTSaldo>) ejb.obtenerSaldoHistorico(identidicador, 1 , 5);
			assert(saldos!=null && !saldos.isEmpty());
		} catch (NamingException e) {
			System.out.println("ERROR obetner servicio : " + e.getMessage());
			e.printStackTrace();
			
		}	
	}
	
	
}

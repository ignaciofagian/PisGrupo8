package test.model;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;

import model.Accion;
import model.Cliente;
import model.ClientePaquete;
import model.Historico;
import model.IProveedorValor;
import model.Paquete;



public class SaldoSplitTest {

	class TestValorProvider implements IProveedorValor {

		@Override
		public double getValor(long accionId, Calendar fecha) {
			Historico h = findHistorico(accionId, fecha);
			return h.getAdjClose();

		}

		private Accion getApple() {
			Accion apple = new Accion();
			apple.setId(1L);
			apple.setSymbol("APPL");
			apple.setNombre("Apple");
			return apple;
		}

		private Accion getCash() {
			Accion cash = new Accion();
			cash.setId(2L);
			cash.setSymbol("$");
			cash.setNombre("$");
			return cash;
		}

		public Historico findHistorico(long accionId, Calendar fecha) {
			Accion apple = getApple();
			Calendar t1 = new GregorianCalendar(2014, Calendar.JUNE, 06);
			Historico h1 = new Historico(apple, 90.225449, 645.570023, t1.getTime());
			Calendar t2 = new GregorianCalendar(2014, Calendar.JUNE, 07);
			Historico h2 = new Historico(apple, 91.669173, 93.699997, t2.getTime());
			Calendar t_ultimo = new GregorianCalendar(2015, Calendar.SEPTEMBER, 15);
			Historico h_ultimo = new Historico(apple, 114.209999, 114.209999, t_ultimo.getTime());
			if (accionId == 1) {
				if (fecha.after(t_ultimo)) {
					return h_ultimo;
				} else if (fecha.after(t1)) {
					return h2;
				} else {
					return h1;
				}
			} else {
				return new Historico(getCash(), 100, 100, fecha.getTime());
			}
		}

		
		public double getAdjFactor(Calendar t1, Calendar t2, long accionId) {

			Historico h1 = findHistorico(accionId, t1);
			Historico h2 = findHistorico(accionId, t2);
			//double adj1 = h1.getClose() / h1.getAdjClose();
			//double adj2 = h2.getClose() / h2.getAdjClose();
			
			return Historico.getAdjFactor(h1, h2);//adj1 / adj2;

		}

		@Override
		public double getValorSinAdj(long accionId, Calendar fecha) {
			// TODO Auto-generated method stub
			Historico h = findHistorico(accionId, fecha);
			return h.getClose();
		}

	}

	@Test
	public void testAdjFactor() {
		TestValorProvider testProv = new TestValorProvider();
		Calendar t1 = new GregorianCalendar(2014, Calendar.JUNE, 6);
		Calendar t2 = new GregorianCalendar(2015, Calendar.JUNE, 9);
		double adj = testProv.getAdjFactor(t1, t2, 1);
		assertEquals("Error en adj factor", 7, adj, 0.01);
	}

	@Test
	public void testSplitSimple() {
		TestValorProvider testProv = new TestValorProvider();
		Accion apple = testProv.getApple();
		Paquete paquete = new Paquete();
		paquete.agregarAccion(apple, 100);
		Cliente cli = new Cliente();
		double inversion = 100.0;
		ClientePaquete cli_apple = ClientePaquete.generar(cli, paquete, inversion);
		Calendar t1 = new GregorianCalendar(2014, Calendar.JUNE, 6);
		Calendar t2 = new GregorianCalendar(2015, Calendar.JUNE, 9);
		cli_apple.actualizarInversion(t1, t2, testProv);
		double inversionEsperada = inversion * (91.669173 / 90.225449);
		double inversionRes = cli_apple.getInversion();
		assertEquals("Error en split", inversionEsperada, inversionRes, 0.0000001);
	}

	@Test
	public void testSplitComplejo() {
		TestValorProvider testProv = new TestValorProvider();
		Accion apple = testProv.getApple();
		Paquete paquete = new Paquete();
		paquete.agregarAccion(apple, 50);
		paquete.agregarAccion(testProv.getCash(), 50);
		Cliente cli = new Cliente();
		double inversion = 100.0;
		ClientePaquete cli_apple = ClientePaquete.generar(cli, paquete, inversion);
		Calendar t1 = new GregorianCalendar(2014, Calendar.JUNE, 6);
		Calendar t2 = new GregorianCalendar(2015, Calendar.JUNE, 9);
		cli_apple.actualizarInversion(t1, t2, testProv);
		double inversionEsperada = inversion * (91.669173 / 90.225449) * 0.5 + 50;
		double inversionRes = cli_apple.getInversion();
		assertEquals("Error en split", inversionEsperada, inversionRes, 0.0000001);
	}

}

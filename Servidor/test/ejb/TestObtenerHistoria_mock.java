package ejb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Date;



import org.junit.*;
import static org.junit.Assert.*;

import datatypes.DTEstado;
import datatypes.DTSaldo;
import ejb.IEjbCalculoSaldoHist;
import model.Accion;
import model.Historico;
import model.PaqueteAlgoritmico;


public class TestObtenerHistoria_mock {
	private IEjbCalculoSaldoHist mockSH(){

		
		return new IEjbCalculoSaldoHist() {
			

			@Override
			public List<Historico> consultaUltimoHistoricoPorIntervaloDia(Calendar inicio, Calendar fin,List<Long> idAcciones) {
				ArrayList<Historico> hlist = new ArrayList<Historico>();
				Calendar f = (Calendar)inicio.clone();
				f.set(Calendar.HOUR_OF_DAY,0);
				Accion a = new Accion();
				a.setId(idAcciones.get(0));
				a.setSymbol(idAcciones.get(0).toString());
				while(!f.after(fin)){
					if (!f.before(inicio)){
					double val = f.get(Calendar.YEAR) * 10000 + (f.get(Calendar.MONTH) +1 ) * 100 + f.get(Calendar.DATE);
					Historico h = new Historico(a,val,val,(Date)f.getTime().clone());
					hlist.add(h);
					}
					f.add(Calendar.DATE, 1);
				}
				return hlist;
				
			}
			
			@Override public void pruebaDatosHistoricos(String id) {}
			@Override public DTEstado maquinaTiempo(String iden, String fecha) {return null;}
			@Override public void crearHistoriaDiariaAlgoritmo(PaqueteAlgoritmico p) { }
			@Override public void configSaldoHistorico(String identificador, Calendar fecha) {}
			@Override public void calcularHistoriaAlgoritmos() {}
			@Override public List<DTSaldo> avanzarProximaFecha(String identificador, String proxFecha) {return null;}

			@Override
			public void calcularEvolucionAlgoritmo(PaqueteAlgoritmico pa) { 	}
		};
	}
	
	
	
	IEjbCalculoSaldoHist sh;
	EjbAlgoritmo ejbA;
	
	@Before
	public  void init(){
		sh = mockSH();
		ejbA = new EjbAlgoritmo();
		ejbA.ejbSH = sh;
		ejbA.ejbS = new EjbServidor();
	}
	
	 @Test 
	public void ObtenerHistoria_mock(){
		Calendar ini = new GregorianCalendar(2015,Calendar.NOVEMBER,16);
		Calendar fin = new GregorianCalendar(2015,Calendar.NOVEMBER,20);
		PaqueteAlgoritmico p = new PaqueteAlgoritmico();
		Accion a = new Accion();
		a.setId(1L);
		p.setAccionHistoria(a);
		Map<Integer,Double> res = ejbA.obtenerHistoria(p, -4, 0, fin);
		assertEquals("Tiene 5 elementos",5,res.size());
		assertEquals("-4",Double.valueOf(20151116),res.get(-4) );
		assertEquals("-3",Double.valueOf(20151117),res.get(-3) );
		assertEquals("-2",Double.valueOf(20151118),res.get(-2) );
		assertEquals("-1",Double.valueOf(20151119),res.get(-1) );
		assertEquals("0",Double.valueOf(20151120),res.get(0) );
	}
	 
	 @Test
	public void ObtenerHistoria_weekend(){
		Calendar fin = new GregorianCalendar(2015,Calendar.NOVEMBER,22);
		PaqueteAlgoritmico p = new PaqueteAlgoritmico();
		Accion a = new Accion();
		a.setId(1L);
		p.setAccionHistoria(a);
		Map<Integer,Double> res = ejbA.obtenerHistoria(p, -4, 0, fin);
		assertEquals("Tiene 5 elementos",5,res.size());
		assertEquals("-4",Double.valueOf(20151116),res.get(-4) );
		assertEquals("-3",Double.valueOf(20151117),res.get(-3) );
		assertEquals("-2",Double.valueOf(20151118),res.get(-2) );
		assertEquals("-1",Double.valueOf(20151119),res.get(-1) );
		assertEquals("0",Double.valueOf(20151120),res.get(0) );
	}
	 
	@Test
	public void ObtenerHistoria_weekend2(){
		Calendar fin = new GregorianCalendar(2015,Calendar.NOVEMBER,17);
		PaqueteAlgoritmico p = new PaqueteAlgoritmico();
		Accion a = new Accion();
		a.setId(1L);
		p.setAccionHistoria(a);
		Map<Integer,Double> res = ejbA.obtenerHistoria(p, -4, 0, fin);
		assertEquals("Tiene 5 elementos",5,res.size());
		assertEquals("-4",Double.valueOf(20151111),res.get(-4) );
		assertEquals("-3",Double.valueOf(20151112),res.get(-3) );
		assertEquals("-2",Double.valueOf(20151113),res.get(-2) );
		assertEquals("-1",Double.valueOf(20151116),res.get(-1) );
		assertEquals("0",Double.valueOf(20151117),res.get(0) );
	}
}

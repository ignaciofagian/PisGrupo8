package ejb;

import interprete.Category;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.xml.datatype.XMLGregorianCalendar;

import datatypes.DTPregunta;
import datatypes.DTSaldo;
import model.Paquete;
import model.PaqueteCategoria;
import model.Pregunta;
import model.PreguntaEspecifica;
import model.PreguntaGeneral;

@Local
public interface IEjbServidor {

	
	
	public DTSaldo obtenerSaldo(String identificador);
	public List<DTSaldo> obtenerSaldoHistorico(String identificador, long desdeFecha,long hastaFecha);
	
	public ArrayList<DTPregunta> obtenerPreguntasGenerales();
	public void actualizarSaldosAll();
	public void persistirArbol(Category c);
	
	boolean estaAbiertoStockExchangeAhora();

	List<DTSaldo> obtenerSaldoHistorico2(String identificador, String desdeFecha, String hastaFecha);

	
}

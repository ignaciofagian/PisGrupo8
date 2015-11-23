package ejb;

import interprete.Category;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Local;
import javax.xml.datatype.XMLGregorianCalendar;

import datatypes.DTPregunta;
import datatypes.DTPreguntaGeneral;
import datatypes.DTSaldo;
import model.Historico;
import model.Paquete;
import model.PaqueteCategoria;
import model.Portafolio;
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

	public List<Historico> obtenerHistorico(Calendar desde, Calendar hasta, List<Long> acciones);
	boolean estaAbiertoStockExchange(Calendar ahora);


	public String obtenerCantidadUsuariosPaquetesAcciones();
	
	public void agregarNuevaPreguntaGeneral(DTPreguntaGeneral dtpg);
	
	public  ArrayList<DTPreguntaGeneral> obtenerPreguntasGeneralesWeb();
	
	public void intentarBorrarPreguntasGeneralesDeprecated();
	
	public void borrarPreguntaGeneral(long idPregunta);
	Calendar horaCierreMercado(String ymd);

	boolean abreStockExchangeDia(Calendar c);
	
}

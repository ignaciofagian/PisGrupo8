package ejb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import datatypes.DTEstado;
import datatypes.DTPaquete;
import datatypes.DTPaqueteDetalle;
import datatypes.DTPaqueteIdNombre;
import model.Accion;
import model.Historico;
import model.Paquete;
import model.PaqueteAlgoritmico;
import model.PaqueteCategoria;
import model.Portafolio;

@Local
public interface IEjbPaquete {
	public ArrayList<Paquete> obtenerPaquetesPorCategoria(ArrayList<PaqueteCategoria> categorias, Calendar fecha);
	public ArrayList<PaqueteAlgoritmico> obtenerPaquetesAlgoritmicos();
	public ArrayList<Accion> obtenerConjuntoAccionesUsadasPorPaquetes();
	public double obtenerVariacionPaquete(Calendar t1, Calendar t2, Paquete paquete);
	void actualizarValorPaquetesAlgoritmicos();
	void setInversionTodosLosPaquetes(String idCliente, int inversion);


	public  Set<String> symAccionesEnHistorico(Calendar desde,Calendar hasta);
	void actualizarAccionDesdeYahooYAgregarHistorico(Accion accion, Date ahora, double valor, List<Historico> antiguos);
	public Paquete paquetePorNombre(String string);

	public ArrayList<PaqueteCategoria> obtenerCategoriasDePaquetes();
	public void crearPaquete(DTPaquete dtPaquete);
	public ArrayList<DTPaqueteIdNombre> obtenerPaquetesConIdNombre();
	public DTPaquete obtenerPaquete(long idPaquete);
	public ArrayList<DTPaqueteDetalle> obtenerPaquetesConDetalle();
	public void intentarBorrarPaquetesDeprecated();
	public void borrarPaquete(long idPaquete);
	public void calcularFechaPaquetes();
	
	
	public Calendar getPrimerFechaPaquete(Paquete p);
	public Calendar getPrimerFechaHistoria(PaqueteAlgoritmico p);
	public void pedidoDeValoresYahoo(boolean calcularSaldos, Calendar fechaDatosHistoricos);


}

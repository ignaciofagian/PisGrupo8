package ejb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Local;

import model.Accion;
import model.Historico;
import model.Paquete;
import model.PaqueteAlgoritmico;
import model.PaqueteCategoria;

@Local
public interface IEjbPaquete {
	public ArrayList<Paquete> obtenerPaquetesPorCategoria(ArrayList<PaqueteCategoria> categorias);
	public ArrayList<PaqueteAlgoritmico> obtenerPaquetesAlgoritmicos();
	public ArrayList<Accion> obtenerConjuntoAccionesUsadasPorPaquetes();
	public void actualizarAccionDesdeYahooYAgregarHistorico(Accion accion, double valor,List<Historico> antiguos);
	public double obtenerVariacionPaquete(Calendar t1, Calendar t2, Paquete paquete);
	void actualizarValorPaquetesAlgoritmicos();
	void setInversionTodosLosPaquetes(String idCliente, int inversion);
	void pedidoDesdeHistorico();
}

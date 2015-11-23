package ejb;

import java.util.Calendar;
import java.util.List;

import datatypes.DTEstado;
import datatypes.DTSaldo;
import model.Historico;
import model.PaqueteAlgoritmico;

public interface IEjbCalculoSaldoHist {

	void pruebaDatosHistoricos(String id);
	public void configSaldoHistorico(String identificador,Calendar fecha);
	public DTEstado maquinaTiempo(String iden,String fecha);
	void crearHistoriaDiariaAlgoritmo(PaqueteAlgoritmico p);
	void calcularHistoriaAlgoritmos();
	List<DTSaldo> avanzarProximaFecha(String identificador, String proxFecha);
	List<Historico> consultaUltimoHistoricoPorIntervaloDia(Calendar inicio, Calendar fin, List<Long> idAcciones);
	void calcularEvolucionAlgoritmo(PaqueteAlgoritmico pa);
}
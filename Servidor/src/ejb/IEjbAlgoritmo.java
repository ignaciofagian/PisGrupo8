package ejb;

import java.util.Calendar;
import java.util.Map;

import javax.ejb.Local;

import auxiliar.CacheTreeMap;

@Local
public interface IEjbAlgoritmo {

	public void buy(long idPaquete, double monto);
	
	public void sell(long idPaquete, double monto);
	
	public Map<Integer, Double> obtenerHistoria(long idPaquete, int comienzo, int fin,Calendar fecha);

	void setCache(CacheTreeMap val);

}

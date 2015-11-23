package auxiliar;

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import model.Historico;
import model.IProveedorValor;

public class CacheHistPorFecha implements IProveedorValor{
	private TreeMap<Calendar,Map<Long,Historico>> cache;
	
	public void agregarHistorico(Historico h){
		Long accKey = h.getAccion().getId();
		GregorianCalendar timeKey = new GregorianCalendar();
		timeKey.setTime(h.getFecha());
		timeKey.set(Calendar.SECOND, 0);
		timeKey.set(Calendar.MILLISECOND,0);
		if (!cache.containsKey(timeKey)){
			cache.put(timeKey, new HashMap<Long, Historico>());
		}
		cache.get(timeKey).put(accKey,h);
	}

	@Override
	public double getValor(long accionId, Calendar fecha) {
		return findHistorico(accionId, fecha).getAdjClose();
	}

	@Override
	public double getValorSinAdj(long accionId, Calendar fecha) {
		return findHistorico(accionId, fecha).getClose();
	}

	@Override
	public Historico findHistorico(long accionId, Calendar fecha) {
		Calendar aprox = cache.floorKey(fecha);
		if (aprox == null || !cache.get(aprox).containsKey(accionId)){
			aprox = cache.higherKey(fecha);
		}
		if (aprox == null || !cache.get(aprox).containsKey(accionId)){
			return null;
		}
		return cache.get(aprox).get(accionId);
	}
	
	public Set<Calendar> getFechas(){
		return cache.keySet();
	}


}

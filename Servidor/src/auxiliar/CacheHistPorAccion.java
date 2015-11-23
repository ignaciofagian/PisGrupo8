package auxiliar;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TreeMap;

import model.Historico;
import model.IProveedorValor;

public class CacheHistPorAccion extends ProvBase{
	private Map<Long,TreeMap<Calendar,Historico>> cache;
	private Calendar max;
	
	public void agregarHistorico(Historico h){
		Long accKey = h.getAccion().getId();
		GregorianCalendar timeKey = new GregorianCalendar();
		timeKey.setTime(h.getFecha());
		timeKey.set(Calendar.SECOND, 0);
		timeKey.set(Calendar.MILLISECOND,0);
		if (!cache.containsKey(accKey)){
			cache.put(accKey, new TreeMap<Calendar, Historico>());
		}
		cache.get(accKey).put(timeKey, h);
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
	protected Historico findHistorico_interno(long accionId, Calendar fecha) {
		
		if  (max != null && fecha.after(max)) return null;
		TreeMap<Calendar, Historico> historicos = cache.get(accionId);
		Map.Entry<Calendar,Historico>  entry;
		entry = historicos.floorEntry(fecha);
		if (entry == null){
			// devuelvo de la fecha mas cercana
			entry = historicos.ceilingEntry(fecha);
		}
		return (entry == null) ? null : entry.getValue();
	}


}

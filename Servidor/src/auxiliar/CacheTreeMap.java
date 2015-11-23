package auxiliar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import model.Historico;

public class CacheTreeMap extends ProvBase{
	private Calendar max;
	TreeMap<Calendar,Historico> cache = new TreeMap<Calendar, Historico>();
	
	public void agregarList(List<Historico> hlist){
		for(Historico h : hlist){
			agregarHistorico(h);
		}
	}
	
	public void agregarHistorico(Historico h){
		
			GregorianCalendar timeKey = new GregorianCalendar();
			timeKey.setTime(h.getFecha());
			timeKey.set(Calendar.SECOND, 0);
			timeKey.set(Calendar.MILLISECOND,0);
			if (!cache.containsKey(timeKey)){
				cache.put(timeKey,h);
			}
			cache.put(timeKey, h);
		}
	
	@Override
	protected Historico findHistorico_interno(long accionId, Calendar fecha) {
		// TODO Auto-generated method stub
		if  (max != null && fecha.after(max)) return null;
		Map.Entry<Calendar,Historico>  entry;
		entry = cache.floorEntry(fecha);
		if (entry == null){
			// devuelvo de la fecha mas cercana
			entry = cache.ceilingEntry(fecha);
		}
		return (entry == null) ? null : entry.getValue();
	}
	
	public Collection<Calendar> fechas(){
		return cache.keySet();
	}
	
	public List<Historico> acceder(Calendar inicio,Calendar fin,Long idAccion){
			Calendar c = (Calendar)inicio.clone();
			ArrayList<Historico> res = new ArrayList<Historico>();
			while(fin.after(c)){
				res.add(findHistorico(idAccion, c));
				c.add(Calendar.DATE, 1);
			}
			return res;
		}

}

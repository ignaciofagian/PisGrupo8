package auxiliar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Historico;
import model.IProveedorValor;

public class CacheHistPorFecha2 extends ProvBase implements Comparator<Historico>{
	private Map<Long,Map<Long,Historico>> cache = new HashMap<Long, Map<Long,Historico>>();
	private List<Calendar> fechas = new ArrayList<Calendar>();
	
	public void setearHistoricos(List<Historico> lista,boolean isAsc){
		if (!isAsc){
			Collections.sort(lista,this);
		}
		Calendar clave = new GregorianCalendar(1900,0,1);
		 Map<Calendar,Map<Long,Historico>> tmp = new HashMap<Calendar, Map<Long,Historico>>();
		for(Historico h : lista){
			if (h.getFecha().getTime() - clave.getTimeInMillis() > 60000){
				clave = new GregorianCalendar();
				clave.setTime(h.getFecha());
			}
			Long accKey = h.getAccion().getId();
			if (!tmp.containsKey(clave)){
				tmp.put(clave, new HashMap<Long, Historico>());
			}
			tmp.get(clave).put(accKey,h);
		}
		for(Calendar c : tmp.keySet()){
			Calendar key = (Calendar)c.clone();
			key.set(Calendar.SECOND, 0);
			key.set(Calendar.MILLISECOND, 0);
			cache.put(key.getTimeInMillis(), tmp.get(c));
			fechas.add(key);
		}
		Collections.sort(fechas);
	}

	@Override
	public double getValor(long accionId, Calendar fecha) {
		Historico h = null;
		try{
			h = super.findHistorico(accionId, fecha);
			return h.getAdjClose();
		}
		catch(NullPointerException ex){
			System.out.println(ex.getMessage());
			System.out.println("H = " + ((h == null) ? "NULL" : h.toString()));
			System.out.println("acc=" + accionId + " fecha=" + fecha.getTime().toString());
			return 1;
		}
	}

	@Override
	public double getValorSinAdj(long accionId, Calendar fecha) {
		return findHistorico(accionId, fecha).getClose();
	}
	
	public List<Calendar> getFechas(){
		return fechas;
	}
	
	public Map<Long,Historico> getMap(Calendar cal){
		return cache.get(cal.getTimeInMillis());
	}

	@Override
	public int compare(Historico o1, Historico o2) {
		// TODO Auto-generated method stub
		return o1.getFecha().compareTo(o2.getFecha());
	}

	@Override
	protected Historico findHistorico_interno(long accionId, Calendar fecha) {
		Map<Long,Historico> m =  cache.get(fecha.getTimeInMillis());
		return m == null ? null : m.get(accionId);
	}


}

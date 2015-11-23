package auxiliar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Singleton;

import ejb.IEjbCalculoSaldoHist;
import model.Historico;
import model.IProveedorValor;

@Singleton
/**
 * Codigo en progreso para cachear la historia del algo si es demasiado lento obtenerla 
 * @author guillermo
 *
 */
public class CacheSingleton {
	
	
	@EJB  IEjbCalculoSaldoHist sh;
	private Map<Long,IProveedorValor> cache;
	private Map<Long,Calendar> max;
	
	
	public void cargar(Calendar inicio, Calendar fin,Long idAccion){
		List<Historico> h = sh.consultaUltimoHistoricoPorIntervaloDia(inicio, fin, Arrays.asList(idAccion));
		CacheHistPorAccion ch = new CacheHistPorAccion();
		cache.put(idAccion, ch );
	}
	
	public void borrar(Long idAccion){
		cache.remove(idAccion);
	}
	
	public List<Historico> acceder(Calendar inicio,Calendar fin,Long idAccion){
		if (fin.after(max)) {
			return sh.consultaUltimoHistoricoPorIntervaloDia(inicio, fin, Arrays.asList(idAccion));
		}
		else{
			Calendar c = (Calendar)inicio.clone();
			ArrayList<Historico> res = new ArrayList<Historico>();
			while(!fin.after(c)){
				res.add(cache.get(idAccion).findHistorico(idAccion, c));
				c.add(Calendar.DATE, 1);
			}
			return res;
			
			}
		}
}

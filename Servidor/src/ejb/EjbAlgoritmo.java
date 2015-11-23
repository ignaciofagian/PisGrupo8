package ejb;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import auxiliar.CacheTreeMap;
import model.Historico;
import model.IProveedorValor;
import model.Paquete;
import model.PaqueteAlgoritmico;

@Stateless
public class EjbAlgoritmo implements IEjbAlgoritmo {
	@PersistenceContext( unitName = "Servidor")
	private EntityManager em;

	@Override
	public void buy(long idPaquete, double monto) {
		
		if (monto < 0) {
			this.sell(idPaquete, Math.abs(monto));
			return;
		}
				
		Query q = em.createQuery("select p from Paquete p where p.id = :idPaquete").setParameter("idPaquete",
				idPaquete);
		
		PaqueteAlgoritmico paquete = (PaqueteAlgoritmico) q.getSingleResult();
		
		paquete.comprar(monto);
		
	}

	@Override
	public void sell(long idPaquete, double monto) {
		
		if (monto < 0) {
			this.buy(idPaquete, Math.abs(monto));
			return;
		}
		
		Query q = em.createQuery("select p from Paquete p where p.id = :idPaquete").setParameter("idPaquete",
				idPaquete);
		
		PaqueteAlgoritmico paquete = (PaqueteAlgoritmico) q.getSingleResult();
		
		paquete.vender(monto);
		
	}
	
	@EJB IEjbServidor ejbS;
	@EJB IEjbCalculoSaldoHist ejbSH;
	

	private void ajustarCalendarHabil(Calendar fecha,int diasNeg){
		// el metodo abreStock no requiere ir a la BD (en esta implementacion) asi que es liviano
		int f = 0;
		while(f > diasNeg || !ejbS.abreStockExchangeDia(fecha)) {
			if (ejbS.abreStockExchangeDia(fecha)) f--;
			fecha.add(Calendar.DATE, -1);
		}
	}

	CacheTreeMap cache;
	@Override
	public void setCache(CacheTreeMap val){
		cache = val;
	}
	
	Map<Integer, Double> obtenerHistoria(PaqueteAlgoritmico p, int comienzo, int fin,Calendar fecha){
		Calendar t_fin = (Calendar)fecha.clone();
		//if (fin < 0){
			t_fin.set(Calendar.HOUR_OF_DAY, 23);
			ajustarCalendarHabil(t_fin, fin);
		//}

		Calendar t_inicio = (Calendar)fecha.clone();
		t_inicio.set(Calendar.HOUR_OF_DAY,0);
		t_inicio.set(Calendar.MINUTE,0);
		ajustarCalendarHabil(t_inicio, comienzo);
	    List<Historico> lst;
	    if (cache == null){
	    	lst = ejbSH.consultaUltimoHistoricoPorIntervaloDia(t_inicio, t_fin, Arrays.asList(p.getAccionHistoria().getId()));
	    }
	    else{
	    	lst = cache.acceder(t_inicio, t_fin,p.getAccionHistoria().getId());
	    }
	    HashMap<Integer,Double> resp = new HashMap<Integer, Double>();
	    if (lst.isEmpty()){
	    	String msg = "Lista vacia!!!@idPaq="+p.getId()+" [" + comienzo + "," + fin + "] " + fecha.getTime().toString();
	    	System.out.println(msg); 
	    	// pongo un 1 cte para que no se caiga, pero es un error
	    	 for(int i = fin; i >= comienzo; i--) resp.put(i, 1.0);
	    	 return resp;
	    }
	    int i = fin;
	    // agrego los elementos controlando que abra el mercado ese dia
	    for(int idx = lst.size() - 1;idx >= 0 && i >= comienzo; idx--){
	    	Historico h = lst.get(idx); 
	    	Calendar c = new GregorianCalendar();
	    	c.setTime(h.getFecha());
	    	if (ejbS.abreStockExchangeDia(c)){
	    		resp.put(i, h.getAdjClose());
	    		i--;
	    	} 
	    }
	    // relleno con el elemento mas antiguo si me faltan
	    while(i >= comienzo){
	    	resp.put(i, lst.get(0).getAdjClose());
	    	i--;
	    }
	    return resp;
	}
	
	@Override
	public Map<Integer, Double> obtenerHistoria(long idPaquete, int comienzo, int fin,Calendar fecha) {
		// TODO Auto-generated method stub

		PaqueteAlgoritmico p = (PaqueteAlgoritmico)em.find(PaqueteAlgoritmico.class, idPaquete);
		return obtenerHistoria(p, comienzo, fin, fecha);
	}

	@Override
	public void buy(PaqueteAlgoritmico pa, double monto) {

		if (monto < 0) {
			this.sell(pa, Math.abs(monto));
			return;
		}
		pa.comprar(monto);	
	}

	@Override
	public void sell(PaqueteAlgoritmico pa, double monto) {

		if (monto < 0) {
			this.buy(pa, Math.abs(monto));
			return;
		}
		pa.vender(monto);
		
	}
}

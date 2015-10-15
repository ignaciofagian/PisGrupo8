package model;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import model.PK.HistoricoPK;

public class JPAProveedorValor implements IProveedorValor {
	private EntityManager em;
	
	
	
	public JPAProveedorValor(EntityManager em) {
		super();
		this.em = em;
	}


	
	@Override
	public double getValor(long accionId, Calendar fecha) {
		Historico h = findHistorico(accionId,fecha);
		return h.getAdjClose();
	
	}
	

	public Historico findHistorico(long accionId, Calendar fecha) {
		boolean altaResolucion = fecha.after(new GregorianCalendar(2015, Calendar.OCTOBER, 10));
		GregorianCalendar cal = new GregorianCalendar(fecha.get(Calendar.YEAR), fecha.get(Calendar.MONTH),
				fecha.get(Calendar.DAY_OF_MONTH));
		if (altaResolucion) {
			// tengo datos al minuto
			cal.add(Calendar.HOUR, fecha.get(Calendar.HOUR_OF_DAY));
			cal.add(Calendar.MINUTE, fecha.get(Calendar.MINUTE));
		}
		Historico h = null;
		String query = "Select h from Historico h Where h.accion.id = :accId And h.fecha = :t ";
		if (!altaResolucion) {
			HistoricoPK hid = new HistoricoPK();
			hid.setAccion(accionId);
			hid.setFecha(cal.getTime());
			h = em.find(Historico.class, hid);
		}
		if (h != null) {
			return h;
		} else {
			query = "Select h from Historico h Where h.accion.id = :accId And h.fecha <= :t Order By h.fecha DESC";

			TypedQuery<Historico> q = em.createQuery(query, Historico.class);
			q.setParameter("accId", accionId);
			q.setParameter("t", fecha.getTime(), TemporalType.TIMESTAMP);
			q.setMaxResults(1); // LIMIT 1
			List<Historico> lst_res = q.getResultList();
			if (lst_res.isEmpty()) return null;
			h = lst_res.get(0);
			return h;
		}
	}

	/*private List<Historico> findHistoricos(long accionId,Calendar t1,Calendar t2){
		TypedQuery<Historico> q = em.createQuery("Select h from Historico Where h.accion.id = :accId And h.fecha IN (:t1,:t2)",Historico.class);
		q.setParameter("accId", accionId);
		q.setParameter("t1", t1, TemporalType.TIMESTAMP);
		q.setParameter("t2", t1, TemporalType.TIMESTAMP);
		List<Historico> res = q.getResultList();
		
	}*/

	@Override
	public double getAdjFactor(Calendar t1, Calendar t2, long accionId) {
		// 
		Historico h1 = findHistorico(accionId,t1);
		Historico h2 = findHistorico(accionId,t2);
		//if (h1 == null | h2 == null) return 1;
		//return (h2.getClose() / h2.getAdjClose()) / (h1.getClose() / h1.getAdjClose());
		return Historico.getAdjFactor(h1, h2);
		
	}

	@Override
	public double getValorSinAdj(long accionId, Calendar fecha) {
		// TODO Auto-generated method stub
		Historico h = findHistorico(accionId,fecha);
		return h.getClose();
	}

}

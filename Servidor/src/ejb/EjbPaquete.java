package ejb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;

import model.Accion;
import model.Cliente;
import model.ClientePaquete;
import model.Historico;
import model.JPAProveedorValor;
import model.Paquete;
import model.PaqueteAlgoritmico;
import model.PaqueteCategoria;
import yahoofinance.YahooFinance;

@Stateless
public class EjbPaquete implements IEjbPaquete {
	
	@PersistenceContext(unitName = "Servidor")
	private EntityManager em;
	
	@Override
	public ArrayList<Paquete> obtenerPaquetesPorCategoria(ArrayList<PaqueteCategoria> categorias)
	{	
		List<Long> ids = new ArrayList<Long>();
		for(int i = 0;i < categorias.size();i++)
		{
		   ids.add(categorias.get(i).getId());
		}
				
		Query q = em.createQuery("select paq from Paquete paq join paq.categorias cat where cat.id IN :listaIdCat");
		q.setParameter("listaIdCat", ids);
			
		ArrayList<Paquete> listaPaquetes = (ArrayList<Paquete>) q.getResultList();
		
		//Retorno todos los paquetes que cumplen con las categorias pasadas como parametro
		return listaPaquetes;
	}
	
	@Override
	public ArrayList<PaqueteAlgoritmico> obtenerPaquetesAlgoritmicos()
	{
		Query q = em.createQuery("select pa from PaqueteAlgoritmico pa");
		ArrayList<PaqueteAlgoritmico> listaPaquetes = (ArrayList<PaqueteAlgoritmico>) q.getResultList();
		
		return listaPaquetes;
	}
	
	@Override
	public ArrayList<Accion> obtenerConjuntoAccionesUsadasPorPaquetes()
	{
		Query q = em.createQuery("select a from Accion a");
		
		ArrayList<Accion> accionesDelSistema = (ArrayList<Accion>) q.getResultList();
		
		return accionesDelSistema;	
	}

	
	public void actualizarAccionDesdeYahooYAgregarHistorico(Accion accion, double valor) {
		
		Historico historicoAccion;
		
		//Agrego al historico este valor
		historicoAccion = new Historico(accion, valor, valor, Calendar.getInstance().getTime());
		//Actualizo el valor de la accion
		accion.getHistoricos().add(historicoAccion);
		accion.setValorActual(valor);
		em.flush();
	//	em.merge(accion);
				
	}
	
	@Override
	public double obtenerVariacionPaquete(Calendar t1, Calendar t2, Paquete paquete)
	{
		JPAProveedorValor jpa = new JPAProveedorValor(em);
		return paquete.getValorRatio(t1, t2, jpa);
	}
	
	
	
	@Override
	public void actualizarValorPaquetesAlgoritmicos(){
		Calendar ahora = new GregorianCalendar();
		List<PaqueteAlgoritmico> todos = obtenerPaquetesAlgoritmicos();
		JPAProveedorValor jpa = new JPAProveedorValor(em);
		for(PaqueteAlgoritmico pa : todos){
			pa.calcularValorPorVarMercado(jpa, ahora);
		}
	}
	
	@Override
	public void setInversionTodosLosPaquetes(String idCliente,int inversion){
		Query q = em.createQuery("select c from Cliente c where c.idTelefono = :idTel").setParameter("idTel",
				idCliente);
		Cliente c = (Cliente) q.getSingleResult();
		for(ClientePaquete p : c.getPortafolio().getPaquetes()){
			p.asignarInversion(inversion);
		}
		c.getPortafolio().setSinInvertir(0);
	}
	
	public void pedidoDesdeHistorico(){
		List<Accion> accionesDelSistema = obtenerConjuntoAccionesUsadasPorPaquetes();
		Calendar ahora = Calendar.getInstance();
		for(Accion a : accionesDelSistema){
			JPAProveedorValor jpa = new JPAProveedorValor(em);
			Historico h = jpa.findHistorico(a.getId(), ahora);
		}
	}

	@Override
	public void actualizarAccionDesdeYahooYAgregarHistorico(Accion accion, double valor, List<Historico> antiguos) {
		actualizarAccionDesdeYahooYAgregarHistorico(accion, valor);
		for(Historico h : antiguos){
			em.merge(h);
		}
		
	}
}

package ejb;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import datatypes.DTEstado;
import ejb.remote.IEjbAdminRemote;
import model.Administrador;
import model.Cliente;

@Local(IEjbAdmin.class)
@Stateless
public class EjbAdmin implements IEjbAdmin, IEjbAdminRemote {
	
	@PersistenceContext(unitName = "Servidor")
	private EntityManager em;
	
	public boolean existeUsuario(String idAdmin)
	{
		Query q = em.createQuery("select a from Administrador a where a.nombre = :idAdmin").setParameter("idAdmin",
				idAdmin);
		
		return (q.getResultList().size() > 0);
		
	}
	
	public boolean esValidaPassword(String idAdmin, String password)
	{
		Query q = em.createQuery("select a from Administrador a where a.nombre = :idAdmin").setParameter("idAdmin",
				idAdmin);
		
		Administrador a = (Administrador) q.getSingleResult();
	
		return a.getPassword().equals(password);
		
	}
	
	@Override
	public DTEstado CrearAdmin(String username, String password)
	{
		Query q = em.createQuery("select a from Administrador a "
				+ "where a.nombre = :username").setParameter("username", username);
		if(q.getResultList().size()>0){ // existe un usuario con el mismo username
			return new DTEstado("Error ya existe un administrador con el mismo username");
		}
		Administrador adm = new Administrador();
		adm.setNombre(username);
		adm.setPassword(password);
		em.persist(adm);
		return new DTEstado("El administrador fue creado con exito");
	}
}

package ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class EjbAdmin implements IEjbAdmin {
	
	@PersistenceContext(unitName = "Servidor")
	private EntityManager em;
	
}

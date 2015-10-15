package interprete;

import javax.ejb.EJB;
import javax.inject.Inject;

import ejb.IEjbServidor;

public class TestInterprete {

	@Inject
	private static IEjbServidor ejb;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Category c = new Category();
		
		Category hijo1 = new Category();
		Category hijo2 = new Category();
		
		Category nieto = new Category();
		
		c.getChildren().add(hijo1);
		c.getChildren().add(hijo2);
		
		hijo2.getChildren().add(nieto);
		
		ejb.persistirArbol(c);
		
	}

}

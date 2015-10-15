package model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PaqueteIndice")
@DiscriminatorValue("Indice")
public class PaqueteIndice extends Paquete {

	private static final long serialVersionUID = 1L;

	 @Override public boolean detalladoPorUsuario() {
		  // hay que ver si no conviene guardar cantidad de empresas
		  // o guardar esto como un campo
		return this.getAccionesPaquete().size() > 1;
	  }
	 
	 public PaqueteIndice()
		{
			super("Indice");
		}
	 
	  
	 @Override
	public double getValorTotal() {
		 if(this.getAccionesPaquete().size() == 1){
			 PaqueteAccion paq = getAccionesPaquete().iterator().next();
			 return paq.getAccion().getValorActual()  ;
		 }
		 else{
			 return 1;
		 }
	 }
	}
	 

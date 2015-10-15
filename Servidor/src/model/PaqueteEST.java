package model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PaqueteEST")
@DiscriminatorValue("EST")
public class PaqueteEST extends Paquete {

	private static final long serialVersionUID = 1L;
	
	private String algoritmoEST;
	
	public PaqueteEST()
	{
		super("EST");
	}
}

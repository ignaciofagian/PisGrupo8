package datatypes;

import java.io.Serializable;

public class DTEstado implements Serializable  {

	private static final long serialVersionUID = 1L;
	
	private String estado;
	
	public DTEstado()
	{
		
	}
	
	public DTEstado(String estado)
	{
		this.estado = estado;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
}

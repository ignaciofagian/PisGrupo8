package datatypes;

public class DTPaqueteIdNombre {
	private String nombre;
	private long id;
	
	public DTPaqueteIdNombre(long id,String nombre)
	{
		this.id= id;
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
}

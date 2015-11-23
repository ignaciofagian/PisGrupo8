package datatypes;

public class DTPaqueteAccion {
	private String simbolo;
	private String nombre;
	private int porcentaje;
	
	public DTPaqueteAccion(String simbolo, String nombre, int porcentaje)
	{
		this.simbolo = simbolo;
		this.nombre = nombre;
		this.porcentaje = porcentaje;
	}

	public String getSimbolo() {
		return simbolo;
	}

	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(int porcentaje) {
		this.porcentaje = porcentaje;
	}
}

package datatypes;

import java.util.List;

public class DTPaqueteDetalle {
	private long idPaquete;
	private String nombre;
	private String tipo;
	private List<DTPaqueteAccion> acciones;
	private int cantidadInversionistas;
	
	public DTPaqueteDetalle(long id, String nombre, String tipo,
			List<DTPaqueteAccion> acciones,int cantidadInversionistas)
	{
		this.nombre = nombre;
		this.tipo = tipo;
		this.acciones = acciones;
		this.cantidadInversionistas = cantidadInversionistas;
		this.idPaquete = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public List<DTPaqueteAccion> getAcciones() {
		return acciones;
	}

	public void setAcciones(List<DTPaqueteAccion> acciones) {
		this.acciones = acciones;
	}

	public int getCantidadInversionistas() {
		return cantidadInversionistas;
	}

	public void setCantidadInversionistas(int cantidadInversionistas) {
		this.cantidadInversionistas = cantidadInversionistas;
	}

	public long getIdPaquete() {
		return idPaquete;
	}

	public void setIdPaquete(long idPaquete) {
		this.idPaquete = idPaquete;
	}
	
	
}

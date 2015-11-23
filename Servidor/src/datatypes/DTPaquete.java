package datatypes;

import java.util.List;

public class DTPaquete {
	private String nombre;
	private List<DTCategoria> categorias;
	private List<DTPaqueteAccion> acciones;
	private List<DTPaquetePregunta> preguntas;
	private int tipo;
	private String algoritmo;
	
	public DTPaquete()
	{
		
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<DTCategoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<DTCategoria> categorias) {
		this.categorias = categorias;
	}

	public List<DTPaqueteAccion> getAcciones() {
		return acciones;
	}

	public void setAcciones(List<DTPaqueteAccion> acciones) {
		this.acciones = acciones;
	}

	public List<DTPaquetePregunta> getPreguntas() {
		return preguntas;
	}

	public void setPreguntas(List<DTPaquetePregunta> preguntas) {
		this.preguntas = preguntas;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public String getAlgoritmo() {
		return algoritmo;
	}

	public void setAlgoritmo(String algoritmo) {
		this.algoritmo = algoritmo;
	}		
}

 

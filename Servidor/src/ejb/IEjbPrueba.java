package ejb;

import model.Accion;

public interface IEjbPrueba {
	public void crearClientesPrueba(int numero);

	void borrarClientesPrueba();

	Accion buscarCrearAccion(String symbol);
	
}

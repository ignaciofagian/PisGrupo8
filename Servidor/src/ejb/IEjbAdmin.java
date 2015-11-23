package ejb;

import datatypes.DTEstado;


public interface IEjbAdmin {

	public boolean existeUsuario(String admin);
	
	public boolean esValidaPassword(String admin,String password);
	
	public DTEstado CrearAdmin(String username, String password);
}

package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Administrador")
public class Administrador extends Usuario {

	private static final long serialVersionUID = 1L;

	@Column(name = "Nombre")
	private String nombre;

	@Column(name = "Password")
	private String password;
	
	public Administrador()
	{ 
		  super();
	}

	public String get_nombre() {
		return nombre;
	}

	public void set_nombre(String _nombre) {
		this.nombre = _nombre;
	}

	public String get_password() {
		return password;
	}

	public void set_password(String _password) {
		this.password = _password;
	}
}

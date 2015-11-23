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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


}

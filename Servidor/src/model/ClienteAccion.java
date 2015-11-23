package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;



@Entity
@Table(name = "ClienteAccion")
public class ClienteAccion implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//@SequenceGenerator(name="seq_clienteAcc")
	@Id  @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="IDCliAccion")
	private long id;
	
	/*
	//Agregue jointable porque JPA me insultaba cada vez que cargaba algo y no reconocia columnas
	@ManyToOne(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinTable(name="ClienteAccion_Padre",joinColumns={@JoinColumn(name="IDCliAccion", referencedColumnName="IDCliAccion")}, 
	inverseJoinColumns={@JoinColumn(name="IDUsuario", referencedColumnName="IDUsuario"),
						@JoinColumn(name="IDPaq", referencedColumnName="IDPaq")})
	
	*/

	

	//inverseJoinColumns={@JoinColumn(name="IDAccion", referencedColumnName="IDAccion")})
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="IdAccion")
	private Accion accion;
	
	private double cantidad;
	

	public Accion getAccion() {
		return accion;
	}
	public void setAccion(Accion accion) {
		this.accion = accion;
	}
	public double getCantidad() {
		return cantidad;
	}
	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}
	public ClienteAccion( Accion accion, double cantidad) {
		super();
		this.accion = accion;
		this.cantidad = cantidad;
	}
	
	public ClienteAccion() {
		super();
		
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClienteAccion other = (ClienteAccion) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	

	
	
}

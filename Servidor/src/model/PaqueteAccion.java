package model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import model.PK.PaqueteAccionPK;

@Entity
//@Table(name = "PaqueteAccion",uniqueConstraints={@UniqueConstraint(columnNames={"IDPaq","IDAccion"})})
@IdClass(PaqueteAccionPK.class)
public class PaqueteAccion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne
	@JoinColumn(name = "IDPaq")
	private Paquete paquete;

	@Id
	@ManyToOne
	@JoinColumn(name = "IDAccion")
	private Accion accion;

	private int inversion;

	public int getInversion() {
		return inversion;
	}

	public void setInversion(int inversion) {
		this.inversion = inversion;
	}

	public Accion getAccion() {
		return accion;
	}

	public void setAccion(Accion accion) {
		this.accion = accion;
	}

	

	public Paquete getPaquete() {
		return paquete;
	}

	public void setPaquete(Paquete paquete) {
		this.paquete = paquete;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		//result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		PaqueteAccion other = (PaqueteAccion) obj;
		
		return true;
	}

	public PaqueteAccion()
	{
		
	}
	
	public PaqueteAccion(Accion accion, Paquete paquete, int inversion)
	{
		this.paquete = paquete;
		this.accion = accion;
		this.inversion = inversion;
	}
}

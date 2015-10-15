package model.PK;

import java.io.Serializable;

import javax.persistence.Column;

public class PaqueteAccionPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	//@Column(name = "IDPaq")
	private Long paquete;

	//@Column(name = "IDAccion")
	private Long accion;

	

	public Long getPaquete() {
		return paquete;
	}

	public void setPaquete(Long paquete) {
		this.paquete = paquete;
	}

	public Long getAccion() {
		return accion;
	}

	public void setAccion(Long accion) {
		this.accion = accion;
	}

	public PaqueteAccionPK() {
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof PaqueteAccionPK)) {
			return false;
		}
		PaqueteAccionPK castOther = (PaqueteAccionPK) other;
		return this.accion.equals(castOther.accion) && this.paquete.equals(castOther.paquete);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.accion.hashCode();
		hash = hash * prime + this.paquete.hashCode();

		return hash;
	}
}

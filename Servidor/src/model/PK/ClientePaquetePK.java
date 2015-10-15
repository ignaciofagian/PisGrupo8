package model.PK;

import java.io.Serializable;

import javax.persistence.Column;

public class ClientePaquetePK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	private Long portafolio;

	private Long paquete;

	



	public Long getPortafolio() {
		return portafolio;
	}

	public void setPortafolio(Long portafolio) {
		this.portafolio = portafolio;
	}

	public Long getPaquete() {
		return paquete;
	}

	public void setPaquete(Long paquete) {
		this.paquete = paquete;
	}

	public ClientePaquetePK() {
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((paquete == null) ? 0 : paquete.hashCode());
		result = prime * result + ((portafolio == null) ? 0 : portafolio.hashCode());
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
		ClientePaquetePK other = (ClientePaquetePK) obj;
		if (paquete == null) {
			if (other.paquete != null)
				return false;
		} else if (!paquete.equals(other.paquete))
			return false;
		if (portafolio == null) {
			if (other.portafolio != null)
				return false;
		} else if (!portafolio.equals(other.portafolio))
			return false;
		return true;
	}

	
}

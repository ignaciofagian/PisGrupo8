package model.PK;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class SaldoHistoricoPK implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name="portafolio_idportafolio")
	private Long portafolio;
	@Column(name="saldohistorico_key") @Temporal(TemporalType.TIMESTAMP)
	private Calendar fecha;
	

	
	
	public Long getPortafolio() {
		return portafolio;
	}
	public void setPortafolio(Long portafolio) {
		this.portafolio = portafolio;
	}
	public Calendar getFecha() {
		return fecha;
	}
	public void setFecha(Calendar fecha) {
		this.fecha = fecha;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
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
		SaldoHistoricoPK other = (SaldoHistoricoPK) obj;
		if (fecha == null) {
			if (other.fecha != null)
				return false;
		} else if (!fecha.equals(other.fecha))
			return false;
		if (portafolio == null) {
			if (other.portafolio != null)
				return false;
		} else if (!portafolio.equals(other.portafolio))
			return false;
		return true;
	}

	

	
	
}

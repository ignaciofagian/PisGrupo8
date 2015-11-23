package model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import model.PK.HistoricoPK;

@Entity() @Access(AccessType.PROPERTY)
@Table(name = "Historico")//,uniqueConstraints={@UniqueConstraint(columnNames={"IDAccion","Fecha"})})
@IdClass(value=HistoricoPK.class)
public class Historico implements Serializable {

	private static final long serialVersionUID = 1L;

//	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	//private Long Id;
	

	private Accion accion;
	

	private Date fecha;
	
	
	@Id
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name = "IDAccion")
	public Accion getAccion() {
		return accion;
	}

	public void setAccion(Accion accion) {
		this.accion = accion;
	}

	
	@Id
	@JoinColumn(name = "Fecha") @Temporal(TemporalType.TIMESTAMP)
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	private double adjClose;

	private double close;

	public double getClose() {
		return close;
	}

	public void setClose(double close) {
		this.close = close;
	}

	public double getAdjClose() {
		return adjClose;
	}

	public void setAdjClose(double adjClose) {
		this.adjClose = adjClose;
	}
	
	
	public Historico() {
		super();
	}

	public Historico(Accion accion,double adjClose, double close,Date fecha) {
		this.accion = accion;
		this.fecha = fecha;
		this.adjClose = adjClose;
		this.close = close;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accion == null) ? 0 : accion.hashCode());
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
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
		Historico other = (Historico) obj;
		if (accion == null) {
			if (other.accion != null)
				return false;
		} else if (!accion.equals(other.accion))
			return false;
		if (fecha == null) {
			if (other.fecha != null)
				return false;
		} else if (!fecha.equals(other.fecha))
			return false;
		return true;
	}
	
	public static double getAdjFactor(Historico h1,Historico h2) {
		// 

		if (h1 == null | h2 == null) return 1;
		return (h1.getClose() / h1.getAdjClose()) / (h2.getClose() / h2.getAdjClose());
		
	}

	@Override
	public String toString() {
		return "Hist(acc="+(accion == null ? "NULL!" : accion.getId())+ "@" + (fecha == null ? "NULL!" :  fecha.toString()) + " AC=" + adjClose + " CL=" + close;
	}
	
	

}

	





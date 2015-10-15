package model;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Creo una clase sino probablemente JPA termine cargando todo y queda dificil pedir solo una parte
 * @author Guillermo
 *
 */
@Table(name="portafolio_saldohistorico")
@Entity //@IdClass(value=SaldoHistoricoPK.class)
public class SaldoHistorico  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) 
	private Long Id;
	
	@ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="portafolio_idportafolio")
	private Portafolio portafolio;
	 @Column(name="saldohistorico_key") @Temporal(TemporalType.TIMESTAMP)
	private Calendar fecha;
	@Column(name="saldohistorico",nullable=false)
	private double valor;
	
	public SaldoHistorico(Portafolio portafolio, Calendar fecha, double valor) {
		this.portafolio = portafolio;
		this.fecha = fecha;
		this.valor = valor;
	}
	
	public SaldoHistorico() {

	}

	public Portafolio getPortafolio() {
		return portafolio;
	}

	public void setPortafolio(Portafolio portafolio) {
		this.portafolio = portafolio;
	}

	public Calendar getFecha() {
		return fecha;
	}

	public void setFecha(Calendar fecha) {
		this.fecha = fecha;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	} 
	
	
	
	
}

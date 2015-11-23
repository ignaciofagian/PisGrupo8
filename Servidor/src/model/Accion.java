package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollectionOption;

import org.hibernate.annotations.LazyCollection;

@Entity
@Table(name = "Accion")
@Cacheable(value= true)
@Access(AccessType.PROPERTY) // para implementar lazy-loading en ManyToOne AccessType debe ser = Property
public class Accion implements Serializable {

	private static final long serialVersionUID = 1L;


	private Long id;

	private String symbol;

	private String nombre;
	
	
	private Collection<Historico> historicos = new ArrayList<Historico>();

	private double valorActual;
	
	// si es una accion "ficticia" en vez de real
	@Column(name="equivalente")
	private boolean equivalente = false;

	//@LazyCollection(LazyCollectionOption.EXTRA)
	@OneToMany(mappedBy = "accion",cascade={CascadeType.ALL},orphanRemoval=true,fetch=FetchType.LAZY)
	public Collection<Historico> getHistoricos() {
		return historicos;
	}

	public void setHistoricos(Collection<Historico> historicos) {
		this.historicos = historicos;
	}
	
	public Historico agregarHistorico(Date fecha,double valor){
		Historico h = new Historico(this,valor,valor,fecha); 
		this.getHistoricos().add(h);
		return h;
	}
	
	public void agregarHistorico(Date fecha,double valor,double valAdj){
		Historico h = new Historico(this,valAdj,valor,fecha); 
		this.getHistoricos().add(h);
	}

	public double getValorActual() {
		return valorActual;
	}

	public void setValorActual(double valorActual) {
		this.valorActual = valorActual;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	@Column(name="pedirhistorico")
	private boolean pedirHistorico = true;

	public boolean isPedirHistorico() {
		return pedirHistorico;
	}

	public void setPedirHistorico(boolean pedirHistorico) {
		this.pedirHistorico = pedirHistorico;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
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
		Accion other = (Accion) obj;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		return true;
	}

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "IDAccion")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Accion(String symbol) {
		super();
		this.symbol = symbol;
	}

	public Accion() {
		super();
	}

	public boolean isEquivalente() {
		return equivalente;
	}

	public void setEquivalente(boolean equivalente) {
		this.equivalente = equivalente;
	}

	
}

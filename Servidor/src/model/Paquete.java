package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "Paquete")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "Paquete_Tipo", discriminatorType = DiscriminatorType.STRING)

public class Paquete implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IDPaq")
	private Long id;
	
	private String nombre;

	@Column (name = "Paquete_Tipo")
    private String tipoPaquete = null;
	

	@OneToMany(cascade={CascadeType.PERSIST,CascadeType.REMOVE, CascadeType.MERGE})
	@JoinTable(joinColumns = @JoinColumn(name = "IDPaq"), 
	   inverseJoinColumns = {   
		        @JoinColumn(name="IDPaqac", referencedColumnName="IDPaq"),
		        @JoinColumn(name="IDAccion", referencedColumnName="IDAccion") 
		        })
	private Collection<PaqueteAccion> accionesPaquete  = new ArrayList<PaqueteAccion>();


	public Collection<PaqueteAccion> getAccionesPaquete() {
		return accionesPaquete;
	}

	public void setAccionesPaquete(Collection<PaqueteAccion> accionesPaquete) {
		this.accionesPaquete = accionesPaquete;
	}
	
	public void agregarAccion(Accion a, int inversion){
		PaqueteAccion p = new PaqueteAccion();
		p.setPaquete(this);
		p.setAccion(a);
		p.setInversion(inversion);
		this.accionesPaquete.add(p);
	}

	@OneToMany(mappedBy = "paquete", cascade={CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE})
	private List<PreguntaEspecifica> preguntaEspecificas  = new ArrayList<PreguntaEspecifica>();

	@ManyToMany
	private List<PaqueteCategoria> categorias = new ArrayList<PaqueteCategoria>();
	

	/**
	 * True si es necesario mantener cantidad de acciones por usuario para
	 * calcular el saldo. False si la proporcion se mantiene constante para
	 * todos los usuarios.
	 * 
	 * @return
	 */
	public boolean detalladoPorUsuario() {
		return false;
	}

	
	public Paquete(String tipo)
	{
		this.preguntaEspecificas = new ArrayList<PreguntaEspecifica>();
		this.accionesPaquete = new ArrayList<PaqueteAccion>();
		this.categorias = new ArrayList<PaqueteCategoria>();
		this.tipoPaquete = tipo;
	}
	

	
	/**
	 * Establece el cuanto valdria el paquete en t2 por cada dolar en t1
	 * si se mantiene la cantidad de cada  accion (pre ajuste por acciones corporativas) 
	 * @param t1
	 * @param t2
	 * @param prov
	 * @return
	 */
	public double getValorRatio(Calendar t1,Calendar t2, IProveedorValor prov){
		
		double val1 = 1;
		double val2 = 0;

		if (t1 == null) { // si t2 es null probablemente sea un error, por eso no lo controlo
			return 1; // t1 null puede significar que algo no se corrio antes
		}

		for(PaqueteAccion a : accionesPaquete){
			long id = a.getAccion().getId();
			Historico h1 = prov.findHistorico(id, t1);
			Historico h2 = prov.findHistorico(id, t2);
			if (h1 == null){
				return 1;
			}
			else{
				double cantInv_t1 = 1 * a.getInversion() / 100.0;
				double close_t1 =  h1.getClose();//prov.getValorSinAdj(id, t1);
				double cant_t1 = cantInv_t1 / close_t1;
				double adjFactor = Historico.getAdjFactor(h1, h2);//prov.getAdjFactor(t1, t2, id);
				double cant_t2 = adjFactor * cant_t1;
				double valor_t2 =h2.getClose();//(id, t2);
				double cantInv_t2  = cant_t2 * valor_t2;
				val2 += cantInv_t2;
			}
		}
		return val2;
	}
	
	/**
	 * Valor total del paquete si no es detallado por usuario
	 */
	public  double getValorTotal(){
		return 1;
	} 

	public List<PreguntaEspecifica> getPreguntaEspecificas() {
		return preguntaEspecificas;
	}

	public void setPreguntaEspecificas(List<PreguntaEspecifica> preguntaEspecificas) {
		this.preguntaEspecificas = preguntaEspecificas;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipoPaquete() {
		return tipoPaquete;
	}

	public void setTipoPaquete(String tipoPaquete) {
		this.tipoPaquete = tipoPaquete;
	}

	public List<PaqueteCategoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<PaqueteCategoria> categorias) {
		this.categorias = categorias;
	}
	
	public void addCategorias(PaqueteCategoria... cat){
		if (this.categorias == null) this.categorias = new ArrayList<PaqueteCategoria>();
		this.categorias.addAll(Arrays.asList(cat));
	}

	public Paquete() {
		super();
		this.accionesPaquete = new ArrayList<PaqueteAccion>();
		this.categorias = new ArrayList<PaqueteCategoria>();
	}
	
	

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Paquete other = (Paquete) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	/**
	 * metodo interno para asegurarme que no haya hecho un paquete sin nombre o acciones o categorias
	 */
	public void validar(){
		if (this.getNombre() == null || this.getNombre().isEmpty()) throw new IllegalArgumentException("No tiene nombre");
		if (this.getAccionesPaquete().isEmpty()) throw new IllegalArgumentException("Falta agregarle acciones");
		if (this.getCategorias().isEmpty()) throw new IllegalArgumentException("Falta agregarle categorias");
	}

}

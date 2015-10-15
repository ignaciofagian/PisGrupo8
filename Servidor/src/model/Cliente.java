package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "Cliente")
public class Cliente extends Usuario {

	private static final long serialVersionUID = 1L;

	@OneToMany
	@JoinTable(joinColumns = @JoinColumn(name = "IDUsuario"), 
	   inverseJoinColumns = @JoinColumn(name = "IDResp"))
	private Collection<Respuesta> respuestas;

	@Column(unique=true) 
	private String idTelefono;
	
	@OneToOne(cascade = CascadeType.ALL, fetch=FetchType.EAGER,orphanRemoval=true)
	private Portafolio portafolio;
	
	public Cliente()
	{
		super();
		this.respuestas = new ArrayList<Respuesta>();
		this.portafolio = new Portafolio(this);
	}
	
	public Collection<Respuesta> getRespuestas() {
		return respuestas;
	}

	public void setRespuestas(Collection<Respuesta> respuestas) {
		this.respuestas = respuestas;
	}

	public String getIdTelefono() {
		return idTelefono;
	}

	public void setIdTelefono(String idTelefono) {
		this.idTelefono = idTelefono;
	}

	public Portafolio getPortafolio() {
		return portafolio;
	}

	public void setPortafolio(Portafolio portafolio) {
		this.portafolio = portafolio;
	}
	
	public List<PreguntaEspecifica> obtnenerPreguntasEspecificas()
	{
		List<PreguntaEspecifica> preguntas = new ArrayList<PreguntaEspecifica>();
		
		for(ClientePaquete cp : this.getPortafolio().getPaquetes())
		{
			preguntas.addAll(cp.getPaquete().getPreguntaEspecificas());
		}
		
		return preguntas;
		
	}

}

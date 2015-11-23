package model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@Table(name = "Cliente")
public class Cliente extends Usuario {

	private static final long serialVersionUID = 1L;
	
	
	//@Version @Column(name="entity_version",nullable=false)
	//private int version; // campo de version para optimistic locking de JPA

	@OneToMany
	@JoinTable(joinColumns = @JoinColumn(name = "IDUsuario"), 
	   inverseJoinColumns = @JoinColumn(name = "IDResp"))
	private Collection<Respuesta> respuestas;

	@Column(unique=true) 
	private String idTelefono;
	
	@Column(name="fecharegistro") @Temporal(TemporalType.TIMESTAMP) 
	private Calendar fechaRegistro;
	
	@OneToOne(cascade = CascadeType.ALL, fetch=FetchType.EAGER,orphanRemoval=true)
	private Portafolio portafolio;
	
	public Cliente()
	{
		super();
		this.respuestas = new ArrayList<Respuesta>();
		this.portafolio = new Portafolio(this);
		this.portafolio.setUltimoAcceso(Calendar.getInstance());
		this.fechaRegistro = Calendar.getInstance(); 
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
	
	public List<PreguntaEspecifica> obtenerPreguntasEspecificas()
	{
		List<PreguntaEspecifica> preguntas = new ArrayList<PreguntaEspecifica>();
		
		for(ClientePaquete cp : this.getPortafolio().getPaquetes())
		{
			preguntas.addAll(cp.getPaquete().getPreguntaEspecificas());
		}
		
		return preguntas;
		
	}
	
	public HashMap<Long,RespuestaEspecifica> obtenerRespuestaEspecificas(){
		HashMap<Long,RespuestaEspecifica> res = new HashMap<Long, RespuestaEspecifica>();
		for(Respuesta r : this.respuestas){
			if (r instanceof RespuestaEspecifica){
				res.put(r.getPregunta().getId(),(RespuestaEspecifica)r);
			}
		}
		return res;
	}

}

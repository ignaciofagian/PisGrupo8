package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "Pregunta_Tipo", discriminatorType = DiscriminatorType.STRING)
public class Pregunta implements Serializable {

	private static final long serialVersionUID = 1L;
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "IDPreg")
	private Long id;
	
	private String sentenciaESP;

	private String sentenciaENG;

	@Column (name = "Pregunta_Tipo")
    private String tipoPregunta = null;
	
//	@ManyToOne
//	private Administrador administrador;

	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name="IDPreg")
	private List<Respuesta> respuestas;

	
	public Pregunta(String tipo) {
		this.tipoPregunta = tipo;
		this.respuestas = new ArrayList<Respuesta>();
	}
	
	public Pregunta()
	{
	}

	public String getSentenciaESP() {
		return sentenciaESP;
	}

	public void setSentenciaESP(String sentenciaESP) {
		this.sentenciaESP = sentenciaESP;
	}

	public String getSentenciaENG() {
		return sentenciaENG;
	}

	public void setSentenciaENG(String sentenciaENG) {
		this.sentenciaENG = sentenciaENG;
	}

	public String getTipoPregunta() {
		return tipoPregunta;
	}

	public void setTipoPregunta(String tipoPregunta) {
		this.tipoPregunta = tipoPregunta;
	}

	public List<Respuesta> getRespuestas() {
		return respuestas;
	}

	public void setRespuestas(List<Respuesta> respuestas) {
		this.respuestas = respuestas;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}

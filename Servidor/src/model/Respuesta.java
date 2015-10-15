package model;

import java.io.Serializable;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Respuesta")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "Respuesta_Tipo", discriminatorType = DiscriminatorType.STRING)
public class Respuesta implements Serializable {

	private static final long serialVersionUID = 1L;
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "IDResp")
	private Long id;

	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="IDPreg",insertable=true,
	        updatable=true,nullable=false)
	private Pregunta pregunta;

	private String sentenciaESP;
    
	private String sentenciaENG;

	@Column (name = "Respuesta_Tipo")
    private String tipoRespuesta = null;

		
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

	public String getTipoRespuesta() {
		return tipoRespuesta;
	}

	public void setTipoRespuesta(String tipoRespuesta) {
		this.tipoRespuesta = tipoRespuesta;
	}
	
	public Respuesta(String tipo) {
		this.tipoRespuesta = tipo;
	}
	
	public Respuesta()
	{
		
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Pregunta getPregunta() {
		return pregunta;
	}

	public void setPregunta(Pregunta pregunta) {
		this.pregunta = pregunta;
	}
	
	
}

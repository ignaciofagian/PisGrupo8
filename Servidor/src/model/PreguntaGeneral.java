package model;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PreguntaGeneral")
@DiscriminatorValue(value="General")
public class PreguntaGeneral extends Pregunta {

	private static final long serialVersionUID = 1L;
	
	private boolean deprecated = false;

//	@OneToMany(cascade=CascadeType.ALL)
//	@JoinTable(name="PregGeneralHabilita",
//	joinColumns = @JoinColumn(name = "IDPregGeneral"), 
//			   inverseJoinColumns = @JoinColumn(name = "IDPregEsp"))
//	private Collection<PreguntaEspecifica> preguntasQueHabilita;
	
	public PreguntaGeneral()
	{
		super("General");
//		this.preguntasQueHabilita = new ArrayList<PreguntaEspecifica>();
	}

//	public Collection<PreguntaEspecifica> getPreguntasQueHabilita() {
//		return preguntasQueHabilita;
//	}
//
//	public void setPreguntasQueHabilita(
//			Collection<PreguntaEspecifica> preguntasQueHabilita) {
//		this.preguntasQueHabilita = preguntasQueHabilita;
//	}
	
	public void agregarRespuestaGeneral(String eng,String espa, PaqueteCategoria... categorias){
		RespuestaGeneral r = new RespuestaGeneral(eng, espa, this, categorias);
		this.getRespuestas().add(r);
	}

	public boolean isDeprecated() {
		return deprecated;
	}

	public void setDeprecated(boolean deprecated) {
		this.deprecated = deprecated;
	}
	
	
}

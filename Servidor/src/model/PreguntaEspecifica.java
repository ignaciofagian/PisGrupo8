package model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PreguntaEspecifica")
@DiscriminatorValue(value="Especifica")
public class PreguntaEspecifica extends Pregunta {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	private Paquete paquete;

	public PreguntaEspecifica()
	{
		super("Especifica");
	}

	public Paquete getPaquete() {
		return paquete;
	}

	public void setPaquete(Paquete paquete) {
		this.paquete = paquete;
	}
	
	public void agregarRespuestaEspecifica(int puntaje,String eng,String espa){
		RespuestaEspecifica r = new RespuestaEspecifica(puntaje, eng, espa, this);
		r.setPregunta(this);
		this.getRespuestas().add(r);
	}
}

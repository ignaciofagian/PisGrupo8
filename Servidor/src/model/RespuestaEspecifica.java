package model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "RespuestaEspecifica")
@DiscriminatorValue(value="Especifica")
public class RespuestaEspecifica  extends Respuesta {
	int puntaje;
	
	public RespuestaEspecifica()
	{
		super("Especifica");
	}

	public int getPuntaje() {
		return puntaje;
	}

	public void setPuntaje(int puntaje) {
		this.puntaje = puntaje;
	}

	public RespuestaEspecifica( int puntaje,String eng,String espa,Pregunta question) {
		super("Especifica");
		this.setSentenciaESP(espa);
		this.setSentenciaENG(eng);
		this.puntaje = puntaje;
		this.setPregunta(question);
	}
	
	
}

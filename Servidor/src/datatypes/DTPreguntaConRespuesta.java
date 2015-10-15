package datatypes;

public class DTPreguntaConRespuesta {
	private DTPregunta pregunta;
	private long idRespuestaUsuario;
	
	public DTPreguntaConRespuesta()
	{
		
	}
	
	

	public DTPreguntaConRespuesta(DTPregunta pregunta, long idRespuestaUsuario) {
		super();
		this.pregunta = pregunta;
		this.idRespuestaUsuario = idRespuestaUsuario;
	}



	public DTPregunta getPregunta() {
		return pregunta;
	}

	public void setPregunta(DTPregunta pregunta) {
		this.pregunta = pregunta;
	}

	public long getIdRespuestaUsuario() {
		return idRespuestaUsuario;
	}

	public void setIdRespuestaUsuario(long idRespuestaUsuario) {
		this.idRespuestaUsuario = idRespuestaUsuario;
	}

	
	
	
}

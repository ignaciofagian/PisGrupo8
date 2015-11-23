package datatypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DTPreguntaGeneral implements Serializable {
	private long id;
	private String eng;
	private String esp;
	private List<DTRespuestaGeneral> respuestas;
	
	public DTPreguntaGeneral()
	{
		respuestas = new ArrayList<DTRespuestaGeneral>();
	}
	
	public DTPreguntaGeneral(long id, String eng, String esp, List<DTRespuestaGeneral> respuestas)
	{
		this.eng = eng;
		this.esp = esp;
		this.respuestas = respuestas;
		this.id = id;
	}

	public String getEng() {
		return eng;
	}

	public void setEng(String eng) {
		this.eng = eng;
	}

	public String getEsp() {
		return esp;
	}

	public void setEsp(String esp) {
		this.esp = esp;
	}

	public List<DTRespuestaGeneral> getRespuestas() {
		return respuestas;
	}

	public void setRespuestas(List<DTRespuestaGeneral> respuestas) {
		this.respuestas = respuestas;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	
}

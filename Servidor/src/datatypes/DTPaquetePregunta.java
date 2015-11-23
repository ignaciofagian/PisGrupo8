package datatypes;

import java.util.List;

public 	class DTPaquetePregunta
{
	private String eng;
	private String esp;
	private List<DTPaqueteRespuesta> respuestas;
	
	public DTPaquetePregunta(String eng, String esp, List<DTPaqueteRespuesta> respuestas)
	{
		this.eng = eng;
		this.esp = esp;
		this.respuestas = respuestas;
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

	public List<DTPaqueteRespuesta> getRespuestas() {
		return respuestas;
	}

	public void setRespuestas(List<DTPaqueteRespuesta> respuestas) {
		this.respuestas = respuestas;
	}
	
	
}

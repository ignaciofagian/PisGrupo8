package datatypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DTRespuestaGeneral implements Serializable{
	private String eng;
	private String esp;
	private List<Long> categorias;
	private String categoriasTexto;
	
	public DTRespuestaGeneral()
	{
		this.categorias = new ArrayList<Long>();
		this.categoriasTexto = "";
	}
	
	public DTRespuestaGeneral(String eng, String esp, List<Long> categorias)
	{
		this.eng = eng;
		this.esp = esp;
		this.categorias = categorias;
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

	public List<Long> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Long> categorias) {
		this.categorias = categorias;
	}

	public String getCategoriasTexto() {
		return categoriasTexto;
	}

	public void setCategoriasTexto(String categoriasTexto) {
		this.categoriasTexto = categoriasTexto;
	}
	
	
}

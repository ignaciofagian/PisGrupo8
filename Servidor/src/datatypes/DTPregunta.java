package datatypes;

import java.util.List;

import model.PreguntaGeneral;

public class DTPregunta {
	
	private long id;
	private String ing;
	private String esp;
	private List<DTRespuesta> resp;
	
	public DTPregunta(long id, String ing, String esp,
			List<DTRespuesta> resp) {
		super();
		this.id = id;
		this.ing = ing;
		this.esp = esp;
		this.resp = resp;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getIng() {
		return ing;
	}
	public void setIng(String ing) {
		this.ing = ing;
	}
	public String getEsp() {
		return esp;
	}
	public void setEsp(String esp) {
		this.esp = esp;
	}
	public List<DTRespuesta> getResp() {
		return resp;
	}
	public void setResp(List<DTRespuesta> resp) {
		this.resp = resp;
	}

	
}

package datatypes;

public class DTRespuesta {

	private long id;
	private String ing;
	private String esp;
	
	public DTRespuesta(long id, String ing, String esp) {
		this.id = id;
		this.ing = ing;
		this.esp = esp;
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
	
}

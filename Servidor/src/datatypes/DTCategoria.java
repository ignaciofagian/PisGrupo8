package datatypes;

public class DTCategoria {
	private long id;
	private String name;
	
	public DTCategoria(long id, String name)
	{
		this.name = name;
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}

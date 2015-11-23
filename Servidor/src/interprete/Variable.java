package interprete;

public class Variable {
	public String identifier;
	public String type;
	public PSValue value;
	
	public Variable(String iden, String typ, PSValue val){
		this.identifier = iden;
		this.type = typ;
		value = val;
	}
	
	public String getIdentifier(){
		return this.identifier;
	}
	
	public String getType(){
		return this.type;
	}
	
	public PSValue getValue(){
		return this.value;
	}
	
}

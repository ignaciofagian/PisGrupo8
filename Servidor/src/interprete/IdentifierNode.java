package interprete;
import java.util.Calendar;
import java.util.Map;

public class IdentifierNode implements PSNode {  
  
  private String identifier;  
  private Map<String, Variable> scope;  
  
  public IdentifierNode(String id, Map<String, Variable> s) {  
    identifier = id;  
    scope = s;  
  }  
  
  @Override  
  public PSValue evaluate(long idPaquete, Calendar fecha, IInterpreter intepr) {  
    Variable value = scope.get(identifier);  
    if(value == null) {  
      throw new RuntimeException("Variable '" + identifier + "' not definied.");  
    }
    if (value.getValue() == null){
    	throw new RuntimeException("Variable '" + identifier + "' not initialized.");
    }
    return value.getValue();  
  }  
  
  @Override  
  public PSValue check() {  
    Variable value = scope.get(identifier);  
    if(value == null) {  
      throw new RuntimeException("Variable '" + identifier + "' not definied.");  
    }
    if (value.getValue() == null){
    	throw new RuntimeException("Variable '" + identifier + "' not initialized.");
    }
    return value.getValue();  
  }
  
  @Override  
  public String toString() {  
    return identifier;  
  }
  
  @Override
  public boolean isAtomic(){
  	return false;
  }
 
} 
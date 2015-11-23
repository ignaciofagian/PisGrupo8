package interprete;
import java.util.Calendar;
import java.util.Map;

public class AssignmentNode implements PSNode {

  protected String identifier;
  protected PSNode rhs;
  protected Map<String, Variable> scope;

  public AssignmentNode(String i, PSNode n, Map<String, Variable> s) {
    identifier = i;
    rhs = n;
    scope = s;
  }

  @Override
  public PSValue evaluate(long idPaquete, Calendar fecha, IInterpreter intepr) {

    PSValue value = rhs.evaluate(idPaquete, fecha, intepr);

    if (value == PSValue.VOID) {
    	//actually may never get here, its already filtered
      throw new RuntimeException("can't assign VOID to " + identifier);
    }

      Variable v = scope.get(identifier);
      if (v != null){
	    	  String typ = v.type;
	          if (typ.equals("Float") && value.isBoolean()){
	        	  throw new RuntimeException("Can't assign boolean to float type. Variable '" + identifier + "'");
	          }else{
	        	  if (typ.equals("Boolean") && value.isNumber()){
	        		  throw new RuntimeException("Can't assign number to boolean type. Variable '" + identifier + "'");
	        	  }
	          }
	          v.value = value;
      }else{
    	  throw new RuntimeException("Variable '" + identifier + "' not definied."); 
      }
      
    return PSValue.VOID;
  }
  
  @Override
  public PSValue check() {

    PSValue value = rhs.check();

    if (value == PSValue.VOID) {
      throw new RuntimeException("can't assign VOID to " + identifier);
    }

      Variable v = scope.get(identifier);
      if (v != null){
	    	  String typ = v.type;
	          if (typ.equals("Float") && value.isBoolean()){
	        	  throw new RuntimeException("Can't assign boolean to float type. Variable '" + identifier + "'");
	          }else{
	        	  if (typ.equals("Boolean") && value.isNumber()){
	        		  throw new RuntimeException("Can't assign number to boolean type. Variable '" + identifier + "'");
	        	  }
	          }
	          v.value = value;
      }else{
    	  throw new RuntimeException("Variable '" + identifier + "' not definied."); 
      }
      
    return PSValue.VOID;
  }
  
  @Override
  public String toString() {
    return String.format("(%s = %s)", identifier,  rhs);
  }
  
  @Override
  public boolean isAtomic(){
	  return false;
  }
}
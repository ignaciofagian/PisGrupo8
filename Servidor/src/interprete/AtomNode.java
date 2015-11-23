package interprete;

import java.util.Calendar;

public class AtomNode implements PSNode {  
  
  private PSValue value;  
  
  public AtomNode(Object v) {  
    value = (v == null) ? PSValue.NULL : new PSValue(v);  
  }  
  
  @Override  
  public PSValue evaluate(long idPaquete, Calendar fecha, IInterpreter intepr) {  
    return value;  
  }  
  
  @Override  
  public PSValue check() {  
    return value;  
  }  
  
  @Override  
  public String toString() {  
    return value.toString();  
  } 
 
  public boolean isAtomic(){
	  return true;
  }
}
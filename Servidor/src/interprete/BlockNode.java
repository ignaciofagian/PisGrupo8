package interprete;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;  
  
public class BlockNode implements PSNode {  
  
  private List<PSNode> statements;  
  private PSNode returnStatement;  
  
  public BlockNode() {  
    statements = new ArrayList<PSNode>();  
    returnStatement = null;  
  }  
  
  public void addReturn(PSNode stat) {  
    returnStatement = stat;  
  }  
  
  public void addStatement(PSNode stat) {  
    statements.add(stat);  
  }  
  
  @Override  
  public PSValue evaluate(long idPaquete, Calendar fecha, IInterpreter intepr) {  
    for(PSNode stat : statements) {  
      PSValue value = stat.evaluate(idPaquete, fecha, intepr);  
      if(value != PSValue.VOID) {  
        // return early from this block if value is a return statement  
        return value;  
      }  
    }  
  
    // return VOID or returnStatement.evaluate() if it's not null  
    return returnStatement == null ? PSValue.VOID : returnStatement.evaluate(idPaquete, Calendar.getInstance(), intepr);  
  } 
  
  @Override  
  public PSValue check() {  
    for(PSNode stat : statements) {  
      PSValue value = stat.check();  
      if(value != PSValue.VOID) {  
        // return early from this block if value is a return statement  
        return value;  
      }  
    }  
  
    // return VOID or returnStatement.evaluate() if it's not null  
    return returnStatement == null ? PSValue.VOID : returnStatement.check();  
  }
  
  @Override 
  public boolean isAtomic(){
	  return false;
  }
}  
package interprete; 
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;  
  
public class IfNode implements PSNode {  
  
  private List<Choice> choices;  
  
  public IfNode() {  
    choices = new ArrayList<Choice>();  
  }  
  
  public void addChoice(PSNode e, PSNode b) {  
    choices.add(new Choice(e, b));  
  }  
  
  @Override  
  public PSValue evaluate(long idPaquete, Calendar fecha, IInterpreter intepr) {  
  
    for(Choice ch : choices) {  
      PSValue value = ch.expression.evaluate(idPaquete,fecha, intepr);  
  
      if(!value.isBoolean()) {  
        throw new RuntimeException("illegal boolean expression " +   
            "inside if-statement: " + ch.expression);  
      }  
  
      if(value.asBoolean()) {  
        return ch.block.evaluate(idPaquete, fecha, intepr);  
      }  
    }  
  
    return PSValue.VOID;  
  }  
  
  @Override  
  public PSValue check() {  
  
    for(Choice ch : choices) {  
      PSValue value = ch.expression.check();  
  
      if(!value.isBoolean()) {  
        throw new RuntimeException("illegal boolean expression " +   
            "inside if-statement: " + ch.expression);  
      }  
      
      ch.block.check();
      
      /*if(value.asBoolean()) {  
        return ch.block.evaluate();  
      } */ 
    }  
  
    return PSValue.VOID;  
  }
  
  private class Choice {  
  
    PSNode expression;  
    PSNode block;  
  
    Choice(PSNode e, PSNode b) {  
      expression = e;  
      block = b;  
    }  
  }  
  
  @Override
  public boolean isAtomic(){
  	return false;
  }
} 
package interprete;
import java.io.PrintStream;
import java.util.Calendar;  
  
public class PrintlnNode implements PSNode {  
  
  private PSNode expression;  
  private PrintStream out;  
  
  public PrintlnNode(PSNode e) {  
    this(e, System.out);  
  }  
  
  public PrintlnNode(PSNode e, PrintStream o) {  
    expression = e;  
    out = o;  
  }  
  
  @Override  
  public PSValue evaluate(long idPaquete, Calendar fecha, IInterpreter intepr) {  
    PSValue value = expression.evaluate(idPaquete, fecha, intepr);  
    out.println(value);  
    return PSValue.VOID;        //el print no retorna nada, solo imprime, por eso retorna void ! para eso se usa el PSValue.VOID
  } 
  
  @Override  
  public PSValue check() {  
    PSValue value = expression.check();  
    out.println(value);  
    return PSValue.VOID;        //el print no retorna nada, solo imprime, por eso retorna void ! para eso se usa el PSValue.VOID
  } 
  
  @Override
  public boolean isAtomic(){
  	return false;
  }
} 
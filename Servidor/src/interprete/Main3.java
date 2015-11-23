package interprete;

public class Main3 {  
  public static void main(String[] args) throws Exception {  
           
	  IInterpreter inter = new InterpreterController();
	  String algorithm = "if true then println(1) else println(x) endif";
	  
	  try{
		  inter.check(algorithm);
	  }
	  catch(Exception e){
		  System.out.println("here i am");
		  System.out.println(e.getMessage());
	  }
    
  }  
}
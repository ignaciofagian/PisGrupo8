package interprete;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;

import ejb.EjbAlgoritmo;
import ejb.IEjbAlgoritmo;

public class InterpreterController implements IInterpreter {
	private IEjbAlgoritmo ejb;
	private boolean debug = false;

	public static String jdni = "java:app/Servidor/EjbAlgoritmo";
	
	@Override
	public void check(String algorithm) {
		// TODO Auto-generated method stub
		CharStream stream = new ANTLRStringStream(algorithm);
		  
		PSLexer lexer = new PSLexer(stream); 
	          
	    // wrap a token-stream around the lexer  
	    CommonTokenStream tokens = new CommonTokenStream(lexer);  
	     
	    // create the parser  
	    PSParser parser = new PSParser(tokens); 
	    IErrorReporter errorReporter = new StdErrReporter();
	    lexer.setErrorReporter(errorReporter);
	    parser.setErrorReporter(errorReporter);
	    
	    parser.initializeVariables();
	    
	     try{
		    // walk the tree  
		    CommonTree tree = (CommonTree)parser.parse().getTree(); 
		   
		    CommonTreeNodeStream nodes = new CommonTreeNodeStream(tree); 
		    HashMap <String, Variable> v = (HashMap<String, Variable>) parser.getVariables();
	   
		    PSTreeWalker walker = new PSTreeWalker(nodes);
		    walker.initializeVariables(v);
		    walker.setOperations();
		    
		    PSNode p = walker.walk();
		    
		    p.check();
   
		    if (debug) System.out.println("done");   
	    }
	    catch (Exception e){
	    	throw new RuntimeException(e.getMessage());
	    }

	}

	@Override
	public void evaluate(long idPaquete, String algorithm,Calendar fecha) {
		// TODO Auto-generated method stub
		CharStream stream = new ANTLRStringStream(algorithm);
		  
		PSLexer lexer = new PSLexer(stream); 
	          
	    // wrap a token-stream around the lexer  
	    CommonTokenStream tokens = new CommonTokenStream(lexer);  
	     
	    // create the parser  
	    PSParser parser = new PSParser(tokens); 
	    IErrorReporter errorReporter = new StdErrReporter();
	    lexer.setErrorReporter(errorReporter);
	    parser.setErrorReporter(errorReporter);
	    
	    parser.initializeVariables();
	    
	     try{
		    // walk the tree  
		    CommonTree tree = (CommonTree)parser.parse().getTree(); 
		   
		    CommonTreeNodeStream nodes = new CommonTreeNodeStream(tree); 
		    HashMap <String, Variable> v = (HashMap<String, Variable>) parser.getVariables();
	   
		    PSTreeWalker walker = new PSTreeWalker(nodes);
		    walker.initializeVariables(v);
		    walker.setOperations();
		    
		    PSNode p = walker.walk();
		    
		    p.evaluate(idPaquete, fecha, this);
		    
		    Operations d = walker.getOperations();
		    IEjbAlgoritmo al = this.getEJBAlgoritmo();
		    
		    if (d.getBuyOperations() > 0){
		    	if(debug) System.out.println("Buy Operations " + d.getBuyOperations());
		    	al.buy(idPaquete, d.getBuyOperations());
		    }
		    
		    if (d.getSellOperations() > 0){
		    	if (debug) System.out.println("Sell Operations " + d.getSellOperations());
		    	al.sell(idPaquete, d.getSellOperations());
		    }
		    
		    if (debug) System.out.println("done");    
	    }
	    catch (Exception e){
	    	e.printStackTrace();
	    	throw new RuntimeException(e.getMessage());
	    }
		

	}

	@Override
	public void evaluate(long idPaquete, String algorithm) {
		evaluate(idPaquete, algorithm, Calendar.getInstance());
		
	}

	@Override
	public void setEJBAlgoritmo(IEjbAlgoritmo ejb) {
		this.ejb  = ejb;
		
	}

	@Override
	public IEjbAlgoritmo getEJBAlgoritmo() {
		return ejb;
		
	}

	@Override
	public void setDebug(boolean debug) {
		this.debug = debug;
		
	}

	@Override
	public boolean getDebug() {
		return this.debug;
		
	}
	
	

}

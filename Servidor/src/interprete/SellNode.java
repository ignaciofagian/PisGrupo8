package interprete;

import java.util.Calendar;

public class SellNode implements PSNode{
	
	private PSNode exp;
	private Operations oper;
	
	public SellNode(PSNode exp, Operations oper) {
		this.exp = exp;
		this.oper = oper;
	}
	
	@Override
	public PSValue evaluate(long idPaquete, Calendar fecha, IInterpreter intepr) {
		PSValue a = exp.evaluate(idPaquete, fecha, intepr);
        

        if (!a.isNumber()){
        	throw new RuntimeException("Invalid expression in Sell expression.");
        }else{
        	if (a.asDouble() != 0){
        		double o = oper.getSellOperations();
        		o = o + a.asDouble();
        		oper.setSellOperations(o);	
        	}
	        	
        	return PSValue.VOID;
        	
        }
	}
	
	@Override
	public PSValue check() {
		PSValue a = exp.check();
        

        if (!a.isNumber()){
        	throw new RuntimeException("Invalid expression in Sell expression.");
        }else{
        	if (exp.isAtomic()){
	        	if (a.asDouble() < 0){
	        		throw new RuntimeException("Error: negative SELL operation not allowed.");
	        	}else{
	        		double o = oper.getSellOperations();
	        		o = o + a.asDouble();
	        		oper.setSellOperations(o);
	        	}
        	}
        	//ver como obtengo el average aqui
        	return PSValue.VOID;
        	
        }
	}
	
	@Override
    public boolean isAtomic(){
    	return false;
    }
}
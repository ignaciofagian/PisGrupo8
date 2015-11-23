package interprete;

import java.util.Calendar;

public class BuyNode implements PSNode{
	
	private PSNode exp;
	private Operations oper;
	
	public BuyNode(PSNode exp, Operations oper) {
		this.exp = exp;
		this.oper = oper;
	}
	
	@Override
	public PSValue evaluate(long idPaquete, Calendar fecha, IInterpreter intepr) {
		PSValue a = exp.evaluate(idPaquete, fecha, intepr);
        

        if (!a.isNumber()){
        	throw new RuntimeException("Invalid expression in Buy expression.");
        }else{
        	if (a.asDouble() != 0){
        		double o = oper.getBuyOperations();
        		o = o + a.asDouble();
        		oper.setBuyOperations(o);
        	}
        	return PSValue.VOID;
        	
        }
	}
	
	@Override
	public PSValue check() {
		PSValue a = exp.check();
        if (!a.isNumber()){
        	throw new RuntimeException("Invalid expression in Buy expression.");
        }else{
        	if ((exp.isAtomic())){
	        	if (a.asDouble() < 0){
	        		throw new RuntimeException("Error: negative Buy operation not allowed.");
	        		
	        	}else{
	        		double o = oper.getBuyOperations();
	        		o = o + a.asDouble();
	        		oper.setBuyOperations(o);
	        	}
        	}
        }
        	return PSValue.VOID;

	}
	
	@Override
	public boolean isAtomic(){
		return false;
	}
}

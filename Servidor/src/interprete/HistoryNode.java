package interprete;

import java.util.Calendar;
import java.util.HashMap;

import ejb.EjbAlgoritmo;
import ejb.IEjbAlgoritmo;

public class HistoryNode implements PSNode{
	
	
	private PSNode exp;
	
	public HistoryNode(PSNode exp) {
		this.exp = exp;
	}
	
	@Override
	public PSValue evaluate(long idPaquete, Calendar fecha, IInterpreter intepr) {
		PSValue a = exp.evaluate(idPaquete, fecha, intepr);
		double result = 0;
		
        if (!a.isNumber()){
        	throw new RuntimeException("Invalid expression in History expression.");
        }else{
        	int indexA = a.asDouble().intValue();
        	if (indexA != 0){
        		if (indexA > 0){
        			throw new RuntimeException("Index out of bound in History expression. Greater than zero.");
        		}
        		
        		IEjbAlgoritmo iejbalgoritmo = intepr.getEJBAlgoritmo();
        		HashMap<Integer, Double> historic = (HashMap<Integer, Double>) iejbalgoritmo.obtenerHistoria(idPaquete, indexA, indexA,fecha);
        		
        		int size = historic.size();
        		if (size > 0){
        			double suma = 0;
	        		for (Double value : historic.values()){
	        			result = value.doubleValue();
	        		}
        		}
        		//QUE HAGO SI SIZE ES 0 ????
        	}

        	return new PSValue(result);
        	
        }
	}
	
	@Override
	public PSValue check() {
		PSValue a = exp.check();
        
        if (!a.isNumber()){
        	throw new RuntimeException("Invalid expression in History expression.");
        }else{
        	if (exp.isAtomic()){ //this means, values can be calculates now
	        	//truncate values, -14.9 turns into -14
	        	int indexA = a.asDouble().intValue();
	        	if ((indexA > 0)){
	        		throw new RuntimeException("Index out of bound in History expression. Greater than zero.");
	        	}
        	}
	        return new PSValue(-1.0);
        	
        }
	}
	
	@Override
    public boolean isAtomic(){
    	return false;
    }
}
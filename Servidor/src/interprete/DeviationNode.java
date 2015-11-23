package interprete;

import java.util.Calendar;
import java.util.HashMap;

import ejb.EjbAlgoritmo;
import ejb.IEjbAlgoritmo;

public class DeviationNode implements PSNode{
	
	private PSNode lhs;
	private PSNode rhs;
	
	public DeviationNode(PSNode lhs, PSNode rhs){
		this.lhs =lhs;
		this.rhs = rhs;
	}

	@Override
	public PSValue evaluate(long idPaquete, Calendar fecha, IInterpreter intepr) {
		PSValue a = lhs.evaluate(idPaquete, fecha, intepr);
        PSValue b = rhs.evaluate(idPaquete, fecha, intepr);
        
        double result = 0;
        double average = 0;

        if (!a.isNumber() || !b.isNumber()){
        	throw new RuntimeException("Invalid expression in deviation expression.");
        }else{
        	//truncate values, -14.9 turns into -14
        	int indexA = a.asDouble().intValue();
        	int indexB = b.asDouble().intValue();
        	if (indexA > 0 || indexB > 0){
        		throw new RuntimeException("Index out of bound in deviation expression. Greater than zero.");
        	}
	        	if (indexA > indexB){
	        		throw new RuntimeException("Invalid range in deviation expression.");
	        	}
	        	
	        	IEjbAlgoritmo iejbalgoritmo = intepr.getEJBAlgoritmo();
        		HashMap<Integer, Double> historic = (HashMap<Integer, Double>) iejbalgoritmo.obtenerHistoria(idPaquete, indexA, indexA,fecha);
        		
        		int size = historic.size();
        		
        		
        		if (size > 0){
        			double sum = 0;
	        		for (Double value : historic.values()){
	        			average += value.doubleValue();
	        		}
	        		
	        		average = average/size;
	        		
	        		for (Double value : historic.values()){
	        			sum += Math.pow((value.doubleValue() - average),2);
	        		}
	        		
	        		result = sum/size;
        		}
        		//QUE HAGO SI SIZE ES 0 ????
        
        	return new PSValue(result);
        	
        }
	}
	
	@Override
	public PSValue check() {

        PSValue a = lhs.check();
        PSValue b = rhs.check();
         
        if (!a.isNumber() || !b.isNumber()){
        	throw new RuntimeException("Invalid expression in deviation expression.");
        }else{
        	if (lhs.isAtomic() && rhs.isAtomic()){ //this means, values can be calculates now
	        	//truncate values, -14.9 turns into -14
	        	int indexA = a.asDouble().intValue();
	        	int indexB = b.asDouble().intValue();
	        	
	        	if ((indexA > 0) || indexB > 0){
	        		throw new RuntimeException("Index out of bound in deviation expresion. Greater than zero.");
	        	}
	        	
	        	if (indexA > indexB){
	        		throw new RuntimeException("Invalid range in deviation expression.");
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

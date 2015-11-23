package interprete;

import java.util.Calendar;
import java.util.HashMap;

import ejb.EjbAlgoritmo;
import ejb.IEjbAlgoritmo;

public class AverageNode implements PSNode {
	
	 private PSNode lhs;
	 private PSNode rhs;
	    
	public AverageNode(PSNode lhs, PSNode rhs){
		this.lhs = lhs;
		this.rhs = rhs;
	}
	
	@Override
	public PSValue evaluate(long idPaquete, Calendar fecha, IInterpreter intepr) {

        PSValue a = lhs.evaluate(idPaquete, fecha, intepr);
        PSValue b = rhs.evaluate(idPaquete, fecha, intepr);
        
        double result = 0;
         
        if (!a.isNumber() || !b.isNumber()){
        	throw new RuntimeException("Invalid expression in average expression.");
        }else{
        	//truncate values, -14.9 turns into -14
        	int indexA = a.asDouble().intValue();
        	int indexB = b.asDouble().intValue();
        	if (indexA > 0 || indexB > 0){
        		throw new RuntimeException("Index out of bound in average expression. Greater than zero.");
        	}
	        	if (indexA > indexB){
	        		throw new RuntimeException("Invalid range in average expression.");
	        	}
	        	
	        	IEjbAlgoritmo iejbalgoritmo = intepr.getEJBAlgoritmo();
        		HashMap<Integer, Double> historic = (HashMap<Integer, Double>) iejbalgoritmo.obtenerHistoria(idPaquete, indexA, indexB,fecha);
        		
        		int size = historic.size();
        		if (size > 0){
        			double suma = 0;
	        		for (Double value : historic.values()){
	        			suma += value.doubleValue();
	        		}
	        		result = suma/size;
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
        	throw new RuntimeException("Invalid expression in average expression.");
        }else{
        	if (lhs.isAtomic() && rhs.isAtomic()){ //this means, values can be calculates now
	        	//truncate values, -14.9 turns into -14
	        	int indexA = a.asDouble().intValue();
	        	int indexB = b.asDouble().intValue();
	        	
	        	if ((indexA > 0) || indexB > 0){
	        		throw new RuntimeException("Index out of bound in average expresion. Greater than zero.");
	        	}
	        	
	        	if (indexA > indexB){
	        		throw new RuntimeException("Invalide range in average expression.");
	        	}
	        	
	        	//return default value, packet not created yet so there is no data available, in order to continue parsing return a -1.0
	        	//-1.0 allows use as index, not greater than 0, so no errors possibly thrown
        	}
	        return new PSValue(-1.0);
        	
        }
    }

	@Override
	public boolean isAtomic(){
		return false;
	}
}

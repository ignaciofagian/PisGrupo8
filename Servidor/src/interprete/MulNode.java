package interprete;

import java.util.Calendar;

public class MulNode implements PSNode {

    private PSNode lhs;
    private PSNode rhs;

    public MulNode(PSNode lhs, PSNode rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public PSValue evaluate(long idPaquete, Calendar fecha, IInterpreter intepr) {

        PSValue a = lhs.evaluate(idPaquete, fecha, intepr);
        PSValue b = rhs.evaluate(idPaquete, fecha, intepr);

        // number * number
        if(a.isNumber() && b.isNumber()) {
            return new PSValue(a.asDouble() * b.asDouble());
        }

        throw new RuntimeException("illegal expression: " + this);
    }

    @Override
    public PSValue check() {

        PSValue a = lhs.check();
        PSValue b = rhs.check();

        // number * number
        if(a.isNumber() && b.isNumber()) {
        	if (lhs.isAtomic() && rhs.isAtomic()){
        		return new PSValue(a.asDouble() * b.asDouble()); 
        	}
            return new PSValue(-1.0);
        }

        throw new RuntimeException("illegal expression: " + this);
    }
    
    @Override
    public String toString() {
        return String.format("(%s * %s)", lhs, rhs);
    }

    @Override
    public boolean isAtomic(){
    	return lhs.isAtomic() && rhs.isAtomic();
    }
}
package interprete;

import java.util.Calendar;

public class UnaryMinusNode implements PSNode {

    public PSNode exp;

    public UnaryMinusNode(PSNode e) {
        exp = e;
    }

    @Override
    public PSValue evaluate(long idPaquete, Calendar fecha, IInterpreter intepr) {

        PSValue v = exp.evaluate(idPaquete, fecha, intepr);

        if(!v.isNumber()) {
            throw new RuntimeException("illegal expression: " + this);
        }

        return new PSValue(-v.asDouble());
    }

    @Override
    public PSValue check() {

        PSValue v = exp.check();

        if(!v.isNumber()) {
            throw new RuntimeException("illegal expression: " + this);
        }

        return new PSValue(-v.asDouble());
    }
    
    @Override
    public String toString() {
        return String.format("(-%s)", exp);
    }
    
    @Override
    public boolean isAtomic(){
    	return exp.isAtomic();
    }
}
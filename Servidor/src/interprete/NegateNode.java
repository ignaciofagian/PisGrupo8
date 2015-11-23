package interprete;

import java.util.Calendar;

public class NegateNode implements PSNode {

    private PSNode exp;

    public NegateNode(PSNode e) {
        exp = e;
    }

    @Override
    public PSValue evaluate(long idPaquete, Calendar fecha, IInterpreter intepr) {

        PSValue v = exp.evaluate(idPaquete, fecha, intepr);

        if(!v.isBoolean()) {
            throw new RuntimeException("illegal expression: " + this);
        }

        return new PSValue(!v.asBoolean());
    }
    
    @Override
    public PSValue check() {

        PSValue v = exp.check();

        if(!v.isBoolean()) {
            throw new RuntimeException("illegal expression: " + this);
        }

        return new PSValue(true);
    }
    
    @Override
    public String toString() {
        return String.format("(not %s)", exp);
    }
    
    @Override
    public boolean isAtomic(){
    	return false;
    }
}
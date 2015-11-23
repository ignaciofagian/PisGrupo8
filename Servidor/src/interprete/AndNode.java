package interprete;

import java.util.Calendar;

public class AndNode implements PSNode {

    private PSNode lhs;
    private PSNode rhs;

    public AndNode(PSNode lhs, PSNode rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public PSValue evaluate(long idPaquete, Calendar fecha, IInterpreter intepr) {

        PSValue a = lhs.evaluate(idPaquete, fecha, intepr);
        PSValue b = rhs.evaluate(idPaquete, fecha, intepr);

        if(!a.isBoolean() || !b.isBoolean()) {
            throw new RuntimeException("illegal expression: " + this);
        }

        return new PSValue(a.asBoolean() && b.asBoolean());
    }
    
    @Override
    public PSValue check() {

        PSValue a = lhs.check();
        PSValue b = rhs.check();

        if(!a.isBoolean() || !b.isBoolean()) {
            throw new RuntimeException("illegal expression: " + this);
        }

        return new PSValue(true);
    }
    
    @Override
    public String toString() {
        return String.format("(%s and %s)", lhs, rhs);
    }
    
    @Override
    public boolean isAtomic(){
    	return lhs.isAtomic() && rhs.isAtomic();
    }
}
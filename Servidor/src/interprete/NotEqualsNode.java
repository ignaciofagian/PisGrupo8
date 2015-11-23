package interprete;

import java.util.Calendar;

public class NotEqualsNode implements PSNode {

    private PSNode lhs;
    private PSNode rhs;

    public NotEqualsNode(PSNode lhs, PSNode rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public PSValue evaluate(long idPaquete, Calendar fecha, IInterpreter intepr) {

        PSValue a = lhs.evaluate(idPaquete, fecha, intepr);
        PSValue b = rhs.evaluate(idPaquete, fecha, intepr);

        return new PSValue(!a.equals(b));
    }

    @Override
    public PSValue check() {

        PSValue a = lhs.check();
        PSValue b = rhs.check();

        return new PSValue(true);
    }
    @Override
    public String toString() {
        return String.format("(%s <> %s)", lhs, rhs);
    }
    
    @Override
    public boolean isAtomic(){
    	return false;
    }
}
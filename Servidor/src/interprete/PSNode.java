package interprete;

import java.util.Calendar;

public interface PSNode {
	PSValue evaluate(long idPaquete, Calendar fecha, IInterpreter intepr);
	PSValue check();
	boolean isAtomic();
}

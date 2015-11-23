package interprete;

import java.util.Calendar;

import ejb.IEjbAlgoritmo;
import model.PaqueteAlgoritmico;

public interface IInterpreter {

	void check(String algorithm);
	
	void evaluate(long idPaquete, String algorithm);

	void evaluate(long idPaquete, String algorithm, Calendar fecha);
	
	void setEJBAlgoritmo(IEjbAlgoritmo ejb);
	
	void setDebug(boolean debug);
	boolean getDebug();
	IEjbAlgoritmo getEJBAlgoritmo();
	
	PaqueteAlgoritmico getPA();
	void setPA(PaqueteAlgoritmico pa);
}

package auxiliar;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import model.Historico;

public class ProvFromMap extends ProvBase{
	private Map<Long,Historico> m;
	

	
	public ProvFromMap(Map<Long, Historico> m) {
		super();
		this.m = m;
	}

	public ProvFromMap() {
		this(new HashMap<Long, Historico>());
	}

	public Map<Long, Historico> getM() {
		return m;
	}


	public void setM(Map<Long, Historico> m) {
		this.m = m;
	}

	@Override
	protected Historico findHistorico_interno(long accionId, Calendar fecha) {
		// TODO Auto-generated method stub
		return m.get(accionId);
	}

}

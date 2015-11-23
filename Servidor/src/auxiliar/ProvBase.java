package auxiliar;

import java.util.Calendar;

import model.Historico;
import model.IProveedorValor;

public abstract class ProvBase implements IProveedorValor {
	private IProveedorValor fallback = null;

	@Override
	public double getValor(long accionId, Calendar fecha) {
		// TODO Auto-generated method stub
		return findHistorico(accionId, fecha).getAdjClose();
	}

	@Override
	public double getValorSinAdj(long accionId, Calendar fecha) {
		// TODO Auto-generated method stub
		return findHistorico(accionId, fecha).getClose();
	}
	
	
	public IProveedorValor getFallback() {
		return fallback;
	}

	public void setFallback(IProveedorValor fallback) {
		this.fallback = fallback;
	}

	protected abstract Historico findHistorico_interno(long accionId, Calendar fecha);
		
	
	
	@Override 
	public Historico findHistorico(long accionId, Calendar fecha) {
		Historico h = findHistorico_interno(accionId,fecha);
		if (h == null && fallback != null){
			h = fallback.findHistorico(accionId, fecha);
		}
		return h;
	}



}

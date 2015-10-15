package model;

import java.util.Calendar;

public interface IProveedorValor {
	public double getValor(long accionId, Calendar fecha);
	public double getValorSinAdj(long accionId,Calendar fecha);
	public Historico  findHistorico(long accionId, Calendar fecha);
	public double getAdjFactor(Calendar t1, Calendar t2, long accionId);
}

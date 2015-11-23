package datatypes;

import java.util.List;

// no se si al final se va a usar
public class DTListaSaldos {
	private String msg = "";
	private int lost = 0;
	private List<DTSaldo> saldos;
	public int getLost() {
		return lost;
	}
	public void setLost(int lost) {
		this.lost = lost;
	}
	public List<DTSaldo> getSaldos() {
		return saldos;
	}
	public void setSaldos(List<DTSaldo> saldos) {
		this.saldos = saldos;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}

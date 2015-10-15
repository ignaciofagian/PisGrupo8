package datatypes;

import java.text.SimpleDateFormat;
import java.util.Calendar;



public class DTSaldo {
	String tiempo = ""; // YYYY-MM-DD hh:mm
	//long tiempoMilis;
	int saldo;

	
	
	
	public String getTiempo() {
		return tiempo;
	}
	public void setTiempo(String tiempo) {
		this.tiempo = tiempo;
	}

	public int getSaldo() {
		return saldo;
	}
	public void setSaldo(int saldo) {
		this.saldo = saldo;
	}


	

	public DTSaldo(String tiempo, int saldo) {
		super();
		this.tiempo = tiempo;
		this.saldo = saldo;
	}
	public DTSaldo( Calendar fecha, int saldo) {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm");
		this.tiempo = sdf.format(fecha.getTime());
		this.saldo = saldo;
	}
	
	
	
	public DTSaldo() {

	}
	
	
	
	
}

package model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PaqueteAlgoritmico")
@DiscriminatorValue("Algoritmico")
public class PaqueteAlgoritmico extends Paquete {
	
	private static final long serialVersionUID = 1L;
	
	private String algoritmo;
	private Double inversion = 100.0;
	private Double disponible = 0.0;
	private Calendar fechaCalculo = new GregorianCalendar();
	
	@Override public boolean detalladoPorUsuario() {
		// se mantiene igual para todos los clientes
		return false;
	  }
	
	public PaqueteAlgoritmico()
	{
		super("Algoritmico");
	}
	
	public String getAlgoritmo() {
		return algoritmo;
	}

	public void setAlgoritmo(String algoritmo) {
		this.algoritmo = algoritmo;
	}
	
	public void calcularValorPorVarMercado(IProveedorValor prov,Calendar ahora){
		double ratio = getValorRatio(fechaCalculo, ahora, prov);
		inversion = inversion * ratio; 
		fechaCalculo = ahora;
	}
	
	public void comprar(double dolares){
		double cantidadComprada = Math.min(dolares, disponible);
		disponible = disponible - cantidadComprada;
		inversion = inversion + cantidadComprada;
	}
	

	
	public void vender(double dolares){
		double cantidadVendido = Math.min(dolares, inversion);
		inversion = inversion - cantidadVendido;
		disponible = disponible + cantidadVendido;
	}

	@Override
	public double getValorTotal(){
		return inversion + disponible;
	}
	
	public double getInversion() {
		return inversion;
	}

	public void setInversion(double inversion) {
		this.inversion = inversion;
	}

	public double getDisponible() {
		return disponible;
	}

	public void setDisponible(double disponible) {
		this.disponible = disponible;
	}

	public Calendar getFechaCalculo() {
		return fechaCalculo;
	}

	public void setFechaCalculo(Calendar fechaCalculo) {
		this.fechaCalculo = fechaCalculo;
	}
	
	
	
}

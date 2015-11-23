package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import log.LogConfig;
import model.PK.ClientePaquetePK;

@Entity
@IdClass(ClientePaquetePK.class)
@Table(name = "ClientePaquete")
public class ClientePaquete implements Serializable {
	private static final Logger log =LogManager.getLogger("ps.cp");

	
	private static final long serialVersionUID = 1L;
	

	@Id
	@ManyToOne(fetch=FetchType.LAZY )
	@JoinColumn(name = "IDPortafolio")
	private Portafolio portafolio;

	@Id
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "IDPaq")
	private Paquete paquete;
	


	@OneToMany(cascade=CascadeType.ALL,orphanRemoval= true,fetch=FetchType.LAZY)
	@JoinColumns(value={@JoinColumn(name="Acc_IDPortafolio", referencedColumnName="IDPortafolio"),
			@JoinColumn(name="Acc_IDPaq", referencedColumnName="IDPaq")})
	private Collection<ClienteAccion> acciones = new ArrayList<ClienteAccion>();
	
	// cantidad si hay una sola accion o es algoritmico
	private double cantidad;
	private double inversion;

	public ClientePaquete()
	{
		
	}


	public Paquete getPaquete() {
		return paquete;
	}

	public void setPaquete(Paquete paquete) {
		this.paquete = paquete;
	}

	public double getInversion() {
		return inversion;
	}

	public void setInversion(double inversion) {
		this.inversion = inversion;
	}
	



	public Portafolio getPortafolio() {
		return portafolio;
	}


	public void setPortafolio(Portafolio portafolio) {
		this.portafolio = portafolio;
	}


	public Collection<ClienteAccion> getAcciones() {
		return acciones;
	}


	public void setAcciones(Collection<ClienteAccion> acciones) {
		this.acciones = acciones;
	}


	/**
	 * Indica si puede ser que necesite ajustar por acciones corporativas
	 * Si es en el mismo dia no es necesario
	 * @param t1
	 * @param t2
	 * @return
	 */
	private boolean necesitoAjuste(Calendar t1,Calendar t2){
		//boolean mismoDia =  (t1.get(Calendar.DAY_OF_YEAR) == t2.get(Calendar.DAY_OF_YEAR)) && 
		//		(t1.get(Calendar.YEAR) == t2.get(Calendar.YEAR));
		//return !mismoDia;
		return false;
	}
	
	private double ajustarCantidad(Calendar ultimaFecha, Calendar momento, IProveedorValor prov, Accion a, double cantidad) {
		Historico h1 = prov.findHistorico(a.getId(), ultimaFecha);
		Historico h2 = prov.findHistorico(a.getId(), momento);
		if (h1 == null || h2 == null){
			if (h1 == null) log.info("h1 null fecha: " + ultimaFecha.getTime());
			if (h2 == null) log.info("h2 null fecha: " + momento.getTime());
			return cantidad;
		}
		double adj = Historico.getAdjFactor(h1, h2);
		if (Math.abs(adj - 1.0) > 0.0001) {
			log.debug("ajuste x" +adj);
			double cantidadAdj = cantidad * adj;
			cantidad = cantidadAdj;
		}
		return cantidad;
	}
	
	/**
	 * Actualizar inversion suponiendo que no es necesario realizar ajustes y que el precio es el actual
	 * Acepta un valor precalculado como optimizacion
	 * Mucho cuidado de cuando se llama
	 * @param momento
	 * @param prov
	 */
	public void actualizarInversionAhora(Double precalc){
		if (precalc != null){
			this.inversion = precalc;
		}
		else if (this.paquete.detalladoPorUsuario()){
			double suma = 0;
			for (ClienteAccion e : acciones) {
				suma += e.getAccion().getValorActual() * e.getCantidad();
			}
			this.inversion = suma;
		}
		else{
			// manejo separado el caso no detallado (paquete con una accion/indice o algoritmico) porque pienso 
			// que es el mas comun y puedo mantenerlo en memoria con un cache, asi evito accesos a la BD
			this.inversion = this.cantidad * this.getPaquete().getValorTotal();
		}
	}







	private double actualizarComplejo(Calendar ultimaFecha, Calendar momento, IProveedorValor prov) {
		double suma = 0;
		// es un paquete "indice" complejo compuesto por varias acciones
		for (ClienteAccion e : acciones) {
			Accion a = e.getAccion();
			if (log.isTraceEnabled()) log.trace("\t\t\t" + a.getSymbol() + " cant " +  e.getCantidad());
			
			double cantidad = e.getCantidad();
			if (necesitoAjuste(ultimaFecha, momento)){
				cantidad = ajustarCantidad(ultimaFecha, momento, prov,  a, cantidad);
				e.setCantidad(cantidad);
			}
			double val =  prov.getValor(a.getId(), momento);
			if (log.isTraceEnabled()) log.trace("val=" +  val);
			double sumando = val * cantidad;
			
			suma += sumando;
			if (log.isTraceEnabled()) log.trace("\t\tSumando: " + sumando  +" sumando " + suma);
		}
		return suma;
	}

	/*
	 * Actualizar inversion mas general, que toma los datos del proveedor
	 */
	public void actualizarInversion(Calendar ultimaFecha, Calendar momento, IProveedorValor prov) {
		double bak = this.inversion;
		if (log.isDebugEnabled()) log.debug("\tPaquete: " + this.getPaquete().getNombre() + " InversionPre: " +   this.getInversion() );
		if (this.paquete.detalladoPorUsuario()) {
			double suma = actualizarComplejo(ultimaFecha, momento, prov);
			inversion = suma;

		} else {

			
			double valRatio = this.paquete.getRazonReal( ultimaFecha,momento, prov);
			
			if (log.isDebugEnabled())  log.debug("\t\tND - vr= " + valRatio  + " cantidad=" + cantidad);
			inversion = inversion * valRatio;

		}
		ultimaFecha = momento;
		if (Double.isNaN(this.inversion) || Double.isInfinite(this.inversion)){
			log.error("Error calculo de saldo no numerico: " + this.inversion);
			this.inversion = bak;
		}
		if (log.isDebugEnabled()) log.debug("\tInversionPost: " + inversion );
	}


	
	
	public ClientePaquete(Portafolio port, Paquete p){
		this.paquete = p;
		this.portafolio = port;
	}
	
	/**
	 * Cambia la inversion en el paquete 
	 * Es como si vendiera todo y invirtiera para comprar de nuevo
	 * @param invers
	 * @return cantidad que hay que sumar a lo no invertido para mantener el saldo total del portafolio
	 */
	public double asignarInversion(double invers){
		double  diff = this.inversion - invers;
		if (paquete.detalladoPorUsuario()) {
			double porcentajeTotal = 0;
			for (PaqueteAccion a : paquete.getAccionesPaquete()) {
				porcentajeTotal += a.getInversion(); // se supone que suma 100%, pero si no hagamos que sea proporcional
			}
			this.acciones.clear();
			
			for (PaqueteAccion a : paquete.getAccionesPaquete()) {
				// asumo que los valores de inversion son porcentajes
				double invEnAccion = a.getInversion() * invers / porcentajeTotal; // dinero asignado a una accion especifica dentro del paquete
				/*
				 * guardo cantidades para minimizar el numero de escrituras en
				 * vez de estar guardando proporciones, que son matematicamente
				 * equivalentes pero cambian constantemente
				 */
				double cantidadAccion = invEnAccion / a.getAccion().getValorActual();
				acciones.add(new ClienteAccion(a.getAccion(), cantidadAccion));
			}
			this.inversion = invers;
			this.cantidad = this.inversion / paquete.getValorTotal();
		} else {
			this.inversion = invers;
			this.cantidad = this.inversion / paquete.getValorTotal();
		}
		return diff;
	}
	
	
	/**
	 * Cambia la inversion en el paquete 
	 * Es como si vendiera todo y invirtiera para comprar de nuevo
	 * @param invers
	 * @return cantidad que hay que sumar a lo no invertido para mantener el saldo total del portafolio
	 */
	public double asignarInversionHistorico(double invers,IProveedorValor prov,Calendar fecha){
		double  diff = this.inversion - invers;
		if (paquete.detalladoPorUsuario()) {
			double porcentajeTotal = 0;
			for (PaqueteAccion a : paquete.getAccionesPaquete()) {
				porcentajeTotal += a.getInversion(); // se supone que suma 100%, pero si no hagamos que sea proporcional
			}
			this.acciones.clear();
			
			for (PaqueteAccion a : paquete.getAccionesPaquete()) {
				// asumo que los valores de inversion son porcentajes
				double invEnAccion = a.getInversion() * invers / porcentajeTotal; // dinero asignado a una accion especifica dentro del paquete
				/*
				 * guardo cantidades para minimizar el numero de escrituras en
				 * vez de estar guardando proporciones, que son matematicamente
				 * equivalentes pero cambian constantemente
				 */
				double cantidadAccion = invEnAccion / prov.getValor(a.getAccion().getId(), fecha);
				acciones.add(new ClienteAccion(a.getAccion(), cantidadAccion));
			}
		}
		this.inversion = invers;
		this.cantidad = this.inversion / paquete.getValorTotal();
		return diff;
	}

	public static ClientePaquete generar(Cliente cli, Paquete p, double invers) {
		ClientePaquete nuevo = new ClientePaquete();
		nuevo.paquete = p;
		nuevo.portafolio = cli.getPortafolio();
		if (invers != 0){
			nuevo.asignarInversion(invers);
		}
	
		cli.getPortafolio().getPaquetes().add(nuevo);
		return nuevo;

	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((paquete == null) ? 0 : paquete.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientePaquete other = (ClientePaquete) obj;
		if (paquete == null) {
			if (other.paquete != null)
				return false;
		} else if (!paquete.equals(other.paquete))
			return false;
		return true;
	}
	
	
	

	
}

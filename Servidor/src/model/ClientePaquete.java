package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import model.PK.ClientePaquetePK;

@Entity
@IdClass(ClientePaquetePK.class)
@Table(name = "ClientePaquete")
public class ClientePaquete implements Serializable {
	private static final long serialVersionUID = 1L;
	

	@Id
	@ManyToOne( )
	@JoinColumn(name = "IDPortafolio")
	private Portafolio portafolio;

	@Id
	@ManyToOne()
	@JoinColumn(name = "IDPaq")
	private Paquete paquete;
	
	//private double saldoDisponible;
	
	/*@JoinTable(joinColumns={@JoinColumn(name="IDUsuario", referencedColumnName="IDUsuario"),
							@JoinColumn(name="IDPaq", referencedColumnName="IDPaq")}, 
				inverseJoinColumns={@JoinColumn(name="IDCliAccion", referencedColumnName="IDCliAccion")})	*/
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acciones == null) ? 0 : acciones.hashCode());
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
		if (acciones == null) {
			if (other.acciones != null)
				return false;
		} else if (!acciones.equals(other.acciones))
			return false;
		if (paquete == null) {
			if (other.paquete != null)
				return false;
		} else if (!paquete.equals(other.paquete))
			return false;
		return true;
	}

	@OneToMany(cascade=CascadeType.ALL,orphanRemoval= true)
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
		boolean mismoDia =  (t1.get(Calendar.DAY_OF_YEAR) == t2.get(Calendar.DAY_OF_YEAR)) && 
				(t1.get(Calendar.YEAR) == t2.get(Calendar.YEAR));
		return !mismoDia;
	}



	public void actualizarInversion(Calendar ultimaFecha, Calendar momento, IProveedorValor prov) {
		double suma = 0;
		double bak = this.inversion;
		System.out.println("\t" + "Paquete:" + this.getPaquete().getNombre() + "InversionPre: " + this.getInversion() );
		if (this.paquete.detalladoPorUsuario()) {
			
			for (ClienteAccion e : acciones) {
				Accion a = e.getAccion();
				System.out.println("\t\t\t" + a.getSymbol() + " cant " +  e.getCantidad());
				double cantidad = e.getCantidad();
				if (necesitoAjuste(ultimaFecha, momento)){
					double adj = prov.getAdjFactor(ultimaFecha, momento, a.getId());
					if (Math.abs(adj - 1.0) > 0.0001) {
						double cantidadAdj = cantidad * adj;
						cantidad = cantidadAdj;
						e.setCantidad(cantidadAdj);
					}
				}
				Calendar rightNow = Calendar.getInstance();
				double sumando;
				if (Math.abs(rightNow.getTimeInMillis() - momento.getTimeInMillis()) > 60000){
					sumando= prov.getValor(a.getId(), momento) * cantidad;
					
				}
				else{
					sumando = a.getValorActual() * cantidad;
				}
				suma += sumando;
				System.out.println("\t\t" + "Sumando:" + sumando + "suma " +  suma);
			}
			inversion = suma;

		} else {
			// simplificacion, se manienen los porcentajes en cada paso de ejecucion
			Calendar rightNow = Calendar.getInstance();
			if (Math.abs(rightNow.getTimeInMillis() - momento.getTimeInMillis()) > 60000){
				inversion = cantidad * this.paquete.getValorRatio( ultimaFecha,momento, prov);
			}
			else{
				System.out.println("\t\t" + "No detallado valorTotal: " + this.paquete.getValorTotal());
				inversion = cantidad * this.paquete.getValorTotal();
			}
		}
		ultimaFecha = momento;
		if (Double.isNaN(this.inversion) || Double.isInfinite(this.inversion)){
			System.out.println("Error calculo de saldo no numerico: " + this.inversion);
			this.inversion = bak;
		}
		System.out.println("\t" + "InversionPost:" + inversion );
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
	
	


	/*public double getSaldoDisponible() {
		return saldoDisponible;
	}


	public void setSaldoDisponible(double saldoDisponible) {
		this.saldoDisponible = saldoDisponible;
	}*/

	
	
	
	
	/*public static UsrPaquete generar(Usuario u, Paquete p, double invers) {
		UsrPaquete nuevo = new UsrPaquete();
		nuevo.paquete = p;
		if (p.detalladoPorUsuario()) {
			for (PaqueteAccion a : p.getAcciones()) {
				// asumo que los valores de inversion son porcentajes
				double invPorAccion = a.getInversion() * invers / 100.0;
				/*
				 * guardo cantidades para minimizar el numero de escrituras en
				 * vez de estar guardando proporciones, que son matematicamente
				 * equivalentes pero cambian constantemente
				 
				double cantidad = invPorAccion / a.getAccion().getValorActual();
				nuevo.acciones.add(new ClienteAccion(nuevo, a.getAccion(), cantidad));
			}
			nuevo.inversion = invers;
		} else {
			nuevo.inversion = invers;
		}
		return nuevo;

	}
*/
	
}

package model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.eclipse.persistence.jpa.config.Cascade;

@Entity
@Table(name = "PaqueteAlgoritmico")
@Cacheable(value=true)
@DiscriminatorValue("Algoritmico")
public class PaqueteAlgoritmico extends Paquete {
	
	private static final long serialVersionUID = 1L;
	
	private String algoritmo;
	@Column(name="nominal")
	private Double nominal = 100.0;
	private Double inversion = 100.0;
	private Double disponible = 0.0;
	private Calendar fechaCalculo = new GregorianCalendar();
	
	// para no complicarme creo una action ficticia asi tengo el saldo historico
	// sin tener que alterar todas las consultas de saldo
	// lo mas adecuado seria especializar historico en historico_accion y historico_paquete, pero...
	
	// debo tener cascade.ALL o hibernate me da error que tengo estado transient
	@OneToOne(cascade = CascadeType.ALL) @JoinColumn(name="acc_equivalente")
	private Accion equivalente;  //  invertida ajustada por inversion
	@OneToOne(cascade = CascadeType.ALL) @JoinColumn(name="acc_historia")
	private Accion accionHistoria; // totalmente invertida para calcular la historia
	@OneToOne(cascade = CascadeType.ALL) @JoinColumn(name="acc_debug")
	private Accion accionDebug; // disponible y invertido para propositos de debug
	
	@Column(name="recalcular")
	private boolean recalcular = false;
	
	@Override public boolean detalladoPorUsuario() {
		// se mantiene igual para todos los clientes
		return false;
	  }
	
	@Override public double getRazonReal(Calendar t1, Calendar t2, IProveedorValor prov) {
		Historico h1 = prov.findHistorico(equivalente.getId(), t1);
		Historico h2 = prov.findHistorico(equivalente.getId(), t2);
		return Historico.getAdjFactor(h1, h2);
	};
	
	public PaqueteAlgoritmico()
	{
		super("Algoritmico");	
	}
	
	public void crearEquivalente(){
		equivalente = new Accion();
		equivalente.setSymbol("ps.equival_alg_" + this.getNombre());
		equivalente.setNombre(equivalente.getSymbol());
		equivalente.setValorActual(this.getValorTotal());
		

	}
	
	public void crearAccHistoria(){
		accionHistoria = new Accion();
		accionHistoria.setSymbol("ps.historia_alg_" + this.getNombre());
		accionHistoria.setNombre(accionHistoria.getSymbol());
		accionHistoria.setValorActual(this.getInversion());
		accionHistoria.setEquivalente(true);
	}
	
	public void crearAccDebug(){
		accionDebug = new Accion();
		accionDebug.setSymbol("ps.debug_alg_" + this.getNombre());
		accionDebug.setNombre(accionDebug.getSymbol());
		accionDebug.setValorActual(this.getInversion());
		accionDebug.setEquivalente(true);
	}
	
	public boolean checkearAcciones(){
		boolean cambio = equivalente == null || accionHistoria == null || accionDebug == null;
		if (equivalente == null){
			crearEquivalente();
		}
		if (accionHistoria == null){
			crearAccHistoria();
		}
		if (accionDebug == null){
			crearAccDebug();
		}
		if (!equivalente.isEquivalente()) equivalente.setEquivalente(true);
		return cambio;
	}
	
	private void actualizarSaldoEquivalente(){
		checkearAcciones();
		equivalente.agregarHistorico(fechaCalculo.getTime(), this.getValorTotal());
		accionHistoria.agregarHistorico(fechaCalculo.getTime(), this.getValorTotal());
	}
	
	public String getAlgoritmo() {
		return algoritmo;
	}

	public void setAlgoritmo(String algoritmo) {
		this.algoritmo = algoritmo;
	}
	
	public void calcularValorPorVarMercado(IProveedorValor prov,Calendar ahora){
		double ratio = getValorRatio(fechaCalculo, ahora, prov);
		if (nominal == null){
			nominal = 100.0; //TODO: MAL!!!!!!!!!
		}
		else{
			nominal = nominal * ratio;
		}
		
		inversion = inversion * ratio; 
		fechaCalculo = ahora;
		
		actualizarSaldoEquivalente();
	}
	

	
	public void comprar(double dolares){
		double cantidadComprada = Math.min(dolares, disponible);
		disponible = disponible - cantidadComprada;
		inversion = inversion + cantidadComprada;
		//accionDebug.setValorActual(this.inversion);
		//accionDebug.agregarHistorico(this.getFechaCalculo().getTime(), disponible, inversion);	
	}
	
	public void vender(double dolares){
		double cantidadVendido = Math.min(dolares, inversion);
	
		inversion = inversion - cantidadVendido;
		disponible = disponible + cantidadVendido;
		
		// Verifico si debo realizar un short 
		if (cantidadVendido < dolares)
		{
			boolean esRiesgosoOCortoPlazo = false;
			
			for (PaqueteCategoria pc : this.getCategorias())
			{
				if (pc.getCategoria().equals("LowRisk")
						|| pc.getCategoria().equals("ShortTerm")){
					esRiesgosoOCortoPlazo = true;
					break;
				}
			}
			
			// Chequeo si es un paquete Resgoso o de CortoPlazo	
			if (esRiesgosoOCortoPlazo)
			{
				double cantidadEnShort = dolares - cantidadVendido;
				inversion -= cantidadEnShort;
				disponible += cantidadEnShort;
			}
		}
		//accionDebug.setValorActual(this.inversion);
		//accionDebug.agregarHistorico(this.getFechaCalculo().getTime(), disponible, inversion);
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
	
	@Override 
	public Accion getAccionEquivalente() {
		// TODO Auto-generated method stub
		return equivalente;
	}

	public Accion getEquivalente() {
		return equivalente;
	}

	public void setEquivalente(Accion equivalente) {
		this.equivalente = equivalente;
	}

	public Double getNominal() {
		return nominal;
	}

	public void setNominal(Double nominal) {
		this.nominal = nominal;
	}

	public Accion getAccionHistoria() {
		return accionHistoria;
	}

	public void setAccionHistoria(Accion accionHistoria) {
		this.accionHistoria = accionHistoria;
	}

	public Accion getAccionDebug() {
		return accionDebug;
	}

	public void setAccionDebug(Accion accionDebug) {
		this.accionDebug = accionDebug;
	}

	public boolean isRecalcular() {
		return recalcular;
	}

	public void setRecalcular(boolean recalcular) {
		this.recalcular = recalcular;
	}
	
	
	
	
}

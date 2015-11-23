package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;



@Entity
@Table(name = "Portafolio")
public class Portafolio implements Serializable {
	 private static final Logger log = LogManager.getLogger("ps.port");

	private static final long serialVersionUID = 1L;

	//Hay un bug con @Id @OneToOne cliente https://hibernate.atlassian.net/browse/HHH-6813
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "IDPortafolio")
	private Long id;
	
	//@Version @Column(name="entity_version")
	//private int version = 1;  // campo de version para optimistic locking de JPA
	
	// si estoy corriendo en modo historico, 
	// asi uso la query optimizada segun el escenario
	@Column(name="historico")
	private boolean historico = false;
	
	@Column(name="perdio")
	private boolean lost = false;
	
	@Column(name="activo") // indica si debo incluirlo en la actualizacion 
	private boolean activo = true;
	
	

	//Mapeo el id del portafolio en la tabla del cliente (usuario)
	@OneToOne(mappedBy="portafolio",fetch= FetchType.LAZY)
	private Cliente cliente;



	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY, orphanRemoval=true,mappedBy="portafolio")
	private Set<ClientePaquete> paquetes = new HashSet<ClientePaquete>();

	/*@OneToMany(cascade=CascadeType.ALL,orphanRemoval=false,fetch=FetchType.LAZY) @JoinColumn(name="portafolio_idportafolio",updatable=false)
	@LazyCollection(LazyCollectionOption.EXTRA)
	private Collection<SaldoHistorico> saldoHistorico;*/
	
	private Calendar ultimaFechaSaldo = new GregorianCalendar(); // ultima fecha en que se calculo el
										// saldo para ver ajuste
	
	@Temporal(TemporalType.TIMESTAMP) @Column(name="ultimoacceso")
	private Calendar ultimoAcceso; // para futuro
	
	private double ultimoSaldo;
	private double sinInvertir = 0;

	
	public double getSinInvertir() {
		return sinInvertir;
	}

	public void setSinInvertir(double sinInvertir) {
		this.sinInvertir = sinInvertir;
	}

	

	public Portafolio(Cliente c)
	{
		//this.saldoHistorico = new ArrayList<SaldoHistorico>();
		this.paquetes = new HashSet<ClientePaquete>();
		this.cliente = c;
	}
	
	private SaldoHistorico agregarSaldo(Calendar fecha,double valor){
		//this.saldoHistorico.add(new SaldoHistorico(this, fecha, valor));
		SaldoHistorico s = new SaldoHistorico(this, fecha, valor);
		this.ultimoSaldo = valor;
		return s;
	}
	
	public void asignarSaldoInicial()
	{
		//Le pongo 200 es para probar luego se borra o se cambia
		this.ultimaFechaSaldo = Calendar.getInstance();
		agregarSaldo(ultimaFechaSaldo, 200);
		if (this.paquetes.isEmpty()){
		sinInvertir = 200;
		}
	}
	
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Portafolio()
	{
		//this.saldoHistorico = new ArrayList<SaldoHistorico>();
		this.paquetes = new HashSet<ClientePaquete>();
		this.ultimaFechaSaldo= new GregorianCalendar();
		//this.cliente = null;
	}
	
	
	public SaldoHistorico calcularSaldo(Calendar fechaActual, IProveedorValor prov) {
		log.info("***** INICIO Calcular Saldo idPort=" + this.getId());
		if (log.isInfoEnabled()) 
			log.info("\tFecha Anterior: " + this.getUltimaFechaSaldo().getTime() + " Fecha Actual: " + fechaActual.getTime());  
		if (fechaActual.before(ultimaFechaSaldo)) {
			String error = String.format("FechaActual: %1$F %1$T < Ultima: %2$F %2$T", fechaActual, ultimaFechaSaldo);
			log.warn("Advertencia: " + error + " idPort=" +  this.getId());
		}

		for (ClientePaquete p : paquetes) {
			p.actualizarInversion(ultimaFechaSaldo, fechaActual, prov);
		}
		return sumarPaquetes(fechaActual);
	}
	
	public SaldoHistorico calcularSaldoOptimizado(Calendar fechaActual,Map<Long,Double> precalc){
		if (precalc == null) precalc = new HashMap<Long, Double>();
		for(ClientePaquete p : paquetes){
			p.actualizarInversionAhora(precalc.get(p.getPaquete().getId()));
		}
		return sumarPaquetes(fechaActual);
	}
	
	private SaldoHistorico sumarPaquetes(Calendar fechaActual){
		double suma = 0;
		for (ClientePaquete p : paquetes) {
			suma += p.getInversion();
		}
		log.info("***** idPort " + this.id + " -  INVERSION EN PAQUETES = $" + suma + " sinInvertir=$" + sinInvertir);
		suma += sinInvertir;
		SaldoHistorico s = agregarSaldo(fechaActual, suma);
		ultimaFechaSaldo = fechaActual;
		return s;
	}
	
	//**Asumo que ultimaFechaSaldo esta seteada y ademas pertenece a saldoHistorico
	public void ajustarInversionPaquetesEnBaseRespEspecificas(HashMap<Long,RespuestaEspecifica> respuestasEspecificas,IProveedorValor prov)
	{
		//Le asigno estas respuesta al cliente
		System.out.println("Ajusto Inversion Especificas idPort " + this.getId());
		
		this.cliente.getRespuestas().addAll(respuestasEspecificas.values());
		
		//La clave es el Id de la pregunta a la que pertence esa respuesta
		double sumaPuntajeRespuestas = 0;
		double montoEquivalentePorPunto = 0;
		double  montoInversionPaquete = 0;
		List<ClientePaquete> paquetesABorrarInversionCero = new ArrayList<ClientePaquete>();
		//Calculo la suma total obtenida de las respuestas
		for (RespuestaEspecifica resp : respuestasEspecificas.values())
		{
			sumaPuntajeRespuestas += resp.puntaje;
		}
		
		//Calculo que monto del saldo equivale a cada punto
		//montoEquivalentePorPunto = this.saldoHistorico.get(this.ultimaFechaSaldo) / sumaPuntajeRespuestas;
		montoEquivalentePorPunto = this.ultimoSaldo / sumaPuntajeRespuestas;
		
		//Ajusto los ClientePaquete a esa inversion y ademas borro los asociados a respuesta con puntaje 0
		for (ClientePaquete cp : this.paquetes)
		{
			montoInversionPaquete = 0;
			
			for (PreguntaEspecifica pe : cp.getPaquete().getPreguntaEspecificas())
			{
				// deberia tener la respuesta a todas las preguntas,
				// pero por si alguna razon (datos de prueba incorrectos por ej.) no la tengo, la ignoro
				if (respuestasEspecificas.containsKey(pe.getId())){
					montoInversionPaquete += respuestasEspecificas.get(pe.getId()).puntaje * montoEquivalentePorPunto;
				}
			}
			
			if (montoInversionPaquete > 0){
				cp.asignarInversionHistorico(montoInversionPaquete,prov,this.ultimaFechaSaldo);
				sinInvertir = sinInvertir - montoInversionPaquete;
			}else
			{
				paquetesABorrarInversionCero.add(cp);
			}
		}
		
		//Actualizo y remuevo los paquetes con inversion 0
		this.paquetes.removeAll(paquetesABorrarInversionCero);
	}


	public Set<ClientePaquete> getPaquetes() {
		return paquetes;
	}

	public void setPaquetes(Set<ClientePaquete> paquetes) {
		this.paquetes = paquetes;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Calendar getUltimaFechaSaldo() {
		return ultimaFechaSaldo;
	}

	public void setUltimaFechaSaldo(Calendar ultimaFechaSaldo) {
		this.ultimaFechaSaldo = ultimaFechaSaldo;
	}

	/*public Collection<SaldoHistorico> getSaldoHistorico() {
		return saldoHistorico;
	}

	public void setSaldoHistorico(Collection<SaldoHistorico> saldoHistorico) {
		this.saldoHistorico = saldoHistorico;
	}*/

	public double getUltimoSaldo() {
		return ultimoSaldo;
	}

	public void setUltimoSaldo(double ultimoSaldo) {
		this.ultimoSaldo = ultimoSaldo;
	}
	
	public void agregarPaquete(Paquete paq,int porcentaje){
		double fraq = porcentaje / 100.0;
		ClientePaquete cp = new ClientePaquete(this,paq);
		this.paquetes.add(cp);
		this.sinInvertir += cp.asignarInversion(this.getUltimoSaldo() * fraq);
	}
	
	public void agregarPaquete(Paquete paq,int porcentaje,IProveedorValor val){
		double fraq = porcentaje / 100.0;
		ClientePaquete cp = new ClientePaquete(this,paq);
		this.paquetes.add(cp);
		this.sinInvertir += cp.asignarInversionHistorico(this.getUltimoSaldo() * fraq,val,this.getUltimaFechaSaldo());
	}
	
	public void ReseterPaquetes()
	{
		System.out.println("Reseteo preguntas idPort " + this.getId());
		//this.sinInvertir += this.ultimoSaldo;		
		this.sinInvertir = this.ultimoSaldo;		
		this.paquetes.clear();
	}
	
	public Set<Long> idAccionesEnPortafolio(){
		Set<Long> acciones = new HashSet<Long>();
		for (ClientePaquete cp : this.getPaquetes()){
			acciones.addAll(cp.getPaquete().getIdAccionesSaldo());
		}
		return acciones;
	}
	
	public boolean isHistorico() {
		return historico;
	}

	public void setHistorico(boolean historico) {
		this.historico = historico;
	}

	public Calendar getUltimoAcceso() {
		return ultimoAcceso;
	}

	public void setUltimoAcceso(Calendar ultimoAcceso) {
		this.ultimoAcceso = ultimoAcceso;
	}
	
	private boolean esMomentoActual(Calendar momento) {
		Calendar rightNow = Calendar.getInstance();
		boolean optimizo = Math.abs(rightNow.getTimeInMillis() - momento.getTimeInMillis()) > 60000;
		return optimizo;
	}

	public boolean isLost() {
		return lost;
	}

	public void setLost(boolean perdio) {
		this.lost = perdio;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	
	
	
}
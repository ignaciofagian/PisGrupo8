package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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


@Entity
@Table(name = "Portafolio")
public class Portafolio implements Serializable {

	private static final long serialVersionUID = 1L;

	//Hay un bug con @Id @OneToOne cliente https://hibernate.atlassian.net/browse/HHH-6813
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "IDPortafolio")
	private Long id;
	
	//Mapeo el id del portafolio en la tabla del cliente (usuario)
	@OneToOne(cascade = CascadeType.ALL, mappedBy="portafolio")
	//@JoinColumn(name = "IDUsuario")
	private Cliente cliente;

	//Me daba problemas al remover el objeto y luego en ciertas ocasiones cuando recreaba el mismo
	// "a different object with the same identifier value was already associated with the session"
	
	/*@JoinTable(joinColumns={@JoinColumn(name="IDPortafolio", referencedColumnName="IDPortafolio")}, 
	inverseJoinColumns={@JoinColumn(name="IDUsuario", referencedColumnName="IDUsuario"),
						@JoinColumn(name="IDPaq", referencedColumnName="IDPaq")})*/

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true,mappedBy="portafolio")
	private Set<ClientePaquete> paquetes = new HashSet<ClientePaquete>();

	@OneToMany(cascade=CascadeType.ALL) @JoinColumn(name="portafolio_idportafolio")
	private Collection<SaldoHistorico> saldoHistorico;
	
	private Calendar ultimaFechaSaldo = new GregorianCalendar(); // ultima fecha en que se calculo el
										// saldo para ver ajuste
	
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
		this.saldoHistorico = new ArrayList<SaldoHistorico>();
		this.paquetes = new HashSet<ClientePaquete>();
		this.cliente = c;
	}
	
	private void agregarSaldo(Calendar fecha,double valor){
		this.saldoHistorico.add(new SaldoHistorico(this, fecha, valor));
		this.ultimoSaldo = valor;
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
		this.saldoHistorico = new ArrayList<SaldoHistorico>();
		this.paquetes = new HashSet<ClientePaquete>();
		this.ultimaFechaSaldo= new GregorianCalendar();
		//this.cliente = null;
	}
	
	public double calcularSaldoBatch(Calendar fechaActual,IProveedorValor prov){
		long inicio = Calendar.getInstance().getTimeInMillis();
		int minutosIntervalo = 1440;
		Calendar fechaProxima = (Calendar)ultimaFechaSaldo.clone();
		fechaProxima.set(Calendar.SECOND, 0);
		fechaProxima.set(Calendar.MILLISECOND,0);
		fechaProxima.add(Calendar.MINUTE, minutosIntervalo);
		while (!fechaProxima.after(fechaActual)){
			calcularSaldo((Calendar)fechaProxima.clone(), prov);
			fechaProxima.add(Calendar.MINUTE, minutosIntervalo);
		}
		System.out.println("*** Tiempo: " + (Calendar.getInstance().getTimeInMillis() - inicio));
		return this.ultimoSaldo;
	}
	
	public double calcularSaldo(Calendar fechaActual, IProveedorValor prov) {
		double suma = 0;
		if (fechaActual.before(ultimaFechaSaldo)) {
			String error = String.format("FechaActual: %1$F %1$T < Ultima: %2$F %2$T", fechaActual, ultimaFechaSaldo);
			System.out.println("Advertencia: " + error);
		}

		for (ClientePaquete p : paquetes) {
			p.actualizarInversion(ultimaFechaSaldo, fechaActual, prov);
			suma += p.getInversion();
		}
		System.out.println("***** INVERSION EN PAQUETES = $" + suma + " sinInvertir=" + sinInvertir );
		suma += sinInvertir;
		agregarSaldo(fechaActual, suma);
		ultimaFechaSaldo = fechaActual;
		return suma;
	}
	
	//**Asumo que ultimaFechaSaldo esta seteada y ademas pertenece a saldoHistorico
	public void ajustarInversionPaquetesEnBaseRespEspecificas(HashMap<Long,RespuestaEspecifica> respuestasEspecificas)
	{
		//Le asigno estas respuesta al cliente
		
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
				cp.asignarInversion(montoInversionPaquete);
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

	public Collection<SaldoHistorico> getSaldoHistorico() {
		return saldoHistorico;
	}

	public void setSaldoHistorico(Collection<SaldoHistorico> saldoHistorico) {
		this.saldoHistorico = saldoHistorico;
	}

	public double getUltimoSaldo() {
		return ultimoSaldo;
	}

	public void setUltimoSaldo(double ultimoSaldo) {
		this.ultimoSaldo = ultimoSaldo;
	}
	
	public void agregarPaquete(Paquete paq){
		ClientePaquete nuevo = new ClientePaquete(this,paq);
	}
	
	public void ReseterPaquetes()
	{
		this.sinInvertir += this.ultimoSaldo;		
		this.paquetes.clear();
	}
	
}
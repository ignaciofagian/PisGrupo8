package ejb;

import java.util.ArrayList;

import javax.ejb.Local;

import datatypes.DTEstado;
import datatypes.DTPregunta;
import datatypes.DTPreguntaConRespuesta;
import model.ClientePaquete;
import model.PreguntaEspecifica;

@Local
public interface IEjbCliente {
	public String registrarUsuario(String identificador); 
	
	public DTEstado ajustarPaquetesEnBaseRespGenerales(String idCliente,String respuestasGenerales);
	
	public DTEstado ajustarInversionPaquetesEnBaseRespEspecificas(String idCliente,
			String respuestas);
	
	public ArrayList<DTPregunta> obtenerPreguntasEspecificas(String idCliente, String respuestas);
	
	public ArrayList<DTPreguntaConRespuesta> obtenerPreguntasRespondidasPorCliente(String idCliente);
	
	public ArrayList<ClientePaquete> obtenerClientesConPaqueteAlgoritmico(long idPaquete);
	
	public DTEstado resetearTodasLasPreguntas(String idCliente);
	
	public DTEstado borrarCliente(String idCliente);
}

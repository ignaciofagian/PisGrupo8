package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import datatypes.DTCategoria;
import datatypes.DTPaquete;
import datatypes.DTPaqueteAccion;
import datatypes.DTPaquetePregunta;
import datatypes.DTPaqueteRespuesta;
import ejb.IEjbPaquete;
import model.PaqueteCategoria;
import yahoofinance.YahooFinance;

/**
 * Servlet implementation class CreatePacket
 */
@WebServlet("/CreatePacket")
public class CreatePacket extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@EJB
	private IEjbPaquete ejbPaquete;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CreatePacket() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String queryType = request.getParameter("query").trim();
		if (queryType != null) {

			if (queryType.equals("categories")) {
				ArrayList<DTCategoria> dtCategorias = new ArrayList<DTCategoria>();
				ArrayList<PaqueteCategoria> categorias = ejbPaquete.obtenerCategoriasDePaquetes();

				for (PaqueteCategoria pc : categorias) {
					DTCategoria dtc = new DTCategoria(pc.getId(), pc.getCategoria());
					dtCategorias.add(dtc);

				}

				String json = new Gson().toJson(dtCategorias);
				response.setContentType("application/json");
				response.getWriter().write(json);
			}
		} else {
			response.setContentType("text/plain");
			response.getWriter().write("error en la invocacion");
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String json = "";
		
		if (br != null) {
			json = br.readLine();
		}
	
		br.close();
		
		DTPaquete dtPaq = new DTPaquete();
		List<DTCategoria> categoriasIds = new ArrayList<DTCategoria>();
		List<DTPaqueteAccion> accionesPaquete = new ArrayList<DTPaqueteAccion>();
		List<DTPaquetePregunta> preguntasPaquete = new ArrayList<DTPaquetePregunta>();
		
		JsonObject jsonObject = (new JsonParser()).parse(json).getAsJsonObject();

		JsonElement packetName = jsonObject.get("name");
		JsonElement packetType = jsonObject.get("type");
		JsonElement packetCategories = jsonObject.get("categories");
		JsonElement packetStocks = jsonObject.get("stocks");
		JsonElement packetAlgorithm = jsonObject.get("algorithm");
		JsonElement packetQuestions = jsonObject.get("questions");

		if (packetName != null)
			dtPaq.setNombre(packetName.getAsString());
		if (packetType != null)
			dtPaq.setTipo(packetType.getAsInt());
		if (packetAlgorithm != null)
			dtPaq.setAlgoritmo(packetAlgorithm.getAsString());
		
		if (packetCategories != null) {
			JsonArray jsonArray = packetCategories.getAsJsonArray();

			for (JsonElement je : jsonArray) {
				long categoryId = je.getAsLong();
				categoriasIds.add(new DTCategoria(categoryId, ""));
			}

			dtPaq.setCategorias(categoriasIds);
		}

		if (packetStocks != null) {
			
			JsonObject jsonObjectAux = packetStocks.getAsJsonObject();

			Set<Entry<String, JsonElement>> entrys = jsonObjectAux.entrySet();

			for (Entry<String, JsonElement> entryElem : entrys) {
				JsonObject jsonObjectStock = entryElem.getValue().getAsJsonObject();
				DTPaqueteAccion dtPaqueteAccion = new DTPaqueteAccion(jsonObjectStock.get("symbol").getAsString(),
						jsonObjectStock.get("name").getAsString(), jsonObjectStock.get("porcentage").getAsInt());

				accionesPaquete.add(dtPaqueteAccion);
			}
			dtPaq.setAcciones(accionesPaquete);
		}

		if (packetQuestions != null) {
			JsonObject jsonObjectAux = packetQuestions.getAsJsonObject();

			Set<Entry<String, JsonElement>> entrys = jsonObjectAux.entrySet();

			for (Entry<String, JsonElement> entryElem : entrys) {
				JsonObject jsonObjectQuestion = entryElem.getValue().getAsJsonObject();

				
				JsonObject jsonObjectAnswers = jsonObjectQuestion.get("answers").getAsJsonObject();

				Set<Entry<String, JsonElement>> entrysAnswers = jsonObjectAnswers.entrySet();
				
				List<DTPaqueteRespuesta> respuestas = new ArrayList<DTPaqueteRespuesta>();
				
				for (Entry<String, JsonElement> entryElemAnswer : entrysAnswers) {
					JsonObject jsonObjectAnswer = entryElemAnswer.getValue().getAsJsonObject();
					DTPaqueteRespuesta dtPaqueteResp = new DTPaqueteRespuesta(
							jsonObjectAnswer.get("answerEng").getAsString(),
							jsonObjectAnswer.get("answerEsp").getAsString(),
							jsonObjectAnswer.get("points").getAsInt());
														
					respuestas.add(dtPaqueteResp);
				}
				
				DTPaquetePregunta dtPaquetePreg = new DTPaquetePregunta(
						jsonObjectQuestion.get("questionTextEng").getAsString(), 
						jsonObjectQuestion.get("questionTextEsp").getAsString(), 
						respuestas);
				
				preguntasPaquete.add(dtPaquetePreg);
			}
			dtPaq.setPreguntas(preguntasPaquete);
		}
		
		ejbPaquete.crearPaquete(dtPaq);
		response.setContentType("application/json");
		response.getWriter().write("{ \"success\": true }");
	}

}

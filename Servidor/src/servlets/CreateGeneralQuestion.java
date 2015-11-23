package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import datatypes.DTPaqueteAccion;
import datatypes.DTPaqueteRespuesta;
import datatypes.DTPreguntaGeneral;
import datatypes.DTRespuestaGeneral;
import ejb.EjbServidor;
import ejb.IEjbPaquete;
import ejb.IEjbServidor;

/**
 * Servlet implementation class CreateGeneralQuestion
 */
@WebServlet("/CreateGeneralQuestion")
public class CreateGeneralQuestion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@EJB
	private IEjbServidor ejbServidor;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateGeneralQuestion() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("aca");
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String json = "";
		if (br != null) {
			json = br.readLine();
		}
		DTPreguntaGeneral dtPregunta = new DTPreguntaGeneral();
		List<DTRespuestaGeneral> respuestas = new ArrayList<DTRespuestaGeneral>();
		
		JsonObject jsonObject = (new JsonParser()).parse(json).getAsJsonObject();
		
		JsonElement questionTextEng = jsonObject.get("questionTextEng");
		JsonElement questionTextEsp = jsonObject.get("questionTextEsp");
		JsonElement questionAnswers = jsonObject.get("answers");
		
		if (questionTextEng != null)
			dtPregunta.setEng(questionTextEng.getAsString());
		
		if (questionTextEsp != null)
			dtPregunta.setEsp(questionTextEsp.getAsString());
		
		if (questionAnswers != null) {
			
			JsonObject jsonObjectAux = questionAnswers.getAsJsonObject();

			Set<Entry<String, JsonElement>> entrys = jsonObjectAux.entrySet();
			
			List<Long> categoriasIds = new ArrayList<Long>();
			
			for (Entry<String, JsonElement> entryElem : entrys) {
				JsonObject jsonObjectAnswer = entryElem.getValue().getAsJsonObject();
				DTRespuestaGeneral dtRespuesta = new DTRespuestaGeneral();
				dtRespuesta.setEng(jsonObjectAnswer.get("answerEng").getAsString());
				dtRespuesta.setEsp(jsonObjectAnswer.get("answerEsp").getAsString());
				
				JsonArray jsonArray = jsonObjectAnswer.get("categories").getAsJsonArray();

				for (JsonElement je : jsonArray) {
					long categoryId = je.getAsLong();
					categoriasIds.add(categoryId);
				}

				dtRespuesta.setCategorias(categoriasIds);

				respuestas.add(dtRespuesta);
			}
			
			dtPregunta.setRespuestas(respuestas);
			ejbServidor.agregarNuevaPreguntaGeneral(dtPregunta);
		}
		response.setContentType("application/json");
		response.getWriter().write("{ \"success\": true }");
	}

}

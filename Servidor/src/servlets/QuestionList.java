package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import datatypes.DTPaqueteIdNombre;
import datatypes.DTPregunta;
import datatypes.DTPreguntaGeneral;
import datatypes.DTRespuesta;
import datatypes.DTRespuestaGeneral;
import ejb.IEjbServidor;

/**
 * Servlet implementation class QuestionList
 */
@WebServlet("/QuestionList")
public class QuestionList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@EJB
	private IEjbServidor ejbServidor;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public QuestionList() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String queryType = request.getParameter("query");
		if (queryType != null) {
			if (queryType.equals("questionDelete")) {
				String idPregunta = request.getParameter("id").trim();
				ejbServidor.borrarPreguntaGeneral(Long.parseLong(idPregunta));
				response.setContentType("application/json");
				response.getWriter().write("{ \"success\": true }");
			}

		} else {
			ArrayList<DTPreguntaGeneral> lista = ejbServidor.obtenerPreguntasGeneralesWeb();
			ListIterator<DTPreguntaGeneral> it = lista.listIterator();
			if (!lista.isEmpty()) {
				int i = 0;
				while (it.hasNext()) {
					DTPreguntaGeneral dp = it.next();
					request.setAttribute("questionid" + i, dp.getId());
					request.setAttribute("questionesp" + i, dp.getEsp());
					request.setAttribute("questioneng" + i, dp.getEng());
					List<DTRespuestaGeneral> dr = dp.getRespuestas();
					int j = 0;
					ListIterator<DTRespuestaGeneral> itr = dr.listIterator();
					while (itr.hasNext()) {
						DTRespuestaGeneral answer = itr.next();
						request.setAttribute("question" + i + "answer" + j,
								"ENG: " + answer.getEng() + "<br> ESP: " + answer.getEsp());
						request.setAttribute("question" + i + "answerCategories" + j, answer.getCategoriasTexto());
						j++;
					}
					request.setAttribute("question" + i + "answers", j);
					i++;
				}
			}
			request.setAttribute("questionlength", lista.size());
			// response.getWriter().append("Served at:
			// ").append(request.getContextPath());
			RequestDispatcher dispatcher = request.getRequestDispatcher("/questionList.jsp");
			dispatcher.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}

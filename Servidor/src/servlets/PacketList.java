package servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import datatypes.DTPaquete;
import datatypes.DTPaqueteIdNombre;
import ejb.IEjbPaquete;

/**
 * Servlet implementation class PacketList
 */
@WebServlet("/PacketList")
public class PacketList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@EJB
	private IEjbPaquete ejbPaquete;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PacketList() {
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

			if (queryType.equals("packetList")) {
				ArrayList<DTPaqueteIdNombre> listaPaquetes = ejbPaquete.obtenerPaquetesConIdNombre();

				String json = new Gson().toJson(listaPaquetes);
				response.setContentType("application/json");
				response.getWriter().write(json);
			} else if (queryType.equals("packetDetail")) {
				String idPaquete = request.getParameter("id").trim();
				DTPaquete paquete = ejbPaquete.obtenerPaquete(Long.parseLong(idPaquete));

				String json = new Gson().toJson(paquete);
				response.setContentType("application/json");
				response.getWriter().write(json);
			} else if (queryType.equals("packetView")) {
				String packetId = request.getParameter("id").trim();
				request.setAttribute("id", packetId);
				RequestDispatcher dispatcher = request.getRequestDispatcher("/packetList.jsp");
				dispatcher.forward(request, response);
			}
			else if (queryType.equals("packetDelete")) {
				String idPaquete = request.getParameter("id").trim();
			
				ejbPaquete.borrarPaquete(Long.parseLong(idPaquete));
				
				response.setContentType("application/json");
				response.getWriter().write("{ \"success\": true }");
			}
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

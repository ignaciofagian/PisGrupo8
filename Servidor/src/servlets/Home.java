package servlets;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import datatypes.DTPaqueteDetalle;
import ejb.IEjbPaquete;
import ejb.IEjbServidor;

/**
 * Servlet implementation class Home
 */
@WebServlet("/Home")
public class Home extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    
	@EJB
	private IEjbPaquete ejbPaquete;
	
	@EJB
	private IEjbServidor ejbServidor;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Home() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String queryType = request.getParameter("query").trim();
		if (queryType != null) {
			if (queryType.equals("packetDetailList")) {
				List<DTPaqueteDetalle> listaPaqueteDetalles = ejbPaquete.obtenerPaquetesConDetalle();
				
				String json = new Gson().toJson(listaPaqueteDetalles);
				response.setContentType("application/json");
				response.getWriter().write(json);
			}else if (queryType.equals("counters"))
			{
				String jsonCounters = ejbServidor.obtenerCantidadUsuariosPaquetesAcciones();
				response.setContentType("application/json");
				response.getWriter().write(jsonCounters);
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

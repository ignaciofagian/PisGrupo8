package servlets;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ejb.IEjbAdmin;

/**
 * Servlet implementation class Register
 */
@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	@EJB
	private IEjbAdmin ejbAdmin;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String queryType = request.getParameter("query").trim();
		if (queryType != null) {
			
			if (queryType.equals("existsUser")) {
				String username = request.getParameter("username").trim();
				boolean exists = ejbAdmin.existeUsuario(username);
				response.setContentType("text/html;charset=UTF-8");
				
				String returnMsg = (exists ? "1" : "0");
			    response.getWriter().write(returnMsg);
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String username = request.getParameter("username");
		String password = request.getParameter("Pwd");
		String pageForward = "/register.jsp";
		String Error = "";
		
		if(ejbAdmin.existeUsuario(username)){ // si ya existe el usuario
			//devuelvo a register con mens de error
			Error = "That username already exists";
			request.setAttribute("Error", Error);
		}
		else{
			//lo agrego y lo mando al login
			//pageForward = "/login.jsp";
			ejbAdmin.CrearAdmin(username, password);
		}
		
		//Mostrar el error (Sea vacio o no)
		RequestDispatcher dispatcher = request.getRequestDispatcher(pageForward);
		dispatcher.forward(request, response);
	}

}

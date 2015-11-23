package servlets;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ejb.IEjbAdmin;


/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@EJB
	private IEjbAdmin ejbAdmin;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		  String paramUsername = request.getParameter("username");
		  String paramPassword = request.getParameter("password");
		  
		  String pageForward = "/login.jsp"; 
		  String errorText = "";
		  int errorNumber= 0;
			  
		  if (!ejbAdmin.existeUsuario(paramUsername))
		  {
			  errorText = "Error: This username not exists.";
			  errorNumber = 1;
			  
		  }else if (!ejbAdmin.esValidaPassword(paramUsername, paramPassword))
		  {
			  errorText = "Error: Invalid password.";
			  errorNumber = 2;
		  }else
		  {
			  // Todo ok, login satisfactorio
			  HttpSession session = request.getSession(true);
			  session.setAttribute("username",paramUsername);
			  request.setAttribute("pageName", "index");
			  pageForward = "/index.jsp";
		  }
		
		  if (errorNumber != 0)
		  {
			  request.setAttribute("username", paramUsername);
			  request.setAttribute("errorNumber", errorNumber);
			  request.setAttribute("errorText", errorText);
		  }
		  
		  RequestDispatcher dispatcher = request.getRequestDispatcher(pageForward);
		  dispatcher.forward(request, response);
	}
}

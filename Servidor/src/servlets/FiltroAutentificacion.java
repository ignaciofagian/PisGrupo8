package servlets;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class FiltroAutentificacion
 */
@WebFilter("/FiltroAutentificacion")
public class FiltroAutentificacion implements Filter {

    /**
     * Default constructor. 
     */
    public FiltroAutentificacion() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

			HttpServletRequest req = (HttpServletRequest) request;
	        HttpSession session = req.getSession(false);;
	        String loginUsername = null;
	       
	        if (session != null) {
	        	loginUsername = (String) session.getAttribute("username");
	        }
	        
	        boolean isLoggedIn = (loginUsername != null);
	        // Check if the user is accessing login page
	        if (req.getRequestURI().equals(req.getContextPath() + "/login.jsp")) {
	            if (isLoggedIn) {
	                // Redirect to landing or home page
	               
	                request.setAttribute("pageName", "index");
	                RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
	      		  	dispatcher.forward(request, response);
	              
	            } else {
	                // Otherwise, nothing to do if he has not logged in
	                // pass the request along the filter chain
	                chain.doFilter(request, response);
	            }
	        } else {
	            // For all other pages,
	            if (isLoggedIn) {
	                // Nothing to do
	                chain.doFilter(request, response);
	            } else {
	            	 if (!req.getRequestURI().equals(req.getContextPath() + "/register.jsp")) {
	 	                HttpServletResponse res = (HttpServletResponse) response;
	 	                res.sendRedirect(req.getContextPath() + "/login.jsp");
	 	            }
	 	            else{
	 	            	chain.doFilter(request, response);
	 	            }
	            }
	        }
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}

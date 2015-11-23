package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import interprete.IInterpreter;
import interprete.InterpreterController;

/**
 * Servlet implementation class TestAlgorithm
 */
@WebServlet("/TestAlgorithm")
public class TestAlgorithm extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestAlgorithm() {
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
		// TODO Auto-generated method stub
		
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String json = "";
		
		if (br != null) {
			json = br.readLine();
		}
	
		br.close();
		
		JsonObject jsonObject = (new JsonParser()).parse(json).getAsJsonObject();
		JsonElement packetAlgorithm = jsonObject.get("algorithm");
		
		String message = "";
		String error = "";
		
		String algorithm = packetAlgorithm.getAsString();
		IInterpreter interpreter = new InterpreterController();
		try{
			interpreter.check(algorithm);
		}
		catch(Exception e){
			error = e.getMessage();
		}
		
        response.setContentType("text/html");
        // Actual logic goes here.
        PrintWriter out = response.getWriter();
       
        JsonObject myObj = new JsonObject();

        myObj.addProperty("success", true);
        myObj.addProperty("error", error);
        out.println(myObj.toString());
        out.close();
	}

}

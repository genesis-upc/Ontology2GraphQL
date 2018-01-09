package serverGraphQL;
import java.io.*;
import java.util.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/getEsquema"})
public class esquemaCtrl extends HttpServlet{
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		esquema es = new esquema();
		String result = es.getEsquema(getServletContext().getRealPath("WEB-INF/classes/esquema.graphqls"));
		try{
			request.setAttribute("esquema", result);
			request.getRequestDispatcher("/esquema.jsp").forward(request, response);
		}catch(Exception e){
			
		}	
	}
}



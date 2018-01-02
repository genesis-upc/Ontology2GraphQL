package serverGraphQL;
import java.io.*;
import java.util.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/getApi"})
public class apiCtrl extends HttpServlet{
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		api ap = new api();
		String result = ap.getApi(getServletContext().getRealPath("WEB-INF/classes/esquema.graphqls"));
		try{
		request.setAttribute("api", result);
		request.getRequestDispatcher("/api.jsp").forward(request, response);
		}catch(Exception e){
			
		}
		
	}

}

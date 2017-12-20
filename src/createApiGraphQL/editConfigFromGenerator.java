package createApiGraphQL;

import java.io.File;
import java.io.FileInputStream;

import virtuoso.jena.driver.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import virtuoso.jena.driver.VirtGraph;

@WebServlet(urlPatterns = {"/EditConfigFromGenerator"})
public class editConfigFromGenerator extends HttpServlet{
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		    
		    try{
		    	String url = request.getParameter("Url_Virtuoso");
		    	String user = request.getParameter("Usuari");
		    	String pass = request.getParameter("Password");
		    	VirtGraph graph = new VirtGraph (url, user, pass);
		    	if(graph != null){
				    InputStream input = new FileInputStream(new File(getServletContext().getRealPath("config.properties")));
				    Properties prop = new Properties();
					Properties propFinal = new Properties();
					prop.load(input);
					
					
				    propFinal.setProperty("url_hostlist", request.getParameter("Url_Virtuoso"));
				    propFinal.setProperty("user", request.getParameter("Usuari"));
				    propFinal.setProperty("password", request.getParameter("Password"));
				    propFinal.setProperty("dbName", request.getParameter("DbName"));
				    propFinal.setProperty("serverName", prop.getProperty("serverName"));
				    
				    String parentFile = new File(getServletContext().getRealPath("")).getParentFile().getPath();
					String path =parentFile + "/" + prop.getProperty("serverName") +"/config.properties";

				    FileWriter writerr = new FileWriter(new File(path));
				    propFinal.store(writerr, "host settings");
				    writerr.close();
				    
				    input.close();
				    request.setAttribute("tiempo", prop.getProperty("serverName"));
				    request.getRequestDispatcher("/opcions.jsp").forward(request, response);
		    	}
			  
		    } catch (Exception ex){
		      request.setAttribute("error", "fromEdit");
		      request.getRequestDispatcher("/error.jsp").forward(request, response);
		    }
		    

			
		    
		   
	}
}

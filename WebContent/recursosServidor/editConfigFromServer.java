package serverGraphQL;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;

import virtuoso.jena.driver.VirtGraph;

@WebServlet(urlPatterns = {"/EditConfigFromServer"})
public class editConfigFromServer extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String url = request.getParameter("Url_Virtuoso");
    	String user = request.getParameter("Usuari");
    	String pass = request.getParameter("Password");
    	String dbName = request.getParameter("DbName");
    	

	       if(!url.isEmpty() && !user.isEmpty() && !pass.isEmpty() && !dbName.isEmpty()){
		    
		    try{
	
		    	VirtGraph graph = new VirtGraph (url, user, pass);
		    	if(graph != null){
	
				    InputStream input = new FileInputStream(new File(getServletContext().getRealPath("config.properties")));
				    Properties prop = new Properties();
					Properties propFinal = new Properties();
					prop.load(input);
					
					String serverName = getServletContext().getRealPath(".");
					Integer index = serverName.lastIndexOf("\\");
					serverName = serverName.substring(index + 1);
					
				    propFinal.setProperty("url_hostlist", request.getParameter("Url_Virtuoso"));
				    propFinal.setProperty("user", request.getParameter("Usuari"));
				    propFinal.setProperty("password", request.getParameter("Password"));
				    propFinal.setProperty("dbName", request.getParameter("DbName"));
				    propFinal.setProperty("serverName", serverName);
				    
				    String parentFile = new File(getServletContext().getRealPath("")).getParentFile().getPath();
					String path =parentFile + "/" + serverName +"/config.properties";
					
	
				    FileWriter writerr = new FileWriter(new File(path));
				    propFinal.store(writerr, "host settings");
				    writerr.close();
				    
				    input.close();
	
				    request.getRequestDispatcher("/index.jsp").forward(request, response);
		    	}
			  
		    } catch (Exception ex){
		    
		      request.setAttribute("error", "virtuoso");
		      request.getRequestDispatcher("/form.jsp").forward(request, response);
	
		    }
		    
	       }else{
			      request.setAttribute("error", "campos");
			      request.getRequestDispatcher("/form.jsp").forward(request, response);
	       }	   
	    }
	
}

package serverGraphQL;

import java.io.*;
import java.util.*;
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
		    		String rootProp = getServletContext().getRealPath("config.properties");
		    		String serverName = getServletContext().getRealPath(".");
		    		String parentFile = new File(getServletContext().getRealPath("")).getParentFile().getPath();
		    		
		    		config conf = new config(parentFile, url, user, pass, dbName, serverName);
		    		Properties prop = conf.saveConfig();
		    		
		    		if(prop.getProperty("url_hostlist") != null){System.out.println("entro 1"); request.setAttribute("actualUrl", prop.getProperty("url_hostlist"));}
					if(prop.getProperty("user") != null) {request.setAttribute("actualUser", prop.getProperty("user"));}
					if(prop.getProperty("password") != null) {request.setAttribute("actualPassword", prop.getProperty("password"));}
					if(prop.getProperty("dbName") != null){request.setAttribute("actualDbName", prop.getProperty("dbName")); }
		    		
		    		request.setAttribute("error", "correct");
				    request.getRequestDispatcher("/form.jsp").forward(request, response);
		    	}
			  
		    } catch (Exception ex){
				String root = getServletContext().getRealPath("config.properties");
				config conf = new config(root);
				Properties prop = conf.loadConfig();
				
		
				if(prop.getProperty("url_hostlist") != null){request.setAttribute("actualUrl", prop.getProperty("url_hostlist"));}
				if(prop.getProperty("user") != null) {request.setAttribute("actualUser", prop.getProperty("user"));}
				if(prop.getProperty("password") != null) {request.setAttribute("actualPassword", prop.getProperty("password"));}
				if(prop.getProperty("dbName") != null){request.setAttribute("actualDbName", prop.getProperty("dbName")); }
		        request.setAttribute("error", "virtuoso");
		        request.getRequestDispatcher("/form.jsp").forward(request, response);
	
		    }
		    
	       }else{
				String root = getServletContext().getRealPath("config.properties");
				config conf = new config(root);
				Properties prop = conf.loadConfig();
				
		
				if(prop.getProperty("url_hostlist") != null){request.setAttribute("actualUrl", prop.getProperty("url_hostlist"));}
				if(prop.getProperty("user") != null) {request.setAttribute("actualUser", prop.getProperty("user"));}
				if(prop.getProperty("password") != null) {request.setAttribute("actualPassword", prop.getProperty("password"));}
				if(prop.getProperty("dbName") != null){request.setAttribute("actualDbName", prop.getProperty("dbName")); }

			    request.setAttribute("error", "campos");
			    request.getRequestDispatcher("/form.jsp").forward(request, response);
	       }	   
	    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		try{
			String root = getServletContext().getRealPath("config.properties");
			config conf = new config(root);
			Properties prop = conf.loadConfig();
			
	
			if(prop.getProperty("url_hostlist") != null){request.setAttribute("actualUrl", prop.getProperty("url_hostlist"));}
			if(prop.getProperty("user") != null) {request.setAttribute("actualUser", prop.getProperty("user"));}
			if(prop.getProperty("password") != null) {request.setAttribute("actualPassword", prop.getProperty("password"));}
			if(prop.getProperty("dbName") != null){request.setAttribute("actualDbName", prop.getProperty("dbName")); }
			request.setAttribute("error", "no error");
			request.getRequestDispatcher("/form.jsp").forward(request, response);
			
		}catch(Exception e){
			
		}
		
		
	}
	

	
}

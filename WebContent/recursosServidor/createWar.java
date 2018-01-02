package serverGraphQL;

import java.io.*;
import java.util.*;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/createWar"})
public class createWar extends HttpServlet{
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		
		String tiempo = new File(getServletContext().getRealPath(".")).getName();
		String rootPath = getServletContext().getRealPath(".");
		String nameFile = getServletContext().getRealPath(tiempo + ".war");
		
		try {
	    String command = "cmd /c start /wait jar -cvf " + tiempo + ".war *";

		
			if(new File(nameFile).exists() == false){
				Process proc =Runtime.getRuntime().exec(command,null, new File(getServletContext().getRealPath(".")));
				proc.waitFor();
			}


		
		  response.setContentType("APPLICATION/OCTET-STREAM");   
		  response.setHeader("Content-Disposition","attachment; filename=\"" + tiempo + ".war\"");   
		  

		  File file = new File(nameFile);
		  FileInputStream inStream = new FileInputStream(file);

	      OutputStream outStream = response.getOutputStream();
	      
	      byte[] buffer = new byte[4096];
	       
	    
	      while (inStream.read(buffer,0,4096) != -1) {
	          outStream.write(buffer, 0, 4096);
	                
	      }
	       
	      inStream.close();
		  outStream.flush();
	      outStream.close();
	      
	      request.getRequestDispatcher("/index.jsp").forward(request, response);
		
	} catch (Exception e) {
		System.out.println(e.getMessage());
	}
	}
}

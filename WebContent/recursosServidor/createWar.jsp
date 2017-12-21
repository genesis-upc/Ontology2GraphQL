<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.io.*" import="java.util.*" import="java.nio.file.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

</head>
<body>
<%
	String tiempo = new File(getServletContext().getRealPath(".")).getName();
    String command = "cmd /c start /wait jar -cvf " + tiempo + ".war *";


	if(new File(getServletContext().getRealPath(tiempo + ".war")).exists() == false){
		Process proc =Runtime.getRuntime().exec(command,null, new File(getServletContext().getRealPath(".")));
		proc.waitFor();
	}

	
	  response.setContentType("APPLICATION/OCTET-STREAM");   
	  response.setHeader("Content-Disposition","attachment; filename=\"" + tiempo + ".war\"");   
	  

	  File file = new File(getServletContext().getRealPath(tiempo + ".war"));
	  FileInputStream inStream = new FileInputStream(file);

      OutputStream outStream = response.getOutputStream();
      
      byte[] buffer = new byte[4096];
       
    
      while (inStream.read(buffer,0,4096) != -1) {
          outStream.write(buffer, 0, 4096);
                
      }
       
      inStream.close();
	  outStream.flush();
      outStream.close();   
	%>
</body>
</html>
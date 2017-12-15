<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.io.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%
String file = getServletContext().getRealPath("WEB-INF/classes/esquema.graphqls");

FileInputStream fis= new FileInputStream(file);
BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

	String line = reader.readLine();
	while(line != null){
		if(line.contains("{") || line.contains("}")) out.println(line + "<br>");
		else out.println("&nbsp;&nbsp;&nbsp;&nbsp;" + line + "<br>");
	    line = reader.readLine();
	}           

fis.close();
out.println("<button type=\"button\" onclick=\"history.back()\"> Enrrere </button>");
%>

</head>
<body>

</body>
</html>
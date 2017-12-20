<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.io.*" import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Generació semi-automàtica d'APIs GraphQL</title>
<link href = "bootstrap.min.css" type = "text/css" rel= "stylesheet">
</head>
<body> <br><br><br><br>
<% 				    

InputStream input = new FileInputStream(new File(getServletContext().getRealPath("config.properties")));
Properties prop = new Properties();
prop.load(input);

String serverName =  prop.getProperty("serverName");

%>
	<center><h1>Funcionalitats disponibles</h1></center>
    <br><br>
	<table align= "center">
			    <tr>
					<td> <a href= "api.jsp"  class="btn btn-outline-primary"> Veure esquema GraphQL  </a></td>
			   </tr>
			   <tr>
					<td> <a href= " ./../<%=serverName%>/index.html"  class="btn btn-outline-primary"> Executar servidor GraphQL </a></td>
			   </tr>
			   <tr>
					<td> <a href= " ./../<%=serverName%>/createWar.jsp"  class="btn btn-outline-primary"> Descargar .WAR del servidor GraphQL </a></td>
			   </tr>
			   <tr>
					<td> <a href= " form.jsp?editar=true"  class="btn btn-outline-primary"> Editar paràmetres de connexió al virtuoso </a></td>
			   </tr>
	</table>

</body>
</html>
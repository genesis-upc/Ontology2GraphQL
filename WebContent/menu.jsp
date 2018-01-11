<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.io.*" import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Generació semi-automàtica d'APIs GraphQL</title>
<link href = "bootstrap.min.css" type = "text/css" rel= "stylesheet">
<style>
.grid-container {
  display: grid;
  grid-template-columns: auto auto;
  padding: 10px;
}
.grid-item {
  padding: 20px;
  font-size: 30px;
  text-align: center;
}
</style>
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
    
    <div class="grid-container">
    
	  <div class="grid-item"><a href= "./../<%=serverName%>/getEsquema"  class="btn btn-outline-primary btn-lg"> Veure esquema GraphQL  </a></div>
	  <div class="grid-item"><a href= " ./../<%=serverName%>/servidor.html"  class="btn btn-outline-primary btn-lg"> Fer peticions a l'API GraphQL </a></div>
	  <div class="grid-item"><a href= " ./../<%=serverName%>/createWar"  class="btn btn-outline-primary btn-lg"> Descarregar l'aplicació web de l'API GraphQL </a></div>
	  <div class="grid-item"><a href= "./../<%=serverName%>/EditConfigFromServer"  class="btn btn-outline-primary btn-lg"> Editar paràmetres de connexió a la base de dades </a></div>
	
	</div>

</body>
</html>
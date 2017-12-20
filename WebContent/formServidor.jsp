<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.io.*" import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Generaci� semi-autom�tica d'APIs GraphQL</title>
<link href = "bootstrap.min.css" type = "text/css" rel= "stylesheet">
</head>
<% 
InputStream input = new FileInputStream(new File(getServletContext().getRealPath("config.properties")));
Properties prop = new Properties();
prop.load(input);
input.close();

String actualUrl = "";
if(prop.getProperty("url_hostlist") != null) actualUrl = prop.getProperty("url_hostlist");
String actualUser = "";
if(prop.getProperty("user") != null) actualUser = prop.getProperty("user");
String actualPassword = "";
if(prop.getProperty("password") != null) actualPassword = prop.getProperty("password");
String actualDBName = "";
if(prop.getProperty("dbName") != null) actualDBName = prop.getProperty("dbName");

%>

<body>
	 	<form action="./EditConfigFromServer" method="post">
	 	    <center><h1>Connexi� al servidor Virtuoso</h1></center>
    		<br><br>
	    	<table align= "center">
	    		<tr>
		        	<th align="right"> </th>
		        	<th> <font face = "verdana"> Nova connexi� </font> </th>
		        	<th> <font face = "verdana"> Par�metres actuals </font> </th>
		        </tr>
		        <tr>
		        	<th align="right"> URL Virtuoso </th>
		        	<td> <input type="text" size="45"  name="Url_Virtuoso" class="form-control" placeholder = "jdbc:virtuoso://localhost:1111"/> </td>
		        </tr>
		        
		        <tr>
		        	<th align="right"> Usuari </th>
		        	<td> <input type="text" size="45"  name="Usuari" class="form-control" placeholder = "dba"/> </td>
		        </tr>
		        
		        <tr>
		        	<th align="right"> Contrasenya </th>
		        	<td> <input type="text" size="45"  name="Password" class="form-control" placeholder = "dba"/> </td>
		        </tr>
		        
		        <tr>
		        	<th align="right"> Nom de la base de dades </th>
		        	<td> <input type="text" size="45"  name="DbName" class="form-control" placeholder = "http://localhost:8890/NOM"/> </td>
		        </tr>
		        
		        <tr> 
		        	<td> <a href= "iniciServidor.jsp" class = "btn btn-primary"/> Cancelar </a> </td>
		        	<td colspan = "2" align ="right"> <input type="submit" value= "Editar par�metres connexi� virtuoso" class = "btn btn-primary"/> </td>
		        </tr>
		  
		        
		    </table>
	    </form>
</body>
</html>
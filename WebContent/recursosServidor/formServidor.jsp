<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.io.*" import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Generaci� semi-autom�tica d'APIs GraphQL</title>
<link href = "bootstrap.min.css" type = "text/css" rel= "stylesheet">
</head>

<%

	 String actualUrl= (String) request.getAttribute("actualUrl");
     String actualPassword = (String) request.getAttribute("actualPassword");
     String actualDBName = (String) request.getAttribute("actualDbName");
     String actualUser = (String) request.getAttribute("actualUser");
     String error = (String) request.getAttribute("error");

%>

<body> <br><br><br><br>
	 	<form action="./EditConfigFromServer" method="post">
	 	    <center><h1>Connexi� a la base de dades</h1></center>
    		<br><br>
	    	<table align= "center">
	    		<tr>
		        	<th align="right"> </th>
		        	<th> <font face = "verdana"> Nova connexi� </font> </th>
		        </tr>
		        <tr>
		        	<th align="right">Adre�a servidor </th>
		        	<td> <input type="text" size="45"  name="Url_Virtuoso" class="form-control" placeholder = <%= actualUrl%>> </td>
		        </tr>
		        
		        <tr>
		        	<th align="right"> Usuari </th>
		        	<td> <input type="text" size="45"  name="Usuari" class="form-control" placeholder = <%=actualUser %>> </td>
		        </tr>
		        
		        <tr>
		        	<th align="right"> Contrasenya </th>
		        	<td> <input type="text" size="45"  name="Password" class="form-control" placeholder = <%=actualPassword %>> </td>
		        </tr>
		        
		        <tr>
		        	<th align="right"> Base de dades </th>
		        	<td> <input type="text" size="45"  name="DbName" class="form-control" placeholder = <%=actualDBName %>> </td>
		        </tr>
		        
		        <tr> 
		        	
		        	<td> <a  href = "index.jsp"class = "btn btn-primary" align ="center" > Cancelar </a>  
		        	<td colspan = "2" align ="right"> <input type="submit" value= "Editar par�metres connexi� virtuoso" class = "btn btn-primary"/> </td>
		        </tr>
		        
		    </table>
	    </form>
	    
	    	   <% if( error.equals("virtuoso") ) { %>
			        <div class="alert alert-danger" align ="center">
					  Les dades de connexi� al servidor virtuoso no s�n v�lides introdueix-les de nou.
					</div> 
				<% } else if( error.equals("campos") ) { %>
					 <div class="alert alert-danger" align ="center" >
					  Omple tots els camps <br>
					</div>	
				<% } else if( error.equals("correct") ) { %>
					 <div class="alert alert-success" align ="center" >
					  Editat correctament <br>
					</div>	
				<% } %>
</body>
</html>
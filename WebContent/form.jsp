<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<% 
String edit = "false";
if(request.getParameter("editar") != null && request.getParameter("editar").toString().equals("true")) edit = "true";
String value = "";
%>
	<% if (edit.equals("false")) { %>
		<% value = "Crear Api GraphQL i Servidor";%>
	    <form action="./Main" method="post">
	<% } else if( edit.equals("true")) {  %>
		<% value = "Editar paràmetres connexió virtuoso";%>
	 	<form action="./EditConfigFromGenerator" method="post">
    <% } %>
	        Url_Virtuoso  <input type="text" size="45"  name="Url_Virtuoso"/>  <br> <br>
	        
	        Usuari <input type="text"  size="45" name="Usuari"/>  <br> <br>
	        
	        Password <input type="text" size="45" name="Password"/> <br> <br>
	
	        DbName  <input type="text"  size="45" name="DbName"/> <br> <br>
	  
	        <input type="submit" value= "<%= value %> "/>
	    </form>
</body>
</html>
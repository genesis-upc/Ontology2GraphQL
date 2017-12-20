<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Error</title>
</head>
<body>
	<% String error = (String)request.getAttribute("error");%>
	<% if( error.equals("virtuoso") ) { %>
	    Les dades de connexió al servidor virtuoso no són vàlides introdueix-les de nou. <br>
		<a href= "form.jsp"> Enrrere </a>
	<% } else if( error.equals("campos") ) { %>
		Omple tots els camps <br>
	    <a href= "form.jsp"> Enrrere </a>
	<% } else if( error.equals("fromEdit") ) { %>
	    Les dades de connexió al servidor virtuoso no són vàlides introdueix-les de nou. <br>
		<a href= "opcions.jsp"> Enrrere </a>
	<% } %>
	
</body>
</html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.io.*" import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href = "bootstrap.min.css" type = "text/css" rel= "stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style>
#btndisclaimer {
  position: fixed;
  right: 0;
  bottom: 0;
}
</style>
</head>
<body>
    		<center><h1>Esquema GraphQL<h1></center>
    		<br><br>
<a href="index.jsp" class = "btn btn-primary" align ="center" id="btndisclaimer"> Enrrere </a>
<table align = "center">
<% String api= (String) request.getAttribute("esquema"); %>
<%= api%>
</table>
</body>
</html>
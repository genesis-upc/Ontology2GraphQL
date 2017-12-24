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
String edit = "false";
if(request.getParameter("editar") != null && request.getParameter("editar").toString().equals("true")) edit = "true";
String value = "";

String actualUrl = "";
String actualUser = "";
String actualPassword = "";
String actualDBName = "";
String error = "";
if(request.getAttribute("error")!= null) error = (String)request.getAttribute("error");
if(edit.equals("true")){
	InputStream input = new FileInputStream(new File(getServletContext().getRealPath("config.properties")));
	Properties prop = new Properties();
	prop.load(input);
	input.close();
	
	String parentFile = new File(getServletContext().getRealPath("")).getParentFile().getPath();
	String path =parentFile + "/" + prop.getProperty("serverName") +"/config.properties";
	
	InputStream input2 = new FileInputStream(new File(path));
	Properties prop2 = new Properties();
	prop2.load(input2);
	input2.close();
	
	
	if(prop2.getProperty("url_hostlist") != null) actualUrl = prop2.getProperty("url_hostlist");
	
	if(prop2.getProperty("user") != null) actualUser = prop2.getProperty("user");
	
	if(prop2.getProperty("password") != null) actualPassword = prop2.getProperty("password");
	
	if(prop2.getProperty("dbName") != null) actualDBName = prop2.getProperty("dbName");
}


%>
	<% if (edit.equals("false")) { %>
		<% value = "Crear servidor GaphQL per l'ontologia donada";%>
	    <form action="./Main" method="post" >
	<% } else if( edit.equals("true")) {  %>
		<% value = "Editar paràmetres connexió virtuoso";%>
	 	<form action="./EditConfigFromGenerator" method="post">
    <% } %>
    		<center><h1>Connexió al servidor Virtuoso</h1></center>
    		<br><br>
	    	<table align= "center">
	    		<% if (edit.equals("true")){ %>
	    		<tr>
		        	<th align="right"> </th>
		        	<th> <font face = "verdana"> Nova connexió </font> </th>
		        </tr>
		        <% }%>
		        <tr>
		        	<th align="right"> URL Virtuoso </th>
		        	
		        	<% if (edit.equals("true")) {%><td> <input type="text" size="45"  name="Url_Virtuoso" class="form-control" placeholder = <%=actualUrl %>> </td> 
		        	<% } else {%><td> <input type="text" size="45"  name="Url_Virtuoso" class="form-control" placeholder = "jdbc:virtuoso://localhost:1111"> </td><%} %>
		        </tr>
		        
		        <tr>
		        	<th align="right"> Usuari </th>
		        	<% if (edit.equals("true")) {%><td> <input type="text" size="45"  name="Usuari" class="form-control" placeholder = <%=actualUser %>> </td> 
		        	<% } else {%><td> <input type="text" size="45"  name="Usuari" class="form-control" placeholder = "dba"> </td><%} %>
		        </tr>
		        
		        <tr>
		        	<th align="right"> Contrasenya </th>
		        	
		        	<% if (edit.equals("true")) {%><td> <input type="text" size="45"  name="Password" class="form-control" placeholder =  <%=actualPassword %>> </td> 
		        	<% } else {%><td> <input type="text" size="45"  name="Password" class="form-control" placeholder = "dba"> </td><%} %>
		        </tr>
		        
		        <tr>
		        	<th align="right"> Nom de la base de dades </th>
		        	
		        	<% if (edit.equals("true")) {%><td> <input type="text" size="45"  name="DbName"class="form-control" placeholder = <%=actualDBName %>> </td> 
		        	<% } else {%><td> <input type="text" size="45"  name="DbName" class="form-control" placeholder = "http://localhost:8890/NOM"> </td><%} %>
		        	
		        </tr>
		        
		        <tr> 
		            <% if (edit.equals("true")) {%><td> <a href= "menu.jsp" class = "btn btn-primary"/> Cancelar </a> </td>  <% }%>
		        	<td colspan = "2" align ="right"> <input type="submit" value= "<%= value %> " class = "btn btn-primary"/> </td>
		        </tr>
		    </table>
	    </form>
	    		<% if( error.equals("virtuoso") ) { %>
			        <div class="alert alert-danger" align ="center">
					  Les dades de connexió al servidor virtuoso no són vàlides introdueix-les de nou.
					</div> 
				<% } else if( error.equals("campos") ) { %>
					 <div class="alert alert-danger" align ="center" >
					  Omple tots els camps <br>
					</div>	
				<% } %>

</body>
</html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.io.*" import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Generació semi-automàtica d'APIs GraphQL</title>
<link href = "bootstrap.min.css" type = "text/css" rel= "stylesheet">
</head>
<% 
InputStream input = new FileInputStream(new File(getServletContext().getRealPath("config.properties")));
Properties prop = new Properties();
prop.load(input);
input.close();

String appName =  prop.getProperty("appName");
appName = "/../" + appName + "/menu.jsp";

String principal = "";
if(request.getParameter("principal") != null && request.getParameter("principal").toString().equals("true")) principal = "true";

String error = "";
if(request.getAttribute("error")!= null) error = (String)request.getAttribute("error");
String actualUrl = "";
if(prop.getProperty("url_hostlist") != null) actualUrl = prop.getProperty("url_hostlist");
String actualUser = "";
if(prop.getProperty("user") != null) actualUser = prop.getProperty("user");
String actualPassword = "";
if(prop.getProperty("password") != null) actualPassword = prop.getProperty("password");
String actualDBName = "";
if(prop.getProperty("dbName") != null) actualDBName = prop.getProperty("dbName");



%>
<script type="text/javascript">
  function returnApp()
  {
	
  }
</script>

<body>
	 	<form action="./EditConfigFromServer" method="post">
	 	    <center><h1>Connexió al servidor Virtuoso</h1></center>
    		<br><br>
	    	<table align= "center">
	    		<tr>
		        	<th align="right"> </th>
		        	<th> <font face = "verdana"> Nova connexió </font> </th>
		        </tr>
		        <tr>
		        	<th align="right"> URL Virtuoso </th>
		        	<td> <input type="text" size="45"  name="Url_Virtuoso" class="form-control" placeholder = <%=actualUrl %>> </td>
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
		        	<th align="right"> Nom de la base de dades </th>
		        	<td> <input type="text" size="45"  name="DbName" class="form-control" placeholder = <%=actualDBName %>> </td>
		        </tr>
		        
		        <tr> 
		        	
		        	<td> <a  href = "index.jsp"class = "btn btn-primary" align ="center" > Cancelar </a>  
		        	<td colspan = "2" align ="right"> <input type="submit" value= "Editar paràmetres connexió virtuoso" class = "btn btn-primary"/> </td>
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
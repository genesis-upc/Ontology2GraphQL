<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.io.*" import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<% 				    

InputStream input = new FileInputStream(new File(getServletContext().getRealPath("config.properties")));
Properties prop = new Properties();
prop.load(input);

String serverName =  prop.getProperty("serverName");

%>

<a href= "api.jsp"> Api GraphQL </a> <br> <br>
<a href= " ./../<%=serverName%>/index.html"> Servidor GraphQL </a> <br> <br>
<a href= " ./../<%=serverName%>/createWar.jsp"> .WAR del servidor GraphQL </a> <br> <br>
<a href= " form.jsp?editar=true"> Editar paràmetres de connexió al virtuoso </a>
</body>
</html>
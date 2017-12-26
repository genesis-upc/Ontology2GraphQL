<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.io.*" import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href = "bootstrap.min.css" type = "text/css" rel= "stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
<table align = "center">
<%
InputStream input = new FileInputStream(new File(getServletContext().getRealPath("config.properties")));
Properties prop = new Properties();
prop.load(input);

String appURL =  prop.getProperty("appName");
appURL ="./../" + appURL + "/menu.jsp";

String principal = "";
if(request.getParameter("principal") != null && request.getParameter("principal").toString().equals("true")) principal = "true";

String file = getServletContext().getRealPath("WEB-INF/classes/esquema.graphqls");

FileInputStream fis= new FileInputStream(file);
BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

	String line = reader.readLine();
	while(line != null){
		 out.println("<tr> <td align =\"left\">");
		if(line.contains("{") ) out.println("<font face=\"verdana\">" +line + "</font> <br>  " );//out.println(line + "<br>");
		else if(line.contains("}"))out.println("<font face=\"verdana\">" + line + "</font><br> " );
		else out.println(" &nbsp;&nbsp;&nbsp;&nbsp;" + "<font face=\"verdana\">" + line + "</font> <br>" );
	    line = reader.readLine();
	    out.println("</td> </tr> ");
	}           

fis.close();
%>
<tr> 
<td> <a href="index.jsp" class = "btn btn-primary" align ="center"> Enrrere </a> </td> </tr>


</table>
</body>
</html>
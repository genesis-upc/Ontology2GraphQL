package serverGraphQL;
import java.io.*;
import java.util.*;
public class api {

	private String contenido;
	
	public api(){
		
	}
	
	public String getApi(String url){
		String file = url;
		String result = "";
		try{

		FileInputStream fis= new FileInputStream(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

			String line = reader.readLine();
			while(line != null){
				
				result = result.concat("<tr> <td align =\"left\">");
				
				// out.println("<tr> <td align =\"left\">");
				if(line.contains("{") ) result = result.concat("<font face=\"verdana\">" +line + "</font> <br>  "); //out.println("<font face=\"verdana\">" +line + "</font> <br>  " );//out.println(line + "<br>");
				else if(line.contains("}"))result = result.concat("<font face=\"verdana\">" + line + "</font><br> "); //out.println("<font face=\"verdana\">" + line + "</font><br> " );
				else result = result.concat(" &nbsp;&nbsp;&nbsp;&nbsp;" + "<font face=\"verdana\">" + line + "</font> <br>" ); //out.println(" &nbsp;&nbsp;&nbsp;&nbsp;" + "<font face=\"verdana\">" + line + "</font> <br>" );
			    line = reader.readLine();
			    result = result.concat("</td> </tr> ");
			    //out.println("</td> </tr> ");
			}           

		fis.close();
		}catch (Exception e){
			
		}
		return result;
	}
}

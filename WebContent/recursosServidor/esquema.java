package serverGraphQL;
import java.io.*;
import java.util.*;
public class esquema {

	private String contenido;
	
	public esquema(){
		
	}
	
	public String getEsquema(String url){
		String file = url;
		String result = "";
		try{

		FileInputStream fis= new FileInputStream(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

			String line = reader.readLine();
			while(line != null){
				
				result = result.concat("<tr> <td align =\"left\">");
				
				if(line.contains("{") ) result = result.concat("<font face=\"verdana\">" +line + "</font> <br>  ");
				else if(line.contains("}"))result = result.concat("<font face=\"verdana\">" + line + "</font><br> "); 
				else result = result.concat(" &nbsp;&nbsp;&nbsp;&nbsp;" + "<font face=\"verdana\">" + line + "</font> <br>" ); 
			    line = reader.readLine();
			    result = result.concat("</td> </tr> ");
			}           

		fis.close();
		}catch (Exception e){
			
		}
		return result;
	}
}

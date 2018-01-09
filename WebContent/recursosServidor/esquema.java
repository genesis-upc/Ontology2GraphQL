package serverGraphQL;
import java.io.*;
import java.util.*;
public class esquema {

	private String contenido;
	
	public esquema(){
		contenido = "";
	}
	
	public String getEsquema(String url){
		String file = url;
		try{

		FileInputStream fis= new FileInputStream(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

			String line = reader.readLine();
			while(line != null){
				
				contenido = contenido.concat("<tr> <td align =\"left\">");
				
				if(line.contains("{") ) contenido = contenido.concat("<font face=\"verdana\">" +line + "</font> <br>  ");
				else if(line.contains("}"))contenido = contenido.concat("<font face=\"verdana\">" + line + "</font><br> "); 
				else contenido = contenido.concat(" &nbsp;&nbsp;&nbsp;&nbsp;" + "<font face=\"verdana\">" + line + "</font> <br>" ); 
			    line = reader.readLine();
			    contenido = contenido.concat("</td> </tr> ");
			}           

		fis.close();
		}catch (Exception e){
			
		}
		return contenido;
	}
}

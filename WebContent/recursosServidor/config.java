package serverGraphQL;
import java.io.*;
import java.util.*;

public class config {
	
	private String parentFile;
	private String url;
	private String user;
	private String dbName;
	private String password;
	private String serverName;
	private String rootConfig;
	
	public config(String rootConfig){
		this.rootConfig = rootConfig;
	}
	
	public config(String parentFile, String url, String user, String password, String dbName, String serverName){
		this.parentFile = parentFile;
		this.url = url;
		this.user= user;
		this.password = password;
		this.dbName = dbName;
		this.serverName = serverName;
	}
	
	public Properties loadConfig(){
		Properties prop = new Properties();
		try{
			InputStream input = new FileInputStream(new File(rootConfig));
			
			prop.load(input);
			input.close();
		}catch(Exception e){
			
		}
		return prop;

	}

	public Properties saveConfig(){
		Properties propFinal = new Properties();
		try{
		 
		 
		 Integer index = serverName.lastIndexOf("\\");
		serverName = serverName.substring(index + 1);
			
		 propFinal.setProperty("url_hostlist", url);
		   propFinal.setProperty("user", user);
		    propFinal.setProperty("password", password);
		    propFinal.setProperty("dbName", dbName);
		    propFinal.setProperty("serverName", serverName);
		    
			String path =parentFile + "/" + serverName +"/config.properties";
			
			
		    FileWriter writerr = new FileWriter(new File(path));
		    propFinal.store(writerr, "host settings");
		    writerr.close();
		}catch(Exception e){
			
		}
		return propFinal;
	}

}

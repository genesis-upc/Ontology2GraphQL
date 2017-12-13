package createApiGraphQL;

import java.util.ArrayList;

public class Field {

	
	private Modificador hasModifier;
	private String name;
	private String domain;
	private String range;
	
	public Field(String name, String domain, String range,Modificador modifier){
		this.setName(name);
		this.setDomain(domain);
		this.setRange(range);
		this.setModifier(modifier);
	}

	public void setModifier(Modificador mod){
		hasModifier = mod;
	}
	
	public Modificador getModifier() {
		return hasModifier;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

}

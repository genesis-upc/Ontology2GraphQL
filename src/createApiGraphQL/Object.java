package createApiGraphQL;
import java.util.ArrayList;

public class Object {
	private String name;
	private ArrayList<String> subClassOf;
	private ArrayList<Field> fields;
	private boolean isInterface;
	
	
	public Object(String name, ArrayList<String> subClassOf, ArrayList<Field> fields){
		this.setName(name);
		setSubClassOf(subClassOf);
		setFields(fields);
		isInterface = false;
		
	}
	


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public ArrayList<String> getSubClassOf() {
		return subClassOf;
	}


	public void setSubClassOf(ArrayList<String> subClassOf) {
		this.subClassOf = subClassOf;
	}
	
	public void addSubClassOf(String subClassOfString){
		subClassOf.add(subClassOfString);
	}

	
	public void addField(Field field){
		fields.add(field);
	}


	public ArrayList<Field> getFields() {
		return fields;
	}


	public void setFields(ArrayList<Field> fields) {
		this.fields = fields;
	}



	public boolean isInterface() {
		return isInterface;
	}



	public void setInterface(boolean isInterface) {
		this.isInterface = isInterface;
	}

}

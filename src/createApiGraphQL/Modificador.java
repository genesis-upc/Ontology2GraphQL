package createApiGraphQL;
import java.util.ArrayList;

public class Modificador{
	
	private String name;
	private ArrayList<Modificador> combinedWith;
	
	public Modificador(String name, ArrayList<Modificador> combinedWith){
		this.name = name;
		this.combinedWith = combinedWith;
		
	}
	
	public void addCombined(Modificador mod){
		getCombinedWith().add(mod);
	}
	
	public ArrayList<Modificador> getCombinedWith(){
		return combinedWith;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCombinedWith(ArrayList<Modificador> combinedWith) {
		this.combinedWith = combinedWith;
	}
}
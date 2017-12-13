package createApiGraphQL;
import java.util.ArrayList;

public class List<T> extends Modificador{
	
	private ArrayList<String> hasElements;

	public List(String name, ArrayList<Modificador> combinedWith){
		super(name, combinedWith);
		hasElements = new ArrayList<>();
	}
	
	public void addElement(String element){
		hasElements.add(element);
	}
}

package mai.par.trains;

import java.util.HashMap;

@SuppressWarnings("serial")
public class WagonMap extends HashMap<String, Wagon> {

	public void difference(WagonMap wm){
		for (Wagon wagon: wm.values()){
			remove(wagon.getId());
		}
	}
	
	public WagonMap(){
		super();
	}
	
	public WagonMap(WagonMap original, WagonMap elements){
		super();
		String id;
		for (Wagon wagon: original.values()){
			id=wagon.getId();
			put(id,elements.get(id));
		}
	}
}

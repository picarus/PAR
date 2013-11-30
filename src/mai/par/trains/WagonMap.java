package mai.par.trains;

import java.util.HashMap;

@SuppressWarnings("serial")
public class WagonMap extends HashMap<String, Wagon> {

	public void difference(WagonMap wm){
		for (Wagon wagon: wm.values()){
			remove(wagon.getId());
		}
	}
}

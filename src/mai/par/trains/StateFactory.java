package mai.par.trains;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mai.par.trains.predicates.Predicate;

public class StateFactory {

	static private int MAX_RAILWAYS=3;
	
	public static State initialState=null;
	public static State finalState=null;
	State currentState=null;
	public static Map<String, Wagon> wagons;		// all the wagons
	
	
	public static void initialize()
	{
		initialState = new State();
		finalState = new State();
		wagons = new HashMap<String, Wagon>();
	}
	
	public static void setMAX_RAILWAYS(int max_RAILWAYS) {
		if ((initialState==null) && (finalState==null))
			MAX_RAILWAYS = max_RAILWAYS;
	}
	
	public static int getMAX_RAILWAYS() {
		return MAX_RAILWAYS;
	}
	
	public static State createState(List<Predicate> predicates)
	{
		return new State();
	}

	public State getCurrentState() {
		return currentState;
	}

	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}
}

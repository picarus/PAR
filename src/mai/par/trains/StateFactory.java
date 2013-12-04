package mai.par.trains;

import mai.par.trains.predicates.Predicate;
import mai.par.trains.predicates.PredicateGroup;

public class StateFactory {

	static private int MAX_RAILWAYS=3;	// TODO: allow have MAX_RAILWAYS > 3 (as parameter?)
	public static State initialState=null;
	public static State finalState=null;
	State currentState=null;
	public static WagonMap wagons;		// all the wagons	
	
	public static void initialize()
	{
		initialState = new State();
		finalState = new State();
		wagons = new WagonMap();
	}
	
	public static void setMAX_RAILWAYS(int max_RAILWAYS) {
		MAX_RAILWAYS = max_RAILWAYS;
	}
	
	public static int getMAX_RAILWAYS() {
		return MAX_RAILWAYS;
	}
	
//	TODO: Problem here: things will only workout if the predicates are given in the correct order. 
//	It may good enough for now, however it will have to be split in a two step building, where 
//	we can treat a predecessor before the current and then come back(recursively). May be solved with priorities 
	public static State createState(PredicateGroup predicates, Boolean clean)
	{
		State state = new State(wagons, clean);
		state.setPredicateGroup(predicates);
		for(Predicate predicate : predicates)
		{
			System.out.println(predicate);
			switch (predicate.getPredicate()) {
			case PR_ONSTATION:
				state.setToStation(predicate.getId1());
				break;
			case PR_INFRONTOF:
				state.setInFrontOf(predicate.getId1(), predicate.getId2());
				break;
			case PR_FREE:
				state.setWagonFree(predicate.getId1());
				break;
			case PR_FREELOCOMOTIVE:
				state.setFreeLocomotive();
				break;
			case PR_TOWED:
				state.setWagonTowed(predicate.getId1());
				break;
			case PR_LOADED:
				state.loadWagon(predicate.getId1());
				break;
			case PR_EMPTY:
				state.unloadWagon(predicate.getId1());
				break;
			default:
				break;
			}
			state.drawState();
		}
		return state;
	}

	public State getCurrentState() {
		return currentState;
	}

	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}
}

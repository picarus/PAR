package mai.par.trains;

import java.util.List;

import mai.par.trains.predicates.Predicate;
import mai.par.trains.predicates.PredicateFactory;

public final class LinealPlanner {

	protected State initialState;
	protected State finalState;
	
	public static GoalStack goalStack = new GoalStack();
	
	public static Plan createPlan(State initialState, State finalState)
	{
		Plan finalPlan = new Plan();
		// Compare target and initial state
		// Prioritize differences: detect wagons out of order (by railway)
		//     first load/unload
		//			some heuristic to decide order??
		//     sort wagons by railway 
		//	   		some heuristic to decide railway order???
		// TODO: Test with more railways
		
		goalStack.push(finalState);
		goalStack.push(finalState.getPredicateGroup());
		
		List<Predicate> lp;
		WagonMap wagonsLU=finalState.getWagonsRequiringLoadUnload(initialState);
		WagonMap onStationOnFinalState=finalState.getWagonsOnStation(wagonsLU);
		WagonMap onStationOnInitialState=initialState.getWagonsOnStation(wagonsLU);
		wagonsLU.difference(onStationOnFinalState);
		wagonsLU.difference(onStationOnInitialState);
		
		// push all predicates individually
		// except the ones referred to load / unload already satisfied 
		// start with the wagons to load / unload at final state (the last thing to accomplish)
		
		lp=PredicateFactory.createPredicatesLoadUnload(onStationOnFinalState);
		goalStack.pushList(lp);
		System.out.println(goalStack.toString());
		// push all railways configuration 1 by 1 starting from the station (even the ones that are satisfied now)
		// in order
		// wagons to load / unload not at initial not final state
		lp=PredicateFactory.createPredicatesLoadUnload(wagonsLU);
		goalStack.pushList(lp); // TODO: optimize the order
		System.out.println(goalStack.toString());
		// wagons to load / unload at initial state
		lp=PredicateFactory.createPredicatesLoadUnload(onStationOnInitialState);
		goalStack.pushList(lp);
		
		System.out.println(goalStack.toString());
		
		return finalPlan;
	}
	
	 
}

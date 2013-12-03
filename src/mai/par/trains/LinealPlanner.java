package mai.par.trains;

import mai.par.trains.operators.Operator;
import mai.par.trains.predicates.Predicate;
import mai.par.trains.predicates.PredicateFactory;
import mai.par.trains.predicates.PredicateGroup;

public final class LinealPlanner {

	protected static State initialState;
	protected static State finalState;
	protected static State currentState;
	protected static Plan plan=new Plan();
	
	public static GoalStack goalStack = new GoalStack();
	
	public static void init(State initialState, State finalState){
		LinealPlanner.initialState=initialState;
		LinealPlanner.finalState=finalState;
		LinealPlanner.currentState=initialState;
	}
	
	public static void createPlanLoad(WagonMap wagonsToLoad){
		PredicateGroup pg=PredicateFactory.createPredicatesLoadUnload(wagonsToLoad);
		goalStack.push(pg); // "final state"
		System.out.println(goalStack);
	}
	
	public static void createPlanMove(){
		// push all railways configuration 1 by 1 starting from the station 
		//(even the ones that are satisfied now)
		// in order: longer first
		// note that the wagons are not in the initial state order
	}
	
	public static void createPlan() {
		
		// define intermediate state objective
		WagonMap wagonsLU=finalState.getWagonsRequiringLoadUnload(initialState);
		WagonMap onStationOnFinalState=finalState.getWagonsOnStation(wagonsLU);
		WagonMap onStationOnInitialState=initialState.getWagonsOnStation(wagonsLU);
		wagonsLU.difference(onStationOnFinalState);
		wagonsLU.difference(onStationOnInitialState);
						
		// wagons to load / unload at initial state
		if (!onStationOnInitialState.isEmpty()){
			createPlanLoad(onStationOnInitialState);
			solve();
			System.out.println(goalStack);
		}
		
		// wagons to load / unload not at initial not final state
		if (!wagonsLU.isEmpty()){
			createPlanLoad(wagonsLU); // TODO: consider sort the wagons
			solve();
			System.out.println(goalStack);
		}
		
		createPlanMove(); // we will assume this is always required
		//solve();
		
		if (!onStationOnFinalState.isEmpty()){
			createPlanLoad(onStationOnFinalState);
			//solve();
		}
		
		// Compare target and initial state
		// Prioritize differences: detect wagons out of order (by railway)
		//     first load/unload
		//			some heuristic to decide order??
		//     sort wagons by railway 
		//	   		some heuristic to decide railway order???
		// TODO: Test with more railways
		
		goalStack.push(finalState);
		goalStack.push(finalState.getPredicateGroup());
		
		// solution
		//System.out.println(goalStack);
		//System.out.println(plan);
	}
	
	protected static void solve(){
		
		Stackable stackable;
		StackableTypes stackableTypes;
		Operator operator;
		Predicate predicate;
		State state;
		PredicateGroup predicateGroup;
		
		while ( !goalStack.empty() ){
			stackable=goalStack.pop();
			stackableTypes=StackableTypes.getStackableType(stackable);
			switch(stackableTypes){
			case Operator:
				operator=(Operator)stackable;
				System.out.println("Operator:"+operator);
				if (currentState.canApply(operator)){
					plan.add(operator); // the operator is added to the plan
					currentState=currentState.apply(operator); // the current state is updated
				} else {
					System.out.println("ERROR: OPERATOR NOT APPLICABLE");
				}
				break;
			case Predicate:
				predicate=(Predicate)stackable;
				if (!currentState.isCompliant(predicate)){
					// look for an operator to satisfy the predicate
					goalStack.push(predicate); // push back
					operator=currentState.accomplish(predicate);
					goalStack.push(operator);
					System.out.println("Operator selected:"+operator);
				}
				System.out.println("Predicate:"+predicate);
				break;
			case PredicateGroup:  // from an operator ( or state ?)
				predicateGroup=(PredicateGroup)stackable;
				System.out.println("PredicateGroup:"+predicateGroup);
				if (!currentState.isCompliant(predicateGroup)) {
					// if not: ERROR --> our implementation should guarantee when we get here we satisfy all predicates
					System.out.println("ERROR:Predicate Group NOT compliant");
				}	
				break;
			case State:
				state=(State)stackable;
				if (currentState.isCompliant(state))
					currentState=state;
				else {
					System.out.println("ERROR:STATE NOT compliant");
				}
				// TODO: we are done if it is the final state
				// what other state is stacked? I think none.
				break;
			}
			System.out.println(goalStack);
			System.out.println(plan);
			System.out.println(currentState);
		}
	}
	
	 
}

package mai.par.trains;

import java.util.List;

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
	
	public static void createPlanLoad(WagonMap wagonsToLoad)
	{
		sortLUWagons(wagonsToLoad);
		PredicateGroup pg=PredicateFactory.createPredicatesLoadUnload(wagonsToLoad);
		goalStack.push(pg); // "final state"
	}
	
	private static void sortLUWagons(WagonMap wagonsToLoad) 
	{
		// TODO They should be sorted like we agreed to avoid rearranging
		
	}

	public static void createPlanMove(){
		// push all railways configuration 1 by 1 starting from the station 
		//(even the ones that are satisfied now)
		// in order: longer first
		// note that the wagons are not in the initial state order
		sortMoveWagons();
		List<PredicateGroup> lPG=finalState.getWagonsPositions();
		for (PredicateGroup pg: lPG)
			goalStack.push(pg);
	
	}
	
	private static void sortMoveWagons() 
	{
		// TODO They should be sorted like we agreed to avoid rearranging
	}

	public static void createPlan() {
		
		// define intermediate state objective
		WagonMap wagonsLU=finalState.getWagonsRequiringLoadUnload(initialState);
		WagonMap onStationOnFinalState=finalState.getWagonsOnStation(wagonsLU);
		WagonMap onStationOnInitialState=initialState.getWagonsOnStation(wagonsLU);
		wagonsLU.difference(onStationOnFinalState);
		wagonsLU.difference(onStationOnInitialState);
		
		goalStack.push(finalState);
		
		if (!onStationOnFinalState.isEmpty()){
			createPlanLoad(onStationOnFinalState);	
		}
		
		createPlanMove(); // we will assume this is always required
		
		// wagons to load / unload not at initial not final state
		if (!wagonsLU.isEmpty())
		{
			createPlanLoad(wagonsLU);
		}
		
		// wagons to load / unload at initial state
		if (!onStationOnInitialState.isEmpty()){
			createPlanLoad(onStationOnInitialState);
		}
		
		solve();
		
		// solution
		System.out.println(goalStack);
		System.out.println(plan);
	}
	
	protected static void solve(){
		
		Stackable stackable;
		StackableTypes stackableTypes;
		Operator operator;
		Predicate predicate;
		State state;
		PredicateGroup predicateGroup;
		boolean changePlan;
		boolean changeGoalStack;
		boolean verbose=true;
		
		while ( !goalStack.empty() ){
			changePlan = false;
			changeGoalStack= false;
			stackable=goalStack.pop();
			stackableTypes=StackableTypes.getStackableType(stackable);
			switch(stackableTypes){
			case Operator:
				operator=(Operator)stackable;
				if (verbose) 
					System.out.println("Operator:"+operator);
				if (currentState.canApply(operator)){
					plan.add(operator); // the operator is added to the plan
					currentState=currentState.apply(operator); // the current state is updated
					changePlan=true;
				} else {
					System.out.println("ERROR: OPERATOR NOT APPLICABLE");
				}
				break;
			case Predicate:
				predicate=(Predicate)stackable;
				if (!currentState.isCompliant(predicate)){
					// look for an operator to satisfy the predicate
					operator=currentState.accomplish(predicate);
					if(operator != null)
					{
						goalStack.push(predicate); // push back
						goalStack.push(operator);
						if (verbose)
							System.out.println("Operator selected:"+operator);
						changeGoalStack=true;	
					}
				}
				if (verbose)
					System.out.println("Predicate:"+predicate);
				break;
			case PredicateGroup:  // from an operator ( or state ?)
				predicateGroup=(PredicateGroup)stackable;
				if (verbose)
					System.out.println("PredicateGroup:"+predicateGroup);
				if (!currentState.isCompliant(predicateGroup)) {
					// if not: ERROR --> our implementation should guarantee when we get here we satisfy all predicates
					System.out.println("ERROR:Predicate Group NOT compliant");
				} else {
					changeGoalStack=true;
				}
				
				break;
			case State:
				state=(State)stackable;
				if (currentState.isCompliant(state)){
					currentState=state;
					changeGoalStack=true;
				}
				else {
					System.out.println("ERROR:STATE NOT compliant");
				}
				break;
			}
			
			if (changeGoalStack){
				if (verbose)
					System.out.println(goalStack);
			}
			if (changePlan){
				System.out.println(plan.size()+"->"+plan);
				System.out.println(currentState);
			}
		}
	}
	
	 
}

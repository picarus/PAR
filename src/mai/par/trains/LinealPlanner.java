package mai.par.trains;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mai.par.Log;
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
	
	public static void createPlanLoad(WagonMap wagonsToLoad, PredicateGroup pg)
	{
		//sortLUWagons(wagonsToLoad);
		//TODO:Consider sorting 
		PredicateGroup pgNew=PredicateFactory.createPredicatesLoadUnload(wagonsToLoad);
		pg.addAll(pgNew);
	}
	
	private static void sortLUWagons(WagonMap wagonsToLoad) 
	{
		// TODO They should be sorted like we agreed to avoid rearranging
		
	}

	public static void createPlanMove(PredicateGroup pg){
		// push all railways configuration 1 by 1 starting from the station 
		//(even the ones that are satisfied now)
		// in order: longer first
		// note that the wagons are not in the initial state order
		List<PredicateGroup> lPG=finalState.getWagonsPositions();
		//	sortMoveWagons(lPG);
		for (PredicateGroup pgNew: lPG)
			pg.addAll(pgNew);
	
	}
	
	private static List<PredicateGroup> sortMoveWagons(List<PredicateGroup> lPG)
	{
		List<PredicateGroup> preds = new ArrayList<PredicateGroup>();
		Railways clone = new Railways(currentState.railways, currentState.wagons);
		Collections.sort(clone);
		Collections.reverse(clone);
		for(Railway rail :clone)
		{
			for(PredicateGroup pg : lPG)
			{
				Collections.sort(pg);
			}
		}
		return preds;
	}

	public static void createPlan() 
	{
		
		// define intermediate state objective
		WagonMap wagonsLU=finalState.getWagonsRequiringLoadUnload(initialState);
		WagonMap onStationOnFinalState=finalState.getWagonsOnStation(wagonsLU);
		WagonMap onStationOnInitialState=initialState.getWagonsOnStation(wagonsLU);
		wagonsLU.difference(onStationOnFinalState);
		wagonsLU.difference(onStationOnInitialState);
		
		goalStack.push(finalState);
		PredicateGroup pg=new PredicateGroup();
		
		if (!onStationOnFinalState.isEmpty()){
			createPlanLoad(onStationOnFinalState,pg);	
		}
		
		createPlanMove(pg); // we will assume this is always required
		
		// wagons to load / unload not at initial not final state
		if (!wagonsLU.isEmpty())
		{
			createPlanLoad(wagonsLU,pg);
		}
		
		// wagons to load / unload at initial state
		if (!onStationOnInitialState.isEmpty()){
			createPlanLoad(onStationOnInitialState,pg);
		}
		
		PredicateGroup cpg=pg.removeDuplicates(finalState);
		goalStack.push(cpg);
		goalStack.push(pg);
		System.out.println(goalStack);
		Log.log("\nInitial Stack: \n" + goalStack);
		
		Log.log("\nSolution Step by Step: \n");
		solve();
		
		// solution
		System.out.println(goalStack);
		Collections.reverse(plan);
		System.out.print("Resolution plan: ");
		for(Operator step: plan)
		{
			System.out.print(step + "; ");
		}
		System.out.println("\nSteps: "+plan.size());
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
		boolean verbose=false;
		
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
				if (verbose)
					System.out.println("Predicate:"+predicate);
				if (!currentState.isCompliant(predicate)){
					// look for an operator to satisfy the predicate
					operator=currentState.accomplish(predicate);
					if(operator != null)
					{
						System.out.println("Current Goal: " + predicate);
						Log.log("Current Objective: " + predicate);
						goalStack.push(predicate); // push back
						goalStack.push(operator);
						if (verbose)
							System.out.println("Operator selected:"+operator);
						Log.log("Apply operator: " + operator);
						changeGoalStack=true;	
					}
				}
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
				Log.log("Plan: " + plan.size()+"->"+plan);
				Log.log(currentState.toString());
				Log.log("\n");
				System.out.println(plan.size()+"->"+plan);
				System.out.println(currentState);
			}
		}
	}
	
	 
}

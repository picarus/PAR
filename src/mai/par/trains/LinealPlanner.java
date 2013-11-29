package mai.par.trains;

public class LinealPlanner {

	public static GoalStack goalStack = new GoalStack();
	
	public Plan createPlan(State initialState, State finalState)
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
		return finalPlan;
	}
}

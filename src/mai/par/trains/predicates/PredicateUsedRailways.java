package mai.par.trains.predicates;

public class PredicateUsedRailways extends Predicate {
	
	int usedRailways;
	TrainPredicateUsedRailways predUsedRailways;
	
	public PredicateUsedRailways(TrainPredicateUsedRailways predUsedRailways){
		super(TrainPredicate.PR_USEDRAILWAYS);
		this.predUsedRailways=predUsedRailways;
	}
	
	public PredicateUsedRailways(int n){
		super(TrainPredicate.PR_USEDRAILWAYS);
		usedRailways=n;
	}
	
	int getUsedRailways(){
		return usedRailways;
	}
}

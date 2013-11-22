package mai.par.trains.predicates;


public class PredicateFactory {
	
	protected static Predicate createPredicate(TrainPredicate predicate){
		return new Predicate(predicate);
	}
	
	protected static Predicate createPredicate(TrainPredicate predicate, String idX){
		return new Predicate(predicate, idX);
	}
	
	protected static Predicate createPredicate(TrainPredicate predicate, String idX, String idY){
		return new Predicate(predicate, idX, idY);
	}
	
	public static Predicate createPredicateFreeLocomotive(){
		return new Predicate(TrainPredicate.PR_FREELOCOMOTIVE );
	}
	
	public static Predicate createPredicateEmpty(String idX){
		return new Predicate(TrainPredicate.PR_EMPTY, idX);
	}
	
	public static Predicate createPredicateLoaded(String idX){
		return new Predicate(TrainPredicate.PR_LOADED, idX);
	}
	
	public static Predicate createPredicateFree(String idX){
		return new Predicate(TrainPredicate.PR_FREE, idX);
	}
	
	public static Predicate createPredicateTowed(String idX){
		return new Predicate(TrainPredicate.PR_TOWED, idX);
	}
	
	public static Predicate createPredicateOnStation(String idX){
		return new Predicate(TrainPredicate.PR_ONSTATION, idX);
	}
	
	public static Predicate createPredicateInFrontOf(String idX, String idY){
		return new Predicate(TrainPredicate.PR_INFRONTOF, idX, idY);
	}
	
	public static PredicateUsedRailways createPredicateUsedRaildways(int n){
		return new PredicateUsedRailways(n);
	}
	
	public static PredicateUsedRailways createPredicateNotFullUsedRailways(){
		return new PredicateUsedRailways(TrainPredicateUsedRailways.PR_USEDRAILWAYS_NOTFULL);
	}
	
	public static PredicateUsedRailways createPredicateDecreaseUsedRailways(){
		return new PredicateUsedRailways(TrainPredicateUsedRailways.PR_USEDRAILWAYS_DECREASE);
	}
	
	public static PredicateUsedRailways createPredicateIncreaseUsedRailways(){
		return new PredicateUsedRailways(TrainPredicateUsedRailways.PR_USEDRAILWAYS_INCREASE);
	}
}

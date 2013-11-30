package mai.par.trains.predicates;

import java.util.ArrayList;
import java.util.List;

import mai.par.trains.Wagon;
import mai.par.trains.WagonMap;


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
	
	public static Predicate createPredicateNotFullUsedRailways(){
		return new Predicate(TrainPredicate.PR_USEDRAILWAYS_NOTFULL);
	}
	
	public static Predicate createPredicateDecreaseUsedRailways(){
		return new Predicate(TrainPredicate.PR_USEDRAILWAYS_DECREASE);
	}
	
	public static Predicate createPredicateIncreaseUsedRailways(){
		return new Predicate(TrainPredicate.PR_USEDRAILWAYS_INCREASE);
	}
	
	public static List<Predicate> createPredicatesLoadUnload(WagonMap wagonMap){
		List<Predicate> lp=new ArrayList<Predicate>();
		String id;
		Predicate p;
		for (Wagon wagon: wagonMap.values()){
			id=wagon.getId();
			if (wagon.isLoaded()) { // wagon is loaded
				p=createPredicateLoaded(id);
			} else { // wagon is not loaded
				p=createPredicateEmpty(id);
			}
			lp.add(p);	
		}
		return lp;
	}
}

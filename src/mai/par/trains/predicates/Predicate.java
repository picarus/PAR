package mai.par.trains.predicates;

import mai.par.trains.State;
import mai.par.trains.operators.Stackable;

public class Predicate implements Stackable {
	
	TrainPredicate predicate;
	String id1;
	String id2;
	int n;
	
	Predicate(TrainPredicate predicate){
		this.predicate=predicate;
	}
	
	Predicate(TrainPredicate predicate, int n){
		this.predicate=predicate;
		this.n=n;
	}
	
	Predicate(TrainPredicate predicate, String id1){
		this.predicate=predicate;
		this.id1=id1;
	}
	
	Predicate(TrainPredicate predicate, String id1, String id2){
		this.predicate=predicate;
		this.id1=id1;
		this.id2=id2;
	}
	
	public boolean isCompliant(State state){
		return true;
	}
	
	public void instatiate(String id1){
		
	}
	
}

package mai.par.trains.operators;

import mai.par.trains.predicates.Predicate;
import mai.par.trains.predicates.PredicateFactory;

public abstract class OperatorDrop extends Operator {
	OperatorDrop(TrainOperator operator, String id1){
		super(operator,OperatorType.DROP, id1);	
		
		// preconditions
		Predicate freeLocomotivePredicate=PredicateFactory.createPredicateFreeLocomotive();
		Predicate towed=PredicateFactory.createPredicateTowed(id1);
		precondPredicate.add(towed);
	
		// add List
		addPredicate.add(freeLocomotivePredicate);
		
		// delete List
		delPredicate.add(towed);
		
	}
}

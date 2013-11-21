package mai.par.trains.operators;

import mai.par.trains.predicates.Predicate;
import mai.par.trains.predicates.PredicateFactory;

public abstract class OperatorTake extends Operator {
	OperatorTake(TrainOperator operator, String id1){
		super(operator,OperatorType.TAKE, id1);	
		
		// preconditions
		Predicate freeLocomotivePredicate=PredicateFactory.createPredicateFreeLocomotive();
		precondPredicate.add(PredicateFactory.createPredicateFree(id1));
		precondPredicate.add(freeLocomotivePredicate);
		
		// add List
		addPredicate.add(PredicateFactory.createPredicateTowed(id1));
		
		// delete List
		delPredicate.add(freeLocomotivePredicate);
	}
}

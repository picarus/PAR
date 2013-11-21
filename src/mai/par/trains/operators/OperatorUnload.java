package mai.par.trains.operators;

import mai.par.trains.predicates.Predicate;
import mai.par.trains.predicates.PredicateFactory;

public class OperatorUnload extends Operator {
	
	OperatorUnload(TrainOperator operator, String id1) {
		super(operator, OperatorType.LOAD, id1);
		Predicate empty=PredicateFactory.createPredicateEmpty(id1);
		Predicate loaded=PredicateFactory.createPredicateLoaded(id1);
		// preconditions
		precondPredicate.add(loaded);
		precondPredicate.add(PredicateFactory.createPredicateOnStation(id1));
		// add list
		addPredicate.add(empty);
		// delete list
		delPredicate.add(loaded);
	}
}

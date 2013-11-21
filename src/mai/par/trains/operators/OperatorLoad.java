package mai.par.trains.operators;

import mai.par.trains.predicates.Predicate;
import mai.par.trains.predicates.PredicateFactory;

public class OperatorLoad extends Operator {

	OperatorLoad(TrainOperator operator, String id1) {
		super(operator, OperatorType.LOAD, id1);
		// preconditions
		Predicate empty=PredicateFactory.createPredicateEmpty(id1);
		precondPredicate.add(empty);
		precondPredicate.add(PredicateFactory.createPredicateOnStation(id1));
		// add list
		addPredicate.add(PredicateFactory.createPredicateLoaded(id1));
		// delete list
		delPredicate.add(empty);
	}

}

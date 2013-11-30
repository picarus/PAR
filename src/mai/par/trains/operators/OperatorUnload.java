package mai.par.trains.operators;

import mai.par.trains.predicates.Predicate;
import mai.par.trains.predicates.PredicateFactory;

public class OperatorUnload extends Operator {
	
	public OperatorUnload( String id1) {
		super(TrainOperator.OP_UNLOAD, OperatorType.LOAD, id1);
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

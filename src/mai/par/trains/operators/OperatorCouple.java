package mai.par.trains.operators;

import mai.par.trains.predicates.Predicate;
import mai.par.trains.predicates.PredicateFactory;


public class OperatorCouple extends OperatorTake {
	public OperatorCouple(String id1) {
		super(TrainOperator.OP_COUPLE,id1);
		// used railways is not a real precondition. It is true for any n.
		Predicate onStation=PredicateFactory.createPredicateOnStation(id1);
		precondPredicate.add(onStation);
		// add list
		addPredicate.add(PredicateFactory.createPredicateDecreaseUsedRailways());
		addPredicate.add(PredicateFactory.createPredicateDecreaseUsedRailways());
		// delete list
		delPredicate.add(onStation);
	}
}

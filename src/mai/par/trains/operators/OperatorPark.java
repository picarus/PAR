package mai.par.trains.operators;

import mai.par.trains.predicates.PredicateFactory;


public class OperatorPark extends OperatorDrop {
	
	public OperatorPark(String id1) {
		super(TrainOperator.OP_PARK,id1);
		// precondition list
		precondPredicate.add(PredicateFactory.createPredicateNotFullUsedRailways());
		// add list
		addPredicate.add(PredicateFactory.createPredicateIncreaseUsedRailways());
		addPredicate.add(PredicateFactory.createPredicateOnStation(id1));
		// del list
		//			empty
	}
	
}

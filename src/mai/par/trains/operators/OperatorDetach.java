package mai.par.trains.operators;

import mai.par.trains.predicates.Predicate;
import mai.par.trains.predicates.PredicateFactory;


public class OperatorDetach extends OperatorTake {
	public OperatorDetach(String id1, String id2) {
		super(TrainOperator.OP_DETACH,id1);
		this.id2=id2;
		// precondition list
		Predicate inFrontOf=PredicateFactory.createPredicateInFrontOf(id1, id2);
		precondPredicate.add(inFrontOf);
		// add list
		addPredicate.add(PredicateFactory.createPredicateFree(id2));
		// delete list
		delPredicate.add(inFrontOf);
	}
}

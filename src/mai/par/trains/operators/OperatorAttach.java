package mai.par.trains.operators;

import mai.par.trains.predicates.Predicate;
import mai.par.trains.predicates.PredicateFactory;


public class OperatorAttach extends OperatorDrop {

	OperatorAttach(String id1, String id2) {
		super(TrainOperator.OP_ATTACH, id1);
		this.id2=id2;
		// Precondition list
		Predicate free=PredicateFactory.createPredicateFree(id2);
		precondPredicate.add(free);
		// Add list
		addPredicate.add(PredicateFactory.createPredicateInFrontOf(id1, id2));
		addPredicate.add(PredicateFactory.createPredicateFreeLocomotive());
		// Del list
		delPredicate.add(free);
	}

}

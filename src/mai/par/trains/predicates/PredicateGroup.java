package mai.par.trains.predicates;

import java.util.ArrayList;

import mai.par.trains.operators.Stackable;

@SuppressWarnings("serial")
public class PredicateGroup extends ArrayList<Predicate> implements Stackable {
	public PredicateGroup(PredicateGroup pg){
		super(pg);
	}

	public PredicateGroup() {
		super();
	}
}

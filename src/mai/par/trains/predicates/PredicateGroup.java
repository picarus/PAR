package mai.par.trains.predicates;

import java.util.ArrayList;

import mai.par.trains.Stackable;

@SuppressWarnings("serial")
public class PredicateGroup extends ArrayList<Predicate> implements Stackable {
	public PredicateGroup(PredicateGroup pg){
		super(pg);
	}

	public PredicateGroup() {
		super();
	}
	
	public Predicate contains(Predicate pred){
		for (Predicate p: this){
			if (pred.equals(p))
				return p;
		}
		return null;
	}
	
}

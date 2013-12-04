package mai.par.trains.predicates;

import java.util.ArrayList;

import mai.par.trains.Stackable;
import mai.par.trains.State;

@SuppressWarnings("serial")
public class PredicateGroup extends ArrayList<Predicate> implements Stackable
{
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
	
	public PredicateGroup removeDuplicates(State finalState) {
		PredicateGroup cpg=new PredicateGroup(finalState.getPredicateGroup()); // copy the original
		Predicate pred;
		System.out.println(cpg.size());
		for (Predicate p:this){
			pred=cpg.contains(p);
			if (pred!=null)
				cpg.remove(pred);
		}
		System.out.println(cpg.size());
		return cpg;
	}
}

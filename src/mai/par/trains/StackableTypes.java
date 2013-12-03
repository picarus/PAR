package mai.par.trains;

public enum StackableTypes {
	State, Operator, Predicate, PredicateGroup;
	
	public static StackableTypes getStackableType(Stackable st){
		if (st instanceof mai.par.trains.operators.Operator)
			return Operator;
		if (st instanceof mai.par.trains.predicates.Predicate)
			return Predicate;
		return StackableTypes.valueOf(st.getClass().getSimpleName());
	}
	
}

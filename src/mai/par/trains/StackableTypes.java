package mai.par.trains;

public enum StackableTypes {
	State, Operator, Predicate, PredicateGroup;
	
	public static StackableTypes getStackableType(Stackable st){
		return StackableTypes.valueOf(st.getClass().getSimpleName());
	}
	
}

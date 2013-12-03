package mai.par.trains.operators;


import mai.par.trains.Stackable;
import mai.par.trains.predicates.PredicateGroup;

public abstract class Operator implements Stackable {
	
	OperatorType type;
	TrainOperator operator;
	String id1;
	String id2;
	int priority;
	
	PredicateGroup precondPredicate=new PredicateGroup();
	PredicateGroup addPredicate=new PredicateGroup();
	PredicateGroup delPredicate=new PredicateGroup();
	
	Operator(TrainOperator operator, OperatorType type, String id1){
		this.operator=operator;
		this.type=type;
		this.id1=id1;
	}
	
	Operator(TrainOperator operator, OperatorType type, String id1, String id2){
		this.operator=operator;
		this.type=type;
		this.id1=id1;
		this.id2=id2;
	}
	
	public TrainOperator getOperator(){
		return operator;
	}
	
	public String getID1(){
		return id1;
	}
	
	public String getID2(){
		return id2;
	}
	
	public PredicateGroup getPrecondPredicate() {
		return precondPredicate;
	}

	public PredicateGroup getAddPredicate() {
		return addPredicate;
	}

	public PredicateGroup getDelPredicate() {
		return delPredicate;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public void instatiate(String id1){
		
	}
	
	public String toString(){
		return operator + "("+id1+(id2==null?"":","+id2)+")";
	}
	
}

package mai.par.trains.operators;

import java.util.ArrayList;

import mai.par.trains.predicates.Predicate;

public abstract class Operator implements Stackable {
	
	OperatorType type;
	TrainOperator operator;
	String id1;
	String id2;
	int priority;
	
	

	ArrayList<Predicate> precondPredicate=new ArrayList<Predicate>();
	ArrayList<Predicate> addPredicate=new ArrayList<Predicate>();
	ArrayList<Predicate> delPredicate=new ArrayList<Predicate>();
	
	
	Operator(TrainOperator operator, OperatorType type){
		this.operator=operator;
		this.type=type;
	}
	
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
	
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public void instatiate(String id1){
		
	}
	
	public String toString(){
		return operator + "("+id1+(id2.equals("")?"":","+id2)+")";
	}
	
}

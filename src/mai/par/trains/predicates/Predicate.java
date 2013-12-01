package mai.par.trains.predicates;

import mai.par.trains.Stackable;

public class Predicate implements Stackable {
	
	TrainPredicate predicate;
	String id1;
	String id2;
	int priority;
	
	Predicate(TrainPredicate predicate){
		this.predicate=predicate;
		this.id1="";
		this.id2="";
	}
	
	Predicate(TrainPredicate predicate, String id1){
		this.predicate=predicate;
		this.id1=id1;
		this.id2="";
	}
	
	Predicate(TrainPredicate predicate, String id1, String id2){
		this.predicate=predicate;
		this.id1=id1;
		this.id2=id2;
	}
	
	public boolean equals(Predicate p){
		if (p.predicate!=predicate)
			return false;
		if (!p.id1.equals(id1))
			return false;
		if (!p.id2.equals(id2))
			return false;
		return p.priority==priority;
	}
	
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public TrainPredicate getPredicate() {
		return predicate;
	}

	public void setPredicate(TrainPredicate predicate) {
		this.predicate = predicate;
	}

	public String getId1() {
		return id1;
	}

	public void setId1(String id1) {
		this.id1 = id1;
	}

	public String getId2() {
		return id2;
	}

	public void setId2(String id2) {
		this.id2 = id2;
	}
	
	public String toString() {
		if(id1 == "")
			return predicate.toString();
		else if((id2 == ""))
			return predicate.toString()+"("+id1+")";
		else
			return predicate.toString()+"("+id1+","+id2+")";
	}
}

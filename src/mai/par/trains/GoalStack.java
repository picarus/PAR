package mai.par.trains;

import java.util.Stack;

import mai.par.trains.operators.Operator;
import mai.par.trains.predicates.Predicate;
import mai.par.trains.predicates.PredicateGroup;

@SuppressWarnings("serial")
public class GoalStack extends Stack<Stackable>{
	
	public void push(PredicateGroup pg){
		for (Predicate pred: pg){
			push(pred);
		}
	}
	
	public void push(Operator operator){
		super.push(operator);
		push(operator.getPrecondPredicate());
	}
	
	public void push(State state){
		super.push(state);
		//push(state.getPredicateGroup());
	}
	
	@Override
	public String toString() {
		String str;
		int i=0;
		str = "--VVVV--GOAL  STACK--VVVV--\n";
		for (Stackable s: this){
			if (s instanceof PredicateGroup){
				str += s;
			} else if (s instanceof State ){
				str += s ;
			} else if (s instanceof Operator){
				Operator operator=(Operator)s;
				str += "OP|" + s+operator.getPrecondPredicate();
			} else {
				str += i+ "|" + s;
				i++;
			}
			str +="\n------------------------\n";	
		}
		str += "-^^^^-GOAL STACK END-^^^^-\n";
		return str;
	}
	
}

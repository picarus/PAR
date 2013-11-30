package mai.par.trains;

import java.util.List;
import java.util.Stack;
import mai.par.trains.operators.Stackable;
import mai.par.trains.predicates.Predicate;
import mai.par.trains.predicates.PredicateGroup;

@SuppressWarnings("serial")
public class GoalStack extends Stack<Stackable>{
	
	public void pushList(List<Predicate> lp){
		for (Predicate pred: lp){
			push(pred);
		}
	}
	
	@Override
	public String toString() {
		String str;
		int i=0;
		str = "------------------------\n";
		for (Stackable s: this){
			if (s instanceof PredicateGroup){
				// predicate Groups are not printed
				str += s.toString();
			}	
			else if (s instanceof State ){
				str += s.toString() ;
			} else {
				str += i+ "|" + s.toString();
			}
			str += "\n------------------------\n";
			i++;
		}
		
		return str;
	}
	
}

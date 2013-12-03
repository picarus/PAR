package mai.par;

import mai.par.trains.State;
import mai.par.trains.operators.Operator;
import mai.par.trains.operators.OperatorAttach;
import mai.par.trains.operators.OperatorCouple;
import mai.par.trains.operators.OperatorDetach;
import mai.par.trains.operators.OperatorLoad;
import mai.par.trains.operators.OperatorPark;
import mai.par.trains.operators.OperatorUnload;

public abstract class Test {
	public static void testApplyOperators(State state){
		testApplyLoadOperators(state);
		testApplyTakeOperators(state);
		testApplyDropOperators(state);
		testApplyDropOperators2(state);
	}
	
	public static void testApplyDropOperators(State state){
		Operator op;
		State newState,newState2;
		
		op=new OperatorDetach("G","A"); // ok
		state=state.apply(op);
		System.out.println(state.getPredicateGroup());
		
		op=new OperatorAttach("G","E"); // ok
		newState=state.apply(op);
		if (newState!=null){
			System.out.println("G is attached to E");
			newState.drawState();
			System.out.println(newState.getPredicateGroup());
		}
		else {
			System.out.println("ERROR:G cannot be attached to E");
		}
		
		op=new OperatorCouple("A"); // ok
		newState2=newState.apply(op);
		if (newState2!=null){
			System.out.println("A is coupled");
			newState2.drawState();
			System.out.println(newState2.getPredicateGroup());
		}
		else {
			System.out.println("ERROR:A cannot be coupled");
		}
		
		op=new OperatorAttach("G","B"); // wrong
		newState2=state.apply(op);
		if (newState2!=null){
			System.out.println("ERROR: G cannot be attached to B");
			newState2.drawState();
			System.out.println(newState2.getPredicateGroup());
		}
		else {
			System.out.println("G cannot be attached to B");
		}
		
	}
	
	public static void testApplyDropOperators2(State state){
		Operator op;
		State newState,newState2,newState3;
		
		op=new OperatorDetach("E","B"); // ok
		state=state.apply(op);
		System.out.println(state.getPredicateGroup());
		
		op=new OperatorAttach("E","G"); // ok
		newState=state.apply(op);
		if (newState!=null){
			System.out.println("E is attached to G");
			newState.drawState();
			System.out.println(newState.getPredicateGroup());
		}
		else {
			System.out.println("ERROR:E cannot be attached to G");
		}
		
		op=new OperatorCouple("B"); // ok
		newState2=newState.apply(op);
		if (newState2!=null){
			System.out.println("B is coupled");
			newState2.drawState();
			System.out.println(newState2.getPredicateGroup());
		}
		else {
			System.out.println("ERROR:B cannot be coupled");
		}
		
		
		
		op=new OperatorPark("B"); // ok
		newState3=newState2.apply(op);
		if (newState3!=null){
			System.out.println("B is parked");
			newState3.drawState();
			System.out.println(newState3.getPredicateGroup());
		}
		else {
			System.out.println("ERROR:B cannot be parked");
			System.out.println(newState.getPredicateGroup());
		}
		
		op=new OperatorAttach("E","A"); // wrong
		newState2=newState.apply(op);
		if (newState2!=null){
			System.out.println("ERROR: E cannot be attached to A");
			newState2.drawState();
			System.out.println(newState2.getPredicateGroup());
		}
		else {
			System.out.println("E cannot be attached to A");
		}
		
	}
	
	public static void testApplyTakeOperators(State state){
		Operator op;
		State newState,newState2;
		
		op=new OperatorCouple("G"); //wrong: not onstation(g)
		newState=state.apply(op);
		if (newState!=null){
			System.out.println("ERROR:G should not be decouplable");
			newState.drawState();
		}
		else {
			System.out.println("G cannot be coupled");
		}
		
		op=new OperatorDetach("G","A"); // ok
		newState=state.apply(op);
		if (newState!=null){
			System.out.println("G is detached from A");
			newState.drawState();
		}
		else {
			System.out.println("ERROR:G cannot be detached from A");
		}
		
		op=new OperatorCouple("A"); // wrong: not freelocomotive
		newState2=newState.apply(op);
		if (newState2!=null){
			System.out.println("ERROR:A is decoupled");
			newState2.drawState();
		}
		else {
			System.out.println("A cannot be coupled");
		}
		
		op=new OperatorDetach("A","G"); // wrong: not infrontof(a,g)
		newState=state.apply(op);
		if (newState!=null){
			System.out.println("ERROR:A is detached from G");
			newState.drawState();
		}
		else {
			System.out.println("A cannot be detached from G");
		}
	}
	
	public static void testApplyLoadOperators(State state){
		// non-legal operators
		// should let the state unchanged, return null
		Operator op;
		State newState;
		
		op=new OperatorLoad("G");
		newState=state.apply(op);
		if (newState!=null){
			System.err.println("ERROR:G should not be able to be loaded");
			newState.drawState();
		}
		else {
			System.out.println("G cannot be loaded");
		}
		
		op=new OperatorUnload("D");
		newState=state.apply(op);
		if (newState!=null){
			System.err.println("ERROR:D should not be able to be unloaded");
			newState.drawState();
		}
		else {
			System.out.println("D cannot be unloaded");
		}
		
		op=new OperatorLoad("B");
		newState=state.apply(op);
		if (newState!=null){
			System.out.println("B is loaded");
			newState.drawState();
		}
		else {
			System.err.println("ERROR:B cannot be loaded");
		}
		
		op=new OperatorUnload("A");
		newState=state.apply(op);
		if (newState!=null){
			System.out.println("A is unloaded");
			newState.drawState();
		}
		else {
			System.err.println("ERROR:A cannot be unloaded");
		}
		
	}
}

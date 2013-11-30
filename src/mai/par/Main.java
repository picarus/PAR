package mai.par;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import mai.par.parse.TrainParser;
import mai.par.trains.LinealPlanner;
import mai.par.trains.State;
import mai.par.trains.StateFactory;
import mai.par.trains.operators.Operator;
import mai.par.trains.operators.OperatorAttach;
import mai.par.trains.operators.OperatorCouple;
import mai.par.trains.operators.OperatorDetach;
import mai.par.trains.operators.OperatorLoad;
import mai.par.trains.operators.OperatorUnload;

public class Main {

	static String file = "./problem.txt";
	 
	public static void main(String[] args) 
	{
		String s;
		State copyState;
		try {
			StateFactory.initialize();
			s = readProblemFile();
			TrainParser.tokenize(s);
			System.out.println("Initial State:");
			StateFactory.initialState.drawState();
			System.out.println("Final State:");
			StateFactory.finalState.drawState();
			System.out.println("Copy Final State:");
			copyState=new State(StateFactory.finalState);
			copyState.drawState();
			LinealPlanner.createPlan(StateFactory.initialState, StateFactory.finalState);
			
			testApplyOperators(StateFactory.finalState);
			
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void testApplyOperators(State state){
		//testApplyLoadOperators(state);
		testApplyTakeOperators(state);
		testApplyDropOperators(state);
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
	
	public static String readProblemFile() throws IOException
	{
		Path path = Paths.get(file);
	    List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
	    StringBuilder sb = new StringBuilder();
	    for(String line:lines){
	    	sb.append(line);
	    	sb.append("\n");
	    }
		return sb.toString();
	}

}

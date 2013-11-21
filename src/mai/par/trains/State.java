package mai.par.trains;

import java.util.Map;
import java.util.Stack;

import mai.par.trains.operators.Operator;

public class State {
	
	// predicates that apply
	
	
	// state:
	Map<String, Wagon> wagons;		// all the wagons
	boolean freeLocomotive=true;	// is the locomotive free?
	int usedRailways=0;				// the railways in use				
	Map<String,Wagon> freeWagonsSet;// the wagons in front of each rail (maybe a Set to break simmetries)
	String towed;					// the wagon being towed, if any
	Stack<Wagon>[] railways;		// the railways (maybe a Set to break simmetries)   			
	Map<String, Wagon> onStationSet;// the wagons parked in the station 
	Map<String, Integer> indexMap;	// in what railway is every wagon, railways[index]
	Map<String, Integer> posMap;	// in what position is every wagon
		
	// TODO: add a tabu operator to make sure there are no loops (at distance 2: do-undo)
	//       also the operator will indicate that if a drop operator has been applied
	//       the next will be a take operator (or a load/unload)
	//		 if a take operator has been applied the next will be a drop
	
	// can the operators be applied???
	protected boolean canTake(String wagonId){
		if ( ! freeLocomotive )
			return false;
		return freeWagonsSet.containsKey(wagonId);
	}
	
	protected boolean canDrop(String wagonId){
		return towed.equals(wagonId);			
	}
	
	protected boolean isOnStation(String wagonId){
		return onStationSet.containsKey(wagonId);
	}
	
	protected boolean isWagonLoaded(String wagonId){
		return wagons.get(wagonId).isLoaded();
	}
	
	//////////////////////////////////////////
	//    OPERATORS CHECK
	boolean canCouple(String wagonId){
		if ( ! canTake( wagonId ) )
			return false;
		return isOnStation(wagonId);	
	}
	
	boolean canPark(String wagonId){
		if ( ! ( usedRailways < StateFactory.getMAX_RAILWAYS() ) )
			return false;
		return canDrop(wagonId);
	}
	
	boolean canDetach(String wagonIDx, String wagonIDy){
		if ( ! canTake( wagonIDx ) )
			return false;
		if ( indexMap.get( wagonIDx )!=indexMap.get( wagonIDy ) )
			return false;										// not in the same railway
		return (posMap.get( wagonIDx )-1)==posMap.get( wagonIDy ); 	// attached to each other
	}
	
	boolean canAttach(String wagonIDx, String wagonIDy){
		if ( ! freeWagonsSet.containsKey( wagonIDy ) )
			return false;
		return canDrop(wagonIDx);
	}
	
	boolean canLoad(String wagonId){
		if (isWagonLoaded(wagonId))
			return false;		// already loaded
		return isOnStation(wagonId);
	}
	
	boolean canUnload(String wagonId){
		if (!isWagonLoaded(wagonId))	
			return false;		// already unloaded
		return isOnStation(wagonId);
	}
	
	
	////////////////////////
	// 
	State apply(Operator operator){
		return null;
	}
}

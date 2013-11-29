package mai.par.trains;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import mai.par.trains.operators.Operator;
import mai.par.trains.predicates.Predicate;

public class State {
	
	// predicates that apply
	private List<Predicate> predicateList;
	
	// state:
	Map<String, Wagon> wagons;		// all the wagons
	boolean freeLocomotive=true;	// is the locomotive free?
	int usedRailways=0;				// the railways in use				
	Map<String,Wagon> freeWagonsSet;// the wagons in front of each rail (maybe a Set to break simmetries)
	String towed;					// the wagon being towed, if any
	List<Stack<Wagon>> railways;	// instantiation of array of wagon stacks aren't allowed. Ordering seems important here, 
									// so i used List. Subject to change.   			
	Map<String, Wagon> onStationSet;// the wagons parked in the station 
	Map<String, Integer> indexMap;	// in what railway is every wagon, railways[index]
	Map<String, Integer> posMap;	// in what position is every wagon
	
	public State()
	{
		wagons = new HashMap<String, Wagon>();
		freeWagonsSet = new HashMap<String, Wagon>();
		towed = new String();
		railways = new ArrayList<Stack<Wagon>>();
		for(int i = 0; i < StateFactory.getMAX_RAILWAYS(); i++){
			railways.add(new Stack<Wagon>());
		}
		onStationSet = new HashMap<String, Wagon>();
		indexMap = new HashMap<String, Integer>();
		posMap = new HashMap<String, Integer>();
	}
	
	// The parameter clean, if set to false, will create the state with the wagons in the same state (loaded, predecessor)
	// otherwise will create copies of the last state
	public State(Map<String, Wagon> wagons2, Boolean clean)
	{
		this.wagons = new HashMap<String, Wagon>();
		if(clean)
		{
			for(Wagon wagon: wagons2.values())
			{
				Wagon w = new Wagon(wagon.getId()); 
				this.wagons.put(w.getId(), w);
			}
		}
		else
		{
			for(Wagon wagon: wagons2.values())
			{
				Wagon w = new Wagon(wagon.getId(), wagon.getPredecessor()); 
				w.setLoad(wagon.isLoaded());
				this.wagons.put(w.getId(), w);
			}
		}
		
		freeWagonsSet = new HashMap<String, Wagon>();
		towed = new String();
		railways = new ArrayList<Stack<Wagon>>();
		for(int i = 0; i < StateFactory.getMAX_RAILWAYS(); i++){
			railways.add(new Stack<Wagon>());
		}
		onStationSet = new HashMap<String, Wagon>();
		indexMap = new HashMap<String, Integer>();
		posMap = new HashMap<String, Integer>();
	}
	
	public void addWagon(String id)
	{
		wagons.put(id, new Wagon(id));
	}
		
	// TODO: add a tabu operator to make sure there are no loops (at distance 2: do-undo)
	//       also the operator will indicate that if a drop operator has been applied
	//       the next will be a take operator (or a load/unload)
	//		 if a take operator has been applied the next will be a drop
	
	// can the operators be applied???
	protected boolean canTake(String wagonId)
	{
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
	State apply(Operator operator)
	{
		return null;
	}
	
	public void applyPredicate(Predicate predicate)
	{
		
	}

	public void setWagons(Map<String, Wagon> wagons) 
	{
		this.wagons = wagons;
	}
	
	public Map<String, Wagon> getWagons() 
	{
		return this.wagons;
	}

	public void unloadWagon(String id1) {
		this.wagons.get(id1).unload();
	}
	
	public void loadWagon(String id1) {
		this.wagons.get(id1).load();
	}

	public void setToStation(String id1) {
		Wagon wagon = wagons.get(id1);
		wagon.setPredecessor(null);
		onStationSet.put(id1, wagon);
		indexMap.put(id1, usedRailways);
		posMap.put(id1, 0);
		railways.get(usedRailways).add(wagon);
		usedRailways++;
	}

	public void setWagonFree(String id1) {
		Wagon wagon = wagons.get(id1);
		freeWagonsSet.put(id1, wagon);
	}

	public void setInFrontOf(String id1, String id2) {
		Wagon wagon = wagons.get(id1);
		wagon.setPredecessor(id2);
		indexMap.put(id1, indexMap.get(id2));
		posMap.put(id1, posMap.get(id2)+1);
		railways.get(indexMap.get(id2)).add(wagon);
		
	}

	public void setFreeLocomotive() 
	{
		freeLocomotive = true;
		towed = null;
	}

	public void setWagonTowed(String id1) {
		freeLocomotive = false;
		towed = id1;
	}

	public List<Predicate> getPredicateList() {
		return predicateList;
	}

	public void setPredicateList(List<Predicate> predicateList) {
		this.predicateList = predicateList;
	}

	public void drawState() 
	{
		StringBuilder sb = new StringBuilder();
		for (Stack<Wagon> railway: railways)
		{
			sb.append("|");
			for (Wagon w : railway)
			{
				if(w.isLoaded())
				{
					sb.append("-(");
					sb.append(w.getId());
					sb.append(")");
				}
				else
				{
					sb.append("-");
					sb.append(w.getId());
				}
			}
			sb.append("\n");
		}
		if(!this.freeLocomotive)
		{
			sb.append("Loc[");
			sb.append(towed);
			sb.append("]");
		}
		else
		{
			sb.append("Loc[]\n");
		}
		System.out.println(sb.toString());
	}
}

package mai.par.trains;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import mai.par.trains.operators.Operator;
import mai.par.trains.operators.Stackable;
import mai.par.trains.predicates.Predicate;
import mai.par.trains.predicates.PredicateGroup;

public class State implements Stackable{
	
	// predicates that apply

	private PredicateGroup predicateGroup;
	
	// state:
	WagonMap wagons;				// all the wagons
	boolean freeLocomotive=true;	// is the locomotive free?
	int usedRailways=0;				// the railways in use				
	WagonMap freeWagonsSet;			// the wagons in front of each rail (maybe a Set to break simmetries)
	String towed;					// the wagon being towed, if any
	List<Stack<Wagon>> railways;	// instantiation of array of wagon stacks aren't allowed. Ordering seems important here, 
									// so i used List. Subject to change.   			
	WagonMap onStationSet;			// the wagons parked in the station 
	Map<String, Integer> indexMap;	// in what railway is every wagon, railways[index]
	Map<String, Integer> posMap;	// in what position is every wagon
	
	public State()
	{
		init();
		wagons = new WagonMap();	
	}
	
	// The parameter clean, if set to false, will create the state with the wagons in the same state (loaded, predecessor)
	// otherwise will create copies of the last state
	public State(Map<String, Wagon> wagons2, Boolean clean)
	{
		init();
		this.wagons = new WagonMap();
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
	}
	
	protected void init(){
		freeWagonsSet = new WagonMap();
		towed = new String();
		railways = new ArrayList<Stack<Wagon>>();
		for(int i = 0; i < StateFactory.getMAX_RAILWAYS(); i++){
			railways.add(new Stack<Wagon>());
		}
		onStationSet = new WagonMap();
		indexMap = new HashMap<String, Integer>();
		posMap = new HashMap<String, Integer>();
	}
	
	/**
	 * Returns the list of wagons that require a load/unload operation compared to the state parameter
	 * @param state state to compare to obtain the wagon list
	 * @return the list of Wagons that have a different load state compared to the state given as parameter 
	 */
	public WagonMap getWagonsRequiringLoadUnload(State state){
		WagonMap wagonRequiringLoadUnloadMap=new WagonMap();
		Wagon w;
		String id;
		for (Wagon wagon: state.getWagons().values()){
			id=wagon.getId();
			w=wagons.get(id);
			if (w.isLoaded()!=wagon.isLoaded())
				wagonRequiringLoadUnloadMap.put(id,w);
		}
		return wagonRequiringLoadUnloadMap;
	}
	
	public WagonMap getWagonsOnStation(WagonMap wagonList){
		// we only care about the Wagon id, not its load
		WagonMap wagonOnStationMap=new WagonMap();
		String id;
		for (Wagon wagon: wagonList.values()){
			id=wagon.getId();
			if (isOnStation(wagon.getId())){
				wagonOnStationMap.put(id, wagon);
			}
		}
		return wagonOnStationMap;
	}
	
	public List<Predicate> difference(List<Predicate> listPred){
		List<Predicate> diffListPred=new ArrayList<Predicate>();
		for (Predicate pred:listPred){
			if (!isCompliant(pred)){
				diffListPred.add(pred);
			}
		}
		return diffListPred;
	}
	
	public List<Predicate> difference(State state){
		PredicateGroup listPred=state.getPredicateGroup();
		return difference(listPred);
	}
	
	public void addWagon(String id){
		wagons.put(id, new Wagon(id));
	}
		
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
	
	protected boolean isTowed(String wagonId){
		return towed.equals(wagonId);
	}
	
	protected boolean isWagonLoaded(String wagonId){
		return wagons.get(wagonId).isLoaded();
	}
	
	protected boolean isWagonFree(String wagonId){
		return freeWagonsSet.containsKey( wagonId );
	}
	
	protected boolean isRailwayFree() {
		return usedRailways < StateFactory.getMAX_RAILWAYS();
	}
	
	protected boolean isOnSameRailway(String wagonIDx, String wagonIDy){
		return indexMap.get( wagonIDx )==indexMap.get( wagonIDy );
	}
	
	protected boolean isInFrontOf(String wagonIDx, String wagonIDy){
		if ( !isOnSameRailway(wagonIDx, wagonIDy) )
			return false;										// not in the same railway
		return (posMap.get( wagonIDx )-1)==posMap.get( wagonIDy ); 	// attached to each other
	}
	
	//////////////////////////////////////////
	//    OPERATORS CHECK
	boolean canCouple(String wagonId){
		if ( ! canTake( wagonId ) )
			return false;
		return isOnStation(wagonId);	
	}
	
	boolean canPark(String wagonId){
		if ( ! isRailwayFree() )
			return false;
		return canDrop(wagonId);
	}
	
	boolean canDetach(String wagonIDx, String wagonIDy){
		if ( !canTake( wagonIDx ) )
			return false;
		return isInFrontOf(wagonIDx, wagonIDy);
	}
	
	boolean canAttach(String wagonIDx, String wagonIDy){
		if ( ! isWagonFree(wagonIDy) )
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
	State apply(Operator operator) {
		return null; // TODO: complete, consider move to StateFactory
	}
	
	public boolean isCompliant(Predicate predicate) {
		// TODO: compliant with partially instantiated predicates? can this happen?
		String id1, id2;
		id1=predicate.getId1();
		switch (predicate.getPredicate()){
			case PR_EMPTY:
				return !isWagonLoaded(id1); 
			case PR_LOADED:
				return isWagonLoaded(id1);
			case PR_FREELOCOMOTIVE:
				return freeLocomotive;
			case PR_FREE:
				return isWagonFree(id1);
			case PR_INFRONTOF:
				id2=predicate.getId2();
				return isInFrontOf(id1, id2);
			case PR_ONSTATION:
				return isOnStation(id1);
			case PR_TOWED:
				return isTowed(id1);
			case PR_USEDRAILWAYS_NOTFULL: 
				return isRailwayFree();
			case PR_USEDRAILWAYS_DECREASE:
			case PR_USEDRAILWAYS_INCREASE:
			// should not appear
			break;		
		}
		return false; // default: false
	}

	public void setWagons(WagonMap wagons) {
		this.wagons = wagons;
	}
	
	public Map<String, Wagon> getWagons() {
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

	public void setFreeLocomotive() {
		freeLocomotive = true;
		towed = null;
	}

	public void setWagonTowed(String id1) {
		freeLocomotive = false;
		towed = id1;
	}

	public PredicateGroup getPredicateGroup() {
		return predicateGroup;
	}

	public void setPredicateGroup(PredicateGroup predicateList) {
		this.predicateGroup = predicateList;
	}

	public String toString(){
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
			sb.append("Loc[]");
		}
		return sb.toString();
	}
	
	public void drawState() 
	{
		
		System.out.println(toString()+"\n");
	}
}

package mai.par.trains;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import mai.par.trains.operators.Operator;
import mai.par.trains.operators.OperatorLoad;
import mai.par.trains.operators.OperatorUnload;
import mai.par.trains.predicates.Predicate;
import mai.par.trains.predicates.PredicateGroup;
import mai.par.trains.predicates.TrainPredicate;

public class State implements Stackable{
	
	// predicates that apply
	private PredicateGroup predicateGroup;
	
	// operator
	private Operator operator=null;	// the operator that brough to this state
									// null for initial and final state
	
	// state:
	WagonMap wagons;				// all the wagons
	boolean freeLocomotive=true;	// is the locomotive free?
	int usedRailways=0;				// the railways in use				
	WagonMap freeWagonsSet;			// the wagons in front of each rail (maybe a Set to break simmetries)
	String towed;					// the wagon being towed, if any
	List<Stack<Wagon>> railways;	// instantiation of array of wagon stacks aren't allowed. Ordering seems important here, 
									// so I used List. Subject to change.   			
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
	public State(WagonMap wagons2, Boolean clean)
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
	
	public State(State state){
		this(state.getWagons(),false);
		predicateGroup=new PredicateGroup(state.getPredicateGroup());
		freeLocomotive=state.isFreeLocomotive();
		indexMap=new HashMap<String, Integer>(state.indexMap);
		posMap=new HashMap<String, Integer>(state.posMap);
		towed=state.towed;
		usedRailways=state.usedRailways;
		operator=state.operator;
		// the structures are used including the wagons created for this state 
		// to avoid strange propagation issues if something is modified in other state
		freeWagonsSet=new WagonMap(state.freeWagonsSet, wagons);
		onStationSet=new WagonMap(state.onStationSet, wagons);
		copyRailways(state.railways, wagons);
	}
	
	private void copyRailways(List<Stack<Wagon>> original, WagonMap elements){
		railways=new ArrayList<Stack<Wagon>>();
		Stack<Wagon> newStack;
		String id;
		for (Stack<Wagon> stack: original){
			newStack=new Stack<Wagon>();
			for (Wagon w: stack){
				id=w.getId();
				newStack.push(elements.get(id));
			}
			railways.add(newStack);
		}
	}
	
	protected void init(){
		freeWagonsSet = new WagonMap();
		towed = "";
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
	//    OPERATORS CHECK (not based on PREDICATES but on the STATE: NOT USED)
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
	boolean canApply(Operator operator){
		boolean can=true;
		PredicateGroup precondPredGroup=operator.getPrecondPredicate();
		for (Predicate pred: precondPredGroup){
			can &= isCompliant(pred);
			//System.out.println(can+" "+pred);
		}
		return can;
		/*
		String id=operator.getID1();
		String id2=operator.getID2();
		switch (operator.getOperator()) {
		case OP_ATTACH:
			can=canAttach(id,id2);
			break;
		case OP_COUPLE:
			can=canCouple(id);
			break;
		case OP_DETACH:
			can=canDetach(id,id2);
			break;
		case OP_LOAD:
			can=canLoad(id);
			break;
		case OP_PARK:
			can=canPark(id);
			break;
		case OP_UNLOAD:
			can=canUnload(id);
			break;
		}
		*/
	}
	
	public State apply(Operator operator) {
		State state=null;
		if (canApply(operator)) {
			state=new State(this);
			state.applyADD(operator);
			state.applyDEL(operator);
		}
		return state; 
	}

	private void applyDEL(Operator operator) {
		PredicateGroup delPredGroup=operator.getDelPredicate();
		Predicate p;
		for (Predicate pred:delPredGroup){
			p=predicateGroup.contains(pred);
			if (p!=null){
				predicateGroup.remove(p);  // remove from the DEL list
			} else
				System.err.println("ERROR: predicate not found");
		}
	}

	private void applyADD(Operator operator) {
		PredicateGroup addPredGroup=operator.getAddPredicate();
		for (Predicate pred:addPredGroup){
			applyADD(pred);		// apply the ADD list
		}
	}
	
	protected void applyADD(Predicate pred){
		// the predicates will be the ones in the add list
		// some of the predicates will not appear
		// some can be ignored because will be "obtained" indirectly when applying others
		String id=pred.getId1();
		boolean add=true;
		switch (pred.getPredicate()){
		case PR_EMPTY:
			unloadWagon(id);
			break;
		case PR_INFRONTOF:
			String id2=pred.getId2();
			setInFrontOf(id, id2);
			break;
		case PR_LOADED:
			loadWagon(id);
			break;
		case PR_ONSTATION:
			setToStation(id);
			break;
		case PR_TOWED:
			setWagonTowed(id);
			break;
		case PR_FREE:
			setFree(id);
			break;
		case PR_FREELOCOMOTIVE:
			setFreeLocomotive();
			break;
		case PR_USEDRAILWAYS_DECREASE:
		case PR_USEDRAILWAYS_INCREASE:
			add=false;
			break;
		case PR_USEDRAILWAYS_NOTFULL:
			// will not appear in the add list
			break;
		}
		if (add) predicateGroup.add(pred);
	}
	
	public Operator accomplish(Predicate predicate){
		// we can assume that the predicate is not satisfied
		Operator operator=null;
		TrainPredicate tp=predicate.getPredicate();
		String id=predicate.getId1();
		switch(tp){
		///////////////LOAD/UNLOAD///////////////////////////////////////////////
		// Our order should guarantee that when this predicate is tried to be accomplished
		// the wagon is onstation, if not it will raise from operator checking
		case PR_EMPTY:
			operator=new OperatorUnload(id);
			break;
		case PR_LOADED:
			operator=new OperatorLoad(id);
			break;
		//////////////////////////////////////////////////////////////////////
		case PR_FREE:
			break;
		case PR_TOWED:
			break;
		case PR_FREELOCOMOTIVE:
			break;
		case PR_INFRONTOF:
			break;
		case PR_ONSTATION:
			break;
		case PR_USEDRAILWAYS_NOTFULL:
			break;
		case PR_USEDRAILWAYS_DECREASE:
		case PR_USEDRAILWAYS_INCREASE:
			// should not occur here
			break;
		}
		return operator;
	}
	
	public boolean isCompliant(State state){
		return isCompliant(state.getPredicateGroup());
	}
	
	public boolean isCompliant(PredicateGroup predicateGroup){
		for (Predicate pred:predicateGroup){
			if (!isCompliant(pred)){
				System.out.println("ERROR: Predicate not compliant in Group");
				System.out.println(pred);
				return false;
			}
		}
		return true;
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
				return isFreeLocomotive();
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

	public boolean isFreeLocomotive(){
		return freeLocomotive;
	}
	
	public void setWagons(WagonMap wagons) {
		this.wagons = wagons;
	}
	
	public WagonMap getWagons() {
		return this.wagons;
	}

	public void unloadWagon(String id1) {
		this.wagons.get(id1).unload();
	}
	
	public void loadWagon(String id1) {
		this.wagons.get(id1).load();
	}
	
	///////////////////////////////////////////////////////////////////////////
	// used to create the state from the initial predicates on the text file
	public void setToStation(String id1) {
		int railwayNumber;
		if (usedRailways<StateFactory.getMAX_RAILWAYS()){
			Wagon wagon = wagons.get(id1);
			wagon.setPredecessor(null);
			onStationSet.put(id1, wagon);
			railwayNumber=getEmptyRailway(); // we take the first empty railway, knowing that there is one because of the if
			indexMap.put(id1, railwayNumber); 
			posMap.put(id1, 0);
			railways.get(railwayNumber).add(wagon); 
			usedRailways++;
		}
	}

	private int getEmptyRailway(){
		int number=0;
		while (railways.get(number).size()>0)
			number++;
		return number;
	}
	
	public void setFree(String id){
		Wagon wagon=wagons.get(id);
		freeWagonsSet.put(id,wagon);
	}
	
	public void setWagonFree(String id1) {
		Wagon wagon = wagons.get(id1);
		freeWagonsSet.put(id1, wagon);
	}

	public void setInFrontOf(String id1, String id2) {
		Wagon wagon = wagons.get(id1);
		wagon.setPredecessor(id2);
		int railwayPos=indexMap.get(id2);
		indexMap.put(id1, railwayPos );
		posMap.put(id1, posMap.get(id2)+1);
		railways.get(railwayPos).add(wagon);
	}

	public void setFreeLocomotive() {
		freeLocomotive = true;
		towed = "";
	}

	public void setWagonTowed(String id1) {
		freeLocomotive = false;
		towed = id1;
		Integer railwayNumber=indexMap.get(id1);
		if (railwayNumber!=null){ // the wagon is on the system
			int railwayPosition=posMap.get(id1);
			Stack<Wagon> railway=railways.get(railwayNumber);
			Wagon wagon=railway.pop(); // remove
			// the wagon is not removed from the Free Set
			if (railwayPosition==0) {
				usedRailways--;			// if last wagon, release one railway
			} else {
				wagon=railway.peek();
				freeWagonsSet.put(wagon.getId(),wagon); // if not last wagon, set free
			}
		}
	}
	///////////////////////////////////////////////////////////////////
	
	
	
	public PredicateGroup getPredicateGroup() {
		return predicateGroup;
	}

	public void setPredicateGroup(PredicateGroup predicateList) {
		this.predicateGroup = predicateList;
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		for (Stack<Wagon> railway: railways) {
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

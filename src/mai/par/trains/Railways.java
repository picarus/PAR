package mai.par.trains;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import mai.par.trains.predicates.Predicate;
import mai.par.trains.predicates.PredicateFactory;
import mai.par.trains.predicates.PredicateGroup;

@SuppressWarnings("serial")
public class Railways extends ArrayList<Railway> {
	
	public Railways(){
		super();
		for(int i = 0; i < StateFactory.getMAX_RAILWAYS(); i++){
			add(new Railway());
		}
	}
	
	public Railways(Railways original, WagonMap elements){
		Railway newStack;
		String id;
		for (Railway stack: original)
		{
			newStack=new Railway(stack.done);
			for (Wagon w: stack){
				id=w.getId();
				newStack.push(elements.get(id));
			}
			add(newStack);
		}
	}
	
	public List<PredicateGroup> getWagonsPosition(){
		List<PredicateGroup> lolPredicate=new ArrayList<PredicateGroup>();
		PredicateGroup pg;
		Predicate p;
		Wagon wagon;
		String idWagon="",idWagon2;
		Iterator<Wagon> itWagon;
		for (Railway stackWagon :this){
			pg = new PredicateGroup();
			itWagon=stackWagon.iterator();
			if (itWagon.hasNext()){
				wagon=itWagon.next();
				idWagon=wagon.getId();
				p=PredicateFactory.createPredicateOnStation(idWagon);
				pg.add(p);
			}
			while (itWagon.hasNext()){
				wagon=itWagon.next();
				idWagon2=wagon.getId();
				p=PredicateFactory.createPredicateInFrontOf(idWagon2, idWagon);
				pg.add(p);
				idWagon=idWagon2;
			}
			Collections.reverse(pg);
			lolPredicate.add(pg);
		}
		
		Collections.sort(lolPredicate, new Comparator<PredicateGroup>(){
			public int compare(PredicateGroup pg1, PredicateGroup pg2){
				return pg1.size()-pg2.size();
			}
		});
		
		//System.out.println(lolPredicate);
		
		return lolPredicate;
	}
		
}

package mai.par.parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import mai.par.trains.State;
import mai.par.trains.StateFactory;
import mai.par.trains.Wagon;
import mai.par.trains.predicates.Predicate;
import mai.par.trains.predicates.PredicateFactory;

public class TrainParser {
	
	public static void tokenize(String input)
	{
		int idxWagons=input.indexOf(TrainParseConstants.ST_WAGONS);
		int idxInitialState=input.indexOf(TrainParseConstants.ST_INITIALSTATE);
		int idxFinalState=input.indexOf(TrainParseConstants.ST_FINALSTATE);
		
		String strWagons=input.substring(idxWagons+TrainParseConstants.ST_WAGONS.length(), idxInitialState-1);
		String strInitialState=input.substring(idxInitialState+TrainParseConstants.ST_INITIALSTATE.length(), idxFinalState-1);
		String strFinalState=input.substring(idxFinalState+TrainParseConstants.ST_FINALSTATE.length());
		
		StateFactory.wagons = readStation(strWagons);
		StateFactory.initialState = StateFactory.createState(readState(strInitialState));
		StateFactory.finalState = StateFactory.createState(readState(strFinalState));		
	}
	
	protected static Map<String, Wagon> readStation(String strWagons){
		System.out.println(strWagons);
		StringTokenizer strTkn=new StringTokenizer(strWagons, ",");
		String str;
		HashMap<String, Wagon> wagons = new HashMap<String, Wagon>();
		while (strTkn.hasMoreElements()){
			str=strTkn.nextToken();
			wagons.put(str, new Wagon(str));
			System.out.println(str);
		}
		return wagons;
	}
	
	private static String removeParentheses(String str){
		int idx=str.indexOf(TrainParseConstants.PARENTHESES);
		str=str.substring(idx+1, str.length()-1);
		return str;
	}
	
	protected static String readOneParameter(String str){
		str=removeParentheses(str);
		return str;
	}
	
	protected static String[] readTwoParameters(String str){
		String tkn;
		String params[] = new String[2];
		str=removeParentheses(str);
		StringTokenizer strTkn=new StringTokenizer(str,",");
		int i = 0;
		while (strTkn.hasMoreElements()){
			tkn=strTkn.nextToken();
			params[i] = tkn;
		}
		return params;
	}
	
	protected static ArrayList<Predicate> readState(String state){
		System.out.println(state);
		StringTokenizer strTkn=new StringTokenizer(state, TrainParseConstants.SEPARATORS);
		String str;
		ArrayList<Predicate> predicates = new ArrayList<Predicate>();
		while (strTkn.hasMoreElements()){
			str=strTkn.nextToken();
			System.out.println(str);
			
			if (str.contains(TrainParseConstants.PR_FREELOCOMOTIVE)) {
				predicates.add(PredicateFactory.createPredicateFreeLocomotive());
			} else if (str.contains(TrainParseConstants.PR_ONSTATION)){
				String param = readOneParameter(str);
				predicates.add(PredicateFactory.createPredicateOnStation(param));
			} else if (str.contains(TrainParseConstants.PR_FREE)) {
				String param = readOneParameter(str);
				predicates.add(PredicateFactory.createPredicateFree((param)));
			} else if (str.contains(TrainParseConstants.PR_LOADED)) {
				String param = readOneParameter(str);
				predicates.add(PredicateFactory.createPredicateLoaded((param)));
			} else if (str.contains(TrainParseConstants.PR_EMPTY)) {
				String param = readOneParameter(str);
				predicates.add(PredicateFactory.createPredicateEmpty((param)));
			} else if (str.contains(TrainParseConstants.PR_TOWED)) {
				String param = readOneParameter(str);
				predicates.add(PredicateFactory.createPredicateTowed((param)));
			} else if (str.contains(TrainParseConstants.PR_USEDRAILWAYS)) {
				String param = readOneParameter(str);
				try
				{
					int n = Integer.valueOf(param);
					predicates.add(PredicateFactory.createPredicateUsedRaildways((n)));
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			} else if (str.contains(TrainParseConstants.PR_INFRONTOF)) {
				String[] params = readTwoParameters(str);
				predicates.add(PredicateFactory.createPredicateInFrontOf(params[0], params[1]));
			} 
		}
		return predicates;
	}

}

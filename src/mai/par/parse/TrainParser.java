package mai.par.parse;

import java.util.StringTokenizer;

public class TrainParser {
	
	public static void tokenize(String input){
		int idxWagons=input.indexOf(TrainParseConstants.ST_WAGONS);
		int idxInitialState=input.indexOf(TrainParseConstants.ST_INITIALSTATE);
		int idxFinalState=input.indexOf(TrainParseConstants.ST_FINALSTATE);
		
		String strWagons=input.substring(idxWagons+TrainParseConstants.ST_WAGONS.length(), idxInitialState-1);
		String strInitialState=input.substring(idxInitialState+TrainParseConstants.ST_INITIALSTATE.length(), idxFinalState-1);
		String strFinalState=input.substring(idxFinalState+TrainParseConstants.ST_FINALSTATE.length());
		
		readStation(strWagons);
		readState(strInitialState);
		readState(strFinalState);
		
	}
	
	protected static void readStation(String strWagons){
		System.out.println(strWagons);
		StringTokenizer strTkn=new StringTokenizer(strWagons, ",");
		String str;
		while (strTkn.hasMoreElements()){
			str=strTkn.nextToken();
			System.out.println(str);
		}
	}
	
	private static String removeParentheses(String str){
		int idx=str.indexOf(TrainParseConstants.PARENTHESES);
		str=str.substring(idx+1, str.length()-1);
		return str;
	}
	
	protected static void readOneParameter(String str){
		str=removeParentheses(str);
		System.out.println(str);
	}
	
	protected static void readTwoParameters(String str){
		String tkn;
		str=removeParentheses(str);
		StringTokenizer strTkn=new StringTokenizer(str,",");
		while (strTkn.hasMoreElements()){
			tkn=strTkn.nextToken();
			System.out.println(tkn);
		}
		
	}
	
	protected static void readState(String state){
		System.out.println(state);
		StringTokenizer strTkn=new StringTokenizer(state, TrainParseConstants.SEPARATORS);
		String str;
		while (strTkn.hasMoreElements()){
			str=strTkn.nextToken();
			System.out.println(str);
			
			if (str.contains(TrainParseConstants.PR_FREELOCOMOTIVE)) {
				// nor parameters
			} else if (str.contains(TrainParseConstants.PR_ONSTATION)){
				readOneParameter(str);
			} else if (str.contains(TrainParseConstants.PR_FREE)) {
				readOneParameter(str);
			} else if (str.contains(TrainParseConstants.PR_LOADED)) {
				readOneParameter(str);
			} else if (str.contains(TrainParseConstants.PR_EMPTY)) {
				readOneParameter(str);
			} else if (str.contains(TrainParseConstants.PR_TOWED)) {
				readOneParameter(str);
			} else if (str.contains(TrainParseConstants.PR_ONSTATION)) {
				readTwoParameters(str);
			} else if (str.contains(TrainParseConstants.PR_INFRONTOF)) {
				readTwoParameters(str);
			} 
		}
	}

}

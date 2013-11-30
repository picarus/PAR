package mai.par.trains.predicates;

import mai.par.parse.TrainParseConstants;

public enum TrainPredicate {
	PR_EMPTY, PR_FREE, 
	PR_FREELOCOMOTIVE, PR_INFRONTOF, PR_LOADED,
	PR_ONSTATION, PR_TOWED, 
	PR_USEDRAILWAYS_NOTFULL,PR_USEDRAILWAYS_INCREASE, PR_USEDRAILWAYS_DECREASE;
	
	public String toString() {
		switch(this){
		case PR_EMPTY:
			return TrainParseConstants.PR_EMPTY;
		case PR_FREE:
			return TrainParseConstants.PR_FREE;
		case PR_FREELOCOMOTIVE:
			return TrainParseConstants.PR_FREELOCOMOTIVE;
		case PR_INFRONTOF:
			return TrainParseConstants.PR_INFRONTOF;
		case PR_LOADED:
			return TrainParseConstants.PR_LOADED;
		case PR_ONSTATION:
			return TrainParseConstants.PR_ONSTATION;
		case PR_TOWED:
			return TrainParseConstants.PR_TOWED;
		case PR_USEDRAILWAYS_NOTFULL:
			return "USEDRAILWAYS_NOTFULL";
		case PR_USEDRAILWAYS_DECREASE:
			return "USEDRAILWAYS_DECREASE";
		case PR_USEDRAILWAYS_INCREASE:
			return "USEDRAILWAYS_INCREASE";
		}
		return "PREDICATE NOT FOUND";
	};
}

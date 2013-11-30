package mai.par.parse;

public class TrainParseConstants {
	
	// PREDICATES	
	public static final String PR_ONSTATION="ON-STATION";
	public static final String PR_INFRONTOF="IN-FRONT-OF";
	public static final String PR_FREE="FREE";
	public static final String PR_FREELOCOMOTIVE="FREE-LOCOMOTIVE";
	public static final String PR_TOWED="TOWED";
	public static final String PR_USEDRAILWAYS="USED-RAILWAYS";
	public static final String PR_LOADED="LOADED";
	public static final String PR_EMPTY="EMPTY";
	
	// OPERATORS
	// free locomotive operators
	static final String OP_COUPLE="COUPLE";
	static final String OP_ATTACH="ATTACH";
	// busy locomotive operators
	static final String OP_PARK="PARK";
	static final String OP_DETACH="DETACH";
	// load - unload
	static final String OP_LOAD="LOAD";
	static final String OP_UNLOAD="UNLOAD";
	
	// STATE HEADERS
	static final String ST_WAGONS="Wagons=";
	static final String ST_INITIALSTATE="Initial_state=";
	static final String ST_FINALSTATE="Goal_state=";
	
	// SEPARATOR
	static final String SEPARATORS=";\n";	
	static final String PARENTHESES="(";
}

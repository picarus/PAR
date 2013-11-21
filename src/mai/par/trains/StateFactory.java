package mai.par.trains;

public class StateFactory {

	static private int MAX_RAILWAYS=3;
	
	static State initialState=null;
	static State finalState=null;
	
	State currentState=null;
	
	public static void setMAX_RAILWAYS(int max_RAILWAYS) {
		if ((initialState==null) && (finalState==null))
			MAX_RAILWAYS = max_RAILWAYS;
	}
	
	public static int getMAX_RAILWAYS() {
		return MAX_RAILWAYS;
	}
	
	protected State createState(){
		return null;
	}
	
}

package mai.par.trains.operators;

public enum TrainOperator {
		OP_COUPLE,
		OP_ATTACH,
		OP_PARK,
		OP_DETACH,
		OP_LOAD,
		OP_UNLOAD;
		
	public String toString(){
		switch(this){
		case OP_ATTACH:
			return "ATTACH";
		case OP_COUPLE:
			return "COUPLE";
		case OP_DETACH:
			return "DETACH";
		case OP_LOAD:
			return "LOAD";
		case OP_PARK:
			return "PARK";
		case OP_UNLOAD:
			return "UNLOAD";		
		}
		return "OP NOT FOUND";
	}
}

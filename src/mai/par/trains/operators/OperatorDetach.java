package mai.par.trains.operators;


public class OperatorDetach extends OperatorTake {
	public OperatorDetach(String id1, String id2) {
		super(TrainOperator.OP_DETACH,id1);
		this.id2=id2;
	}
}

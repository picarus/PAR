package mai.par.trains.operators;


public class OperatorCouple extends OperatorTake {
	public OperatorCouple(String id1) {
		super(TrainOperator.OP_COUPLE,id1);
		// used railways is not a real precondition. It is true for any n.
	}
}

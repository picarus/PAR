package mai.par.trains.operators;


public class OperatorAttach extends OperatorDrop {

	OperatorAttach(String id1, String id2) {
		super(TrainOperator.OP_ATTACH, id1);
		this.id2=id2;
	}

}

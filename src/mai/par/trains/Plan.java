package mai.par.trains;

import java.util.ArrayList;

import mai.par.trains.operators.Operator;


@SuppressWarnings("serial")
public class Plan extends ArrayList<Operator>{
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for(Operator op : this)
		{
			sb.append(op.toString());
		}
		return sb.toString(); 
	}

}

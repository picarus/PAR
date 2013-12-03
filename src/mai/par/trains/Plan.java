package mai.par.trains;

import java.util.ArrayList;
import java.util.Collections;

import mai.par.trains.operators.Operator;


@SuppressWarnings("serial")
public class Plan extends ArrayList<Operator>{
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		ArrayList<Operator> copy=new ArrayList<Operator>(this);
		Collections.reverse(copy);
		
		for(Operator op : copy)
		{
			sb.append(op);
		}
		return sb.toString(); 
	}

}

package mai.par.trains;

import java.util.Stack;

public class Railway extends Stack<Wagon> implements Comparable<Railway>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Boolean done = false;
	
	public Railway()
	{
		super();
	}
	
	public Railway(Boolean done)
	{
		super();
		this.done = done;
	}

	@Override
	public int compareTo(Railway o) 
	{
		if(this.size() < o.size())
			return -1;
		else if(this.size() == o.size())
			return 0;
		else
			return 1;
	}
}

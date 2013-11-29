package mai.par.trains;

public class Wagon {
	
	private String id;
	private boolean loaded;
	
	public boolean isLoaded(){
		return loaded;
	}
	
	public void load(){
		loaded=true;
	}
	
	public void unload(){
		loaded=false;
	}
	
	public void setLoad(Boolean loaded)
	{
		this.loaded = loaded;
	}
	
	public String getPredecessor() {
		return predecessor;
	}

	public void setPredecessor(String predecessor) {
		this.predecessor = predecessor;
	}

	public String getId() {
		return id;
	}

	private String predecessor;
	
	public Wagon(String id){
		this(id,"");
	}
	
	public Wagon(String id, String predecessor){
		this.id=id;
		this.predecessor=predecessor;
	}
	
	public String toString()
	{
		if(loaded)
			return "("+id+")";
		else
			return id;
	}

}

package mai.par;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import mai.par.parse.TrainParser;
import mai.par.trains.LinealPlanner;
import mai.par.trains.State;
import mai.par.trains.StateFactory;


public class Main {

	static String file = "sussman.txt"; // TODO: get file as parameter in args
	 
	public static void main(String[] args) 
	{
		if(args.length > 1)
		{
			if(new String("-t").equalsIgnoreCase(args[0]))
			{
				file = args[1];
			}	
		}
		String s;
		try 
		{
			Log.init(file);
			StateFactory.initialize();
			s = readProblemFile();
			TrainParser.tokenize(s);
			
			Log.log("Initial State: \n " + StateFactory.initialState.toString());
			Log.log(StateFactory.initialState.getPredicatesAsString());
			StateFactory.initialState.drawState();
			Log.log("");
			Log.log("Final State: \n" + StateFactory.finalState.toString());
			Log.log(StateFactory.finalState.getPredicatesAsString());
			StateFactory.finalState.drawState();
			
			LinealPlanner.init(StateFactory.initialState, StateFactory.finalState);
			LinealPlanner.createPlan();
			
			Log.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String readProblemFile() throws IOException
	{
		Path path = Paths.get("./tests/" + file);
	    List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
	    StringBuilder sb = new StringBuilder();
	    for(String line:lines){
	    	sb.append(line);
	    	sb.append("\n");
	    }
		return sb.toString();
	}

}

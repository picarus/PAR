package mai.par;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import mai.par.parse.TrainParser;
import mai.par.trains.StateFactory;

public class Main {

	static String file = "./problem.txt";
	 
	
	public static void main(String[] args) 
	{
		String s;
		try {
			StateFactory.initialize();
			s = readProblemFile();
			TrainParser.tokenize(s);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String readProblemFile() throws IOException
	{
		Path path = Paths.get(file);
	    List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
	    StringBuilder sb = new StringBuilder();
	    for(String line:lines){
	    	sb.append(line);
	    	sb.append("\n");
	    }
		return sb.toString();
	}

}

package mai.par;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Log 
{
	private static BufferedWriter br;
	
	public static void init(String filename)
	{
		Path path = Paths.get("./log/"+filename);
		try {
			path.toFile().createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			br = Files.newBufferedWriter(path, Charset.forName("UTF-8"), new OpenOption[] {StandardOpenOption.WRITE});
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void log(String message)
	{
		try {
			br.write(message);
			br.write("\n");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
        
	}
	
	public static void close()
	{
		try 
		{
			br.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}


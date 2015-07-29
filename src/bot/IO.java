package bot;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class IO 
{

	/**
	 * Scrive un array di stringhe in un file esterno(append)
	 * @param filename, il nome del file
	 * @param messageList
	 * @throws IOException
	 */
	public static void writeOUT(String filename, ArrayList<Message> messageList) throws IOException
	{
		  BufferedWriter outputWriter = null;
		  outputWriter = new BufferedWriter(new FileWriter(filename, true));
		  for (int i = 0; i < messageList.size(); i++) 
		  	{
			  	outputWriter.write(messageList.get(i).toString());
			  	outputWriter.newLine();
		  	}
		  	outputWriter.flush();  
		  	outputWriter.close();  
	}
	

	/**
	 * Scrive una String in un file esterno(append)
	 * @param filename, il nome del file
	 * @param messageList
	 * @throws IOException
	 */
	public static void writeOUT(String filename, String log) throws IOException
		{
			BufferedWriter outputWriter = null;
			outputWriter = new BufferedWriter(new FileWriter(filename, true));
			outputWriter.write(log);
			outputWriter.newLine();
			outputWriter.flush();  
			outputWriter.close();  
		}
}

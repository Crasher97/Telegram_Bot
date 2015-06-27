package bot;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class IO 
{

	/**
	 * Scrive un array di stringhe un file esterno
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
	 * Carica comandi dal file commands all'interno di addons
	 * @return ArrayList<String>, contenente la lista degli addons disponibili
	 */
	public static ArrayList<String> readCommands()
	{
		try
		{
			ArrayList<String> commands = new ArrayList<String>();
			BufferedReader br = new BufferedReader(new FileReader("addons//commands.cfg"));
			String command;
			while((command = br.readLine()) != null)
			{
					commands.add(command);
			}
			br.close();
			return commands;
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			System.err.println("comandi non caricati");
			Sender.sendMessage(84985065, "comandi non caricati");
			return new ArrayList<String>();
		}
	}
}

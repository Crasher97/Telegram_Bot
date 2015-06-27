package bot;
import java.util.ArrayList;

public class Identifier 
{
	private static ArrayList<String> commandList = new ArrayList<String>();
	
	/**
	 * addCommand - aggiunge il comando
	 * @param command
	 * @return true se il comando è stato aggiunto
	 */
	public static boolean addCommand(String command)
	{
		if(command!=null && command!="")
		{
			Identifier.commandList.add(command);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Ritorna la lista di comandi
	 * @return ArrayList<string>, la lista di comandi.
	 */
	public static ArrayList<String> getCommandList()
	{
		return commandList;
	}
	
	/**
	 * Controlla se un comando è presente all'interno dell'array
	 * @param String command
	 * @return true se il comando è presente
	 */
	public static boolean contains(String command)
	{
		boolean trovato = false;
		for(String trueCommand : Identifier.commandList)
		{
			if(trueCommand.equals(command))
			{
				trovato = true;
			}
		}
		return trovato;
	}
	
	/**
	 * Carica i comandi allo startup del programma
	 * @return true se i comandi sono stati caricati
	 */
	public static void loadCommands()
	{
		Identifier.commandList = IO.readCommands();
	}
	
	/**
	 * Controlla se il testo del messaggio è un comado
	 * @param msg, istanza dell'oggetto messaggio
	 * @return true se il testo è un comando
	 */
	public static boolean checkCommand(Message msg)
	{
		if(msg.getText().charAt(0)=='/')
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Fa eseguire il comando dalla classe Executer
	 * @param msg
	 * @return true se è stato eseguito
	 */
	public static boolean exeCommand(Message msg)
	{
		Executer.execute(msg);
		return true;
	}
}

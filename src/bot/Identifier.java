package bot;
import addons.Commands;
public class Identifier 
{
	
	
	/**
	 * Controlla se un comando è presente all'interno dell'array
	 * @param String command
	 * @return true se il comando è presente
	 */
	public static boolean contains(String command)
	{
		return Commands.commandExist(command);
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

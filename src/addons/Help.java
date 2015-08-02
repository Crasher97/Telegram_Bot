package addons;

import java.util.HashMap;

import bot.Message;
import bot.Sender;

public class Help
{
	private static HashMap<String, String> helps = new HashMap<String, String>();
	//Per andare a capo usare %0A nel messaggio
	
	/**
	 * Aggiunge il comando help alla lista dei comandi
	 */
	public static void load()
	{
		Commands.addCommand(new Command("help", "Help", "helpRequest"));
	}
	
	/**
	 * Aggiunge un help
	 * 
	 * @param command Comando di cui aggiungere l'help
	 * @param help Contenuto dell'help
	 */
	public static void addHelp(String command, String help)
	{
		helps.put(command, help);
	}
	
	/**
	 * Gestisce una richiesta di help e risponde con il contenuto, se viene lanciato solo "help" fa la lista dei comandi disponibili
	 * @param message Messaggio da cui prendere l'argomento e il sender id
	 */
	public static void helpRequest(Message message)
	{
		String args[] = message.getText().split(" ");
		if(args.length == 2)
		{
			Sender.sendMessage((int) message.getSender_id(), helps.get(args[1]));
		}
		else 
			{
				Sender.sendMessage((int) message.getSender_id(), Commands.getCommands().keySet().toString());
			}
	}
}

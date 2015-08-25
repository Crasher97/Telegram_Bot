package addons;

import java.util.HashMap;

import bot.Log;
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
		Commands.addCommand(new Command("help", "addons.Help", "helpRequest"));
		addHelp("help", "Do you need some help?");

		Commands.addCommand(new Command("start", "addons.Help", "startRequest"));
	}
	
	/**
	 * Aggiunge un help
	 * 
	 * @param command Cgaggiaomando di cui aggiungere l'help
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
			Sender.sendMessage( message.getSender_id(), helps.get(args[1]));
		}
		else 
			{
				Sender.sendMessage(message.getSender_id(), Commands.getCommands().keySet().toString());
			}
	}
	
	/**
	 * Gestisce una richiesta di help dalla console e risponde con il contenuto, se viene lanciato solo "help" fa la lista dei comandi disponibili
	 */
	public static void helpRequest(String args[])
	{
		if(args.length == 1)
		{
			Log.info(helps.get(args[0]));
		}
		else 
			{
				Log.info(Commands.getCommands().keySet().toString());
			}
	}
	
	/**
	 * It starts when people start chat with the bot
	 * @param message
	 */
	public static void startRequest(Message message)
	{
		Sender.sendMessage( message.getSender_id(), "This bot is in development, please stand by");
	}
}

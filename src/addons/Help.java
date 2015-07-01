package addons;

import java.util.HashMap;

import bot.Message;
import bot.Sender;

public class Help
{
	private static HashMap<String, String> helps = new HashMap<String, String>();
	//Per andare a capo usare %0A nel messaggio
	public static void load()
	{
		Commands.addCommand(new Command("help", "Help", "helpRequest"));
	}
	
	public static void addHelp(String command, String help)
	{
		helps.put(command, help);
	}
	
	public static void helpRequest(Message message)
	{
		String args[] = message.getText().split(" ");
		if(args.length == 2)
		{
			Sender.sendMessage((int) message.getSender_id(), helps.get(args[1]));
		}
		else Sender.sendMessage((int) message.getSender_id(), "TODO Lista dei comandi");
		
	}
}

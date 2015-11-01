package addons;

import java.util.HashMap;
import java.util.Iterator;

import bot.log.Log;
import bot.Message;
import bot.functions.Sender;

public class Help
{
	private static HashMap<String, String> helps = new HashMap<String, String>();
	//Per andare a capo usare %0A nel messaggio

	/**
	 * Add the command help
	 */
	public static void load()
	{
		Commands.addCommand(new Command("help", "addons.Help", "helpRequest"));
		addHelp("help", "Do you need some help?");
		Commands.addCommand(new Command("start", "addons.Help", "startRequest"));
	}

	/**
	 * Add a help
	 *
	 * @param command Comando di cui aggiungere l'help
	 * @param help    Content of the help
	 */
	public static void addHelp(String command, String help)
	{
		helps.put(command, help);
	}

	/**
	 * Handle a request and reply with the content, if only "help" is called it make a list of the existing command
	 *
	 * @param msg Message received
	 */
	public static void helpRequest(Message msg)
	{
		String args[] = msg.getText().split(" ");
		if (args.length == 2)
		{
			Sender.sendMessage(msg.getSender_id(), helps.get(args[1]));
		}
		else
		{
			HashMap<String,Command> commandList = Commands.getCommands();
			Iterator<String> commands = commandList.keySet().iterator();
			String message = "Get help for a command. write one of line below.%0A";
			while(commands.hasNext())
			{
				Command command = commandList.get(commands.next());
				if(!command.isHidden())
				{
					message += "/help " + command.getCommandName() + "%0A";
				}
			}
			if(msg.getChat().getType().equals("group"))
			{
				Sender.sendMessage(msg.getChat().getId(), message, msg.getMessage_id());
			}
			else
			{
				Sender.sendMessage(msg.getChat().getId(), message);
			}
		}
	}

	/**
	 * Handle a request and reply with the content, if only "help" is called it make a list of the existing command
	 */
	public static void helpRequest(String args[])
	{
		if (args.length == 1)
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
		Sender.sendMessage(message.getSender_id(), "Faggots. faggots everywere. Try /help to list commands. Try /help %2Bcommand for help about command");
	}
}

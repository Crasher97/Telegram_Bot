package bot.UpdateReader;

import bot.botType.Message;
import bot.botType.User;
import bot.collections.Commands;
import bot.collections.Users;
import bot.functions.Sender;
import bot.functions.SimSimi;
import bot.log.Log;
import bot.translation.Sentences;

/**
 * Created by Paolo on 15/11/2015.
 */
public class Validator
{
	/**
	 * Check if text is command, if it is, return command without /, else if command is null or doesn't exist return null
	 *
	 * @param msg message received from bot
	 * @return commandName
	 */
	public static boolean isCommand(Message msg)
	{
		if (msg.getText() != null && msg.getText().charAt(0) == '/')
		{
			String command = msg.getText();
			if (!command.equals(msg.getText().split("@"))) ;
			{
				command = msg.getText().split("@")[0];
				msg.setText(command);
			}
			command = command.substring(1).split(" ")[0];
			if (Commands.commandExist(command))
			{
				Log.info("Command " + Sentences.FROM.getSentence() + " [" + msg.getUserFrom().getSenderId() + "] " + msg.getUserFrom().getFirst_name() + " " + msg.getUserFrom().getLast_name() + " group[" + msg.getChat().getTitle() + "]" + ": " + msg.getText());
				return true;
			}
			else
			{
				Log.warn(Sentences.UNKNOWN_COMMAND.getSentence() + " " + Sentences.FROM.getSentence() + " [" + msg.getUserFrom().getSenderId() + "] " + msg.getUserFrom().getFirst_name() + " " + msg.getUserFrom().getLast_name() + " group[" + msg.getChat().getTitle() + "]" + ": " + msg.getText());
				if (msg.getChat().getType().equals("group"))
				{
					Sender.sendMessage(msg.getChat().getId(), Sentences.UNKNOWN_COMMAND.getSentence(), msg.getMessageId());
				}
				else
				{
					Sender.sendMessage(msg.getChat().getId(), Sentences.UNKNOWN_COMMAND.getSentence());
				}
				return false;
			}
		}
		else
		{

			if (msg.getChat().getType().equals("group"))
			{
				Sender.sendMessage(msg.getChat().getId(), SimSimi.toSimSimi(msg.getText()), msg.getMessageId());
			}
			else
			{
				Sender.sendMessage(msg.getChat().getId(), SimSimi.toSimSimi(msg.getText()));
			}
			return false;
		}
	}

	/**
	 * Check if user is banned from this bot
	 *
	 * @param msg
	 * @return
	 */
	public static boolean isBanned(Message msg)
	{
		User utente = Users.getUser(msg.getUserFrom().getSenderId());
		if (utente == null || utente.isBan())
		{
			Log.warn("BANNED USER [" + msg.getUserFrom().getSenderId() + "] " + msg.getUserFrom().getFirst_name() + " " + msg.getUserFrom().getLast_name() + ": " + msg.getText());
			if (msg.getChat().getType().equals("group"))
			{
				Sender.sendMessage(msg.getChat().getId(), "%E2%80%A0YOU ARE BANNED FROM THIS BOT%E2%80%A0", msg.getMessageId());
			}
			else
			{
				Sender.sendMessage(msg.getChat().getId(), "%E2%80%A0YOU ARE BANNED FROM THIS BOT%E2%80%A0");
			}
			return true;
		}
		return false;
	}

	/**
	 * Check if user exist. if exist user in message is replaced by user inside colletion Users. if not, user is added to users file and in collection
	 *
	 * @param msg
	 * @return true if user is already in users
	 */
	public static boolean checkUserExist(Message msg)
	{
		User usr = new User(new User(msg.getUserFrom()));
		if (!Users.userExist(usr))
		{
			Users.addUserToFile(usr); // NON INVERTIRE
			Users.addUser(usr);
			return false;
		}
		msg.setUserFrom(Users.getUser(usr.getSenderId()));
		return true;
	}

}

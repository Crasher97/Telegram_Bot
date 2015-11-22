package bot.UpdateReader;

import bot.botType.Command;
import bot.botType.Message;
import bot.botType.User;
import bot.collections.Commands;
import bot.collections.Users;
import bot.collections.WaitingCommands;
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
	 * Delete message reference to bot.
	 * -EXEMPLE '/command@YOURBOT' is processed to '/command'
	 *
	 * @param msg
	 */
	public static void deleteBotRef(Message msg)
	{
		if (!msg.getText().equals(msg.getText().split("@"))) ;
		{
			msg.setText(msg.getText().split("@")[0]);
		}
	}

	/**
	 * Check if text is command, if it is, return true, else if it is not command return false
	 * ATTENTION: THIS METHOD DELETE ALL TEXT AFTER SPACE
	 * -example: "/command itdoesntmatter yourbrainhasgone" becomes "/command"
	 *
	 * @param msg message received from bot
	 * @return true if is command.
	 */
	public static boolean isCommand(Message msg)
	{
		if (msg.getText() != null && msg.getText().charAt(0) == '/')
		{
			String command = msg.getText();
			command = command.substring(1);
			msg.setText(command);
			return true;
		}
		return false;
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
	 * Check if text in received message is param.
	 *
	 * @param msg
	 * @return true if message start with '-'
	 */
	public static boolean isParam(Message msg)
	{
		if (msg.getText() != null && msg.getText().charAt(0) == '-')
		{
			String param = msg.getText().substring(1);
			msg.setText(param);
			Log.info("Param " + Sentences.FROM.getSentence() + " [" + msg.getUserFrom().getSenderId() + "] " + msg.getUserFrom().getFirst_name() + " " + msg.getUserFrom().getLast_name() + " group[" + msg.getChat().getTitle() + "]" + ": " + msg.getText());
			return true;
		}
		else
		{
			return false;
		}
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

	/**
	 * Check is command is contained in commands list. If command do not exist, bot send a message to tell user
	 *
	 * @param msg
	 * @return Command, if it is null means that command does not exist.
	 */
	public static Command checkCommandExist(Message msg)
	{
		String command = msg.getText();
		command = command.split(" ")[0];
		if (Commands.commandExist(command)|| msg.getText().equals("cancel"))
		{
			Log.info("Command " + Sentences.FROM.getSentence() + " [" + msg.getUserFrom().getSenderId() + "] " + msg.getUserFrom().getFirst_name() + " " + msg.getUserFrom().getLast_name() + " group[" + msg.getChat().getTitle() + "]" + ": " + msg.getText());
			return Commands.getCommand(command);
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
			return null;
		}
	}

}

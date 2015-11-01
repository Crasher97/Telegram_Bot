package bot.collections;

import bot.IO;
import bot.Message;
import bot.log.Log;

import java.io.IOException;
import java.util.ArrayList;

public class Messages
{
	private static Message[] messages = new Message[100];
	private static int index = 0;

	/**
	 * Add a message to the array, when it reach 1oo write to the log and reset the array
	 *
	 * @param msg Message to add
	 */
	public static void addMessage(Message msg)
	{
		if (index < 100)
		{
			messages[index++] = msg;
		}
		else
		{
			printLog();
			index = 0;
			messages[index++] = msg;
		}
	}

	/**
	 * Return message array
	 *
	 * @return array
	 */
	public static Message[] getArray()
	{
		return messages;
	}

	public static boolean printLog()
	{
		try
		{
			ArrayList<Message> printMessages = new ArrayList<Message>();
			for (int tmpIndex = 0; tmpIndex < index; tmpIndex++)
			{
				printMessages.add(messages[tmpIndex]);
			}
			IO.writeOUT("Messageslog", printMessages);
			return true;
		}
		catch (IOException e)
		{
			Log.stackTrace(e.getStackTrace());
			return false;
		}
	}

}

package bot.collections;

import bot.botType.Message;
import bot.functions.FileManager;
import bot.log.Log;
import bot.translation.Sentences;

import java.io.File;

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
		String printMessages = "";
		for (int tmpIndex = 0; tmpIndex < index; tmpIndex++)
		{
			printMessages += messages[tmpIndex].toString() + "\n";
		}
		if(FileManager.writeFile(new File("log/MessageLog.log"), printMessages, true))
		{
			Log.info(Sentences.MESSAGES_SAVED.getSentence());
			return true;
		}
		Log.error(Sentences.MESSAGES_NOT_SAVED.getSentence());
		return false;
	}

}

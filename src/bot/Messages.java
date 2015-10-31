package bot;

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

	/**
	 * Test if the 2 message passed are equals
	 *
	 * @param msg
	 * @param msg2
	 * @return true if are equals, false if not
	 */
	public static boolean equals(Message msg, Message msg2)
	{
		if (msg.getDate() != msg2.getDate())
		{
			return false;
		}
		if (msg.getUpdate_id() == msg2.getUpdate_id() &&
				msg.getText() == msg2.getText() &&
				msg.getFirst_name() == msg2.getFirst_name() &&
				msg.getLast_name() == msg2.getLast_name() &&
				msg.getMessage_id() == msg2.getMessage_id() &&
				msg.getSender_id() == msg2.getSender_id())
		{
			return true;
		}
		return false;
	}

	/**
	 * Print all the message in the array
	 */
	public static void printMessagesList()
	{
		for (Message m : messages)
		{
			System.out.println(m.getUpdate_id());
			System.out.println(m.getMessage_id());
			System.out.println(m.getSender_id());
			System.out.println(m.getFirst_name());
			System.out.println(m.getLast_name());
			System.out.println(m.getText());
			System.out.println("\n-----------------------------------------\n");
		}
	}


}

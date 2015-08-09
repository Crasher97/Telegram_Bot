package bot;
import java.io.IOException;
import java.util.ArrayList;

public class Messages 
{
	private static Message[] messages = new Message[100];
	private static int index = 0;
	
	/**
	 * addMessage - aggiunge un messaggio in memoria, quando si raggiungono i 100 messaggi li scrive in un log esterno e resetta l'array
	 * @param msg
	 */
	public static void addMessage(Message msg)
	{
		if(index < 100)
		{
			messages[index] = msg;
		}
		else
		{
			printLog();
			index = 0;
			messages[index] = msg;
		}
	}
	
	/**
	 * Restituisce l'intero array dei messaggi
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
				for(Message message : messages)
					{
						printMessages.add(message);
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
	 * equals, controlla se due parametri passati come parametro sono uguali
	 * @param msg
	 * @param msg2
	 * @return boolean, true se è lo stesso messaggio, false se non lo è
	 */
	public static boolean equals(Message msg, Message msg2)
	{
		if(msg.getDate()!=msg2.getDate())
		{
			return false;
		}
		if     (msg.getUpdate_id()==msg2.getUpdate_id() &&
				msg.getText()==msg2.getText() &&
				msg.getFirst_name()==msg2.getFirst_name() &&
				msg.getLast_name()==msg2.getLast_name() &&
				msg.getMessage_id()==msg2.getMessage_id() &&
				msg.getSender_id()==msg2.getSender_id())
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Stampa su console tutti i messaggi in memoria
	 */
	public static void printMessagesList()
	{
		for(Message m : messages)
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

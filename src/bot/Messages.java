package bot;
import java.io.IOException;
import java.util.ArrayList;

public class Messages 
{
	private static ArrayList<Message> messages = new ArrayList<Message>();
	
	/**
	 * addMessage - aggiunge un messaggio in memoria, quando si raggiungono i 100 messaggi li scrive in un log esterno e resetta l'array
	 * @param msg
	 */
	public static void addMessage(Message msg)
	{
		if(messages.size()<50)
		{
			messages.add(msg);
		}
		else
		{
			messages.add(msg);
			try 
			{
				IO.writeOUT("log", messages);
			} 
			catch (IOException e) 
			{
				Log.stackTrace(e.getStackTrace());
			}
			messages.clear();
		}
	}
	
	/**
	 * Restituisce l'intero array dei messaggi
	 * @return array
	 */
	public static ArrayList<Message> getArray()
	{
		return messages;
	}
	
	/**
	 * equals, controlla se due parametri passati come parametro sono uguali
	 * @param msg
	 * @param msg2
	 * @return boolean, true se sono uguali, false se non lo sono
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

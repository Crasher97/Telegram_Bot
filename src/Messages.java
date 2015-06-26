import java.io.IOException;
import java.util.ArrayList;


public class Messages 
{
	private static ArrayList<Message> messages = new ArrayList<Message>();
	
	/**
	 * addMessage - aggiunge un messaggio in memoria
	 * @param msg
	 * @throws IOException 
	 */
	public static void addMessage(Message msg)
	{
		if(messages.size()<0)
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
				e.printStackTrace();
			}
			messages.clear(); //se non funziona mettere come parametro una collezione vuota
		}
	}
	
	/**
	 * Restituisce l'intero array
	 * @return array
	 */
	public static ArrayList<Message> getArray()
	{
		return messages;
	}
	
	/**
	 * equals, controlla se due parametri passati come parametro sono uguali
	 * @param msg
	 * @param messaggio2
	 * @return boolean, true se sono uguali, false se non lo sono
	 */
	public static boolean equals(Message msg, Message messaggio2)
	{
		if(msg.getDate()!=messaggio2.getDate())
		{
			return false;
		}
		if     (msg.getUpdate_id()==messaggio2.getUpdate_id() &&
				msg.getText()==messaggio2.getText() &&
				msg.getFirst_name()==messaggio2.getFirst_name() &&
				msg.getLast_name()==messaggio2.getLast_name() &&
				msg.getMessage_id()==messaggio2.getMessage_id() &&
				msg.getSender_id()==messaggio2.getSender_id())
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

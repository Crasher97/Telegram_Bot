import java.util.ArrayList;


public class Messages 
{
	private static ArrayList<Message> messages = new ArrayList<Message>();
	
	public static void addMessage(Message msg)
	{
		messages.add(msg);
	}
	
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

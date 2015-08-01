package bot;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import addons.Commands;


public class Reader 
{
	/**
	 * Metodo che si connette al sito e scarica gli updates
	 */
	public static void getUpdate()
	{
		Document doc;
		String receivedJSON;
		try 
		{
			doc = Jsoup.connect(Main.getUrl() + "/getUpdates").ignoreContentType(true).get();
			receivedJSON = doc.text();
			parseJSON(receivedJSON);
			//aggiungere metodo che chiama l'oggetto identifier, per identificare il comando etc
		} 
		catch (IOException e) 
		{
				e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * Prende i messaggi dal metodo getUpdates di telegram, lo scompone in tutti i suoi contenuti(vedere documentazione telegram) e assegna il compito di rispondere
	 * a un altra parte di programma a seconda che sia un comando o no.
	 * @param String receivedJSON
	 */
	public static void parseJSON(String receivedJSON)
	{
		JSONParser parser = new JSONParser();
		
		try {
			JSONObject jsonObject = (JSONObject) parser.parse(receivedJSON);
			JSONArray resultList = (JSONArray) jsonObject.get("result"); //Crea un array dei vari risultati
			
			Iterator<String> iterator = resultList.iterator(); //Crea un iteratore per poter scorrere l'array
            while (iterator.hasNext()) 
            {
            	Message message;
            	
            	long update_id;
            	long message_id;
            	long sender_id;
            	String first_name;
            	String last_name;
            	Date date;
            	String text;
            	
            	Object obj = iterator.next();
            	JSONObject jsonObjectResult = (JSONObject) obj;
            	update_id = (long)jsonObjectResult.get("update_id");
                JSONObject jsonObjectMessage = (JSONObject) jsonObjectResult.get("message");
                JSONObject jsonObjectChat = (JSONObject) jsonObjectMessage.get("chat");
                
                message_id = (long)jsonObjectMessage.get("message_id");
                sender_id = (long)jsonObjectChat.get("id");
                first_name = (String)jsonObjectChat.get("first_name");
                last_name = (String)jsonObjectChat.get("last_name");
                date = new Date((long)jsonObjectMessage.get("date") * 1000);
                text = (String)jsonObjectMessage.get("text");
                
                message = new Message(update_id, message_id, sender_id, first_name, last_name, date, text);
				Messages.addMessage(message);
				Jsoup.connect(Main.getUrl() + "/getUpdates?offset=" + (update_id + 1)).ignoreContentType(true).post();
				if(text.charAt(0)=='/')
				{
					String command = message.getText();
					command = command.substring(0, 0) + command.substring(1);
					
					//spegne il bot
					boolean admin = Main.getOwner().equals(String.valueOf(message.getSender_id()));
					if(admin)
						if(text.equals("/stop"))System.exit(0);
					
					//Comando Alderico
					if(Commands.commandExist(command.split(" ")[0]))
					{
						Commands.exeCommand(command.split(" ")[0], message);
					}
					else
					{
							System.out.println("comando non riconosciuto");
							Sender.sendMessage((int)message.getSender_id(), "comando non riconosciuto");
					}
				}
				else
				{
					//messaggio normale per ora nulla
					Sender.sendMessage((int)message.getSender_id(), "Non sono ancora in grado di parlare, per ora interpreto solo i comandi passati con /");
					System.out.println(message.getText());
				}
            }
			//Messages.printMessagesList();
			
		} 
		catch (ParseException | IOException e) 
		{
			e.printStackTrace();
		}
		
	}
}

package bot;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import addons.Commands;


public class UpdatesReader 
{
	/**
	 * Metodo che si connette al sito e scarica gli updates
	 * @return receivedJSON, null if there are no updates
	 */
	public static String getUpdate()
	{
		Document doc;
		String receivedJSON;
		try 
		{
			doc = Jsoup.connect(Main.getUrl() + "/getUpdates").ignoreContentType(true).get();
			receivedJSON = doc.text();
			return receivedJSON;
			//aggiungere metodo che chiama l'oggetto identifier, per identificare il comando etc
		} 
		catch (IOException e) 
		{
			Log.stackTrace(e.getStackTrace());
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * Scompone il JSON ricevuto e crea un arrayList con dentro tutti i messaggi da processare
	 * @param receivedJSON
	 * @return messages
	 */
	public static ArrayList<Message> parseJSON(String receivedJSON)
	{
		JSONParser parser = new JSONParser();
		
		try {
			JSONObject jsonObject = (JSONObject) parser.parse(receivedJSON);
			JSONArray resultList = (JSONArray) jsonObject.get("result"); //Crea un array dei vari risultati
			
			Iterator<String> iterator = resultList.iterator(); //Crea un iteratore per poter scorrere l'array
			ArrayList<Message> messages = new ArrayList<Message>();
            while (iterator.hasNext()) 
            {
            	try
            		{
            			Object obj = iterator.next();
            			JSONObject jsonObjectResult = (JSONObject) obj;
            			long update_id = (long)jsonObjectResult.get("update_id");
            			JSONObject jsonObjectMessage = (JSONObject) jsonObjectResult.get("message");
            			JSONObject jsonObjectChat = (JSONObject) jsonObjectMessage.get("chat");
                
            			long message_id = (long)jsonObjectMessage.get("message_id");
            			long sender_id = (long)jsonObjectChat.get("id");
            			String first_name = (String)jsonObjectChat.get("first_name");
                		String last_name = (String)jsonObjectChat.get("last_name");
                		Date date = new Date((long)jsonObjectMessage.get("date") * 1000);
                		String text = (String)jsonObjectMessage.get("text");
                
                		Message message = new Message(update_id, message_id, sender_id, first_name, last_name, date, text);
                		Messages.addMessage(message);
                		Jsoup.connect(Main.getUrl() + "/getUpdates?offset=" + (update_id + 1)).ignoreContentType(true).post();
				
                		boolean owner = Main.getOwner().equals(String.valueOf(message.getSender_id()));
    					if(owner)
    						if(text.equals("/stop"))System.exit(0);
                		
                		if(isCommand(message) != null)
                			{
                				messages.add(message);
                			}
            		}
            	catch(IOException err)
            		{
            			Log.error("Errore IOException in UpdatesReader: method parseJSON");
            			Log.stackTrace(err.getStackTrace());
            		}
            }
        	return messages;
		} 
		catch (ParseException e) 
		{
			Log.error("Errore ParseException in UpdatesReader: method parseJSON");
			Log.stackTrace(e.getStackTrace());
			return null;
		}
		
	}
	
	/**
	 * Check if text is command, if it is, return command without /, else if command is null or doesn't exist return null
	 * @param text
	 * @return commandName
	 */
	public static String isCommand(Message msg)
	{
		if(msg.getText().charAt(0)=='/')
			{
				String command = msg.getText();
				command = command.substring(1).split(" ")[0];
				if(Commands.commandExist(command))
					{
						return command;
					}
				else
					{
						Log.warn("Comando non riconosciuto ricevuto da: " + msg.getFirst_name() + " " + msg.getLast_name());
						Sender.sendMessage(msg.getSender_id(), "Comando non riconosciuto");
						return null;
					}
			}
		else
			{
				//Sender.sendMessage(msg.getSender_id(), "Non sono ancora in grado di parlare, per ora interpreto solo i comandi passati con /");
				Sender.sendMessage(msg.getSender_id(), SimSimi.toSimSimi(msg.getText()));
				Log.info(msg.getText());
				return null;
			}
	}
}

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


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
                date = new Date((long)jsonObjectMessage.get("date"));
                text = (String)jsonObjectMessage.get("text");
                
                message = new Message(update_id, message_id, sender_id, first_name, last_name, date, text);
				Messages.addMessage(message);
            }
			Messages.printMessagesList();
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
}

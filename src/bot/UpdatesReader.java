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
	 * Connect to Telegram and get new message
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
		}
		catch (IOException e)
		{
			Log.error("CONNECTION ERROR");
			return null;
		}
	}


	/**
	 * Parse the received JSON and create an Arraylist of all the messages
	 *
	 * @param receivedJSON Received JSON
	 * @return messages
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Message> parseJSON(String receivedJSON)
	{
		JSONParser parser = new JSONParser();

		try
		{
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
					long update_id = (long) jsonObjectResult.get("update_id");
					JSONObject jsonObjectMessage = (JSONObject) jsonObjectResult.get("message");
					JSONObject jsonObjectChat = (JSONObject) jsonObjectMessage.get("from");

					long message_id = (long) jsonObjectMessage.get("message_id");
					long sender_id = (long) jsonObjectChat.get("id");
					String first_name = (String) jsonObjectChat.get("first_name");
					String last_name = (String) jsonObjectChat.get("last_name");
					Date date = new Date((long) jsonObjectMessage.get("date") * 1000);
					String text = (String) jsonObjectMessage.get("text");

					Message message = new Message(update_id, message_id, sender_id, first_name, last_name, date, text);

					Messages.addMessage(message);
					if(!Users.userExist(message.getUser()))
					{
						Users.addUser(message.getUser());
					}

					Jsoup.connect(Main.getUrl() + "/getUpdates?offset=" + (update_id + 1)).ignoreContentType(true).post();
					messages.add(message);
				}
				catch (IOException err)
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
	 * Parse the JSON received with WebHook and return the message
	 *
	 * @param receivedJSON Received JSON
	 * @return messages
	 */
	@SuppressWarnings("unchecked")
	public static Message webhookParseJSON(String receivedJSON)
	{
		JSONParser parser = new JSONParser();

		try
		{
			JSONObject jsonObject = (JSONObject) parser.parse(receivedJSON);
			long update_id = (long) jsonObject.get("update_id");
			JSONObject jsonObjectMessage = (JSONObject) jsonObject.get("message");
			JSONObject jsonObjectChat = (JSONObject) jsonObjectMessage.get("chat");
			long message_id = (long) jsonObjectMessage.get("message_id");
			long sender_id = (long) jsonObjectChat.get("id");
			String first_name = (String) jsonObjectChat.get("first_name");
			String last_name = (String) jsonObjectChat.get("last_name");
			Date date = new Date((long) jsonObjectMessage.get("date") * 1000);
			String text = (String) jsonObjectMessage.get("text");
			Message message = new Message(update_id, message_id, sender_id, first_name, last_name, date, text);
			return message;
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
	 *
	 * @param msg
	 * @return commandName
	 */
	public static boolean isCommand(Message msg)
	{
		if (msg.getText() != null && msg.getText().charAt(0) == '/')
		{
			String command = msg.getText();
			command = command.substring(1).split(" ")[0];
			if (Commands.commandExist(command))
			{
				Log.info("Comando riconosciuto ricevuto da [" + msg.getSender_id()+"] " + msg.getFirst_name() + " " + msg.getLast_name() + ": " + msg.getText());
				return true;
			}
			else
			{
				Log.warn("Comando NON riconosciuto ricevuto da [" + msg.getSender_id()+ "] " + msg.getFirst_name() + " " + msg.getLast_name() + ": " + msg.getText());
				Sender.sendMessage(msg.getSender_id(), "Comando non riconosciuto");
				return false;
			}
		}
		else
		{

			Log.info("Messaggio ricevuto da [" + msg.getSender_id() + "] " + msg.getFirst_name() + " " + msg.getLast_name() + ": " + msg.getText());
			Sender.sendMessage(msg.getSender_id(), SimSimi.toSimSimi(msg.getText()));
			return false;
		}
	}

	/**
	 * Check if user is banned from this bot
	 * @param msg
	 * @return
	 */
	public static boolean isBanned(Message msg)
	{
		User utente = Users.getUser(msg.getSender_id());
		if(utente.isBan() || utente == null)
		{
			Log.warn("BANNED USER [" + msg.getSender_id()+ "] " + msg.getFirst_name() + " " + msg.getLast_name() + ": " + msg.getText());
			Sender.sendMessage(msg.getSender_id(),"YOU ARE BANNED FROM THIS BOT");
			return true;
		}
		return false;
	}
}
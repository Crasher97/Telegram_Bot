package bot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import bot.telegramType.Chat;
import bot.telegramType.User;
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


					//INFORMAZIONI SULLA CHAT DI PROVENIENZA
					Chat chat = getChatInfo(jsonObjectResult);

					//INFORMAZIONI SUL MITTENTE & INFO SUL MESSAGGIO
					Message message = getMessageInfo(jsonObjectResult, chat);


					//AGGIUNGE IL MESSAGGIO ALLA RACCOLTA
					Messages.addMessage(message);

					//AGGIUNGE L'UTENTE
					if(!Users.userExist(message.getUser()))
					{
						Users.addUser(message.getUser());
					}

					//CANCELLA I MESSAGGI
					Jsoup.connect(Main.getUrl() + "/getUpdates?offset=" + (message.getUpdate_id() + 1)).ignoreContentType(true).post();
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
	 * Return informaticn about chat where message has been sent.
	 * @param jsonObjectResult result passed by telegram
	 * @return information about chat
	 */
	public static Chat getChatInfo(JSONObject jsonObjectResult)
	{
		JSONObject jsonObjectMessage = (JSONObject) jsonObjectResult.get("message");
		JSONObject jsonObjectChat = (JSONObject) jsonObjectMessage.get("chat");
		long chat_id = (long) jsonObjectChat.get("id");
		String title = (String) jsonObjectChat.get("title");
		String type = (String) jsonObjectChat.get("type");
		String username = (String) jsonObjectChat.get("username");
		String first_name = (String) jsonObjectChat.get("first_name");
		String last_name = (String) jsonObjectChat.get("last_name");

		return new Chat(chat_id, type, title, username, first_name, last_name);
	}

	/**
	 * Return informaticn about sender.
	 * @param jsonObjectResult result passed by telegram
	 * @return information about sender
	 */
	public static Message getMessageInfo(JSONObject jsonObjectResult, Chat chat)
	{
		long update_id = (long) jsonObjectResult.get("update_id");
		JSONObject jsonObjectMessage = (JSONObject) jsonObjectResult.get("message");
		JSONObject jsonObjectFrom = (JSONObject) jsonObjectMessage.get("from");
		String first_name = (String) jsonObjectFrom.get("first_name");
		String last_name = (String) jsonObjectFrom.get("last_name");
		long sender_id = (long) jsonObjectFrom.get("id");
		long message_id = (long) jsonObjectMessage.get("message_id");
		Date date = new Date((long) jsonObjectMessage.get("date") * 1000);
		String text = (String) jsonObjectMessage.get("text");


		return new Message(update_id, message_id, sender_id, first_name, last_name, date, text, chat);

	}


	/**
	 * Parse the JSON received with WebHook and return the message
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
			Chat chat = getChatInfo(jsonObject);
			Message msg = getMessageInfo(jsonObject, chat);
			Messages.addMessage(msg);

			//AGGIUNGE L'UTENTE
			if(!Users.userExist(msg.getUser()))
			{
				Users.addUser(msg.getUser());
			}
			return msg;
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
	 * @param msg message received from bot
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
				Log.info("Command from [" + msg.getSender_id()+"] " + msg.getFirst_name() + " " + msg.getLast_name() + " [group" + msg.getChat().getTitle() + "]" + ": " + msg.getText());
				return true;
			}
			else
			{
				Log.warn("Command NOT recognized from [" + msg.getSender_id()+ "] " + msg.getFirst_name() + " " + msg.getLast_name()  + " [group" + msg.getChat().getTitle() + "]"+ ": " + msg.getText());
				if(msg.getChat().getType().equals("group"))
				{
					Sender.sendMessage(msg.getChat().getId(), "Unknown command", msg.getMessage_id());
				}
				else
				{
					Sender.sendMessage(msg.getChat().getId(), "Unknown command", msg.getMessage_id());
				}
				return false;
			}
		}
		else
		{

			Log.info("Message received from [" + msg.getSender_id() + "] " + msg.getFirst_name() + " " + msg.getLast_name() + " [group" + msg.getChat().getTitle() + "]"+ ": " + msg.getText());
			if(msg.getChat().getType().equals("group"))
			{
				Sender.sendMessage(msg.getChat().getId(), SimSimi.toSimSimi(msg.getText()), msg.getMessage_id());
			}
			else
			{
				Sender.sendMessage(msg.getChat().getId(), SimSimi.toSimSimi(msg.getText()));
			}
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
		if(utente == null || utente.isBan())
		{
			Log.warn("BANNED USER [" + msg.getSender_id() + "] " + msg.getFirst_name() + " " + msg.getLast_name() + ": " + msg.getText());
			if(msg.getChat().getType().equals("group"))
			{
				Sender.sendMessage(msg.getChat().getId(), "YOU ARE BANNED FROM THIS BOT", msg.getMessage_id());
			}
			else
			{
				Sender.sendMessage(msg.getChat().getId(), "YOU ARE BANNED FROM THIS BOT");
			}
			return true;
		}
		return false;
	}
}
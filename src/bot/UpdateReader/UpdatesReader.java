package bot.UpdateReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import bot.Main;
import bot.botType.Message;
import bot.collections.Messages;
import bot.functions.JsonManager;
import bot.log.Log;
import bot.telegramType.Chat;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


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
		JSONObject jsonObject = JsonManager.getJsonFromString(receivedJSON);
		JSONArray resultList = (JSONArray) jsonObject.get("result"); //Crea un array dei vari risultati

		Iterator<String> iterator = resultList.iterator(); //Crea un iteratore per poter scorrere l'array
		ArrayList<Message> messages = new ArrayList<Message>();
		while (iterator.hasNext())
		{
			try
			{
				Object obj = iterator.next();
				JSONObject jsonObjectResult = (JSONObject) obj;
				Message msg = new Message();
				UpdateSplitter splitter = new UpdateSplitter(jsonObjectResult, msg);

				//INFO ABOUT MESSAGE
				splitter.getMessageInfo();

				//INFO ABOUT CHAT
				splitter.getChatInfo();

				//INFO ABOUT SENDER
				splitter.getUserInfo();

				if(Validator.isCommand(msg))msg.setMessageType(Message.MessageType.COMMAND);
				else if(Validator.isParam(msg))msg.setMessageType(Message.MessageType.PARAM);
				else msg.setMessageType(Message.MessageType.TEXT);

				//AGGIUNGE IL MESSAGGIO ALLA RACCOLTA
				Messages.addMessage(msg);

				//CANCELLA I MESSAGGI
				Jsoup.connect(Main.getUrl() + "/getUpdates?offset=" + (msg.getUpdate_id() + 1)).ignoreContentType(true).post();
				messages.add(msg);
			}
			catch (IOException err)
			{
				Log.error("Errore IOException in UpdatesReader: method parseJSON");
				Log.stackTrace(err.getStackTrace());
			}
		}
		return messages;
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
		Message msg = new Message();

		JSONObject jsonObject = JsonManager.getJsonFromString(receivedJSON);
		UpdateSplitter split = new UpdateSplitter(jsonObject, msg);
		split.getMessageInfo();
		split.getUserInfo();
		split.getChatInfo();

		if(Validator.isCommand(msg))msg.setMessageType(Message.MessageType.COMMAND);
		else if(Validator.isParam(msg))msg.setMessageType(Message.MessageType.PARAM);
		else msg.setMessageType(Message.MessageType.TEXT);

		if (jsonObject != null)
			return msg;
		return null;
	}


}
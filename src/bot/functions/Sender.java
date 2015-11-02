package bot.functions;

import java.io.IOException;

import bot.log.Log;
import bot.Main;
import bot.translation.Sentences;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class Sender
{
	/**
	 * Send message with customized keyboard
	 *
	 * @param chatId
	 * @param message
	 * @param keyboard
	 * @return true if message has been sent
	 */
	public static boolean sendMessage(long chatId, String message, Keyboard keyboard)
	{
		return sendMessage(chatId, message + "&reply_markup=" + keyboard.toJSONString(), 0);
	}

	public static boolean sendMessage(long chatId, String message)
	{
		return sendMessage(chatId, message, 0);
	}


	/**
	 * Metodo per inviare messaggi
	 *
	 * @param chatId  Chat id to send message
	 * @param message Messagge to send
	 * @param reply, message id to reply. Pass 0 for no reply
	 * @return true if it has been sent
	 */
	public static boolean sendMessage(long chatId, String message, long reply)
	{
		try
		{
			if (message == null)
			{
				Log.warn("Message not sent, text is empty");
				return false;
			}
			Document doc;
			if(reply!=0)
				doc = Jsoup.connect(Main.getUrl() + "/sendMessage" + "?chat_id=" + chatId + "&reply_to_message_id=" + reply + "&text=" + message).ignoreContentType(true).post();
			else
				doc = Jsoup.connect(Main.getUrl() + "/sendMessage" + "?chat_id=" + chatId + "&text=" + message).ignoreContentType(true).post();

			if (doc.text().contains("\"ok\":true"))
			{
				String log = message.split("&")[0];
				if(log.length()>55)
					Log.info("Messaggio inviato: " + message.split("&")[0].substring(0,50) + "...");
				else
					Log.info("Messaggio inviato: " + message.split("&")[0]);
				return true;
			}
			else
			{
				Log.error("Messaggio non inviato: " + doc.text());
				return false;
			}
		}
		catch (IOException e)
		{
			Log.error("Messaggio non inviato");
			return false;
		}

	}

	/**
	 * Send bot condition to be accepted by user
	 * @return true if message has been sent
	 */
	public static boolean sendConditions(long privateChatId)
	{
		Document doc;
		String message = Sentences.CONDITIONS.getSentence();
		if(Sender.sendMessage(privateChatId,message))
		{
			Log.info("Condizioni inviate");
			return true;
		}
		else
		{
			Log.info("Condizioni non inviate");
			return false;
		}

	}
}

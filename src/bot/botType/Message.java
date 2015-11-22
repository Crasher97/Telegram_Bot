package bot.botType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message extends bot.telegramType.Message
{
	private long update_id;
	private static DateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private MessageType messageType;
	public enum MessageType
	{
		COMMAND, PARAM, TEXT;
	}

	public Message()
	{

	}

	public void setUpdate_id(long update_id)
	{
		this.update_id = update_id;
	}

	/**
	 * Ritorna l'id del messaggio
	 *
	 * @return long update_id
	 */
	public long getUpdate_id()
	{
		return update_id;
	}

	public MessageType getMessageType()
	{
		return messageType;
	}

	public void setMessageType(MessageType messageType)
	{
		this.messageType = messageType;
	}

	/**
	 * Ritorna la data sotto forma di stringa
	 *
	 * @param date
	 * @return String date
	 */
	public static String dataToString(Date date)
	{
		return formatoData.format(date);
	}

	/**
	 * toString restituisce una string contenente tutte le informazioni in sequenza
	 */
	@Override
	public String toString()
	{
		return getUpdate_id() + " " + getMessageId() + " " + getUserFrom().getSenderId() + " " + getUserFrom().getFirst_name() + " " + getUserFrom().getLast_name() + " " + dataToString(getDate()) + " " + getText();
	}

}

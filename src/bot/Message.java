package bot;

import bot.telegramType.Chat;
import bot.telegramType.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message
{
	private Chat chat;
	private long update_id;
	private long message_id;
	private User user;
	private Date date;

	private String text;

	private static DateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	/**
	 * Constructor of Message
	 *
	 * @param update_id
	 * @param message_id
	 * @param sender_id
	 * @param first_name
	 * @param last_name
	 * @param date
	 * @param text
	 */
	public Message(long update_id, long message_id, long sender_id, String first_name, String last_name, String username ,Date date, String text, Chat chat)
	{
		this.update_id = update_id;
		this.message_id = message_id;
		user = new User(sender_id, first_name, last_name, username);
		this.date = date;
		this.text = text;
		this.chat = chat;
	}

	/**
	 * Set text
	 * @param text
	 */
	public void setText(String text)
	{
		this.text = text;
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

	/**
	 * Ritorna l'id del messaggio
	 *
	 * @return long message_id
	 */
	public long getMessage_id()
	{
		return message_id;
	}

	/**
	 * Ritorna l'id del mittente del messaggio
	 *
	 * @return long sender_id
	 */
	public long getSender_id()
	{
		return user.getSenderId();
	}

	/**
	 * Ritorna il nome del mittente
	 *
	 * @return String first_name
	 */
	public String getFirst_name()
	{
		return user.getFirst_name();
	}

	/**
	 * Ritorna il cognome del mittente
	 *
	 * @return String last_name
	 */
	public String getLast_name()
	{
		return user.getLast_name();
	}

	/**
	 * Ritorna la data del messaggio
	 *
	 * @return Date date (dd/MM/yyyy HH:mm:ss)
	 */
	public Date getDate()
	{
		return date;
	}

	/**
	 * Ritorna il testo del messaggio
	 *
	 * @return String text
	 */
	public String getText()
	{
		return text;
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
	 * Ritorna le informazioni sull'utente del messaggio
	 */
	public User getUser()
	{
		return user;
	}

	/**
	 * Return chat where message has been sent
	 * @return chat
	 */
	public Chat getChat()
	{
		return chat;
	}

	/**
	 * Set chat where message has been sent.
	 * @param chat
	 */
	public void setChat(Chat chat)
	{
		this.chat = chat;
	}

	/**
	 * toString restituisce una string contenente tutte le informazioni in sequenza
	 */
	@Override
	public String toString()
	{
		return getUpdate_id() + " " + getMessage_id() + " " + getSender_id() + " " + getFirst_name() + " " + getLast_name() + " " + Message.dataToString(getDate()) + " " + getText();
	}
}

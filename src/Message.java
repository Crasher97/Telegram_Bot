import java.util.Date;


public class Message {
	private long update_id;
	private long message_id;
	private long sender_id;
	private String first_name;
	private String last_name;
	private Date date;
	private String text;
	
	public Message(long update_id, long message_id, long sender_id, String first_name, String last_name, Date date, String text) 
	{
		this.update_id = update_id;
		this.message_id = message_id;
		this.sender_id = sender_id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.date = date;
		this.text = text;
	}

	public long getUpdate_id() {
		return update_id;
	}

	public long getMessage_id() {
		return message_id;
	}

	public long getSender_id() {
		return sender_id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public Date getDate() {
		return date;
	}

	public String getText() {
		return text;
	}
	
	/**
	 * toString restituisce una string contenente tutte le informazioni in sequenza
	 * @override
	 */
	public String toString()
	{
		return getUpdate_id() + " " + getMessage_id()+ " " + getSender_id() + " " + getFirst_name() + " " + getLast_name() + " " + getDate() + " " + getText();
	}
}

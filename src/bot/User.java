package bot;

/**
 * Created by Paolo on 13/10/2015.
 */
public class User
{
	private long senderId;
	private String first_name;
	private String last_name;
	private boolean ban = false;

	/**
	 *
	 * @param senderId
	 * @param first_name
	 * @param last_name
	 */
	public User(long senderId, String first_name, String last_name)
	{
		this.senderId = senderId;
		this.first_name = first_name;
		this.last_name = last_name;
	}

	public long getSenderId()
	{
		return senderId;
	}

	public String getFirst_name()
	{
		return first_name;
	}

	public String getLast_name()
	{
		return last_name;
	}

	public boolean isBan()
	{
		return ban;
	}

	public void setBan(boolean ban)
	{
		this.ban = ban;
	}
}

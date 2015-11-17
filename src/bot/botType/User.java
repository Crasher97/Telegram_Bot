package bot.botType;

/**
 * Created by Paolo on 13/11/2015.
 */
public class User extends bot.telegramType.User
{

	private long timeFromLastTerms = -1;
	private boolean ban = false;
	private boolean subscrived = false;

	public User(long senderId, String first_name, String last_name, String username)
	{
		super(senderId, first_name, last_name, username);
	}

	public User(bot.telegramType.User usr)
	{
		super(usr.getSenderId(), usr.getFirst_name(), usr.getLast_name(), usr.getUsername());
	}

	public long getTimeFromLastTerms()
	{
		return timeFromLastTerms;
	}

	public void setTimeFromLastTerms(long timeFromLastAdvice)
	{
		this.timeFromLastTerms = timeFromLastAdvice;
	}

	public boolean isSubscrived()
	{
		return subscrived;
	}

	public void setSubscrived(boolean subscrived)
	{
		this.subscrived = subscrived;
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


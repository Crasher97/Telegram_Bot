package bot.telegramType;

/**
 * Created by Paolo on 15/11/2015.
 */
public class ForceReply
{
	private boolean selective = true;

	public ForceReply(boolean selective)
	{
		this.selective = selective;
	}

	public boolean isSelective()
	{
		return selective;
	}

	public void setSelective(boolean selective)
	{
		this.selective = selective;
	}

	public boolean isForceReply()
	{
		return true;
	}
}

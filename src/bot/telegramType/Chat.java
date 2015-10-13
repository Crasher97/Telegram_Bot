package bot.telegramType;

public class Chat
{
	private int id;
	private String type; //“private”, or “group”, or “channel”
	private String title;
	private String username;
	private String first_name;
	private String last_name;

	public Chat(int id, String type, String title, String username, String first_name, String last_name)
	{
		this.id = id;
		this.type = type;
		this.title = title;
		this.username = username;
		this.first_name = first_name;
		this.last_name = last_name;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getFirst_name()
	{
		return first_name;
	}

	public void setFirst_name(String first_name)
	{
		this.first_name = first_name;
	}

	public String getLast_name()
	{
		return last_name;
	}

	public void setLast_name(String last_name)
	{
		this.last_name = last_name;
	}
}

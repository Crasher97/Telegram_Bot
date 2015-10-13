package bot.telegramType;

public class User
{
	private int id;
	private String first_name;
	private String last_name;
	private String username;

	public User(int id, String first_name, String last_name, String username)
	{
		this.id = id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.username = username;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
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

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}
}

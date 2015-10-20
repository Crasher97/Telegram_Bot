package bot;

import addons.Command;
import addons.Commands;
import bot.telegramType.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Paolo on 13/10/2015.
 * List that contains all users that write to bot.
 */
public class Users
{
	private static File usersFile = new File("config/users.json");
	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private static HashMap<Long, User> users = new HashMap<>();

	/**
	 * Add User to users list
	 *
	 * @param usr
	 * @return true if it has been added
	 */
	public static void addUser(User usr)
	{
		users.put(usr.getSenderId(), usr);
	}

	/**
	 * Return user from userList
	 *
	 * @param senderId, unique id for sender
	 * @return User if it has been found
	 */
	public static User getUser(long senderId)
	{
		return users.get(senderId);
	}

	/**
	 * Create a new file for users
	 */
	public static void createUserFile()
	{
		if (usersFile.exists()) return;
		JSONObject obj = new JSONObject();
		try
		{
			FileWriter outFile = new FileWriter(usersFile);
			outFile.write(gson.toJson(obj));
			Log.info("Creato file Users");
			outFile.flush();
			outFile.close();
		}
		catch (IOException e)
		{
			Log.stackTrace(e.getStackTrace());
		}
	}

	/**
	 * Add user to external file
	 *
	 * @param usr
	 */
	public static void addUserToFile(User usr)
	{
		if (!usersFile.exists()) createUserFile();
		if (userExist(usr)) return;
		JSONParser parser = new JSONParser();
		JSONObject obj;
		try
		{
			obj = (JSONObject) parser.parse(new FileReader(usersFile));
			JSONObject users = (JSONObject) obj.getOrDefault(usr.getSenderId(), new JSONObject());
			users.put("First_Name", usr.getFirst_name());
			users.put("Last_Name", usr.getLast_name());
			users.put("Username", usr.getUsername());
			users.put("Ban", usr.isBan());
			obj.put(usr.getSenderId(), users);

			FileWriter outFile = new FileWriter(usersFile);
			outFile.write(gson.toJson(obj));
			outFile.flush();
			outFile.close();
		}
		catch (IOException | ParseException e)
		{
			Log.stackTrace(e.getStackTrace());
		}
	}

	public static boolean userExist(User usr)
	{
		return users.containsKey(usr.getSenderId());
	}

	/**
	 * Load users from saved file
	 *
	 * @return true if users have been loaded
	 */
	public static boolean loadUsers()
	{
		if (!usersFile.exists()) createUserFile();
		JSONParser parser = new JSONParser();
		JSONObject obj;
		try
		{
			obj = (JSONObject) parser.parse(new FileReader(usersFile));
			ArrayList<String> keyList = new ArrayList<String>(obj.keySet());
			for (int i = 0; i < keyList.size(); i++)
			{
				JSONObject user = (JSONObject) obj.get(keyList.get(i));
				String firstName = (String) user.get("First_Name");
				String lastName = (String) user.get("Last_Name");
				String username = (String) user.get("Username");
				boolean ban = (Boolean) user.get("Ban");
				User newUser = new User(Long.parseLong(keyList.get(i)),firstName,lastName,username);
				newUser.setBan(ban);
				addUser(newUser);
			}
		}
		catch (IOException | ParseException e)
		{
			Log.stackTrace(e.getStackTrace());
			return false;
		}
		return true;
	}

	/**
	 * Load some commands for users management
	 */
	public static void loadUsersCommand()
	{
		Commands.addCommand(new Command("ban", "bot.Users", "banUser"));
	}

	/**
	 * Ban user from this bot
	 * @param msg in field text of message, there should be the sender id of the user to ban.
	 */
	public static void banUser(Message msg)
	{
		if (msg.getText() != null && Owners.isOwner(msg.getSender_id()))
		{
			String[] text = msg.getText().split(" ");
			if (text.length >= 2)
			{
				User usr = getUser(Long.parseLong(text[1]));
				if (usr != null)
				{
					usr.setBan(!usr.isBan());
				}
				else if (msg.getChat().getType().equals("group"))
				{
					Sender.sendMessage(msg.getChat().getId(), "User doesn't exist", msg.getMessage_id());
				}
				else
				{
					Sender.sendMessage(msg.getChat().getId(), "User doesn't exist");
				}
			}
			else
			{
				if (msg.getChat().getType().equals("group"))
				{
					Sender.sendMessage(msg.getChat().getId(), "Bad parameter", msg.getMessage_id());
				}
				else
				{
					Sender.sendMessage(msg.getChat().getId(), "Bad parameter");
				}
			}

		}
	}
}

package bot;

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
import java.util.Set;

/**
 * Created by Paolo on 13/10/2015.
 * List that contains all users that write to bot.
 */
public class Users
{
	private static File usersFile = new File("users.json");
	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private static HashMap<Long, User> users = new HashMap<>();

	/**
	 * Add User to users list
	 * @param usr
	 * @return true if it has been added
	 */
	public static void addUser(User usr)
	{
		users.put(usr.getSenderId(), usr);
	}

	/**
	 * Return user from userList
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

	public static void addUserTofile(User usr)
	{
		if (!usersFile.exists()) createUserFile();
		JSONParser parser = new JSONParser();
		JSONObject obj;
		try
		{
			obj = (JSONObject) parser.parse(new FileReader(usersFile));
			JSONObject user = (JSONObject) obj.getOrDefault("Users", new JSONObject());
			user.put(usr.getSenderId(), usr.getFirst_name() + "__" + usr.getLast_name() + "__" + usr.isBan());
			obj.put("Users", user);

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


	public static boolean loadUsers()
	{
		JSONParser parser = new JSONParser();
		JSONObject obj;
		try
		{
			obj = (JSONObject) parser.parse(new FileReader(usersFile));
			JSONObject usersInJson = (JSONObject) obj.get("Users");
			ArrayList<String> keyList = new ArrayList<String>(usersInJson.keySet());
			for(int i = 0; i < keyList.size(); i++)
			{
				String usr = (String) usersInJson.get(keyList.get(i));
				String[] usrInfo = usr.split("__");
				User newUser = new User(Long.parseLong(keyList.get(i)),usrInfo[0],usrInfo[1]);
				newUser.setBan(usrInfo[2].equals("true"));
				addUser(newUser);
			}
		}
		catch (IOException | ParseException e)
		{
			Log.stackTrace(e.getStackTrace());
		}
		return true;
	}

	public static void saveUsers()
	{
		JSONObject obj = new JSONObject();
		try
		{
			usersFile.delete();
			FileWriter outFile = new FileWriter(usersFile);
			outFile.write(gson.toJson(obj));
			outFile.flush();
			outFile.close();

		}
		catch(IOException e)
		{
			Log.error("Saving users before quit");
		}
		ArrayList<Long> keyList = new ArrayList<Long>(users.keySet());
		for (int i = 0; i < keyList.size(); i++)
		{
			addUserTofile(users.get(keyList.get(i)));
		}

		Log.info("Utenti salvati");
	}
}

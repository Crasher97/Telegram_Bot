package bot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Owners
{
	private static File ownersFile = new File("config/owners.json");
	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

	/**
	 * Create a new file for the setting
	 */
	public static void createOwnersFile()
	{
		if (ownersFile.exists()) return;
		JSONObject obj = new JSONObject();
		JSONArray array = new JSONArray();
		obj.put("Owners", array);
		try
		{
			FileWriter outFile = new FileWriter(ownersFile);
			outFile.write(gson.toJson(obj));
			Log.info("Creato file owners");
			outFile.flush();
			outFile.close();
		}
		catch (IOException e)
		{
			Log.stackTrace(e.getStackTrace());
		}
	}


	public static void addOwner(String userId)
	{
		if (isOwner(userId)) return;
		JSONParser parser = new JSONParser();
		JSONObject obj;
		try
		{
			obj = (JSONObject) parser.parse(new FileReader(ownersFile));
			JSONArray array = (JSONArray) obj.get("Owners");
			array.add(userId);

			FileWriter outFile = new FileWriter(ownersFile);
			outFile.write(gson.toJson(obj));
			outFile.flush();
			outFile.close();
		}
		catch (IOException | ParseException e)
		{
			Log.stackTrace(e.getStackTrace());
		}

	}

	public static void deleteOwner(String userId)
	{
		JSONParser parser = new JSONParser();
		JSONObject obj;
		try
		{
			obj = (JSONObject) parser.parse(new FileReader(ownersFile));
			JSONArray array = (JSONArray) obj.get("Owners");
			array.remove(userId);

			FileWriter outFile = new FileWriter(ownersFile);
			outFile.write(gson.toJson(obj));
			outFile.flush();
			outFile.close();
		}
		catch (IOException | ParseException e)
		{
			Log.stackTrace(e.getStackTrace());
		}
	}

	public static boolean isOwner(String userId)
	{
		JSONParser parser = new JSONParser();
		JSONObject obj;
		try
		{
			obj = (JSONObject) parser.parse(new FileReader(ownersFile));
			JSONArray array = (JSONArray) obj.get("Owners");

			return array.contains(userId);
		}
		catch (IOException | ParseException e)
		{
			Log.stackTrace(e.getStackTrace());
		}
		return false;
	}

}

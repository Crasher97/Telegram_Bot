package bot.functions;

import bot.log.Log;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONObject;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class JsonManager
{
	/**
	 * Read json from file then put it inside a JSONObject
	 * @param file
	 * @return the contenent of the file in JSONObject
	 */
	public static JSONObject readJsonFromFile(File file)
	{
		JSONParser parser = new JSONParser();
		try
		{
			FileReader reader = new FileReader(file);
			JSONObject obj = (JSONObject) parser.parse(reader);
			return obj;
		}
		catch (ParseException | IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Read json from String, then put it inside a JSONObject.
	 * @param str , String to be converted to JSONObject
	 * @return the contenent of the file in JSONObject
	 */
	public static JSONObject getJsonFromString(String str)
	{
		JSONParser parser = new JSONParser();
		try
		{
			JSONObject obj = (JSONObject) parser.parse(str);
			return obj;
		}
		catch (ParseException e)
		{
			Log.stackTrace(e.getStackTrace());
			return null;
		}
	}

	/**
	 * Get JSONArray from JSONObject
	 * @param obj JSONObject
	 * @param arrayName
	 * @return the array inside the JSONObject
	 */
	public static JSONArray getArrayFromJson(JSONObject obj, String arrayName)
	{
		return (JSONArray) obj.getOrDefault(arrayName, null);
	}

	public static JSONObject getFromJSONObject(JSONObject obj, String category)
	{
		return  (JSONObject) obj.getOrDefault(category, new JSONObject());
	}
}

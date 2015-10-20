package bot;

import bot.webServer.Server;
import bot.webServer.WebHook;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Setting
{
	private static File settingFile = new File("setting.json");
	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

	/**
	 * Create a new file for the setting
	 */
	public static void createSettingFile()
	{
		if (settingFile.exists()) return;
		JSONObject obj = new JSONObject();
		try
		{
			FileWriter outFile = new FileWriter(settingFile);
			outFile.write(gson.toJson(obj));
			Log.info("Creato file Setting");
			outFile.flush();
			outFile.close();
			writeDefaultSettings();

		}
		catch (IOException e)
		{
			Log.stackTrace(e.getStackTrace());
		}
	}

	/**
	 * Write on the file the default setting
	 */
	public static void writeDefaultSettings()
	{
		//Main Setting
		addSetting("Bot_ID", "", "Main");
		addSetting("Update_Frequence", "1000", "Main");

		//Webhook setting
		addSetting("WebHook_Active", "false", "WebHook");
		addSetting("WebHook_Url", "", "WebHook");
		addSetting("WebHook_Port", "8443", "WebHook");

		//Https server setting
		addSetting("Certificate", "server.crt", "WebHookServer");
		addSetting("Private_Key", "server.key", "WebHookServer");
		addSetting("Certificate_Password", "passwd", "WebHookServer");
	}

	/**
	 * Add a setting to the file
	 *
	 * @param key      Key for the setting
	 * @param value    Default value
	 * @param category Category of the setting
	 */
	@SuppressWarnings("unchecked")
	public static void addSetting(String key, String value, String category)
	{
		if (!settingFile.exists()) createSettingFile();
		if (settingExist(key, category)) return;
		JSONParser parser = new JSONParser();
		JSONObject obj;
		try
		{
			obj = (JSONObject) parser.parse(new FileReader(settingFile));
			JSONObject setting = (JSONObject) obj.getOrDefault(category, new JSONObject());
			setting.put(key, value);
			obj.put(category, setting);

			FileWriter outFile = new FileWriter(settingFile);
			outFile.write(gson.toJson(obj));
			outFile.flush();
			outFile.close();
		}
		catch (IOException | ParseException e)
		{
			Log.stackTrace(e.getStackTrace());
		}
	}

	/**
	 * Read a setting from the file
	 *
	 * @param key      Key
	 * @param category Category
	 * @return Value of the setting
	 */
	public static String readSetting(String key, String category)
	{
		JSONParser parser = new JSONParser();
		JSONObject obj;
		String readingResult = "";
		try
		{
			obj = (JSONObject) parser.parse(new FileReader(settingFile));
			JSONObject setting = (JSONObject) obj.get(category);
			readingResult = setting.get(key).toString();

		}
		catch (IOException | ParseException e)
		{
			Log.stackTrace(e.getStackTrace());
		}
		return readingResult;
	}

	/**
	 * Edit a setting
	 *
	 * @param key      Key to edit
	 * @param value    New value
	 * @param category Category
	 * @return True if the setting was successfully edited, else false
	 */
	@SuppressWarnings("unchecked")
	public static boolean editSetting(String key, String value, String category)
	{
		if (!settingExist(key, category)) return false;
		JSONParser parser = new JSONParser();
		JSONObject obj;
		try
		{
			obj = (JSONObject) parser.parse(new FileReader(settingFile));
			JSONObject setting = (JSONObject) obj.getOrDefault(category, new JSONObject());
			setting.put(key, value);
			obj.put(category, setting);

			FileWriter outFile = new FileWriter(settingFile);
			outFile.write(gson.toJson(obj));
			outFile.flush();
			outFile.close();
			return true;
		}
		catch (IOException | ParseException e)
		{
			Log.stackTrace(e.getStackTrace());
		}
		return false;

	}

	/**
	 * Test if a key exist
	 *
	 * @param key      Key
	 * @param category Category
	 * @return true if exist, else false
	 */
	public static boolean settingExist(String key, String category)
	{
		JSONParser parser = new JSONParser();
		JSONObject obj;
		try
		{
			obj = (JSONObject) parser.parse(new FileReader(settingFile));
			JSONObject setting = (JSONObject) obj.get(category);
			if (setting == null) return false;
			if (setting.get(key) == null) return false;
			if (setting.get(key) != "")
			{
				return true;
			}

		}
		catch (IOException | ParseException e)
		{
			Log.stackTrace(e.getStackTrace());
		}
		return false;
	}

	/**
	 * Return last launching configuration
	 *
	 * @return an array[2], where in [0] there is bot ID & in [1] there is the owner ID (saved in settings)
	 */
	public static String[] readSettings()
	{
		String idCode = Setting.readSetting("Bot_ID", "Main");

		if (idCode != null)
		{
			String[] tmp = new String[5];
			tmp[0] = idCode;
			return tmp;
		}
		return null;
	}


}

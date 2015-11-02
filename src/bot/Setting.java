package bot;

import bot.functions.FileManager;
import bot.functions.JsonManager;
import bot.log.LogFileManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONObject;
import java.io.File;

public class Setting extends LogFileManager
{
	private static File file = new File("config/setting.json");
	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

	/**
	 * Create a new file for the setting
	 */
	public static void createSettingFile()
	{
		createLogFolder(new File("config"));
		if (!file.exists())
		{
			LogFileManager.createLogFile(file);
			writeDefaultSettings();
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

		addSetting("Region", "EN" , "Language");
		addSetting("Language", "en" , "Language");
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
		if (!file.exists()) createSettingFile();
		if (settingExist(key, category)) return;

		JSONObject obj = JsonManager.readJsonFromFile(file);
		JSONObject setting = JsonManager.getFromJSONObject(obj, category);
		setting.put(key, value);
		obj.put(category, setting);

		FileManager.writeFile(file, gson.toJson(obj));
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
		JSONObject obj = JsonManager.readJsonFromFile(file);
		JSONObject setting = JsonManager.getFromJSONObject(obj, category);
		String readingResult = setting.get(key).toString();
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

		JSONObject obj = JsonManager.readJsonFromFile(file);
		JSONObject setting = JsonManager.getFromJSONObject(obj, category);
		setting.put(key, value);
		obj.put(category, setting);
		return FileManager.writeFile(file, gson.toJson(obj));
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
		JSONObject obj = JsonManager.readJsonFromFile(file);
		JSONObject setting = (JSONObject) obj.get(category);
		if (setting == null) return false;
		if (setting.get(key) == null) return false;
		if (setting.get(key) != "")
		{
			return true;
		}
		return false;
	}


}

package bot.log;

import bot.functions.FileManager;
import bot.functions.FolderManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.File;

public class LogFileManager
{
	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

	/**
	 * Create the file
	 *
	 * @param file,          file to be created
	 * @param startingArray, first category of Json log file if you pass
	 */
	public static boolean createLogFile(File file, String startingArray)
	{
		JSONObject obj = new JSONObject();
		if (startingArray != null)
		{
			JSONArray files = new JSONArray();
			obj.put(startingArray, files);
		}
		return FileManager.writeFile(file, gson.toJson(obj));
	}

	/**
	 * Create a new file, without create new json array inside file
	 *
	 * @param file to be created
	 */
	public static boolean createLogFile(File file)
	{
		return createLogFile(file, null);
	}

	/**
	 * Create new folder
	 *
	 * @param folder
	 */
	public static boolean createLogFolder(File folder)
	{
		return FolderManager.createFolder(folder);
	}
}

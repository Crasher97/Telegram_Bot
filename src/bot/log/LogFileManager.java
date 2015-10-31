package bot.log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogFileManager
{
	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

	/**
	 * Create the file
	 * @param file, file to be created
	 * @param startingCategory, first category of Json log file
	 */
	public static void createLogFile(File file, String startingCategory)
	{

		JSONObject obj = new JSONObject();
		JSONArray files = new JSONArray();
		obj.put(startingCategory, files);
		try
		{
			FileWriter outFile = new FileWriter(file);
			outFile.write(gson.toJson(obj));
			Log.config("Created file: " + file.getName());
			outFile.flush();
			outFile.close();

		}
		catch (IOException e)
		{
			Log.stackTrace(e.getStackTrace());
		}
	}

	/**
	 * Create the file
	 * @param file, file to be created
	 */
	public static void createSettingsFile(File file)
	{
		JSONObject obj = new JSONObject();
		try
		{
			FileWriter outFile = new FileWriter(file);
			outFile.write(gson.toJson(obj));
			Log.config("Created file: " + file.getName());
			outFile.flush();
			outFile.close();

		}
		catch (IOException e)
		{
			Log.stackTrace(e.getStackTrace());
		}
	}


	/**
	 * Create a new file
	 * @param file to be created
	 */
	public static void createLogFile(File file)
	{
		try
		{
			FileWriter outFile = new FileWriter(file);
			outFile.write("");
			Log.config("Created file: " + file.getName());
			outFile.flush();
			outFile.close();

		}
		catch (IOException e)
		{
			Log.stackTrace(e.getStackTrace());
		}
	}

	public static void createFolder(File folder)
	{
		if (folder.exists() && !folder.isDirectory())
		{
			Log.error("Error creating log folder, delete file that name is log");
			return;
		}
		if (!folder.exists()) folder.mkdir();
	}
}

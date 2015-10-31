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
		obj.put("Files", files);
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
}

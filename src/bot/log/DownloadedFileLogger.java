package bot.log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Iterator;

/**
 * Class to log all downloaded file and youtube video
 */
public class DownloadedFileLogger extends LogFileManager
{
	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private static File file = new File("log/downloadedFileLog.json");

	/**
	 * Create the file downloadedFileLog.json that contain information on downloaded file
	 */
	public static void createLogFile()
	{
		LogFileManager.createLogFile(file, "Files");
	}

	/**
	 * Add file's information to the log
	 *
	 * @param filePath File path
	 */
	@SuppressWarnings("unchecked")
	public static void addYoutubeLink(String url, String filePath)
	{
		if (!file.exists()) createLogFile();
		String fileMD5 = calculateFileMD5(filePath);
		JSONParser parser = new JSONParser();
		JSONObject obj;
		try
		{
			obj = (JSONObject) parser.parse(new FileReader(file));
			JSONArray files = (JSONArray) obj.get("Files");
			JSONObject jsonFile = new JSONObject();
			jsonFile.put("youtubeID", url.replace("https://www.youtube.com/watch?v=", ""));
			jsonFile.put("fileName", filePath.split("/")[1]);
			jsonFile.put("hash", fileMD5);
			jsonFile.put("fileType", "YOUTUBE");
			files.add(jsonFile);

			FileWriter outFile = new FileWriter(file);
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
	 * Delete a file from the log
	 *
	 * @param filePath File path
	 * @return True if the file was deleted, else false
	 */
	public static boolean deleteFile(String filePath)
	{
		if (!file.exists()) return false;
		JSONParser parser = new JSONParser();
		JSONObject obj;
		try
		{
			obj = (JSONObject) parser.parse(new FileReader(file));
			JSONArray filesInJson = (JSONArray) obj.getOrDefault("Files", new JSONArray());
			for (int i = 0; i < filesInJson.size(); i++)
			{
				JSONObject file = (JSONObject) filesInJson.get(i);
				if (file.get("fileName").equals(filePath))
				{
					filesInJson.remove(file);
				}
			}


			FileWriter outFile = new FileWriter(file);
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
	 * Calculate MD5 hash of the file passed as argument
	 *
	 * @param path File's path
	 * @return String file's MD5 hash
	 */
	public static String calculateFileMD5(String path)
	{
		try
		{
			FileInputStream fis = new FileInputStream(new File(path));
			String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis);
			fis.close();
			return md5;
		}
		catch (IOException e)
		{
			Log.stackTrace(e.getStackTrace());
			return null;
		}
	}

	/**
	 * Get the path of a youtube video already downloaded
	 *
	 * @param youtubeUrl Url of the video
	 * @return File path
	 */
	public static String getFileNameFromYoutube(String youtubeUrl)
	{
		if (!file.exists()) createLogFile();
		JSONParser parser = new JSONParser();
		JSONObject obj;
		try
		{
			obj = (JSONObject) parser.parse(new FileReader(file));
			JSONArray files = (JSONArray) obj.get("Files");

			Iterator<JSONObject> iterator = files.iterator();
			while (iterator.hasNext())
			{
				Object objI = iterator.next();
				JSONObject jsonObjectResult = (JSONObject) objI;
				if (jsonObjectResult.get("youtubeID").equals(youtubeUrl.replace("https://www.youtube.com/watch?v=", "")))
				{
					return (String) jsonObjectResult.get("fileName");
				}
			}
			return null;
		}
		catch (IOException | ParseException e)
		{
			Log.stackTrace(e.getStackTrace());
		}
		return null;
	}

}

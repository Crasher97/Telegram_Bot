package bot.log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import bot.functions.FileManager;
import bot.functions.JsonManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class UploadedFileLogger extends LogFileManager
{
	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private static File file = new File("log/uploadedFileLog.json");

	public enum Type
	{
		PHOTO, VIDEO, DOCUMENT, AUDIO
	}

	/**
	 * Create the file uploadedFileLog.json that contain information on uploaded file
	 */
	public static void createLogFile()
	{
		LogFileManager.createLogFile(file, "Files");
	}

	/**
	 * Extract the file_id form the passed JSON
	 *
	 * @param receivedJSON JSON
	 * @return Name of the file
	 */
	public static String parseJSON(String receivedJSON, Type type)
	{
		JSONParser parser = new JSONParser();

		try
		{
			JSONObject jsonObject = (JSONObject) parser.parse(receivedJSON);
			JSONObject resultList = (JSONObject) jsonObject.get("result");
			switch (type)
			{
				case PHOTO:
				{
					JSONArray photos = (JSONArray) resultList.get("photo");
					JSONObject photo = (JSONObject) photos.get(photos.size() - 1);
					return (String) photo.get("file_id");
				}

				case VIDEO:
				{
					JSONObject document = (JSONObject) resultList.get("video");
					return (String) document.get("file_id");
				}

				case AUDIO:
				{
					JSONObject document = (JSONObject) resultList.get("audio");
					if(document==null)
					{
						document = (JSONObject) resultList.get("voice");
					}
					return (String) document.get("file_id");
				}

				case DOCUMENT:
				{
					JSONObject document = (JSONObject) resultList.get("document");
					return (String) document.get("file_id");
				}

			}

		}
		catch (ParseException e)
		{
			Log.stackTrace(e.getStackTrace());
			return null;
		}
		return null;
	}

	/**
	 * Add file's information to uploadedFileLog.json
	 *
	 * @param filePath     File path
	 * @param receivedJSON JSON received from telegram on file upload
	 * @param type         File type
	 */
	@SuppressWarnings("unchecked")
	public static void addToFileLog(String filePath, String receivedJSON, Type type)
	{
		if (!file.exists()) createLogFile();

		String fileMD5 = calculateFileMD5(filePath);
		String file_id = parseJSON(receivedJSON, type);

		JSONObject obj = JsonManager.readJsonFromFile(file);
		JSONArray files = (JSONArray) obj.get("Files");

		JSONObject jsonFile = new JSONObject();
		jsonFile.put("hash", fileMD5);
		jsonFile.put("file_id", file_id);
		jsonFile.put("fileName", filePath.split("/")[1]);
		jsonFile.put("fileType", type.toString());
		files.add(jsonFile);

		FileManager.writeFile(file, gson.toJson(obj), false);
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
	 * Return the file_id of the file passed as parameter
	 *
	 * @param path File's path
	 * @return String file_id - null if isn't present
	 */
	@SuppressWarnings("unchecked")
	public static String getFileId(String path)
	{
		if (!file.exists()) createLogFile();
		String fileMD5 = calculateFileMD5(path);

		JSONObject obj = JsonManager.readJsonFromFile(file);
		JSONArray files = (JSONArray) obj.get("Files");

		Iterator<JSONObject> iterator = files.iterator();
		while (iterator.hasNext())
		{
			Object objI = iterator.next();
			JSONObject jsonObjectResult = (JSONObject) objI;
			if (jsonObjectResult.get("hash").equals(fileMD5))
			{
				return (String) jsonObjectResult.get("file_id");
			}
		}
		return null;
	}
}

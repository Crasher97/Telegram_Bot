package bot.functions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import bot.Log;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class UploadedFileLogger
{
	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public enum Type
	{
		PHOTO, VIDEO, DOCUMENT, AUDIO
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
	 * Create the file uploadedFileLog.json that contain information on uploaded file
	 */
	@SuppressWarnings("unchecked")
	public static void createLogFile()
	{
		JSONObject obj = new JSONObject();
		JSONArray files = new JSONArray();
		obj.put("Files", files);
		try
		{
			FileWriter outFile = new FileWriter("log/uploadedFileLog.json");
			//outFile.write(obj.toJSONString());
			outFile.write(gson.toJson(obj));
			Log.info("Creato file JSON");
			outFile.flush();
			outFile.close();

		}
		catch (IOException e)
		{
			Log.stackTrace(e.getStackTrace());
		}
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
		File f = new File("log/uploadedFileLog.json");
		if (!f.exists()) createLogFile();
		String fileMD5 = calculateFileMD5(filePath);
		String file_id = parseJSON(receivedJSON, type);
		JSONParser parser = new JSONParser();
		JSONObject obj;
		try
		{
			obj = (JSONObject) parser.parse(new FileReader("log/uploadedFileLog.json"));
			JSONArray files = (JSONArray) obj.get("Files");
			JSONObject file = new JSONObject();
			file.put("hash", fileMD5);
			file.put("file_id", file_id);
			file.put("fileName", filePath.split("/")[1]);
			file.put("fileType", type.toString());
			files.add(file);

			FileWriter outFile = new FileWriter("log/uploadedFileLog.json");
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
		File f = new File("log/uploadedFileLog.json");
		if (!f.exists()) createLogFile();
		JSONParser parser = new JSONParser();
		JSONObject obj;
		String fileMD5 = calculateFileMD5(path);
		try
		{
			obj = (JSONObject) parser.parse(new FileReader("log/uploadedFileLog.json"));
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
		catch (IOException | ParseException e)
		{
			Log.stackTrace(e.getStackTrace());
		}
		return null;
	}
}

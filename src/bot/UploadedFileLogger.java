package bot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class UploadedFileLogger
{	
	/**
	 * Estrae il file_id dal JSON passato come parametro
	 * @param String receivedJSON
	 * @return String file_id - il nome del file
	 */
	public static String parseJSON(String receivedJSON)
	{
		JSONParser parser = new JSONParser();
		
		try 
		{
			JSONObject jsonObject = (JSONObject) parser.parse(receivedJSON);
			JSONObject resultList = (JSONObject) jsonObject.get("result");
			JSONArray photos = (JSONArray) resultList.get("photo");
			JSONObject photo = (JSONObject) photos.get(2);
			//System.out.println(photo.get("file_id"));
			return (String) photo.get("file_id");
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * Crea il file uploadedFileLog.json che contiene le informazioni dei file scaricati
	 */
	@SuppressWarnings("unchecked")
	public static void createLogFile()
	{
		JSONObject obj = new JSONObject();
        JSONArray files = new JSONArray();
        obj.put("Files", files);
        try {
        	FileWriter outFile = new FileWriter("uploadedFileLog.json");
        	outFile.write(obj.toJSONString());
            System.out.println("Creato file JSON");
            outFile.flush();
            outFile.close();
 
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	/**
	 * Aggiunge il file specificato al log
	 * @param String filePath - Percorso del file
	 * @param String receivedJSON - JSON ricevuto dall'upload del file
	 */
	public static void addToFileLog(String filePath, String receivedJSON)
	{
		File f = new File("uploadedFileLog.json");
		if(!f.exists()) createLogFile();
		addToLog(calculateFileMD5(filePath), parseJSON(receivedJSON));
	}
	
	@SuppressWarnings("unchecked")
	public static void addToLog(String fileMD5, String file_id)
	{
		JSONParser parser = new JSONParser();
		JSONObject obj;
		try
		{
			obj = (JSONObject) parser.parse(new FileReader("uploadedFileLog.json"));
			JSONArray files = (JSONArray) obj.get("Files");
			JSONObject file = new JSONObject();
	        file.put("hash", fileMD5);
	        file.put("file_id", file_id);
	        files.add(file);
	        
        	FileWriter outFile = new FileWriter("uploadedFileLog.json");
        	outFile.write(obj.toJSONString());
            //System.out.println("Successfully Copied JSON Object to File...");
            //System.out.println("\nJSON Object: " + obj);
            outFile.flush();
            outFile.close();
		}
		catch (IOException | ParseException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Calcola l'MD5 del file passato come parametro
	 * @param String path - Percorso del file
	 * @return String hash MD5 del file
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
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Restituisce il file_id del file al percorso passato come parametro
	 * @param String path - Percorso del file
	 * @return String file_id - oppure null se il file non è presente nel log
	 */
	public static String getFileId(String path)
	{
		JSONParser parser = new JSONParser();
		JSONObject obj;
		String fileMD5 = calculateFileMD5(path);
		try
		{
			obj = (JSONObject) parser.parse(new FileReader("uploadedFileLog.json"));
			JSONArray files = (JSONArray) obj.get("Files");
			Iterator<JSONObject> iterator = files.iterator();
			while(iterator.hasNext())
			{
            	Object objI = iterator.next();
            	JSONObject jsonObjectResult = (JSONObject) objI;
            	if(jsonObjectResult.get("hash").equals(fileMD5))
            	{
            		return (String) jsonObjectResult.get("file_id");
            	}
			}
			return null;
		}
		catch (IOException | ParseException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}

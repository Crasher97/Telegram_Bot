package bot;

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
	 * Crea un file per le impostazioni
	 */
	public static void createSettingFile()
	{
		if(settingFile.exists()) return;
		JSONObject obj = new JSONObject();
		try {
			FileWriter outFile = new FileWriter(settingFile);
			outFile.write(gson.toJson(obj));
			Log.info("Creato file Setting");
			outFile.flush();
			outFile.close();
			writeDefaultSettings();

		} catch (IOException e) {
			Log.stackTrace(e.getStackTrace());
		}
	}

	/**
	 * Scrive sul file le impostazioni di default
	 */
	public static void writeDefaultSettings()
	{
		addSetting("Bot_ID", "", "Main");
		addSetting("Owner_ID", "", "Main");
		addSetting("Update_Frequence", "1000", "Main");
	}

	/**
	 * Aggiunge un impostazione al file
 	 * @param key Chiave per recuperare l'impostazione
	 * @param value Valore default
	 * @param category Categoria dell'impostazione
	 */
	public static void addSetting(String key, String value, String category)
	{
		if(!settingFile.exists()) createSettingFile();
		if(settingExist(key, category)) return;
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
	 * Legge Un impostazione dal file
	 * @param key Chiave di ricerca
	 * @param category Categoria
	 * @return Valore dell'impostazione con la chiave di ricerca
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
	 * Modifica un impostazione
	 * @param key Chiave da modificare
	 * @param value Nuovo valore
	 * @param category Categoria della chiave
	 * @return True se la modifica è stata effettuata con successo altrimenti false
	 */
	public static boolean editSetting(String key, String value, String category)
	{
		if(!settingExist(key, category)) return false;
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
	 * Verifica se una particolare chiave in una certa categoria esiste
	 * @param key Chiave da cercare
	 * @param category Categoria in cui cercare
	 * @return true se la chiave esiste, false se non esiste
	 */
	public static boolean settingExist(String key, String category)
	{
		JSONParser parser = new JSONParser();
		JSONObject obj;
		try
		{
			obj = (JSONObject) parser.parse(new FileReader(settingFile));
			JSONObject setting = (JSONObject) obj.get(category);
			if(setting.get(key) == null) return false;
			if(setting.get(key) != "")
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
}

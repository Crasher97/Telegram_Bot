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
		//Main Setting
		addSetting("Bot_ID", "", "Main");
		addSetting("Owner_ID", "", "Main");
		addSetting("Update_Frequence", "1000", "Main");

		//Webhook setting
		addSetting("WebHook_Url", "", "WebHook");
		addSetting("WebHook_Local", "webhook", "WebHook");
		addSetting("WebHook_Port", "8443", "WebHook");
	}

	/**
	 * Aggiunge un impostazione al file
 	 * @param key Chiave per recuperare l'impostazione
	 * @param value Valore default
	 * @param category Categoria dell'impostazione
	 */
	@SuppressWarnings("unchecked")
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
	@SuppressWarnings("unchecked")
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
			if(setting == null) return false;
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
	
	/**
	 * Return last launching configuration
	 * @param idCode
	 * @param owner
	 * @return an array[2], where in [0] there is bot ID & in [1] there is the owner ID
	 */
	public static String[] readLastSettings()
	{
		String idCode = Setting.readSetting("Bot_ID", "Main");
		String owner = Setting.readSetting("Owner_ID", "Main");
		if(idCode != null && owner != null)
			{
				String[] tmp = new String[2];
				tmp[0] = idCode;
				tmp[1] = owner;
				return tmp;
			}
		return null;
	}
}

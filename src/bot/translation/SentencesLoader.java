package bot.translation;

import bot.Setting;
import bot.functions.JsonManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;


public class SentencesLoader
{
	public static boolean loadSentences()
	{
		Locale enLocale = new Locale(Setting.readSetting("Language", "Language"), Setting.readSetting("Region", "Language"));
		ResourceBundle bundle = ResourceBundle.getBundle("bot.translation.language", enLocale);
		Enumeration enumratore = bundle.getKeys();
		HashMap<String, String> sentences = Sentences.getSentences();
		if(!enumratore.hasMoreElements())
			return false;
		while (enumratore.hasMoreElements())
		{
			String key = (String) enumratore.nextElement();
			String value = bundle.getString(key);
			sentences.put(key,value);
		}
		return true;
	}
}

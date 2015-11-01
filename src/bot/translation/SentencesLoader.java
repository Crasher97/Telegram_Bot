package bot.translation;

import bot.Setting;
import bot.functions.JsonManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Paolo on 01/11/2015.
 */
public class SentencesLoader
{
	private static File file = new File("config/translation.json");
	public static boolean loadSentences()
	{
		ArrayList<String> sentences = Sentences.getSentences();
		JSONObject object = JsonManager.readJsonFromFile(file);
		if(object!=null)
		{
			JSONArray language = JsonManager.getArrayFromJson(object, Setting.readSetting("Country", "Language"));
			if(language != null && language.size() > 0)
			{
				JSONObject object2 = (JSONObject) language.get(0);
				sentences.add("null");
				sentences.add((String) object2.get("MESSAGE_RECEIVED"));
				sentences.add((String) object2.get("FROM"));
				sentences.add((String) object2.get("NEW_USER"));
				sentences.add((String) object2.get("HAS_CONNECTED"));
			}
		}
		return false;
	}
}

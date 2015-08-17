package bot;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class SimSimi
{
	private static final String SIMSIMI_URL = "http://www.simsimi.com/requestChat?lc=it&ft=1.0&req="; //Change the "it" in the url to the correct language

	public static String toSimSimi(String message)
	{
		String simSimiJSONReply = "";
		String simSimiReply = "";
		JSONParser parser = new JSONParser();
		try
		{
			Document doc = Jsoup.connect(SIMSIMI_URL + message).ignoreContentType(true).get();
			simSimiJSONReply = doc.text();
			JSONObject jsonObject = (JSONObject) parser.parse(simSimiJSONReply);
			simSimiReply = jsonObject.get("res").toString();
			return simSimiReply;

		}
		catch (Exception e)
		{
			Log.stackTrace(e.getStackTrace());
			return null;
		}
	}
}

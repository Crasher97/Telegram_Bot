package bot.functions;

import bot.log.Log;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class SimSimi
{
	private static final String SIMSIMI_URL = "http://www.simsimi.com/requestChat?lc=it&ft=1.0&req="; //Change the "it" in the url to the correct language

	/**
	 * Send to SimSimi the string passed as parameter
	 *
	 * @param message Message to send
	 * @return Reply form simsimi
	 */
	public static String toSimSimi(String message)
	{
		String simSimiJSONReply = "";
		String simSimiReply = "";
		JSONParser parser = new JSONParser();
		if (message != null)
		{
			try
			{
				Document doc = Jsoup.connect(SIMSIMI_URL + message).ignoreContentType(true).get();
				simSimiJSONReply = doc.text();
				JSONObject jsonObject = (JSONObject) ((JSONObject) parser.parse(simSimiJSONReply)).get("res");
				simSimiReply = jsonObject.get("msg").toString();
				return simSimiReply;
			}
			catch (Exception e)
			{
				Log.stackTrace(e.getStackTrace());
				return null;
			}
		}
		return null;
	}
}

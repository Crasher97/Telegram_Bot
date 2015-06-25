import java.io.IOException;

import org.jsoup.*;
import org.jsoup.nodes.*;


public class Main 
{
	private static String idCode = "";
	private static String url;
	
	public static void main(String[] args) 
	{
		idCode = args[0];
		url = "https://api.telegram.org/bot" + idCode;
		Sender.sendMessage(84954308, "MessaggiodiProvaSender");
	}
	
	public static String getIdCode()
	{
		return idCode;
	}
	
	public static String getUrl()
	{
		return url;
	}

	public String getUpdate()
	{
		Document doc;
		try 
			{
				doc = Jsoup.connect(url + "/getUpdates").get();
				String contenent = doc.text();
				return contenent;
			} 
		catch (IOException e) 
			{
				e.printStackTrace();
				return "";
			}
	}


}
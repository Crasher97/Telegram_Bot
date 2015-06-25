import java.io.IOException;

import org.jsoup.*;
import org.jsoup.nodes.*;


public class Main 
{
	private static String idCode = "";
	
	public static void main(String[] args) 
	{
		idCode = args[0];
	}
	
	public String getIdCode()
	{
		return idCode;
	}

	public String getUpdate()
	{
		String url = "https://api.telegram.org/bot" + idCode + "/getUpdates";
		Document doc;
		try 
			{
				doc = Jsoup.connect(url).get();
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
import java.io.IOException;

import org.jsoup.*;
import org.jsoup.nodes.*;


public class Main 
{
	public static void main(String[] args) 
	{
		final String idCode = args[0];
	}

public String getUpdate()
{
	
	String url = "https://api.telegram.org/bot" + idCode+ "/getUpdates";
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
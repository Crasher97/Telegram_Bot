import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class Reader 
{
	public static void getUpdate()
	{
		Document doc;
		try 
			{
				doc = Jsoup.connect(Main.getUrl() + "/getUpdates?limit=1").ignoreContentType(true).get();
				String contenent = doc.text();
			 //aggiungere metodo che chiama l'oggetto identifier, per identificare il comando etc
			} 
		catch (IOException e) 
			{
				e.printStackTrace();
			}
	}
}

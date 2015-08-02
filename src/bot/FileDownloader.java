package bot;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

import com.github.axet.vget.VGet;
import com.github.axet.vget.info.VGetParser;
import com.github.axet.vget.vhs.YouTubeMPGParser;

public class FileDownloader
{
	private static String returnThread;
	/**
	 * Scarica un file dall'url passato come parametro
	 * @param String url - file to be downloaded
 	 * @return true se il download è andato a buon fine, false se è fallito //TODO RETURN BOOLEAN
	 */
	public static String downloadFile(String url)
	{	
		final String urlF = url;
	        	try
	     		{
	     			String[] urlPart = urlF.split("/");
	     			String fileName = urlPart[urlPart.length - 1];
	     			URL link = new URL(urlF);
	     			FileUtils.copyURLToFile(link, new File("tmp/" + fileName));
	     			System.out.println("Download Finished");
	     			returnThread = fileName;
	     		}
	     		catch (IOException e)
	     		{
	     			e.printStackTrace();
	     			returnThread = null;
	     		}
		return returnThread;
	}
	
	/**
	 * Scarica il video da youtube dato l'url e ritorna il nome del file salvato
	 * @param String url
	 * @return String filename, il nome del file
	 */
	public static String downloadVideo(String url)
	{
		try 
		{
			VGetParser user = null;
			user = new YouTubeMPGParser();
			VGet v = new VGet(new URL(url), new File("tmp/"));
	        v.download(user);
	        return v.getTarget().getName();
	    } catch (Exception e) {
	       e.printStackTrace();
	        return null;
	    }
	}
	
}


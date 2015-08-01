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
	 * @param url
	 * @return true se il download è andato a buon fine, false se è fallito //TODO RETURN BOOLEAN
	 */
	public static String downloadFile(String url)
	{	
		final String urlF = url;
		Thread thread = new Thread(new Runnable() {
	         public void run() {
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
	         }
	    });
		thread.start();
		return returnThread;
	}
	
	//TODO COMMENTI
	public static String downloadVideo(String url)
	{
		try 
		{
			VGetParser user = null;
			user = new YouTubeMPGParser();
			VGet v = new VGet(new URL(url), new File("tmp/"));
	        v.download(user);
	        return v.getVideo().getTitle() + ".mp4";
	    } catch (Exception e) {
	       e.printStackTrace();
	        return null;
	    }
	}
	
}


package addons;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Callable;

import org.apache.commons.io.FileUtils;

public class FileDownloader
{
	/**
	 * Scarica un file dall'url passato come parametro
	 * @param url
	 * @return true se il download � andato a buon fine, false se � fallito
	 */
	public static void downloadFile(String url)
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
	     		}
	     		catch (IOException e)
	     		{
	     			e.printStackTrace();
	     		}
	         }
	    });
		thread.start();
	}
}


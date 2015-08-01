package bot;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

import com.github.axet.vget.VGet;

public class FileDownloader
{
	private static String nomeFile;
	/**
	 * Scarica un file dall'url passato come parametro
	 * @param url
	 * @return true se il download è andato a buon fine, false se è fallito //TODO RETURN BOOLEAN
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
	     			nomeFile = fileName;
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
	
	//TODO COMMENTI
	public static boolean downloadVideo(String url)
	{
		try 
		{
			VGet v = new VGet(new URL(url), new File("tmp/"));
	        v.download();
	        nomeFile = v.getVideo().getTitle() + ".webm";
	        return true;
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
	
	/**
	 * getnomefile
	 * @return String nomefile, il nome dell'ultimo file scaricato
	 */
	public static String getNomeFile()
	{
		return nomeFile;
	}
}


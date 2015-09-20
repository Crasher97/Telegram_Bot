package bot.functions;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

import com.github.axet.vget.VGet;
import com.github.axet.vget.info.VGetParser;
import com.github.axet.vget.vhs.YouTubeMPGParser;

import bot.Log;

public class FileDownloader
{
	private static String returnThread;

	/**
	 * Download a file from url
	 *
	 * @param url - File to be downloaded
	 * @return name of file, it download fails return null
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
			Log.info("Download Finished");
			returnThread = fileName;
		}
		catch (IOException e)
		{
			Log.error("Download Error");
			Log.stackTrace(e.getStackTrace());
			returnThread = null;
		}
		return returnThread;
	}

	/**
	 * Download video from youtube
	 *
	 * @param url Youtube url
	 * @return name of video
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
		}
		catch (Exception e)
		{
			Log.stackTrace(e.getStackTrace());
			return null;
		}
	}

	/**
	 * Metodo da realizzare quando le API verranno aggiornate e lo permetteranno TODO download from telegram
	 *
	 * @param fileId
	 * @return
	 */
	public static String downloadFileFormTelegram(String fileId)
	{
		return null;
	}

}


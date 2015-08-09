package bot.webServer;

import bot.Log;
import bot.Main;
import bot.Setting;
import org.jsoup.Jsoup;

import java.io.IOException;

public class WebHook
{
	public static void setWebHook(String ip)
	{
		try
		{
			Jsoup.connect(Main.getUrl() + "/setWebhook" + "?url=" + ip + "/" + Setting.readSetting("Bot_ID", "Main")).ignoreContentType(true).post();
		}
		catch (IOException e)
		{
			Log.stackTrace(e.getStackTrace());
		}
	}

	public static void unsetWebHook()
	{
		try
		{
			Jsoup.connect(Main.getUrl() + "/setWebhook" + "?url=").ignoreContentType(true).post();
		}
		catch (IOException e)
		{
			Log.stackTrace(e.getStackTrace());
		}
	}
}

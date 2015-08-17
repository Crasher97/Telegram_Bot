package bot.webServer;

import bot.Log;
import bot.Main;
import bot.Setting;
import org.jsoup.Jsoup;

import java.io.IOException;

public class WebHook
{
	public static void setWebHook()
	{
		try
		{
			Jsoup.connect(Main.getUrl() + "/setWebhook" + "?url=" + Setting.readSetting("WebHook_Url", "WebHook")).ignoreContentType(true).post();
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

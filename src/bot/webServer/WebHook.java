package bot.webServer;

import bot.Log;
import bot.Main;
import bot.Setting;
import bot.functions.UploadedFileLogger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;

public class WebHook
{
	/**
	 * Set the webhook url and certificate
	 */
	public static void setWebHook()
	{
		HttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost("https://api.telegram.org/bot" + Main.getIdCode() + "/setWebhook" + "?url=" + Setting.readSetting("WebHook_Url", "WebHook") + ":" + Setting.readSetting("WebHook_Port", "WebHook"));

		File file = new File(Setting.readSetting("Certificate", "WebHookServer"));
		FileBody uploadFilePart = new FileBody(file);
		MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
		reqEntity.addPart("certificate", uploadFilePart);
		httpPost.setEntity(reqEntity.build());

		try
		{
			HttpResponse response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			String content = EntityUtils.toString(entity);
			System.out.println(content);
		}
		catch (IOException e)

		{
			Log.stackTrace(e.getStackTrace());
		}

	}

	/**
	 * Unset webhook
	 */
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

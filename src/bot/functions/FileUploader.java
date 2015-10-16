package bot.functions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;

import bot.Log;
import bot.Main;

import java.io.File;
import java.io.IOException;

/**
 * Uploads file
 */
public class FileUploader
{
	public enum FileType
	{
		PHOTO, AUDIO, VIDEO, DOCUMENT
	}

	/**
	 * Send files
	 *
	 * @param fileName
	 * @param chatId where file has to be sent.
	 * @param fileType {PHOTO, AUDIO, VIDEO, DOCUMENT}
	 * @return true if file has been sent
	 */
	public static boolean uploadFile(String fileName, long chatId, FileType fileType)
	{
		String urlMethod = "";
		String urlParameter = "";
		switch (fileType)
		{
			case PHOTO:
			{
				urlMethod = "/sendPhoto";
				urlParameter = "photo";
			}
			break;

			case AUDIO:
			{
				urlMethod = "/sendAudio";
				urlParameter = "audio";
			}
			break;

			case VIDEO:
			{
				urlMethod = "/sendVideo";
				urlParameter = "video";
			}
			break;

			case DOCUMENT:
			{
				urlMethod = "/sendDocument";
				urlParameter = "document";
			}
		}

		String file_id = UploadedFileLogger.getFileId("tmp/" + fileName);
		if (file_id == null)
		{
			HttpClient httpclient = HttpClientBuilder.create().build();
			HttpPost httpPost = new HttpPost("https://api.telegram.org/bot" + Main.getIdCode() + urlMethod + "?chat_id=" + chatId);

			File file = new File("tmp/" + fileName);
			FileBody uploadFilePart = new FileBody(file);
			MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
			reqEntity.addPart(urlParameter, uploadFilePart);
			httpPost.setEntity(reqEntity.build());

			try
			{
				HttpResponse response = httpclient.execute(httpPost);
				HttpEntity entity = response.getEntity();
				String content = EntityUtils.toString(entity);
				UploadedFileLogger.addToFileLog("tmp/" + fileName, content, UploadedFileLogger.Type.valueOf(fileType.toString()));

				return true;
			}
			catch (IOException e)
			{
				Log.stackTrace(e.getStackTrace());
				return false;
			}
		}
		else
		{
			try
			{
				Jsoup.connect(Main.getUrl() + urlMethod + "?chat_id=" + chatId + "&" + urlParameter + "=" + file_id).ignoreContentType(true).post();
				return true;
			}
			catch (IOException e)
			{
				Log.stackTrace(e.getStackTrace());
				return false;
			}
		}
	}
}


package bot.functions;

import bot.log.UploadedFileLogger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;

import bot.log.Log;
import bot.Main;

import java.io.File;
import java.io.IOException;
import java.nio.file.attribute.FileAttributeView;

/**
 * Uploads file
 */
public class FileUploader
{
	private static final String METHOD_SEND_PHOTO = "/sendPhoto";
	private static final String METHOD_SEND_VIDEO = "/sendVideo";
	private static final String METHOD_SEND_AUDIO = "/sendAudio";
	private static final String METHOD_SEND_DOC = "/sendDocument";


	public enum FileType
	{
		PHOTO, AUDIO, VIDEO, DOCUMENT
	}

	/**
	 * Send files to specified chat in telegram
	 * @param file
	 * @param chatId where file has to be sent.
	 * @param fileType {PHOTO, AUDIO, VIDEO, DOCUMENT}
	 * @return true if file has been sent
	 */
	public static boolean uploadFile(File file, long chatId, FileType fileType)
	{
		String urlMethod = "";
		String urlParameter = "";
		String additionalInformation = "";
		switch (fileType)
		{
			case PHOTO:
			{
				urlMethod = METHOD_SEND_PHOTO;
				urlParameter = "photo";
				Sender.sendChatAction(Sender.ChatAction.UPLOADING_PHOTO, chatId);
			}
			break;

			case AUDIO:
			{
				urlMethod = METHOD_SEND_AUDIO;
				urlParameter = "audio";
				Sender.sendChatAction(Sender.ChatAction.UPLOADING_AUDIO, chatId);
			}
			break;

			case VIDEO:
			{
				urlMethod = METHOD_SEND_VIDEO;
				urlParameter = "video";
				Sender.sendChatAction(Sender.ChatAction.UPLOADING_VIDEO, chatId);
			}
			break;

			case DOCUMENT:
			{
				urlMethod = METHOD_SEND_DOC;
				urlParameter = "document";
				Sender.sendChatAction(Sender.ChatAction.UPLOADING_DOCUMENT, chatId);
			}
		}

		String file_id = UploadedFileLogger.getFileId("tmp/" + file.getName());
		if (file_id == null)
		{
			HttpClient httpclient = HttpClientBuilder.create().build();
			HttpPost httpPost = new HttpPost("https://api.telegram.org/bot" + Main.getIdCode() + urlMethod + "?chat_id=" + chatId);

			FileBody uploadFilePart = new FileBody(file);
			MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
			reqEntity.addPart(urlParameter, uploadFilePart);
			httpPost.setEntity(reqEntity.build());

			try
			{
				HttpResponse response = httpclient.execute(httpPost);
				HttpEntity entity = response.getEntity();
				String content = EntityUtils.toString(entity);
				UploadedFileLogger.addToFileLog("tmp/" + file.getName(), content, UploadedFileLogger.Type.valueOf(fileType.toString()));
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


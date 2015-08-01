package bot;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;

public class ImagesUploader
	{

		/**
		 * Metodo per passare come parametro un messaggio al posto del sender id
		 * 
		 * @param Message message
		 * @param String fileName - il nome dell'immagine da aricare in /tmp
		 * @return true se l'invio è andato a buon fine, false altrimenti
		 */
		public static boolean uploadImage(Message message, String fileName)
		{
			return uploadImage(message.getSender_id(), fileName);
		}
		
		/**
		 * Metodo che invia una foto
		 * 
		 * @param senderId - l'id della persona a cui spedire l'immagine
		 * @param fileName - Nome del file immagine da inviare, presente nella cartella tmp
		 * @return true se l'invio è andato a buon fine, false altrimenti
		 */
		public static boolean uploadImage(long senderId, String fileName)
		{
			String file_id = UploadedFileLogger.getFileId("tmp/" + fileName);
			if(file_id == null)
			{
				HttpClient httpclient = HttpClientBuilder.create().build();
				HttpPost httpPost = new HttpPost("https://api.telegram.org/bot" + Main.getIdCode() + "/sendPhoto?chat_id=" + senderId);

				File photo = new File("tmp/" + fileName);
				FileBody uploadFilePart = new FileBody(photo);
				MultipartEntityBuilder  reqEntity = MultipartEntityBuilder.create();
				reqEntity.addPart("photo", uploadFilePart);
				httpPost.setEntity(reqEntity.build());

				try
				{
					HttpResponse response = httpclient.execute(httpPost);
					HttpEntity entity = response.getEntity();
					String content = EntityUtils.toString(entity);
					UploadedFileLogger.addToFileLog("tmp/" + fileName, content);

					return true;
				}
				catch (IOException e)
				{
					e.printStackTrace();
					return false;
				}
			}
			else
			{
				try
				{
					Jsoup.connect(Main.getUrl() + "/sendPhoto?chat_id=" + senderId + "&photo=" + file_id).ignoreContentType(true).post();
					return true;
				}
				catch (IOException e)
				{
					e.printStackTrace();
					return false;
				}
			}
		}
		
	}

package addons;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import bot.ExternalExecuter;
import bot.Main;
import bot.Message;

public class ImagesUploader
	{

		/**
		 * Metodo per passare come parametro un messaggio al posto del sender id
		 * 
		 * @param message
		 * @param fileName
		 * @return true se l'invio è andato a buon fine, false altrimenti
		 */
		public static boolean uploadImage(Message message, String fileName)
		{
			return uploadImage(message.getSender_id(), fileName);
		}
		
		/**
		 * Metodo che invia una foto
		 * 
		 * @param senderId
		 * @param fileName - Nome del file immagine da inviare, presente nella cartella tmp
		 * @return true se l'invio è andato a buon fine, false altrimenti
		 */
		public static boolean uploadImage(long senderId, String fileName)
		{
			HttpClient httpclient = HttpClientBuilder.create().build();
			HttpPost httpPost = new HttpPost("https://api.telegram.org/bot" + Main.getIdCode() + "/sendPhoto?chat_id=" + senderId);

			File photo = new File("tmp/" + fileName);
			FileBody uploadFilePart = new FileBody(photo);
			MultipartEntityBuilder  reqEntity = MultipartEntityBuilder.create();//
			reqEntity.addPart("photo", uploadFilePart);
			httpPost.setEntity(reqEntity.build());

			try
			{
				HttpResponse response = httpclient.execute(httpPost);
				return true;
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return false;
			}
			
		}
		
		/**
		 * Carica un'immagine dato il nome(nella cartella \tmp)
		 * @param message
		 * @param name
		 * @return
		 */
		public static boolean uploadImageOld(Message message, String name)
		{
			boolean uploaded = false;
			
			uploaded = ExternalExecuter.executeCmd("curl -s -X POST \"https://api.telegram.org/bot" + Main.getIdCode() + "/sendPhoto\" -F chat_id="+ message.getSender_id() +" -F photo=\"@\"" + bot.Main.getLocation() + "\\tmp\\" + name +"\"", "imagesLog");
			
			return uploaded;
		}
	}

package bot;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;

public class VideoUploader
	{
		/**
		 * Carica un video data la posizione assoluta
		 * @param message 
		 * @param path
		 * @return true se il file � stato uploadato
		 */
		public static boolean oldUpload(Message message, String path)
		{
			return bot.ExternalExecuter.executeCmd("curl -s -X POST \"https://api.telegram.org/bot" + Main.getIdCode() + "/sendVideo\" -F chat_id=46365292 -F video=\"@" + path + "\"", "ytLog");
		}
		
		/**
		 * Carica un video dato l'url di youtube e il messaggio
		 * @param message
		 * @param url
		 * @return true se il file � stato uploadato
		 */
		public static boolean ytUpload(Message message, String url)
		{
			return ytUpload(message.getSender_id(), url);
		}
		
		/**
		 * Carica un video dato l'url di youtube e l'id del destinatario
		 * @param senderId
		 * @param url
		 * @return true se il file � stato uploadato
		 */
		public static boolean ytUpload(long senderId, String url)
		{
			boolean caricato = false;
			String scaricato = "";
			
			//DOWNLOAD VIDEO
			System.out.println("Scaricamento video avviato");
			scaricato = FileDownloader.downloadVideo(url);
			
			//UPLOAD VIDEO
			
			if(scaricato != null)
				{

					System.out.println("Scaricamento video completato");
					System.out.println("Caricamento video avviato");
					HttpClient httpclient = HttpClientBuilder.create().build();
					HttpPost httpPost = new HttpPost("https://api.telegram.org/bot" + Main.getIdCode() + "/sendVideo?chat_id=" + senderId);

					File video = new File("tmp/" + scaricato);
					FileBody uploadFilePart = new FileBody(video);
					MultipartEntityBuilder  reqEntity = MultipartEntityBuilder.create();//
					reqEntity.addPart("video", uploadFilePart);
					httpPost.setEntity(reqEntity.build());

					try
					{
						HttpResponse response = httpclient.execute(httpPost);
						System.out.println(response);
						caricato = true;
					}
					catch (IOException e)
					{
						e.printStackTrace();
						caricato = false;
					}
				}
			return caricato;	
		}
	}

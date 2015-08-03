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

public class VideoUploader
	{
		/**
		 * Carica un video data la posizione assoluta
		 * @param message 
		 * @param path
		 * @return true se il file è stato uploadato
		 */
		public static boolean oldUpload(Message message, String path)
		{
			return bot.ExternalExecuter.executeCmd("curl -s -X POST \"https://api.telegram.org/bot" + Main.getIdCode() + "/sendVideo\" -F chat_id=46365292 -F video=\"@" + path + "\"", "ytLog");
		}
		
		/**
		 * Carica un video dato l'url di youtube e il messaggio
		 * @param message
		 * @param url
		 * @return true se il file è stato uploadato
		 */
		public static boolean ytUpload(Message message, String url)
		{
			return ytUpload(message.getSender_id(), url);
		}
		
		/**
		 * Carica un video dato l'url di youtube e l'id del destinatario
		 * @param senderId
		 * @param url
		 * @return true se il file è stato uploadato
		 */
		public static boolean ytUpload(long senderId, String url)
		{
			boolean caricato = false;
			String scaricato = "";
			
			//DOWNLOAD VIDEO
			Log.info("Scaricamento video avviato");
			scaricato = FileDownloader.downloadVideo(url);
			
			//UPLOAD VIDEO
			
			if(scaricato != null)
				{
					String file_id = UploadedFileLogger.getFileId("tmp/" + scaricato);
					if(file_id == null)
					{
						Log.info("Scaricamento video completato");
						Log.info("Caricamento video avviato");
						HttpClient httpclient = HttpClientBuilder.create().build();
						HttpPost httpPost = new HttpPost("https://api.telegram.org/bot" + Main.getIdCode() + "/sendVideo?chat_id=" + senderId);

						File video = new File("tmp/" + scaricato);
						FileBody uploadFilePart = new FileBody(video);
						MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();//
						reqEntity.addPart("video", uploadFilePart);
						httpPost.setEntity(reqEntity.build());

						try
						{
							HttpResponse response = httpclient.execute(httpPost);
							HttpEntity entity = response.getEntity();
							String content = EntityUtils.toString(entity);
							UploadedFileLogger.addToFileLog("tmp/" + scaricato, content, UploadedFileLogger.Type.VIDEO);
							//System.out.println(response);
							caricato = true;
						} catch (IOException e)
						{
							e.printStackTrace();
							caricato = false;
						}
					}
					else
					{
						try
						{
							Jsoup.connect(Main.getUrl() + "/sendVideo?chat_id=" + senderId + "&video=" + file_id).ignoreContentType(true).post();
							return true;
						}
						catch (IOException e)
						{
							e.printStackTrace();
							return false;
						}
					}
				}
			return caricato;	
		}
	}

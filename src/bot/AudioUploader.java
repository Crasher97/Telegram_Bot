package bot;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;

public class AudioUploader
	{
		public static boolean uploadAudio(File audio, long senderId)
		{
			boolean caricato = false;
			
			//UPLOAD VIDEO
			
			if(audio != null)
				{
					System.out.println("Caricamento Audio avviato");
					HttpClient httpclient = HttpClientBuilder.create().build();
					HttpPost httpPost = new HttpPost("https://api.telegram.org/bot" + Main.getIdCode() + "/sendDocument?chat_id=" + senderId);

					FileBody uploadFilePart = new FileBody(audio);
					MultipartEntityBuilder  reqEntity = MultipartEntityBuilder.create();//
					reqEntity.addPart("video", uploadFilePart);
					httpPost.setEntity(reqEntity.build());

					try
					{
						HttpResponse response = httpclient.execute(httpPost);
						System.out.println("Caricamento Audio completato");
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

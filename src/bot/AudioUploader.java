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

public class AudioUploader
	{
		public static boolean uploadAudio(File audio, long senderId)
		{
			boolean caricato = false;
			if(audio != null)
				{
					String file_id = UploadedFileLogger.getFileId("tmp/" + audio.getName());
					System.out.println(file_id);
					if(file_id == null)
					{
						System.out.println("Caricamento Audio avviato");
						HttpClient httpclient = HttpClientBuilder.create().build();
						HttpPost httpPost = new HttpPost("https://api.telegram.org/bot" + Main.getIdCode() + "/sendDocument?chat_id=" + senderId);

						FileBody uploadFilePart = new FileBody(audio);
						MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();//
						reqEntity.addPart("document", uploadFilePart);
						httpPost.setEntity(reqEntity.build());

						try
						{
							HttpResponse response = httpclient.execute(httpPost);
							HttpEntity entity = response.getEntity();
							String content = EntityUtils.toString(entity);
							UploadedFileLogger.addToFileLog("tmp/" + audio.getName(), content, UploadedFileLogger.Type.DOCUMENT);
							System.out.println("Caricamento Audio completato");
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
							Jsoup.connect(Main.getUrl() + "/sendDocument?chat_id=" + senderId + "&document=" + file_id).ignoreContentType(true).post();
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

package addons;
import addons.Command;
		import addons.Commands;
		import bot.Message;
		import bot.Sender;
		import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import bot.AudioUploader;
		import bot.EncoderVA;
		import bot.FileDownloader;

		public class TestAdd 
			{
				/**
				 * Funzione yt scarica un video di youtube e lo invia
				 * @param msg
				 */
				public static void functionYt(Message msg)
				{
					String text[] = msg.getText().split(" ");
					if(text.length > 1)
						{
							System.out.println("Function YT");
							if(bot.VideoUploader.ytUpload(msg, text[1]))
								{
									System.out.println("Video caricato");
								}
							else
								{
									System.err.println("Video non caricato");
									bot.Sender.sendMessage(msg.getSender_id(), "Errore nel caricamento del video");
								}
						}
					else
						{
							Sender.sendMessage(msg.getSender_id(), "not enough params");
						}
				}
				
				
				/**
				 * Funzione yt scarica un video di youtube e lo invia
				 * @param msg
				 */
				public static void functionYtAudio(Message msg)
				{
							
					String text[] = msg.getText().split(" ");
					if(text.length > 1)
						{
							System.out.println("Function YTA");
							String fileName = FileDownloader.downloadVideo(text[1]);
							File video = new File("tmp/" + fileName);
							String fileAudioName = fileName.split("mp4")[0] + "mp3";
							File audio = new File("tmp/" + fileAudioName);
							try
								{
									FileWriter outFile = new FileWriter(audio);
									outFile.flush();
						            outFile.close();
								} 
							catch (IOException e)
								{
									System.err.println("Error while trying to create new mp3 file");
									e.printStackTrace();
								}
							if(fileName != null)
								{
									EncoderVA.mp4ToMp3(video, audio);
									AudioUploader.uploadAudio(audio, msg.getSender_id());
								}
						}
					else
						{
							Sender.sendMessage(msg.getSender_id(), "not enough params");
						}
				}
				
				
				/**
				 * Load command in telegram bot
				 */
				public void load()
				{
					Commands.addCommand(new Command("yt", "Ytube.jar", "Main", "functionYt"));
					Commands.addCommand(new Command("yta", "Ytube.jar", "Main", "functionYtAudio"));
				}
			}



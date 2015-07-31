package bot;

public class VideoUploader
	{
		/**
		 * Carica un video data la posizione assoluta
		 * @param message
		 * @param path
		 * @return true se il file è stato uploadato
		 */
		public static boolean upload(Message message, String path)
		{
			return bot.ExternalExecuter.executeCmd("curl -s -X POST \"https://api.telegram.org/bot" + Main.getIdCode() + "/sendVideo\" -F chat_id=46365292 -F video=\"@" + path + "\"", "ytLog");
		}
	}

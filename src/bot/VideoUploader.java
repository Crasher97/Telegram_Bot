package bot;

public class VideoUploader
	{
		public static boolean upload(Message message, String path)
		{
			return bot.ExternalExecuter.executeCmd("curl -s -X POST \"https://api.telegram.org/bot" + Main.getIdCode() + "/sendVideo\" -F chat_id=46365292 -F video=\"@" + path + "\"", "ytLog");
		}
	}

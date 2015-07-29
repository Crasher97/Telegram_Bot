package addons;

import bot.ExternalExecuter;
import bot.Main;
import bot.Message;

public class ImagesUploader
	{
		public static boolean uploadImage(Message message, String name)
		{
			boolean uploaded = false;
			
			uploaded = ExternalExecuter.executeCmd("curl -s -X POST \"https://api.telegram.org/bot" + Main.getIdCode() + "/sendPhoto\" -F chat_id="+ message.getSender_id() +" -F photo=\"@\"" + bot.Main.getLocation() + "\\tmp\\" + name +"\"", "imagesLog");
			
			return uploaded;
		}
	}

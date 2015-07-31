package bot;

public class ImagesUploader
	{
		/**
		 * CArica un'immagine dato il nome(nella cartella \tmp)
		 * @param message
		 * @param name
		 * @return
		 */
		public static boolean uploadImage(Message message, String name)
		{
			boolean uploaded = false;
			
			uploaded = ExternalExecuter.executeCmd("curl -s -X POST \"https://api.telegram.org/bot" + Main.getIdCode() + "/sendPhoto\" -F chat_id="+ message.getSender_id() +" -F photo=\"@\"" + bot.Main.getLocation() + "\\tmp\\" + name +"\"", "imagesLog");
			
			return uploaded;
		}
	}

package bot;
import addons.Help;
import addons.JarFileLoader;
public class Main
{
	private static String idCode = "";
	private static String owner = "";
	private static String location = "";
	private static String url = "";
	private static String wgetLocation;

	/**
	 * Metodo main, punto di partenza del programma, terminare inviando un messaggio con scritto /stop
	 * 
	 * @param args, IN ORDER:l'idCode del bot telegram, l'id del propietario, 
	 */
	public static void main(String[] args)
	{
		// configurazione all'avvio
		idCode = args[0];
		owner = args[1];
		url = "https://api.telegram.org/bot" + idCode;
		//Sender.sendMessage(Integer.parseInt(args[1]), "MessaggiodiProvaSender");
		// Al 84954308 Pa 84985065
		
		//CARICAMENTO ADDONS
		JarFileLoader.loadJarFile();
		Help.load();
		Console.loadCommand();
		Console.openConsole();
	    //addons.testAdd.upload(new Message(0, 0, 84985065, "paolo", "paolo", null, "/uplimg -nasa"));
		//bot.VideoUploader.upload(new Message(0, 0, 84985065, "paolo", "paolo", null, "/uplimg -nasa"), "C:\\Users\\Paolo\\Videos\\Desktop\\Desktop 04.27.2015 - 18.47.14.02.mp4");
		
		// ESEGUZIONE COMANDI ALLA CHIUSUSRA
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			public void run()
			{
				try
				{
					// In chiusura salva i messaggi nel log
					IO.writeOUT("log", Messages.getArray());
					System.out.println("Terminato");
					
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});

		// CONTROLLO NUOVI MESSAGGI
		while (true)
		{
			Reader.getUpdate();
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * getIdCode - restituisce il codice identificativo
	 * 
	 * @return String idCode
	 */
	public static String getIdCode()
	{
		return idCode;
	}

	/**
	 * Restituisce l'url di riferimento del bot
	 * 
	 * @return String - url
	 */
	public static String getUrl()
	{
		return url;
	}
	
	/**
	 * ritorna l'id del propietario del bot
	 * @return String owner - il proprietario
	 */
	public static String getOwner()
	{
		return owner;
	}
}
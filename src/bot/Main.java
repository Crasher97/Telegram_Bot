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
	 * @param args, l'idCode del bot telegram, deve essere inserito
	 */
	public static void main(String[] args)
	{
		// configurazione all'avvio
		idCode = args[0];
		owner = args[1];
		location = args[2];
		wgetLocation = args[3];
		url = "https://api.telegram.org/bot" + idCode;
		//Sender.sendMessage(Integer.parseInt(args[1]), "MessaggiodiProvaSender");
		// Al 84954308 Pa 84985065
		
		//CARICAMENTO ADDONS
		JarFileLoader.loadJarFile();
		Help.load();
		Console.loadCommand();
		Console.openConsole();
	    //addons.testAdd.upload(new Message(0, 0, 84985065, "paolo", "paolo", null, "/uplimg -nasa"));
		
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
	 * ritorna owner, il propietario del bot
	 * @return String owner of bot
	 */
	public static String getOwner()
	{
		return owner;
	}
	
	/**
	 * Ritorna la posizione del bot
	 * @return String - path folder of bot
	 */
	public static String getLocation()
	{
		return location;
	}
	
	/**
	 * Ritorna la posizione di wget(only windows)
	 * @return
	 */
	public static String getWgetLocation()
	{
		return wgetLocation;
	}
}
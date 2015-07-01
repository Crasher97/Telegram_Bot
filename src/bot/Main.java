package bot;
import addons.Help;
import addons.JarFileLoader;

public class Main
{
	private static String idCode = "";
	private static String url;

	/**
	 * Metodo main, punto di partenza del programma
	 * 
	 * @param args
	 *            , l'idCode del bot telegram, deve essere inserito
	 */
	public static void main(String[] args)
	{
		// configurazione all'avvio
		idCode = args[0];
		url = "https://api.telegram.org/bot" + idCode;
		//Sender.sendMessage(Integer.parseInt(args[1]), "MessaggiodiProvaSender");
		// Al 84954308 Pa 84985065
		
		//CARICAMENTO ADDONS
		JarFileLoader.loadJarFile();
		Help.load();
		
		
		// ESEGUZIONE COMANDI ALLA CHIUSUSRA
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			public void run()
			{
				try
				{
					// In chiusura salva i messaggi nel log
					IO.writeOUT("log.log", Messages.getArray());
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

}
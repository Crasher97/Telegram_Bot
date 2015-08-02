package bot;
import java.io.File;

import addons.Commands;
import addons.Help;
import addons.JarFileLoader;

public class Main
{
	private static String idCode = "";
	private static String owner = "";
	private static String url = "";

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
		System.out.println(Commands.getCommands().keySet().toString());
		Console.loadCommand();
		Console.openConsole();
		//addons.TestAdd.functionYtAudio(new Message(0,0,9,"paolo","d",null,"/yta https://www.youtube.com/watch?v=36sambtCsGA"));
	
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
		
		// ESEGUZIONE ELIMINAZIONE FILE 
		
		Thread thread = new Thread(new Runnable() {
	         public void run() {
	        	while(true)
	        		{
	        			try
	        				{
	        					File dir = new File("tmp\\");
	        					File[] directoryListing = dir.listFiles();
	        					if(directoryListing != null)
	        						{
	        							for(File file : directoryListing)
	        								{
	        									if((System.currentTimeMillis() - file.lastModified()) > 259200000)
	        										{
	        											if(file.delete())
	        												{
	        													IO.writeOUT("DeleteLog", "File " + file.getName() + " CANCELLATO");
	        													System.out.print("File Deleted");
	        												}
	        										}
	        								}
	        						}
	    	        			Thread.sleep(43200000);
	        				}
	        			catch (Exception e)
	        				{
	        					System.err.println("Errore durante eliminazione file");
	        				}
	        		}
	        }
		});
		thread.start();
	
		
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
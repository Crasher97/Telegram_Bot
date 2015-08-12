package bot;
import java.io.File;
import java.util.ArrayList;

import addons.Commands;
import addons.Help;
import addons.JarFileLoader;

public class Main
{
	private static String idCode = "";
	private static String owner = "";
	private static String url = "";
	private static String update = "";
	/**
	 * Main method
	 * @param botId - get it from bot Father
	 * @param ownerId - your telegram id
	 */
	public static void main(String[] args)
	{
		idCode = args[0];
		owner = args[1];
		url = "https://api.telegram.org/bot" + idCode;
		
		//LOAD EXTERNAL ADDONS
		JarFileLoader.loadJarFile();
		
		//LOAD INTERNAL COMMANDS
		Help.load();
		
		//PRINT LIST OF LOADED COMMANDS
		System.out.println(Commands.getCommands().keySet().toString());
		
		//LOAD CONSOLE COMMANDS
		Console.loadCommand();
		Console.openConsole();
		
		//CREATE SETTING FILE (NOT ALREADY USED) TODO use setting file
		Setting.createSettingFile();

		//addons.TEst.functionYtAudio(new Message(0,0,84985065,"paolo","d",null,"/yta https://www.youtube.com/watch?v=36sambtCsGA"));

		//THREAD STARTS WHEN PROGRAM HAS BEEN TERMINATED
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			public void run()
			{
				try
				{
					//Saves messages into log
					Messages.printLog();
					Log.info("Terminated");

				} catch (Exception e)
				{
					Log.stackTrace(e.getStackTrace());
				}
			}
		});
		
		//DELETE FILES OLDER THAN 3 DAYS EVERY 12 HOURS
		Thread thread = new Thread(new Runnable() {
	         public void run()
			 {
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
										IO.writeOUT("DeleteLog", "File " + file.getName() + " DELETED");
										System.out.print("File Deleted");
									}
								}
							}
						}
						Thread.sleep(43200000);
					}
					catch (Exception e)
					{
						Log.error("Error deleting file");
					}
				}
	        }
		});
		thread.start();
	
		
		//CHECK FOR NEW UPDATES, STARTS NEW THREAD FOR EVERY UPDATE
	while (true)
		{
			String tmp = UpdatesReader.getUpdate();
			if(tmp != null)
				{
					update = tmp;
					ArrayList<Message> updates = UpdatesReader.parseJSON(update);
					if(updates.size() > 0)
						{
							for(Message msg : updates)
								{
									Thread updateThread = new Thread(new Runnable() {
								         public void run()
										 {
											Commands.exeCommand(msg.getText().substring(1).split(" ")[0], msg);
										 }
									});
									updateThread.start();
								}
						}
				}
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
				Log.stackTrace(e.getStackTrace());
			}
		}
	}

	/**
	 * Return bot idCode
	 * 
	 * @return idCode
	 */
	public static String getIdCode()
	{
		return idCode;
	}

	/**
	 * Return url of bot
	 * 
	 * @return url
	 */
	public static String getUrl()
	{
		return url;
	}
	
	/**
	 * Return owner's id code.
	 * @return owner
	 */
	public static String getOwner()
	{
		return owner;
	}
	
	/**
	 * Return last received update
	 * @return lastUpdate - JSON format
	 */
	public static String getLastUpdate()
	{
		return update;
	}
}
package bot;

import java.io.File;
import java.util.ArrayList;

import addons.Commands;
import addons.Help;
import addons.JarFileLoader;
import bot.functions.DownloadedFileLogger;

public class Main
{
	private static String botId = "";
	private static String owner = "";
	private static String url = "";
	private static String update = "";

	/**
	 * Main method
	 *
	 * @param args botId   - get it from bot Father
	 * @param args ownerId - your telegram id
	 */
	public static void main(String[] args)
	{
		if (args.length == 2)
		{
			server(args);
		}
		else
		{
			GUI.Gui.startGui();
		}
	}

	/**
	 * Start server without gui
	 */
	public static void server(String[] args)
	{
		botId = args[0];
		owner = args[1];
		url = "https://api.telegram.org/bot" + botId;

		//LOAD EXTERNAL ADDONS
		JarFileLoader.loadJarFile();

		//LOAD INTERNAL COMMANDS
		Help.load();

		//PRINT LIST OF LOADED COMMANDS
		Log.info(Commands.getCommands().keySet().toString());

		//LOAD CONSOLE COMMANDS
		Console.loadCommand();
		Console.openConsole();

		//CREATE SETTING FILE
		Setting.createSettingFile();

		//START THREADS
		shutDownThread();
		deletingThread();
		update();
	}

	/**
	 * THREAD STARTS WHEN PROGRAM HAS BEEN TERMINATED
	 */
	private static void shutDownThread()
	{
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			public void run()
			{
				try
				{
					//Saves messages into log
					Messages.printLog();
					Log.info("Terminated");

				}
				catch (Exception e)
				{
					Log.stackTrace(e.getStackTrace());
				}
			}
		});
	}

	/**
	 * CHECK FOR NEW UPDATES, STARTS NEW THREAD FOR EVERY UPDATE
	 */
	public static void update() //TODO far in modo che questo metodo venga avviato alla ricezione di un webhook
	{
		while (true)
		{
			String tmp = UpdatesReader.getUpdate();
			if (tmp != null)
			{
				update = tmp;
				ArrayList<Message> updates;
				updates = UpdatesReader.parseJSON(update);
				if (updates != null && updates.size() > 0)
				{
					for (Message msg : updates)
					{
						updateThread(msg);
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
	 * Start thread that delete files older than 3 days, every 12 hours
	 */
	private static void deletingThread()
	{
		Thread thread = new Thread(new Runnable()
		{
			public void run()
			{
				while (true)
				{
					try
					{
						File dir = new File("tmp/");
						File[] directoryListing = dir.listFiles();
						if (directoryListing != null)
						{
							for (File file : directoryListing)
							{
								if ((System.currentTimeMillis() - file.lastModified()) > 259200000)
								{
									if (file.delete())
									{
										DownloadedFileLogger.deleteFile(file.getName());
										IO.writeOUT("DeleteLog", "File " + file.getName() + " DELETED");
										Log.info("File Deleted");
									}
								}
							}
						}
						Thread.sleep(43200000);
					}
					catch (Exception e)
					{
						Log.error("Error deleting file: %0A" + e.getStackTrace());
					}
				}
			}
		});
		thread.start();
	}

	public static void updateThread(Message msg)
	{
		Thread updateThread = new Thread(new Runnable()
		{
			public void run()
			{
				if(UpdatesReader.isCommand(msg))
				Commands.exeCommand(msg.getText().substring(1).split(" ")[0], msg);
			}
		});
		updateThread.start();
	}

	/**
	 * Return bot idCode
	 *
	 * @return idCode
	 */
	public static String getIdCode()
	{
		return botId;
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
	 * Change bot ID
	 *
	 * @param botIdTmp
	 * @return true if it has been changed
	 */
	public static boolean setBotId(String botIdTmp)
	{
		if (botId != null)
		{
			botId = botIdTmp;
			return true;
		}
		return false;
	}

	/**
	 * Return owner's id code.
	 *
	 * @return owner
	 */
	public static String getOwner()
	{
		return owner;
	}

	/**
	 * Change owner ID
	 *
	 * @param ownerIdTmp
	 * @return true if it has been changed
	 */
	public static boolean setOwnerId(String ownerIdTmp)
	{
		if (botId != null)
		{
			owner = ownerIdTmp;
			return true;
		}
		return false;
	}

	/**
	 * Return last received update
	 *
	 * @return lastUpdate - JSON format
	 */
	public static String getLastUpdate()
	{
		return update;
	}

	/**
	 * Check if user is bot'owner
	 * @param ownerId
	 * @return True if is the owner
	 */
	public static boolean isOwner(long ownerId)
	{
		return getOwner().equals(String.valueOf(ownerId));
	}

	/**
	 * Check if user is bot'owner
	 * @param ownerId
	 * @return True if is the owner
	 */
	public static boolean isOwner(String ownerId)
	{
		return getOwner().equals(ownerId);
	}
}
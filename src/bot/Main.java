package bot;

import java.io.File;
import java.util.ArrayList;

import addons.Commands;
import addons.Help;
import addons.JarFileLoader;
import bot.functions.DownloadedFileLogger;
import bot.webServer.Server;
import bot.webServer.WebHook;

public class Main
{
	private static String botId = "";
	private static String owner = "";
	private static String url = "";
	private static String update = "";
	private static boolean webhook = true; //TODO never used

	/**
	 * Main method
	 *
	 * @param args botId   - get it from bot Father
	 * @param args ownerId - your telegram id
	 */
	public static void main(String[] args)
	{
		Setting.createSettingFile();

		if(!Setting.setBotAndOwnerFromSittings())
		{
			Log.error("WRONG CONFIGURATION");
			System.exit(1);
		}
		if (args != null && args.length > 0 && args[0].equals("-server"))
		{
			Log.info("NO GUI");
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

		//LOAD EXTERNAL ADDONS
		JarFileLoader.loadJarFile();

		//LOAD INTERNAL COMMANDS
		Help.load();

		//PRINT LIST OF LOADED COMMANDS
		Log.info(Commands.getCommands().keySet().toString());

		//LOAD CONSOLE COMMANDS
		Console.loadCommand();
		Console.openConsole();


		//START THREADS
		shutDownThread();
		deletingThread();
		if(Setting.readSetting("WebHook_Active","WebHook").equals("true"))
		{
			WebHook.setWebHook();
			Server.startServer();
		}
		else
		{
			WebHook.unsetWebHook();
			update();
		}
	}

	/**
	 * THREAD STARTS WHEN PROGRAM HAS BEEN TERMINATED
	 */
	private static void shutDownThread()
	{
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			@Override
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
	 * Set args as botId && ownerId
	 * @param botEownerId [0] must be botId, [1] must be owner Id
 	 */
	public static void setFields(String[] botEownerId)
	{
		if (botEownerId.length >= 1 && botEownerId[0] != null && botEownerId[1] != null)
		{
			botId = botEownerId[0];
			owner = botEownerId[1];
			url = "https://api.telegram.org/bot" + botId;
		}
	}


	/**
	 * CHECK FOR NEW UPDATES(getUpdates method), STARTS NEW THREAD FOR EVERY UPDATE
	 */
	public static void update()
	{
		while (true)
		{
				String tmp = UpdatesReader.getUpdate();
				if (tmp != null)
				{
					String update = tmp;
					ArrayList<Message> updates;
					updates = UpdatesReader.parseJSON(update);
					if (updates != null && updates.size() > 0)
					{
						for (Message msg : updates)
						{
							messageProcessThread(msg);
							try
							{
								Thread.sleep(20);
							}
							catch (InterruptedException e)
							{
								e.printStackTrace();
							}
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

	public static void messageProcessThread(Message msg)
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
	 * Set last received update
	 */
	public static void setLastUpdate(String lastUpdate)
	{
		update = lastUpdate;
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

	/**
	 * Save actual configuration
	 */
	public static void saveConfiguration()
	{
		Setting.editSetting("Bot_ID", botId, "Main");
		Setting.editSetting("Owner_ID", owner, "Main");
	}

	/**
	 * Set webhook as method to update
	 * @param use: true to use webhook, false to use getUpdate
	 */
	public static void setWebhook(boolean use)
	{
		webhook = use;
	}
}
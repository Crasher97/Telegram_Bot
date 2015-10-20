package bot;

import java.io.File;
import java.util.ArrayList;

import addons.Command;
import addons.Commands;
import addons.Help;
import addons.JarFileLoader;
import bot.functions.DownloadedFileLogger;
import bot.webServer.Server;
import bot.webServer.WebHook;

public class Main
{
	private static String botId = "";
	private static String url = "https://api.telegram.org/bot";

	private static boolean maintenance = false;

	/**
	 * Main method program starts here
	 *
	 * @param args botId   - get it from bot Father
	 * @param args ownerId - your telegram id
	 */
	public static void main(String[] args)
	{
		Setting.createSettingFile();
		if (!Setting.settingExist("Bot_ID", "Main") || Setting.readSetting("Bot_ID", "Main").equals(""))
		{
			Log.error("WRONG CONFIGURATION: you can configure your bot in file setting inside folder config");
			System.exit(1);
		}
		server(args);
	}

	/**
	 * Start server without gui
	 */
	public static void server(String[] args)
	{

		//LOAD EXTERNAL ADDONS
		JarFileLoader.loadJarFile();

		//LOAD INTERNAL COMMANDS
		loadBasicCommands();
		Help.load();

		//LOAD USERS
		Users.loadUsers();
		Users.loadUsersCommand();

		//PRINT LIST OF LOADED COMMANDS
		Log.info(Commands.getCommands().keySet().toString());

		//LOAD CONSOLE COMMANDS
		Console.loadCommand();
		Console.openConsole();


		//START THREADS
		shutDownThread();
		deletingThread();

		Owners.createOwnersFile();
		url += Setting.readSetting("Bot_ID", "Main");

		if (Setting.readSetting("WebHook_Active", "WebHook").equals("true"))
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
	 * Start thread that delete files older than ~2 days, every ~6 hours
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
								if ((System.currentTimeMillis() - file.lastModified()) > 159200000)
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
						Thread.sleep(23200000);
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
				if(isMaintenance() && !Owners.isOwner(String.valueOf(msg.getSender_id())))
				{
					Sender.sendMessage(msg.getSender_id(), "BOT IS IN MAINTENANCE");
				}
				else if (!UpdatesReader.isBanned(msg) && UpdatesReader.isCommand(msg))
				{
					Commands.exeCommand(msg.getText().substring(1).split(" ")[0], msg);
				}
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
	 * Save actual configuration
	 */
	public static void saveConfiguration()
	{
		Setting.editSetting("Bot_ID", botId, "Main");
	}

	public static boolean isMaintenance()
	{
		return maintenance;
	}

	public static void setMaintenance(boolean maintenance)
	{
		Main.maintenance = maintenance;
	}

	/**
	 * Command stop. Stop the bot
	 * @param msg
	 */
	public static void commandStop(Message msg)
	{
		if(Owners.isOwner(String.valueOf(msg.getSender_id())))
		{
			System.exit(0);
		}
	}

	/**
	 * Command alt. Set bot in maintenance. Owner can write to bot for testing.
	 * @param msg
	 */
	public static void commandAlt(Message msg)
	{
		if (Owners.isOwner(String.valueOf(msg.getSender_id())))
		{
			setMaintenance(!isMaintenance());
		}
	}

	/**
	 * Load basic commands for bot(through message)
	 * Command alt
	 * Command stop
	 * Only owner can use these commands
	 */
	public static void loadBasicCommands()
	{
		Commands.addCommand(new Command("alt", "bot.Main", "commandAlt"));
		Commands.addCommand(new Command("stop", "bot.Main", "commandStop"));
	}

}

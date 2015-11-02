package bot;

import java.io.File;
import java.util.ArrayList;

import bot.telegramType.User;
import bot.translation.Sentences;
import bot.translation.SentencesLoader;
import addons.Command;
import addons.Commands;
import addons.Help;
import addons.JarFileLoader;
import bot.collections.Messages;
import bot.collections.Owners;
import bot.collections.Users;
import bot.log.Log;
import bot.log.DownloadedFileLogger;
import bot.functions.Sender;
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

		//LOAD LENGUAGE
		SentencesLoader.loadSentences();

		//START THREADS
		shutDownThread();
		deletingThread();

		Owners.createOwnersFile();
		botId = Setting.readSetting("Bot_ID", "Main");
		url += Setting.readSetting("Bot_ID", "Main");

		if(Setting.readSetting("WebHook_Active", "WebHook").equals("true"))
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
				if (!UpdatesReader.checkUserExist(msg))
				{
					Log.info(Sentences.NEW_USER.getSentence() + " " + Sentences.HAS_CONNECTED.getSentence());
				}
				else
				{
					msg.setUser(Users.getUser(msg.getSender_id()));
				}
				if(checkConditions(msg))
				{
					if (msg.getText() == null)
					{
						Log.warn("Message empty received from " + msg.getSender_id() + " group[" + msg.getChat().getTitle() + "]");
						Sender.sendMessage(msg.getChat().getId(), "Error. Empty text", msg.getMessage_id());
						return;
					}


					if (isMaintenance() && !Owners.isOwner(msg.getSender_id()))
					{
						Log.info("Message received from [" + msg.getSender_id() + "] " + msg.getFirst_name() + " " + msg.getLast_name() + " group[" + msg.getChat().getTitle() + "]" + ": " + msg.getText());
						Sender.sendMessage(msg.getSender_id(), "BOT IS IN MAINTENANCE");
					}
					else if (!UpdatesReader.isBanned(msg) && UpdatesReader.isCommand(msg))
					{
						Commands.exeCommand(msg.getText().substring(1).split(" ")[0], msg);
					}
				}
				else
				{
					Log.info("Message received from [" + msg.getSender_id() + "] " + msg.getFirst_name() + " " + msg.getLast_name() + " group[" + msg.getChat().getTitle() + "]" + ": " + msg.getText());
					Sender.sendMessage(msg.getChat().getId() ,Sentences.MESSAGE_NOT_SENT.getSentence() + ": " + Sentences.CONDITION_REQUEST.getSentence() ,msg.getMessage_id());
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
	 * check if bot is in maintenance
	 * @return true if it is in maintenance
	 */
	public static boolean isMaintenance()
	{
		return maintenance;
	}

	public static void setMaintenance(boolean maintenance)
	{
		Main.maintenance = maintenance;
	}

	public static boolean checkConditions(Message msg)
	{
		if(Owners.isOwner(msg.getSender_id()))return true;
		String command = msg.getText();
		if(command!=null)
		{
			command = msg.getText().split(" ")[0];
			if(command.equals("/accept") || command.equals("/conditions"))
			{
				return true;
			}
		}
		long timeFromLastAdvice = System.currentTimeMillis() - msg.getUser().getTimeFromLastTerms();
		if(timeFromLastAdvice < 604800000 && timeFromLastAdvice > 0)
		{
			return true;
		}
		return false;
	}

	/**
	 * Command stop. Stop the bot
	 *
	 * @param msg
	 */
	public static void commandStop(Message msg)
	{
		if (Owners.isOwner(msg.getSender_id()))
		{
			System.exit(0);
		}
	}

	/**
	 * Command alt. Set bot in maintenance. Owner can write to bot for testing.
	 *
	 * @param msg
	 */
	public static void commandAlt(Message msg)
	{
		if (Owners.isOwner(msg.getSender_id()))
		{
			setMaintenance(!isMaintenance());
			Log.config("Maintenance mode: " + isMaintenance());
		}
	}

	/**
	 * Command accept. Accept terms of use for users.
	 * @param msg
	 */
	public static void commandAcceptTerms(Message msg)
	{
		User usr = Users.getUser(msg.getSender_id());
		if(usr != null)
		{
			usr.setTimeFromLastTerms(System.currentTimeMillis());
			Log.info(usr.getSenderId() + " Has accepted terms and conditions");
			Sender.sendMessage(msg.getSender_id(),"Terms and conditions accepted.(7 days)");
		}
	}

	/**
	 * Command accept. Accept terms of use for users.
	 * @param msg
	 */
	public static void commandSendTerms(Message msg)
	{
		Sender.sendConditions(msg.getSender_id());
	}

	/**
	 * Load basic commands for bot(through message)
	 * Command alt
	 * Command stop
	 * Only owner can use these commands
	 */
	public static void loadBasicCommands()
	{
		Command accept = new Command("accept", "bot.Main", "commandAcceptTerms");
		Command sendTerms  = new Command("conditions", "bot.Main", "commandSendTerms");
		Command alt = new Command("alt", "bot.Main", "commandAlt");
		Command stop = new Command("stop", "bot.Main", "commandStop");
		alt.setHidden(true);
		stop.setHidden(true);
		Commands.addCommand(alt);
		Commands.addCommand(stop);
		Commands.addCommand(accept);
		Commands.addCommand(sendTerms);
	}

}

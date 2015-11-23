package bot;

import java.io.File;
import java.util.ArrayList;

import bot.UpdateReader.UpdatesReader;
import bot.UpdateReader.Validator;
import bot.botType.Message;
import bot.botType.WaitingCommand;
import bot.collections.*;
import bot.functions.FileManager;
import bot.functions.Keyboard;
import bot.functions.SimSimi;
import bot.translation.Sentences;
import bot.translation.SentencesLoader;
import bot.botType.Command;
import addons.JarFileLoader;
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
	private static final long TERMS_TIME = 315400000 *100;
	private static int UPDATE_TIME;

	/**
	 * Main method program starts here
	 */
	public static void main(String[] args)
	{
		Setting.createSettingFile();
		UPDATE_TIME = Integer.parseInt(Setting.readSetting("Update_Frequence","Main"));
		System.out.println("DEBUGGATE IL DEBUG!");
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
					Users.saveUsers();
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
				Thread.sleep(UPDATE_TIME);
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
		Thread thread = new Thread()
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
								if ((System.currentTimeMillis() - file.lastModified()) > 109200000)
								{
									if (file.delete())
									{
										DownloadedFileLogger.deleteFile(file.getName());
										FileManager.writeFile(new File("log/DeleteLog.log"), "File " + file.getName() + " DELETED \n", true);
										Log.info("File Deleted: " + file.getName());
									}
								}
							}
						}
						Thread.sleep(15000000);
					}
					catch (Exception e)
					{
						Log.error("Error deleting file: " + e.getStackTrace());
					}
				}
			}
		};
		thread.start();
	}

	public static void messageProcessThread(Message msg)
	{
		Thread updateThread = new Thread()
		{

			public void run()
			{
				Command cmd = null;
				Long chatId = msg.getChat().getId();

				if (!Validator.checkUserExist(msg))
				{
					Log.info(Sentences.NEW_USER.getSentence() + " " + Sentences.HAS_CONNECTED.getSentence());
				}

				if (isMaintenance() && !Owners.isOwner(msg.getUserFrom().getSenderId()))
				{
					Sender.sendMessage(chatId, Sentences.BOT_IS_IN_MAINTENANCE.getSentence());
					return;
				}

				if (msg.getText() == null)
				{
					Log.warn(Sentences.EMPTY_MESSAGE.getSentence() + " " + Sentences.FROM.getSentence() + " " + msg.getUserFrom().getSenderId() + " group[" + msg.getChat().getTitle() + "]");
					return;
				}

				if (!Validator.isBanned(msg) && checkConditions(msg))
				{

					//COMMAND
					if (msg.getMessageType() == Message.MessageType.COMMAND)
					{
						if (msg.getText().equals("cancel"))
						{
							Log.info(Sentences.OPERATION_DELETED.getSentence());

							if(WaitingCommands.removeCommand(chatId)!=null)
							Sender.sendMessage(chatId, Sentences.OPERATION_DELETED.getSentence());

							return;
						}

						if ((cmd = Validator.checkCommandExist(msg)) == null)
						{
							return;
						}

						if (cmd instanceof WaitingCommand)
						{
							if (WaitingCommands.isWaitingFor(chatId))
							{
								Sender.sendMessage(chatId, Sentences.WAITING_FOR_OCOMMAND.getSentence());
							}
							else
							{
								WaitingCommand waitingCommand;
								ArrayList<String> validParams;
								{
									waitingCommand = (WaitingCommand) cmd;
									WaitingCommands.addCommand(chatId, waitingCommand);
									validParams = waitingCommand.getValidParams();

									int row = (int) Math.ceil(validParams.size() / 3.0);
									String[][] keys = new String[row][3];
									int a = 0;
									for (int i = 0; i < row; i++)
									{
										for (int k = 0; k < 3 && k < validParams.size(); k++)
										{
											keys[i][k] = validParams.get(a++);
										}
									}
									Keyboard keyboard = new Keyboard(keys, true, true, true);
									Sender.sendMessage(chatId, "Choose your parameter", keyboard);
								}
							}
						}
						else
						{
							Commands.exeCommand(msg.getText().split(" ")[0], msg);
						}
					}

					//PARAM
					else
					{
						if (msg.getMessageType() == Message.MessageType.PARAM)
						{
							if (WaitingCommands.isWaitingFor(chatId))
							{
								WaitingCommand waitingCommand = WaitingCommands.getWaitingCommand(chatId);
								WaitingCommands.removeCommand(chatId);
								msg.setText("/" + waitingCommand.getCommandName() + " " + msg.getText());
								Commands.exeCommand(waitingCommand.getCommandName(), msg);
							}
							else
							{
								Sender.sendMessage(chatId, Sentences.NOT_WAITING_PARAM.getSentence());
							}
						}

						//NORMAL MESSAGE
						else
						{
							Log.info(Sentences.MESSAGE_RECEIVED.getSentence() + " " + Sentences.FROM.getSentence() + " [" + msg.getUserFrom().getSenderId() + "] " + msg.getUserFrom().getFirst_name() + " " + msg.getUserFrom().getLast_name() + " group[" + msg.getChat().getTitle() + "]" + ": " + msg.getText());
							Sender.sendChatAction(Sender.ChatAction.WRITING_MESSAGE, msg.getChat().getId());
							if (msg.getChat().getType().equals("group"))
							{
								Sender.sendMessage(msg.getChat().getId(), SimSimi.toSimSimi(msg.getText()), msg.getMessageId());
							}
							else
							{
								Sender.sendMessage(msg.getChat().getId(), SimSimi.toSimSimi(msg.getText()));
							}
						}
					}
				}
				else
				{
					Sender.sendMessage(chatId, Sentences.MESSAGE_NOT_SENT.getSentence() + ": " + Sentences.CONDITION_REQUEST.getSentence(), msg.getMessageId());
				}
			}
		};
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
	 *
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
		if (Owners.isOwner(msg.getUserFrom().getSenderId())) return true;
		String command = msg.getText();
		if (command != null)
		{
			command = msg.getText().split(" ")[0];
			if (command.equals("accept") || command.equals("conditions"))
			{
				return true;
			}
		}
		long timeFromLastAdvice = System.currentTimeMillis() - Users.getUser(msg.getUserFrom().getSenderId()).getTimeFromLastTerms();
		if (timeFromLastAdvice < TERMS_TIME && timeFromLastAdvice > 0)
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
		if (Owners.isOwner(msg.getUserFrom().getSenderId()))
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
		if (Owners.isOwner(msg.getUserFrom().getSenderId()))
		{
			setMaintenance(!isMaintenance());
			Log.config("Maintenance mode: " + isMaintenance());
		}
	}

	/**
	 * Command accept. Accept terms of use for users.
	 *
	 * @param msg
	 */
	public static void commandAcceptTerms(Message msg)
	{
		bot.botType.User usr = Users.getUser(msg.getUserFrom().getSenderId());
		if (usr != null)
		{
			usr.setTimeFromLastTerms(System.currentTimeMillis());
			Log.info(usr.getSenderId() + " Has accepted terms and conditions");
			Sender.sendMessage(msg.getUserFrom().getSenderId(), "Terms and conditions accepted.(7 days)");
		}
	}

	/**
	 * Command accept. Accept terms of use for users.
	 *
	 * @param msg
	 */
	public static void commandSendTerms(Message msg)
	{
		Sender.sendConditions(msg.getUserFrom().getSenderId());
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
		Command sendTerms = new Command("conditions", "bot.Main", "commandSendTerms");
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

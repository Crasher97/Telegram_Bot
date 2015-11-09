package bot;

import addons.Commands;
import bot.collections.Users;
import bot.functions.FileDownloader;
import bot.functions.Keyboard;
import bot.functions.Sender;
import bot.telegramType.Chat;
import bot.webServer.Server;
import bot.webServer.WebHook;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

import addons.Help;


public class Console
{
	private static HashMap<String, ConsoleCommandCode> consoleCommands = new HashMap<String, ConsoleCommandCode>();

	/**
	 * Opens console and receive commands from users
	 */
	public static void openConsole()
	{
		Runnable consoleRunnable = new Runnable()
		{
			@Override
			public void run()
			{
				@SuppressWarnings("resource")
				Scanner scanner = new Scanner(System.in);
				String[] command;
				while (true)
				{
					synchronized (this)
					{
						//System.out.printf(">");
						command = scanner.nextLine().split(" ");
						if (commandExist(command[0]))
						{
							exeCommand(command);
						}
						else
							System.err.println("Comando Inesistente");
					}

				}
			}
		};
		Thread consoleThread = new Thread(consoleRunnable);
		consoleThread.start();

	}

	/**
	 * Load default commands in console
	 */
	public static void loadCommand()
	{
		addCommand("help", new ConsoleCommandCode()
		{
			@Override
			public void run(String[] args)
			{
				Help.helpRequest(args);
			}

			@Override
			public void run()
			{
			}
		});

		addCommand("stop", new ConsoleCommandCode()
		{
			@Override
			public void run(String[] args)
			{
				System.exit(0);
			}

			@Override
			public void run()
			{
			}
		});

		addCommand("download", new ConsoleCommandCode()
		{
			@Override
			public void run(String[] args)
			{
				FileDownloader.downloadFile(args[0]);
			}

			@Override
			public void run()
			{
			}
		});

		addCommand("video", new ConsoleCommandCode()
		{
			@Override
			public void run(String[] args)
			{
				System.out.println(FileDownloader.downloadVideo(args[0]));
			}

			@Override
			public void run()
			{
			}
		});

		addCommand("webhook", new ConsoleCommandCode()
		{
			@Override
			public void run(String[] args)
			{
				WebHook.setWebHook();
				Server.startServer();
			}

			@Override
			public void run()
			{
			}
		});

		addCommand("webhookUnset", new ConsoleCommandCode()
		{
			@Override
			public void run(String[] args)
			{
				WebHook.unsetWebHook();
				Server.stopServer();
			}

			@Override
			public void run()
			{
			}
		});

		addCommand("testKeyboard", new ConsoleCommandCode()
		{
			@Override
			public void run(String[] args)
			{
				String[][] keys = {{"11", "12", "13"}, {"21", "22", "23"}};
				Keyboard k = new Keyboard(keys, true, false, false);
				Sender.sendMessage(new Long(84954308), "Messaggio prova", k);
			}

			@Override
			public void run()
			{
			}
		});

		addCommand("ban", new ConsoleCommandCode()
		{
			@Override
			public void run(String[] args)
			{
				Users.getUser(Long.parseLong(args[0])).setBan(true);
			}

			@Override
			public void run()
			{
			}
		});

		addCommand("unban", new ConsoleCommandCode()
		{
			@Override
			public void run(String[] args)
			{
				Users.getUser(Long.parseLong(args[0])).setBan(false);
			}

			@Override
			public void run()
			{
			}
		});

		addCommand("alt", new ConsoleCommandCode()
		{
			@Override
			public void run(String[] args)
			{
				Main.setMaintenance(!Main.isMaintenance());
			}

			@Override
			public void run()
			{
			}
		});

		addCommand("msg", new ConsoleCommandCode()
		{
			@Override
			public void run(String[] args)
			{
				if (args.length > 1)
				{
					Message msg = new Message(0, 0, 84985065, "Paolo", "TEST", "pbono", new Date(), args[0] + " " + args[1], new Chat(84985065, null, null, null, null, null));
					Commands.exeCommand(args[0], msg);
				}
				else if (args.length == 0)
				{
					return;
				}
				else
				{
					Message msg = new Message(0, 0, 84985065, "Paolo", "TEST", "pbono", new Date(), args[0], new Chat(84985065, null, null, null, null, null));
					Commands.exeCommand(args[0], msg);
				}

			}

			@Override
			public void run()
			{
			}
		});
	}

	public interface ConsoleCommandCode extends Runnable
	{
		public void run(String[] args);
	}

	/**
	 * Add command to commands list
	 *
	 * @param commandName
	 * @param consoleCommandCode - Source code of command
	 */
	public static void addCommand(String commandName, ConsoleCommandCode consoleCommandCode)
	{
		consoleCommands.put(commandName, consoleCommandCode);
	}

	/**
	 * Performs command that has been given via parameter
	 *
	 * @param command - Command to execute + parameters
	 */
	public static void exeCommand(String[] command)
	{
		String[] args = Arrays.copyOfRange(command, 1, command.length);
		consoleCommands.get(command[0]).run(args);

	}

	/**
	 * Check if commands exist
	 *
	 * @param commandName
	 * @return true if command exist
	 */
	public static boolean commandExist(String commandName)
	{
		if (consoleCommands.containsKey(commandName)) return true;
		return false;
	}
}


package bot;

import bot.functions.FileDownloader;
import bot.webServer.Server;
import bot.webServer.WebHook;

import java.util.Arrays;
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
				while(true)
				{
					synchronized (this)
					{
						//System.out.printf(">");
						command = scanner.nextLine().split(" ");
						if(commandExist(command[0]))
						{
							exeCommand(command);
						}
						else
							System.err.println("Comando Inesistente");
					}

				}
			}
		};
		Thread consoleThread =  new Thread(consoleRunnable);
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
			public void run(){}
		});
		
		addCommand("stop", new ConsoleCommandCode()
			{
				@Override
				public void run(String[] args)
				{
					System.exit(0);
				}

				@Override
				public void run(){}
			});
		
		addCommand("download", new ConsoleCommandCode()
		{
			@Override
			public void run(String[] args)
			{
				FileDownloader.downloadFile(args[0]);
			}

			@Override
			public void run(){}
		});
		
		addCommand("video", new ConsoleCommandCode()
		{
			@Override
			public void run(String[] args)
			{
				System.out.println(FileDownloader.downloadVideo(args[0]));
			}

			@Override
			public void run(){}
		});

		addCommand("webhook", new ConsoleCommandCode()
		{
			@Override
			public void run(String[] args)
			{
				WebHook.setWebHook();
				Server.startWebServer();
			}

			@Override
			public void run(){}
		});

		addCommand("webhookUnset", new ConsoleCommandCode()
		{
			@Override
			public void run(String[] args)
			{
				WebHook.unsetWebHook();
			}

			@Override
			public void run(){}
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
	 * @param command - Command to execute + parameters
	 */
	public static void exeCommand(String[] command)
	{
		String[] args = Arrays.copyOfRange(command, 1, command.length);
		consoleCommands.get(command[0]).run(args);
		
	}
	
	/**
	 * Check if commands exist
	 * @param commandName
	 * @return true if command exist
	 */
	public static boolean commandExist(String commandName)
	{
		if(consoleCommands.containsKey(commandName)) return true;
		return false;
	}
}


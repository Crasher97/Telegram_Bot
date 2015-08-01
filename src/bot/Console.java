package bot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;


public class Console
{
	private static HashMap<String, ConsoleCommandCode> consoleCommands = new HashMap<String, ConsoleCommandCode>();
	
	/**
	 * Metodo che apre la console e riceve i comandi dell'utente
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
	 * Carica i comandi predefiniti nell'hashmap
	 */
	public static void loadCommand()
	{
		addCommand("help", new ConsoleCommandCode()
		{
			@Override
			public void run(String[] args)
			{
				System.out.println("Comando Help non finito");
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
		
		addCommand("upload", new ConsoleCommandCode()
		{
			@Override
			public void run(String[] args)
			{
				ImagesUploader.uploadImage(Long.parseLong(args[0]), args[1]);
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
		
		addCommand("ytvideo", new ConsoleCommandCode()
			{
				@Override
				public void run(String[] args)
				{
					bot.VideoUploader.ytUpload(Long.parseLong(args[0]), args[1]);
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
	 * Aggiunge un comando alla lista
	 * 
	 * @param commandName - Nome del Comando
	 * @param consoleCommandCode - Codice del comando
	 */
	public static void addCommand(String commandName, ConsoleCommandCode consoleCommandCode)
	{
		consoleCommands.put(commandName, consoleCommandCode);
	}
	
	/**
	 * Esegue il comando passato come parametro
	 * @param command
	 */
	public static void exeCommand(String[] command)
	{
		String[] args = Arrays.copyOfRange(command, 1, command.length);
		consoleCommands.get(command[0]).run(args);
		
	}
	
	/**
	 * Verifica se il comando esiste
	 * @param commandName
	 * @return true se esiste, false se non esiste
	 */
	public static boolean commandExist(String commandName)
	{
		if(consoleCommands.containsKey(commandName)) return true;
		return false;
	}
}


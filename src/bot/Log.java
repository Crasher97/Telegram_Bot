package bot;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log
{
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private static File logFile = null;

	/**
	 * Enum contenente i vari livelli di errore
	 */
	private enum Level{INFO, WARN, DEBUG, ERROR, CONFIG, FATAL}

	/**
	 * Restituisce la data in formato "HH:mm:ss" come Stringa
	 * @return Ora attuale come stringa
	 */
	private static String getData()
	{
		return dateFormat.format(new Date());
	}

	/**
	 * Metodo per scrivere sulla console e nei file di log
	 * @param x Testo da scrivere
	 * @param level Livello del messaggio
	 */
	public static void all(String x, Level level)
	{
		if(logFile == null) createLogFile();

		String y = ("[" + getData() + "] " + "[" + level + "] : " + x);
		if(!(level == Level.ERROR) && !(level == Level.FATAL))
		{
			System.out.println(y);
		}
		else
		{
			System.err.println(y);
		}
		addToLog(y);
	}

	public static void info(String x)
	{
		all(x, Level.INFO);
	}

	public static void warn(String x)
	{
		all(x, Level.WARN);
	}

	public static void debug(String x)
	{
		all(x, Level.DEBUG);
	}

	public static void error(String x)
	{
		all(x, Level.ERROR);
	}

	public static void config(String x)
	{
		all(x, Level.CONFIG);
	}

	public static void stackTrace(StackTraceElement[] stackTraceElement)
	{
		String trace = "\n";
		for(StackTraceElement line : stackTraceElement)
		{
			trace += line.toString() + "\n";
		}
		all(trace, Level.FATAL);
	}

	/**
	 * Metodo che crea un nuovo file di log ad ogni avvio del programma, il nome del file è la data e l'ora a
	 * cui viene lanciato il programma
	 */
	public static void createLogFile()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH.mm.ss");
		File logFolder = new File("log");
		if(logFolder.exists() && !logFolder.isDirectory())
		{
			System.err.println("Errore nella creazione della cartella log, esiste già un file con questo nome, eliminarlo per procedere");
			return;
		}
		if(!logFolder.exists()) logFolder.mkdir();

		logFile = new File("log/" + dateFormat.format(new Date()) + ".log");
		BufferedWriter outputWriter;
		try
		{
			outputWriter = new BufferedWriter(new FileWriter(logFile , true));
			outputWriter.flush();
			outputWriter.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Aggiunge la stringa passata come parametro all'ultimo file di log creato
	 * @param x Stringa da aggiungere al file
	 */
	public static void addToLog(String x)
	{
		BufferedWriter outputWriter;
		try
		{
			outputWriter = new BufferedWriter(new FileWriter(logFile , true));
			outputWriter.write(x);
			outputWriter.newLine();
			outputWriter.flush();
			outputWriter.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}

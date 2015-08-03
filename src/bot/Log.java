package bot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log
{
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private static File logFile = null;

	private static enum Level{INFO, WARN, DEBUG, ERROR, CONFIG};

	private static String getData()
	{
		return dateFormat.format(new Date());
	}

	public static void all(String x, Level level)
	{
		if(logFile == null) createLogFile();

		String y = ("[" + getData() + "] " + "[" + level + "] : " + x);
		if(!(level == Level.ERROR))
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

	public static void createLogFile()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH.mm.ss");
		File logFolder = new File("log");
		if(!logFolder.isDirectory()) logFolder.mkdir();
		if(!logFolder.exists()) logFolder.mkdir();

		logFile = new File("log/" + dateFormat.format(new Date()) + ".log");
		BufferedWriter outputWriter = null;
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

	public static void addToLog(String x)
	{
		BufferedWriter outputWriter = null;
		try
		{
			outputWriter = new BufferedWriter(new FileWriter(logFile , true));
			outputWriter.write(x);
			outputWriter.newLine();
			outputWriter.flush();
			outputWriter.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}

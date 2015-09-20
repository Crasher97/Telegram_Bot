package bot;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log
{
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private static File logFile = null;

	/**
	 * Enum contains all printline message tipes
	 */
	private enum Level
	{
		INFO, WARN, DEBUG, ERROR, CONFIG, FATAL
	}

	/**
	 * Return current time
	 *
	 * @return actual time HH:mm:ss
	 */
	private static String getTime()
	{
		return dateFormat.format(new Date());
	}

	/**
	 * Write on console and on log file
	 *
	 * @param textToWrite
	 * @param level       - tipe of message
	 */
	public static void all(String textToWrite, Level level)
	{
		if (logFile == null) createLogFile();

		String y = ("[" + getTime() + "] " + "[" + level + "] : " + textToWrite);
		if (!(level == Level.ERROR) && !(level == Level.FATAL))
		{
			System.out.println(y);
		}
		else
		{
			System.err.println(y);
		}
		addToLog(y);
	}

	/**
	 * Print info on console and write into log
	 *
	 * @param x
	 */
	public static void info(String x)
	{
		all(x, Level.INFO);
	}

	/**
	 * Print warns on console and write into log
	 *
	 * @param x
	 */
	public static void warn(String x)
	{
		all(x, Level.WARN);
	}

	/**
	 * Print debug info on console and write into log
	 *
	 * @param x
	 */
	public static void debug(String x)
	{
		all(x, Level.DEBUG);
	}

	/**
	 * Print errors on console and write into log
	 *
	 * @param x
	 */
	public static void error(String x)
	{
		all(x, Level.ERROR);
	}

	/**
	 * Print config messages on console and write into log
	 *
	 * @param x
	 */
	public static void config(String x)
	{
		all(x, Level.CONFIG);
	}

	/**
	 * Print exeptions on console and into log
	 *
	 * @param stackTraceElement
	 */
	public static void stackTrace(StackTraceElement[] stackTraceElement)
	{
		String trace = "\n";
		for (StackTraceElement line : stackTraceElement)
		{
			trace += line.toString() + "\n";
		}
		all(trace, Level.FATAL);
	}

	/**
	 * Make new log file every time the program starts. file name is current date & time
	 */
	public static void createLogFile()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH.mm.ss");
		File logFolder = new File("log");
		if (logFolder.exists() && !logFolder.isDirectory())
		{
			System.err.println("Log.createLogFile, error creating log folder, delete file that name is log");
			return;
		}
		if (!logFolder.exists()) logFolder.mkdir();

		logFile = new File("log/" + dateFormat.format(new Date()) + ".log");
		BufferedWriter outputWriter;
		try
		{
			outputWriter = new BufferedWriter(new FileWriter(logFile, true));
			outputWriter.flush();
			outputWriter.close();
		}
		catch (IOException e)
		{
			stackTrace(e.getStackTrace());
		}
	}

	/**
	 * Write at the end of the last log file , parameter string
	 *
	 * @param x
	 */
	public static void addToLog(String x)
	{
		BufferedWriter outputWriter;
		try
		{
			outputWriter = new BufferedWriter(new FileWriter(logFile, true));
			outputWriter.write(x);
			outputWriter.newLine();
			outputWriter.flush();
			outputWriter.close();
		}
		catch (IOException e)
		{
			stackTrace(e.getStackTrace());
		}
	}
}

package bot.functions;

import bot.log.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Paolo on 01/11/2015.
 */
public class FileManager
{
	/**
	 * Write text inside a file. If file doesn't exist it is created.
	 * @param file file to be written
	 * @param toWrite text to write inside file
	 * @param append, true if you want to append the text. Else text before is deleted
	 * @return true if file has been written
	 */
	public static boolean writeFile(File file, String toWrite, boolean append)
	{
		BufferedWriter outputWriter;
		try
		{
			outputWriter = new BufferedWriter(new FileWriter(file, true));
			outputWriter.write(toWrite);
			outputWriter.newLine();
			outputWriter.flush();
			outputWriter.close();;
			return true;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}



	/**
	 * Write text inside a file. N.B PREVIOUS TEXT IS DELETED. STRING MUST BE NOT NULL OR EMPTY
	 * @param file file to be written
	 * @param toWrite text to write inside file
	 * @return true if file has been written
	 */
	public static boolean writeFile(File file, String toWrite)
	{
		return writeFile(file, toWrite, false);
	}
}

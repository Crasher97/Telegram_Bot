package bot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class IO
{

	/**
	 * Write an Array of message into a file
	 *
	 * @param filename    - file to be written
	 * @param messageList
	 * @throws IOException
	 */
	public static void writeOUT(String filename, ArrayList<Message> messageList) throws IOException
	{
		File logFolder = new File("log");
		if (logFolder.exists() && !logFolder.isDirectory())
		{
			System.err.println("IO.writeOUT, error creating log folder, delete file that name is log");
			return;
		}
		if (!logFolder.exists()) logFolder.mkdir();

		File file = new File("log/" + filename + ".log");
		BufferedWriter outputWriter = null;
		outputWriter = new BufferedWriter(new FileWriter(file, true));
		for (int i = 0; i < messageList.size(); i++)
		{
			outputWriter.write(messageList.get(i).toString());
			outputWriter.newLine();
		}
		outputWriter.flush();
		outputWriter.close();
	}


	/**
	 * Write a String of message into a file
	 *
	 * @param filename    - file to be written
	 * @param messageList
	 * @throws IOException
	 */
	public static void writeOUT(String filename, String log) throws IOException
	{
		File logFolder = new File("log");
		if (logFolder.exists() && !logFolder.isDirectory())
		{
			System.err.println("IO.writeOUT, error creating log folder, delete file that name is log");
			return;
		}
		if (!logFolder.exists()) logFolder.mkdir();

		File file = new File("log/" + filename + ".log");
		BufferedWriter outputWriter = null;
		outputWriter = new BufferedWriter(new FileWriter(file, true));
		outputWriter.write(log);
		outputWriter.newLine();
		outputWriter.flush();
		outputWriter.close();
	}
}

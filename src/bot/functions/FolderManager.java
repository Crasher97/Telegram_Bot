package bot.functions;

import bot.log.Log;

import java.io.File;

/**
 * Created by Paolo on 01/11/2015.
 */
public class FolderManager
{
	public static boolean createFolder(File folder)
	{
		if (folder.exists() && !folder.isDirectory())
		{
			Log.error("Error creating log folder, delete file that name is " + folder.getName());
			return false;
		}
		if (!folder.exists())
		{
			folder.mkdir();
			return true;
		}
		return false;
	}
}

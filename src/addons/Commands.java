package addons;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import bot.Log;
import bot.Message;

public class Commands
{
	private static HashMap<String, Command> commands = new HashMap<String, Command>();

	/**
	 * Add the command passed as parameter to the Hashmap
	 *
	 * @param command Object Command to add
	 */
	public static void addCommand(Command command)
	{
		Log.info("Command Added: " + command.getCommandName());
		commands.put(command.getCommandName(), command);
	}

	/**
	 * Execute the command
	 *
	 * @param commandName Name of the command
	 * @param message     The message that started the command
	 */
	public static void exeCommand(String commandName, Message message)
	{
		Command command = commands.get(commandName);
		if (command.isExternal())
		{
			JarFileLoader.invokeClassMethod(command.getJarFile(), command.getClassName(), command.getMethodName(), message);
		}
		else
		{
			callInternalCommand(command.getClassName(), command.getMethodName(), message);
		}
	}

	/**
	 * Test if the command exist
	 *
	 * @param commandName Name of the command
	 * @return true if the command exist, false if not
	 */
	public static boolean commandExist(String commandName)
	{
		if (commands.containsKey(commandName)) return true;
		return false;
	}

	/**
	 * Call a command in a internal class
	 *
	 * @param commandClass  Class of the command
	 * @param commandMethod Method of the command
	 * @param message       The message that started the command
	 */
	public static void callInternalCommand(String commandClass, String commandMethod, Message message)
	{
		try
		{
			Class<?> cls = Class.forName(commandClass);
			Method method = cls.getDeclaredMethod(commandMethod, Message.class);
			Object obj = cls.newInstance();
			method.invoke(obj, message);
		}
		catch (NoSuchMethodException | ClassNotFoundException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e)
		{
			Log.stackTrace(e.getStackTrace());
		}
	}

	public static HashMap<String, Command> getCommands()
	{
		return commands;
	}
}

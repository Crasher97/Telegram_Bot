package addons;

import java.util.HashMap;

import bot.Message;

public class Commands
{
	private static HashMap<String, Command> commands = new HashMap<String, Command>();
	
	public static void addCommad(Command command)
	{
		commands.put(command.getCommandName(), command);
	}
	
	public static void exeCommand(String commandName, Message message)
	{
		Command command = commands.get(commandName);
		JarFileLoader.invokeClassMethod(command.getJarFile(), command.getClassName(), command.getMethodName(), message);
	}
	
	public static boolean commandExist(String commandName)
	{
		if(commands.containsKey(commandName)) return true;
		return false;
	}
}

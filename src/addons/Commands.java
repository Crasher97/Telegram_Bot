package addons;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import bot.Message;

public class Commands
{
	private static HashMap<String, Command> commands = new HashMap<String, Command>();
	
	/**
	 * Aggiunge alla lista il comando passato come parametro
	 * 
	 * @param command
	 */
	public static void addCommand(Command command)
	{
		commands.put(command.getCommandName(), command);
	}
	
	/**
	 * Esegue il comando
	 * 
	 * @param commandName
	 * @param message - Il messaggio da cui è stato lanciato il comando nel caso servano parametri o il sender id
	 */
	public static void exeCommand(String commandName, Message message)
	{
		Command command = commands.get(commandName);
		if(command.isExternal())
		{
			JarFileLoader.invokeClassMethod(command.getJarFile(), command.getClassName(), command.getMethodName(), message);
		}
		else
		{
			callInternalCommand(command.getClassName(), command.getMethodName(), message);
		}
	}
	
	/**
	 * Verifica se il comando passato come parametro esiste
	 * @param commandName
	 * @return
	 */
	public static boolean commandExist(String commandName)
	{
		if(commands.containsKey(commandName)) return true;
		return false;
	}
	
	/**
	 * Chama un comando in una classe interna al programma
	 * 
	 * @param commandClass
	 * @param commandMethod
	 * @param message
	 */
	public static void callInternalCommand(String commandClass, String commandMethod, Message message)
	{
		try
		{
			Class<?> cls = Class.forName("addons." + commandClass);
			Method method = cls.getDeclaredMethod(commandMethod, Message.class);
			Object obj = cls.newInstance();
			method.invoke(obj, message);
		}
		catch (NoSuchMethodException | ClassNotFoundException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e)
		{
			e.printStackTrace();
		}
	}
	
	public static HashMap<String, Command> getCommands()
	{
		return commands;
	}
}

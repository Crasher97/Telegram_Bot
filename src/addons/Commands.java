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
	 * Aggiunge alla lista il comando passato come parametro
	 * 
	 * @param command Oggetto Command da aggiungere
	 */
	public static void addCommand(Command command)
	{
		Log.info("Comando aggiunto: " + command.getCommandName());
		commands.put(command.getCommandName(), command);
	}
	
	/**
	 * Esegue il comando
	 * 
	 * @param commandName Nome del comando
	 * @param message Il messaggio da cui è stato lanciato il comando nel caso servano parametri o il sender id
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
	 * @param commandName Nome del comando
	 * @return true se il comando esiste, altrimenti false
	 */
	public static boolean commandExist(String commandName)
	{
		if(commands.containsKey(commandName)) return true;
		return false;
	}
	
	/**
	 * Chiama un comando in una classe interna al programma
	 * 
	 * @param commandClass Classe del comando
	 * @param commandMethod Metodo del comando
	 * @param message Messaggio dal quale è stato chiamato il comando
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

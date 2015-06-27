package addons;

public class Command
{
	private String commandName;
	private String jarFile;
	private String className;
	private String methodName;
	
	/**
	 * 
	 * @param commandName nome del comando cui cui verrà identificato nei messaggi
	 * @param jarFile il nome del jar da cui si carica il comando
	 * @param className il nome della classe da caricare
	 * @param methodName il nome del metodo da chiamare all'interno della classe
	 */
	public Command(String commandName, String jarFile, String className, String methodName)
	{
		this.commandName = commandName;
		this.jarFile = jarFile;
		this.className = className;
		this.methodName = methodName;
	}

	public String getCommandName()
	{
		return commandName;
	}

	public String getJarFile()
	{
		return jarFile;
	}

	public String getClassName()
	{
		return className;
	}

	public String getMethodName()
	{
		return methodName;
	}
	
	
	
}

package addons;

public class Command
{
	private String commandName;
	private String jarFile;
	private String className;
	private String methodName;
	private boolean external;
	
	/**
	 * Costruttore per i comandi inseriti da addons esterni
	 * 
	 * @param String commandName nome del comando cui cui verrà identificato nei messaggi
	 * @param String jarFile il nome del jar da cui si carica il comando
	 * @param String className il nome della classe da caricare
	 * @param String methodName il nome del metodo da chiamare all'interno della classe
	 */
	public Command(String commandName, String jarFile, String className, String methodName)
	{
		this.commandName = commandName;
		this.jarFile = jarFile;
		this.className = className;
		this.methodName = methodName;
		external = true;
	}
	
	/**
	 * Construttore per comandi interni
	 * 
	 * @param String commandName - il nome del comando con cui verrà riconosciuto
	 * @param String className - il nome della classe dove trovare il metodo load
	 * @param String methodName - il nome del metodo da chiamare dentro la classe quando viene invocato il comando
	 */
	public Command(String commandName, String className, String methodName)
	{
		this.commandName = commandName;
		this.className = className;
		this.methodName = methodName;
		external = false;
	}
	
	/**
	 * Ritorna il nome del comando
	 * @return String commandName
	 */
	public String getCommandName()
	{
		return commandName;
	}

	/**
	 * Ritorna il nome del jar
	 * @return String jarName
	 */
	public String getJarFile()
	{
		return jarFile;
	}

	/**
	 * Ritorna il nome della classe 
	 * @return String className
	 */
	public String getClassName()
	{
		return className;
	}

	/**
	 * Ritorna il nome del metodo da chiamare
	 * @return String mathodName
	 */
	public String getMethodName()
	{
		return methodName;
	}
	
	/**
	 * Ritorna se il comando è esterno o no
	 * @return boolean external
	 */
	public boolean isExternal()
	{
		return external;
	}
	
}

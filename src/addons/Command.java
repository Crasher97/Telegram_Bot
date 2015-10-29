package addons;

public class Command
{
	private String commandName;
	private String jarFile;
	private String className;
	private String methodName;
	private boolean external;
	private boolean hidden = false;

	/**
	 * Constructor for the command added by external addons
	 *
	 * @param commandName name of the command, the way it will be identified in messages
	 * @param jarFile     the name of the jar where is the command located
	 * @param className   the name of the class to load
	 * @param methodName  the name of the method to call
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
	 * Constructor for internal command
	 *
	 * @param commandName name of the command, the way it will be identified in messages
	 * @param className   the name of the class where the method load is
	 * @param methodName  the name of the class to load
	 */
	public Command(String commandName, String className, String methodName)
	{
		this.commandName = commandName;
		this.className = className;
		this.methodName = methodName;
		external = false;
	}

	/**
	 * Return the name of the command
	 *
	 * @return String commandName
	 */
	public String getCommandName()
	{
		return commandName;
	}

	/**
	 * Return the jar name
	 *
	 * @return String jarName
	 */
	public String getJarFile()
	{
		return jarFile;
	}

	/**
	 * Return the name of the class
	 *
	 * @return String className
	 */
	public String getClassName()
	{
		return className;
	}

	/**
	 * Return the name of the method to call
	 *
	 * @return String mathodName
	 */
	public String getMethodName()
	{
		return methodName;
	}

	/**
	 * Return true if the command is external, false if is internal
	 *
	 * @return boolean external
	 */
	public boolean isExternal()
	{
		return external;
	}

	/**
	 * Set if command is hidden
	 * @param hide
	 */
	public void setHidden(boolean hide)
	{
		hidden = hide;
	}

	/**
	 * Return command's hidden status
	 * @return true if command is hidden
	 */
	public boolean isHidden()
	{
		return hidden;
	}
}

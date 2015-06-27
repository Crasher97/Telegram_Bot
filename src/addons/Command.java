package addons;

public class Command
{
	private String commandName;
	private String jarFile;
	private String className;
	private String methodName;
	
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

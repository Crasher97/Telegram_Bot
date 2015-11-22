package bot.botType;

import java.util.ArrayList;

public class WaitingCommand extends Command
{
	private ArrayList<String> params = new ArrayList<String>();
	private int size = 0;

	public WaitingCommand(String commandName, String jarFile, String className, String methodName)
	{
		super(commandName, jarFile, className, methodName);
	}

	public WaitingCommand(Command command)
	{
		super(command.getCommandName(), command.getJarFile(), command.getClassName(), command.getMethodName());
	}

	/**
	 * Add new param that bot wait after command invocation.
	 * @param param , param to be added
	 * @return true if param has been added
	 */
	public boolean addParam(String param)
	{
		boolean done = false;
		param = "-" + param;
		if(size < params.size())
		{
			done = params.set(size,param) != null;
		}
		if(size == params.size())
		{
			done = params.add(param) && params.add(param) && params.add("/cancel");
		}
		size++;
		return done;
	}

	public ArrayList<String> getValidParams()
	{
		return params;
	}
}

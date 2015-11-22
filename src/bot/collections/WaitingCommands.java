package bot.collections;


import bot.botType.WaitingCommand;

import java.util.HashMap;

/**
 * Created by Paolo on 21/11/2015.
 */
public class WaitingCommands
{
	private static HashMap<Long,WaitingCommand> waitingCommandHashMap = new HashMap<Long, WaitingCommand>();

	/**
	 * Add message that bot is waiting for
	 * @param chatId, id of message's sender
	 * @param cmd, command waiting for
	 */
	public static void addCommand(Long chatId, WaitingCommand cmd)
	{
		waitingCommandHashMap.put(chatId, cmd);
	}

	/**
	 * Return if bot is waiting for that user
	 * @param chatId
	 * @return true if it is waiting
	 */
	public static boolean isWaitingFor(Long chatId)
	{
		return waitingCommandHashMap.containsKey(chatId);
	}

	/**
	 * Return command associated with that id
	 * @param chatId, id of the user
	 * @return command
	 */
	public static WaitingCommand getWaitingCommand(Long chatId)
	{
		return waitingCommandHashMap.get(chatId);
	}

	public static WaitingCommand removeCommand(Long chatId)
	{
		return waitingCommandHashMap.remove(chatId);
	}
}

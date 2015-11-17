package bot.telegramType;

import java.util.Arrays;

/**
 * Created by Utente on 13/10/2015.
 */
public class ReplyKeyboardMarkup
{
	private String[][] keyboard = new String[2][2];
	private boolean resizeKeyboard = false;
	private boolean oneTimeKeyboard = false;
	private boolean selective = true;

	public ReplyKeyboardMarkup(String[][] keyboard)
	{
		this.keyboard = keyboard;
	}

	public String[][] getKeyboard()
	{
		return keyboard;
	}

	public void setKeyboard(String[][] keyboard)
	{
		this.keyboard = keyboard;
	}

	public boolean isResizeKeyboard()
	{
		return resizeKeyboard;
	}

	public void setResizeKeyboard(boolean resizeKeyboard)
	{
		this.resizeKeyboard = resizeKeyboard;
	}

	public boolean isOneTimeKeyboard()
	{
		return oneTimeKeyboard;
	}

	public void setOneTimeKeyboard(boolean oneTimeKeyboard)
	{
		this.oneTimeKeyboard = oneTimeKeyboard;
	}

	public boolean isSelective()
	{
		return selective;
	}

	public void setSelective(boolean selective)
	{
		this.selective = selective;
	}
}

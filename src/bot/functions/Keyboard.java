package bot.functions;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Keyboard
{
	private JSONObject keyboard = new JSONObject();
	private JSONArray keys = new JSONArray();

	/**
	 * Constructor for the keyboard
	 *
	 * @param keys 2d array containing the keys for the keyboard
	 * @param oneTime true if you want the keyboard to hide after the user press a key
	 * @param resize true if you want the keyboard to resize itself
	 * @param selective true if you want the keyboard to appear only to a mentioned user or if the bot message is a reply
	 */
	public Keyboard(String[][] keys, boolean oneTime, boolean resize, boolean selective)
	{
		makeKeyboard(keys, oneTime, resize, selective);
	}

	/**
	 * Add a row to the keyboard
	 * @param row array containing the keys for the row
	 */
	@SuppressWarnings("unchecked")
	private void addRow(String[] row)
	{
		JSONArray rowJSON = new JSONArray();
		for(String r : row)
		{
			rowJSON.add(r);
		}
		keys.add(rowJSON);
	}

	/**
	 * Make the keyboard object
	 *
	 * @param keys 2d array containing the keys for the keyboard
	 * @param oneTime true if you want the keyboard to hide after the user press a key
	 * @param resize true if you want the keyboard to resize itself
	 * @param selective true if you want the keyboard to appear only to a mentioned user or if the bot message is a reply
	 */
	@SuppressWarnings("unchecked")
	private void makeKeyboard(String[][] keys, boolean oneTime, boolean resize, boolean selective)
	{
		for(String[] row : keys)
		{
			addRow(row);
		}
		keyboard.put("keyboard", this.keys);
		keyboard.put("one_time_keyboard", oneTime);
		keyboard.put("resize_keyboard", resize);
		keyboard.put("selective", selective);
	}

	/**
	 * Return the keyboard object as string
	 * @return keyboard object
	 */
	public String toJSONString()
	{
		return keyboard.toJSONString();
	}
}

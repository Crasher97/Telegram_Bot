package bot.translation;

import bot.Setting;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Paolo on 31/10/2015.
 */
public enum Sentences
{
	NULL("?"),
	MESSAGE_RECEIVED("message_received"),
	FROM("from"),
	NEW_USER("new_user"),
	HAS_CONNECTED("has_connected"),
	CONDITIONS("conditions1"),
	CONDITION_REQUEST("conditions_request"),
	MESSAGE_NOT_SENT("message_not_sent");

	private String sentence = "";
	private static HashMap<String, String> sentences = new HashMap<String, String>();

	Sentences(String x)
	{
		sentence = x;
	}
	public String getSentence()
	{
		return sentences.get(sentence);
	}

	public static HashMap<String, String> getSentences()
	{
		return sentences;
	}
}

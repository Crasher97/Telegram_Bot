package bot.translation;

import bot.Setting;

import java.util.ArrayList;

/**
 * Created by Paolo on 31/10/2015.
 */
public enum Sentences
{
	NULL(0),
	MESSAGE_RECEIVED(1),
	FROM(2),
	NEW_USER(3),
	HAS_CONNECTED(4);

	private int sentence = 0;
	private static ArrayList<String> sentences = new ArrayList<String>();

	Sentences(int x)
	{
		sentence = x;
	}
	public String getSentence()
	{
		return sentences.get(sentence);
	}

	public static ArrayList<String> getSentences()
	{
		return sentences;
	}
}

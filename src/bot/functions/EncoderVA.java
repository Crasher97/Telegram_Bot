package bot.functions;

import java.io.File;

import bot.Log;
import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;

public class EncoderVA
{
	/**
	 * Converts Mp4 file to MP3
	 *
	 * @param fileMp4
	 * @param fileMp3
	 * @return true if it has been converted
	 */
	public static boolean mp4ToMp3(File fileMp4, File fileMp3)
	{
		AudioAttributes audio = new AudioAttributes();
		audio.setCodec("libmp3lame");
		audio.setBitRate(new Integer(128000));
		audio.setChannels(new Integer(2));
		audio.setSamplingRate(new Integer(44100));
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat("mp3");
		attrs.setAudioAttributes(audio);
		Encoder encoder = new Encoder();
		try
		{
			encoder.encode(fileMp4, fileMp3, attrs);
			Log.info("File MP4 convertito in MP3");
			return true;
		}
		catch (IllegalArgumentException | EncoderException e)
		{
			Log.error("File non convertito");
			Log.stackTrace(e.getStackTrace());
			return false;
		}
	}
}

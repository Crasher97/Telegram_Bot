package bot.telegramType;

/**
 * Created by Paolo on 16/11/2015.
 */
public class Voice
{
	private String fileId = null;
	private int duration = 0;
	private String mimeType = null;
	private int file_size = 0;

	public Voice()
	{
	}

	public Voice(String fileId, int duration, String mimeType, int file_size)
	{
		this.fileId = fileId;
		this.duration = duration;
		this.mimeType = mimeType;
		this.file_size = file_size;
	}

	public int getFile_size()
	{
		return file_size;
	}

	public void setFile_size(int file_size)
	{
		this.file_size = file_size;
	}

	public String getMimeType()
	{
		return mimeType;
	}

	public void setMimeType(String mimeType)
	{
		this.mimeType = mimeType;
	}

	public String getFileId()
	{
		return fileId;
	}

	public void setFileId(String fileId)
	{
		this.fileId = fileId;
	}

	public int getDuration()
	{
		return duration;
	}

	public void setDuration(int duration)
	{
		this.duration = duration;
	}
}

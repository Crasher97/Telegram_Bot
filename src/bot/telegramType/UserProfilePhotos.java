package bot.telegramType;

/**
 * Created by Paolo on 15/11/2015.
 */
public class UserProfilePhotos
{
	public UserProfilePhotos(int total_count, PhotoSize[][] photos)
	{
		this.total_count = total_count;
		this.photos = photos;
	}

	private int total_count = 0;

	private PhotoSize[][] photos = new PhotoSize[3][3];

	public void setPhotos(PhotoSize[][] photos)
	{
		this.photos = photos;
	}

	public int getTotal_count()
	{
		return total_count;
	}

	public void setTotal_count(int total_count)
	{
		this.total_count = total_count;
	}

	public PhotoSize[][] getPhotos()
	{
		return photos;
	}

}

package bot.telegramType;

import org.apache.commons.ssl.org.bouncycastle.asn1.dvcs.Data;

import java.util.Date;

/**
 * Created by Paolo on 16/11/2015.
 */
public class Message
{
	private long messageId = 0;
	private User userFrom = null;
	private Date date = null;
	private Chat chat = null;
	private User fowardDate = null;
	private Message replyToMessage = null;
	private String text = null;
	private Audio audio = null;
	private Document document = null;
	private PhotoSize[] photo = null;
	private Sticker sticker = null;
	private Video video = null;
	private Voice voice = null;
	private String caption = null;
	private Contact contact = null;
	private Location location = null;
	private User newChatParticipant = null;
	private User leftChatParticipant = null;
	private String newChatTitle = null;
	private PhotoSize[] photoSizes = null;

	public Message()
	{
	}

	public long getMessageId()
	{
		return messageId;
	}

	public void setMessageId(long messageId)
	{
		this.messageId = messageId;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public PhotoSize[] getPhoto()
	{
		return photo;
	}

	public void setPhoto(PhotoSize[] photo)
	{
		this.photo = photo;
	}

	public PhotoSize[] getPhotoSizes()
	{
		return photoSizes;
	}

	public void setPhotoSizes(PhotoSize[] photoSizes)
	{
		this.photoSizes = photoSizes;
	}

	public String getNewChatTitle()
	{
		return newChatTitle;
	}

	public void setNewChatTitle(String newChatTitle)
	{
		this.newChatTitle = newChatTitle;
	}

	public String getCaption()
	{
		return caption;
	}

	public void setCaption(String caption)
	{
		this.caption = caption;
	}

	public User getLeftChatParticipant()
	{
		return leftChatParticipant;
	}

	public User getUserFrom()
	{
		return userFrom;
	}

	public void setUserFrom(User userFrom)
	{
		this.userFrom = userFrom;
	}

	public Chat getChat()
	{
		return chat;
	}

	public User getFowardDate()
	{
		return fowardDate;
	}

	public Message getReply_to_message()
	{
		return replyToMessage;
	}

	public Audio getAudio()
	{
		return audio;
	}

	public Document getDocument()
	{
		return document;
	}

	public Sticker getSticker()
	{
		return sticker;
	}

	public Video getVideo()
	{
		return video;
	}

	public Voice getVoice()
	{
		return voice;
	}

	public Contact getContact()
	{
		return contact;
	}

	public Location getLocation()
	{
		return location;
	}

	public User getNewChatParticipant()
	{
		return newChatParticipant;
	}

	public void setChat(Chat chat)
	{
		this.chat = chat;
	}

	public void setFowardDate(User fowardDate)
	{
		this.fowardDate = fowardDate;
	}

	public void setReplyToMessage(Message replyToMessage)
	{
		this.replyToMessage = replyToMessage;
	}

	public void setAudio(Audio audio)
	{
		this.audio = audio;
	}

	public void setDocument(Document document)
	{
		this.document = document;
	}

	public void setSticker(Sticker sticker)
	{
		this.sticker = sticker;
	}

	public void setVideo(Video video)
	{
		this.video = video;
	}

	public void setVoice(Voice voice)
	{
		this.voice = voice;
	}

	public void setContact(Contact contact)
	{
		this.contact = contact;
	}

	public void setLocation(Location location)
	{
		this.location = location;
	}

	public void setNewChatParticipant(User newChatParticipant)
	{
		this.newChatParticipant = newChatParticipant;
	}

	public void setLeftChatParticipant(User leftChatParticipant)
	{
		this.leftChatParticipant = leftChatParticipant;
	}
}

package bot.UpdateReader;

import bot.botType.Message;
import bot.telegramType.Chat;
import bot.telegramType.User;
import org.json.simple.JSONObject;

import java.util.Date;

/**
 * Created by Paolo on 17/11/2015.
 */
public class UpdateSplitter
{
	private JSONObject jsonObjectResult;
	private JSONObject messageJson;
	private Message msg;

	public UpdateSplitter(JSONObject jsonObjectResult, Message msg)
	{
		this.jsonObjectResult = jsonObjectResult;
		messageJson = (JSONObject) jsonObjectResult.get("message");
		this.msg = msg;
	}

	public JSONObject getJsonObjectResult()
	{
		return jsonObjectResult;
	}

	public void setJsonObjectResult(JSONObject jsonObjectResult)
	{
		this.jsonObjectResult = jsonObjectResult;
	}

	public Message getMsg()
	{
		return msg;
	}

	public void setMsg(Message msg)
	{
		this.msg = msg;
	}

	/**
	 * Take field messageJson and put inside it information about messageJson contained inside JSONObject
	 */
	public void getMessageInfo()
	{
		msg.setUpdate_id((long) jsonObjectResult.get("update_id"));
		msg.setMessageId((long) messageJson.get("message_id"));
		msg.setText((String) messageJson.get("text"));
		msg.setDate(new Date((long) messageJson.get("date") * 1000));
	}


	/**
	 * Take field messageJson and put inside it information about Chat contained inside JSONObject
	 */
	public void getChatInfo()
	{
		JSONObject jsonObjectChat = (JSONObject) messageJson.get("chat");
		long chat_id = (long) jsonObjectChat.get("id");
		String title = (String) jsonObjectChat.get("title");
		String type = (String) jsonObjectChat.get("type");
		String username = (String) jsonObjectChat.get("username");
		String first_name = (String) jsonObjectChat.get("first_name");
		String last_name = (String) jsonObjectChat.get("last_name");

		msg.setChat(new Chat(chat_id, type, title, username, first_name, last_name));
	}

	public void getUserInfo()
	{
		JSONObject jsonObjectUser = (JSONObject) messageJson.get("from");
		String first_name = (String) jsonObjectUser.get("first_name");
		String last_name = (String) jsonObjectUser.get("last_name");
		String username = (String) jsonObjectUser.get("username");
		long sender_id = (long) jsonObjectUser.get("id");

		msg.setUserFrom(new User(sender_id, first_name, last_name, username));
	}
}

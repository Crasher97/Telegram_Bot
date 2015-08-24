package bot.functions.polls;

import bot.Message;
import bot.Sender;

public class TelegramInterface
	{
		public static void changeVote(Message msg)
		{
			if(msg.getText().split(" ").length>0) Polls.sendOptions(msg, "/changevote");
			else Sender.sendMessage(msg.getSender_id(), "Syntax error. /changevote pollname");
		}
		
		public static void vote(Message msg)
		{
			if(msg.getText().split(" ").length>0) Polls.sendOptions(msg, "/vf");
			else Sender.sendMessage(msg.getSender_id(), "Syntax error. /vote pollname");
		}
	}		
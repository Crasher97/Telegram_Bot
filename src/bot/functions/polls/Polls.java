package bot.functions.polls;

import java.util.ArrayList;

import bot.Log;
import bot.Message;
import bot.Sender;

public class Polls
	{
		private static ArrayList<Poll> polls = new ArrayList<Poll>();
		//private HashMap<Long, Poll> participants = new HashMap<Long, Poll>();
		
		/**
		 * Adds an option to poll
		 * @param poll 
		 * @return true if it has been added
		 */
		public static boolean addPoll(Poll poll)
		{
			if(poll!=null)
				{
					polls.add(poll);
					return true;
				}
			else return false;
		}
		
		/**
		 * Return all open polls
		 */
		public static ArrayList<Poll> getPolls()
		{
			return polls;
		}
		
		/**
		 * remove poll
		 * @param name - name of the poll
		 * @return true if it has been deleted 
		 */
		public static boolean removePoll(Message msg)
			{
				String[] text = msg.getText().split(" ");
				if(text.length > 1)
						{
							int index = 0;
							for(Poll x : polls)
								{
									if(x.getPollName().equals(text[1]))
										{
											if(x.getOwner()==msg.getSender_id())
												{
													polls.remove(index);
													return true;
												}
										}
									index++;
								}
							return false;
						}
				else 
					{
						Sender.sendMessage(msg.getSender_id(), "Few parameters");
						return false;
					}
			}
		
		/**
		 * Adds a poll
		 * @param poll 
		 * @return true if it has been added
		 */
		public static boolean addPoll(String pollName, String[] pollOptions, long senderId)
		{
			if(pollName!=null || pollOptions.length<2)
				{
					if(pollExist(pollName) < 0)
					{
						Poll poll = new Poll(pollName, senderId);
						polls.add(poll);
						for(int index = 0; index < pollOptions.length; index++)
							{
								poll.addOption(new PollOption(pollOptions[index]));
							}
						return true;
					}
					else
						{
							Log.warn("Poll already exists");
							return false;
						}
				}
			else 
				{
					Log.warn("Pollname = null or PollOptions < 2");
					return false;
				}
		}
		
		/**
		 * Adds a poll
		 * @param msg
		 */
		public void addPoll(Message msg)
		{
			addPoll(msg.getText(), msg.getSender_id());
		}
		
		
		/**
		 * Adds a poll
		 * @param messagetText
		 * @param senderId
		 */
		public static void addPoll(String messageText, long senderId)
		{
			String[] messageSplit = messageText.split(" ");
			String pollName = messageSplit[1];
			if(messageSplit.length > 3)
				{
					String[] options = new String[messageSplit.length-2];
					String optionsToSend = "";
					for(int index = 0; index < options.length; index++)
						{
							optionsToSend = optionsToSend + ", " + messageSplit[index+2];
							options[index] = messageSplit[index+2];
						}
					addPoll(pollName, options, senderId);
					Sender.sendMessage(senderId, "You have created poll " + pollName + ". Options are: " + optionsToSend);
				}
			else
				{
					Sender.sendMessage(senderId, "Few options to change vote");
				}
		}
		
		/**
		 * Send list options of poll
		 * @param name
		 * @param senderId
		 * @return null if poll has not been founded
		 */
		public static void sendPollOptions(Message msg)
		{
			String[] text = msg.getText().split(" ");
			if(text.length > 0)
				{
					String pollName = text[1];
					long senderId = msg.getSender_id();
					int found = pollExist(pollName);
			
					if(found >= 0)
						{
							String tmp = "";
							for(int index = 0; index <= polls.get(found).getOptions().size(); index++)tmp = tmp + " " + polls.get(found).getOptions().get(index);
							Sender.sendMessage(senderId, tmp);
						}
					else Sender.sendMessage(senderId, "Poll non trovata");
				}
		}
		
		/**
		 * Return position of poll
		 * @param pollName
		 * @return int index - position of poll, if poll doesn't exist return -1.
		 */
		public static int pollExist(String pollName)
		{
			int index = 0;
			for(Poll poll : polls)
				{
					if(poll.getPollName().equals(pollName))
						{
							return index;
						}
					index++;
				}
			return -1;
		}
		
		/**
		 * Change vote for a Poll
		 * @param message msg
		 * @return true if vote has been changed
		 */
		public static void changePollVote(Message msg)
		{
			String[] text = msg.getText().split(" ");
			long senderId = msg.getSender_id();
			if(text.length > 1)
				{
					int indexOfPoll;
					if((indexOfPoll = pollExist(text[1]))>-1)
						{
							if(polls.get(indexOfPoll).changeVote(senderId, text[1])) Sender.sendMessage(senderId, "Your vote has been changed");
								
						}
					else
						{
							Sender.sendMessage(senderId, "Poll has not been found");
						}
				}
			else
				{
					Sender.sendMessage(senderId, "Few options to change vote");
				}
		}
		
		public static void vote(Message msg)
		{
			String[] text = msg.getText().split(" ");
			long senderId = msg.getSender_id();
			if(text.length > 1)
				{
					int indexOfPoll;
					if((indexOfPoll = pollExist(text[1]))>-1)
						{
							if(polls.get(indexOfPoll).voteFor(senderId, text[2]))Sender.sendMessage(senderId, "You have voted for " + text[2] + ", in poll " + text[1]);;
								
						}
					else
						{
							Sender.sendMessage(senderId, "Poll has not been found");
						}
				}
		}
	}

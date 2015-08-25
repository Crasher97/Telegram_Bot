package bot.functions.polls;

import java.util.ArrayList;

import addons.Command;
import addons.Commands;
import addons.Help;
import bot.Log;
import bot.Message;
import bot.Sender;
import bot.functions.Keyboard;

public class Polls
	{
		private static ArrayList<Poll> polls = new ArrayList<Poll>();
		
		/**
		 * Add a poll
		 * @param poll 
		 * @return true if it has been added
		 */
		public static boolean addPoll(Poll poll)
		{
			if(poll!=null && pollExist(poll.getPollName()) > -1)
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
									if(x.getPollName().equalsIgnoreCase(text[1]))
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
			if(messageSplit.length > 3 && messageSplit.length < 9)
				{
					String pollName = messageSplit[1];
					if(pollExist(pollName) < 0)
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
							Sender.sendMessage(senderId, "Poll already exists");
						}
				}
			else
				{
					Sender.sendMessage(senderId, "Few or too many options");
				}
		}
		
		/**
		 * Send list options of poll
		 * @param Message
		 */
		public static void sendOptions(Message msg, String command)
		{
			String[] text = msg.getText().split(" ");
			if(text.length > 1)
				{
					String pollName = text[1];
					long senderId = msg.getSender_id();
					int found = pollExist(pollName);
			
					if(found >= 0)
						{
							ArrayList<PollOption> options = polls.get(found).getOptions();
							String[][] keys = new String[3][3];
							for(int index = 0, l = 0, h = 0; index < 9; index++)
								{
									keys[h][l] = (index < options.size())?command + " " + pollName + " " + options.get(index).getOption():"null";
									if(l < 2)l++;
									else
										{
											h++;
											l = 0;
										}
								}
							Keyboard k = new Keyboard(keys, true, true, true);
							Sender.sendMessage(senderId, pollName + "Options are on the custom kayboard", k);
						}
					else Sender.sendMessage(senderId, "Poll non trovata");
				}
		}
		
		/**
		 * Send all poll options and their votes
		 * @param message
		 */
		public static void sendPollOptions(Message msg)
		{
			String[] text = msg.getText().split(" ");
			if(text.length > 1)
				{
					String pollName = text[1];
					long senderId = msg.getSender_id();
					int found = pollExist(pollName);
			
					if(found >= 0)
						{
							String x = "";
							ArrayList<PollOption> options = polls.get(found).getOptions();
							for(PollOption option : options)
								{
									x = x + option.getOption() + " [" + option.getVotes() + "]%0A";
								}
							Sender.sendMessage(msg.getSender_id(), x);
						}
					else Sender.sendMessage(senderId, "Poll non trovata");
				}
		}
		
		/**
		 * Send list of all polls
		 * @param Message
		 */
		public static void sendPolls(Message msg)
		{
			String[] text = msg.getText().split(" ");
			if(text.length > 0)
				{
					String x = "Polls Active: ";
					for(Poll poll : polls)
						{
							x = x + " %0A" + poll.getPollName();
						}
					if(x.equals("Polls Active: "))
						{
							Sender.sendMessage(msg.getSender_id(), "There are no polls actives");
						}
					else
						{
							Sender.sendMessage(msg.getSender_id(), x);
						}
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
					if(poll.getPollName().equalsIgnoreCase(pollName))
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
		
		/**
		 * Vote for an option in a poll
		 * @param msg (/vf pollname)
		 */
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
		
		/**
		 * Load polls commands in bot & add help
		 */
		public static void load()
		{

			Commands.addCommand(new Command("addpoll", "bot.functions.polls.Polls", "addPoll"));
			Help.addHelp("addpoll", "Add a poll. (/addpoll pollname polloption polloption2 ...)");
			
			Commands.addCommand(new Command("rmpoll", "bot.functions.polls.Polls", "removePoll"));
			Help.addHelp("rmpoll", "Remove a poll, only owner can remove poll. (/rmpoll pollname)");
			
			Commands.addCommand(new Command("changevote", "bot.functions.polls.TelegramInterface", "changeVote"));
			Help.addHelp("changevote", "Change your vote in a poll. (/changevote pollname)");
			Commands.addCommand(new Command("cgv", "bot.functions.polls.Polls", "changeVote"));
			
			Commands.addCommand(new Command("vote", "bot.functions.polls.TelegramInterface", "vote"));
			Help.addHelp("vote", "Vote for a poll. (/vote pollname)");
			Commands.addCommand(new Command("vf", "bot.functions.polls.Polls", "vote"));
			
			Commands.addCommand(new Command("poll", "bot.functions.polls.Polls", "sendPollOptions"));
			Help.addHelp("poll", "Send all information abount poll. (/poll pollname)");
			
			Commands.addCommand(new Command("polls", "bot.functions.polls.Polls", "sendPolls"));
			Help.addHelp("polls", "Send polls list. (/polls)");
		}
	}

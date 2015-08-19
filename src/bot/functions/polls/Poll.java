package bot.functions.polls;

import java.util.ArrayList;
import java.util.HashMap;

import bot.Log;
import bot.Sender;

public class Poll
	{
		private String pollName;
		private ArrayList<PollOption> options = new ArrayList<PollOption>(10);
		private HashMap<Long, Integer> participants = new HashMap<Long, Integer>();
		private long time;
		private long ownerOfPoll;
		
		/**
		 * Constructor of class Poll
		 * @param name
		 * @param ownerOfPoll
		 */
		Poll(String name, long ownerOfPoll)
		{
			this.ownerOfPoll = ownerOfPoll;
			pollName = name;
			time = System.currentTimeMillis();
		}
		
		/**
		 * add option to poll
		 * @param option
		 * @return true if option has been added
		 */
		public boolean addOption(PollOption option)
		{
			if(option!=null && options.size() < 10)
				{
					options.add(option);
					return true;
				}
			else
				{	
					return false;
				}
		}
		
		/**
		 * return pollName
		 * @return pollName
		 */
		public String getPollName()
		{
			return pollName;
		}
		
		/**
		 * Return options
		 * @retrun all options
		 */
		public ArrayList<PollOption> getOptions()
		{
			return options;
		}
		
		/**
		 * Add participant to poll
		 * @param senderId
		 * @return true if it has been added
		 */
		public boolean addParticipant(long senderId, int indexOfOptionVoted)
		{
			if(senderId > 0)
				{
					participants.put(senderId, indexOfOptionVoted);
					return true;
				}
			return false;
		}
		
		/**
		 * return all poll's participants
		 * @return
		 */
		public HashMap<Long, Integer> getParticipants()
		{
			return participants;
		}
		
		/**
		 * Return poll's duration
		 * @return duration
		 */
		public long getDuration()
		{
			return System.currentTimeMillis() - time;
			
		}
		
		/**
		 * Vote for an Option
		 * @param senderId
		 * @param optionName
		 * @return true if option has benn voted
		 */
		public boolean voteFor(long senderId, String optionName)
		{
			if(participants.containsKey(senderId))
				{
					Sender.sendMessage(senderId, "You have already voted");
					return false;
				}
			else
				{
					int index;
					if((index = indexOfOption(optionName)) >= 0)
						{
							if(addParticipant(senderId, index))
								{
									options.get(index).vote();
									return true;
								}
							Log.warn("Error while adding participants");
							return false;
							
						}
					else
						{
							Sender.sendMessage(senderId, "There is no option named as " + optionName);
							return false;
						}
				}
		}
		
		/**
		 * Return index of Option
		 * @param optionName
		 * @return optionPosition, -1 if there is no option named as parameter
		 */
		public int indexOfOption(String optionName)
		{
			int index = 0;
			for(PollOption option : options)
				{
					if(optionName.equals(option.getOption()))
						{
							return index;
						}
					index++;
				}
			return -1;
		}
		
		/**
		 * Return owner of poll
		 * @return owner
		 */
		public long getOwner()
			{
				return ownerOfPoll;
			}
		
		/**
		 * Change vote to another option
		 * @param senderId
		 * @param optionName
		 * @return true if vote has been changed
		 */
		public boolean changeVote(long senderId, String optionName)
		{
			if(participants.containsKey(senderId))
				{
					int indexOfOption = participants.get(senderId);
					int indexOfNewOption;
					if((indexOfNewOption = indexOfOption(optionName))>=0)
						{
							options.get(indexOfOption).voteDecrease();
							options.get(indexOfNewOption).vote();
							Sender.sendMessage(senderId, "Vote changed");
							return true;
						}
					else
						{
							Sender.sendMessage(senderId, "Option doesn't exist");
							return false;
						}
				}
			else
				{
					Sender.sendMessage(senderId, "You don't have already voted");
					return false;
				}
		}
	}

package bot.functions.polls;

public class PollOption
	{
		private int votes;
		private String option;
		
		/**
		 * Create new opzion for poll
		 * @param option
		 */
		PollOption(String option)
		{
			votes = 0;
			this.option = option; 
		}
		
		/**
		 * increase votes
		 */
		public void vote()
		{
			votes++;
		}
		
		/**
		 * decrease votes
		 */
		public void voteDecrease()
		{
			votes--;
		}
		
		/**
		 * Change option name
		 * @param newOption
		 * @return true if changed
		 */
		public boolean setOption(String newOption)
		{
			if(newOption!=null)
				{
					option = newOption;
					return true;
				}
			else return false;
		}
		
		/**
		 * Return option
		 * @return optionName
		 */
		public String getOption()
		{
			return option;
		}
		
		/**
		 * Return number of votes
		 * @return votes
		 */
		public int getVotes()
		{
			return votes;
		}
		
		/**
		 * @return option + votes
		 */
		@Override
		public String toString()
		{
			return option + ": votes[" + votes + "]";
		}
	}


public class Identifier 
{
	private static String[] info = new String[20];
	
	/**
	 * getInfo
	 * @param index, la posizione
	 * @return String info, l'informazione richiesta
	 */
	public static String getInfo(int index)
	{
		if(index > 0 & index < info.length)
		{
			return info[index];
		}
		else
		{
			return "";
		}
	}
	
	public static String[] getInfo()
	{
		return info;
	}
	
	public static void stringBreaker()
	{
		
	}
	
}

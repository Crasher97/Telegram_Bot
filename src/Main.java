
public class Main 
{
	private static String idCode = "";
	private static String url;
	
	public static void main(String[] args) 
	{
		idCode = args[0];
		url = "https://api.telegram.org/bot" + idCode;
		Sender.sendMessage(84985065, "MessaggiodiProvaSender");
		Reader.getUpdate();
	}
	
	public static String getIdCode()
	{
		return idCode;
	}
	
	public static String getUrl()
	{
		return url;
	}

}
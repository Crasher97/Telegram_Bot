
public class Main 
{
	private static String idCode = "";
	private static String url;
	
	/**
	 * Metodo main, punto di partenza del programma
	 * @param args, l'idCode del bot telegram, deve essere inserito
	 */
	public static void main(String[] args) 
	{
		idCode = args[0];
		url = "https://api.telegram.org/bot" + idCode;
		Sender.sendMessage(Integer.parseInt(args[1]), "MessaggiodiProvaSender"); // Al: 84954308  Pa: 84985065
		Reader.getUpdate();
	}
	
	/**
	 * getIdCode - restituisce il codice identificativo
	 * @return String idCode
	 */
	public static String getIdCode()
	{
		return idCode;
	}
	
	/**
	 * Restituisce l'url di riferimento del bot
	 * @return String - url
	 */
	public static String getUrl()
	{
		return url;
	}

}
package commands;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Data 
{
    private static DateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static Date data;
    private static String dataString;
    
    public static void functionData()
    {
    	data = new Date();
        dataString = formatoData.format(data);
        //aggiungere Sender.sendMessage(, dataString)
    }
    
    public static String dataToString(Date date)
    {
    	dataString = formatoData.format(date);
    	return dataString;
    }
    
}

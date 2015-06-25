package commands;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Data 
{
    private static DateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static Date data;
    private static String dataString;
    
    public Data()
    {
    	data = new Date();
        dataString = formatoData.format(data);
        //aggiungere Sender.sendMessage(, dataString)
    }
    
}

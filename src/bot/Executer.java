package bot;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Executer 
{
	public static void execute(Message msg)
	{
		try 
		{
			//toglie la / dal comando
			String text = msg.getText();
			text = text.substring(0, 0) + text.substring(1);
			System.out.println(text);
			
			Process process = Runtime.getRuntime().exec("java -jar addons\\" + text + ".jar");
			InputStream stdinput = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(stdinput);
			BufferedReader reader = new BufferedReader(isr);

			String line = null;
			String message = "";
			while ( (line = reader.readLine()) != null)
			{
			    message = message + " " + line;
			}
			Sender.sendMessage((int)msg.getSender_id(), message);
		
		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("comando non eseguito");
			Sender.sendMessage((int)msg.getSender_id(), "Sembra che ci sia stato un maledetto errore");
		}
	}
}

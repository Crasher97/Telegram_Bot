package bot;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ExternalExecuter
	{
		
	/**
     * Esegue un comando esterno Windows & Linux
	 * @param command
	 * @param logFileName the name of the log where images responses are saved
	 * @return true se � stato eseguito correttamente
     */
	public static boolean executeCmd(String command, String logFileName)
	{
			try 
				{
				Process process = Runtime.getRuntime().exec(command);
				Log.info("Eseguzione comando esterno in corso");

				// Get input streams
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

				// write command standard output in a log file
				String s;
				while ((s = stdInput.readLine()) != null) 
					{
            		IO.writeOUT(logFileName, s);
					}
				return true;
				}    
			catch (Exception e)
				{
					e.printStackTrace(System.err);
					Log.error("Errore durante l'eseguzione");
					return false;
				}
	}
}


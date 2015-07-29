package bot;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ExternalExecuter
	{
		
	/**
     * Esegue un comando esterno WINDOWS
	 * @param String command
	 * @param String logFileName , the name of the log where images responses are saved
     */
	public static void executeCmd(String command, String logFileName)
	{
		String os = System.getProperty("os.name");
		if(os.split(" ")[0].equalsIgnoreCase("windows")==true)
		{
			try 
				{
				// Run "netsh" Windows command
				Process process = Runtime.getRuntime().exec(command);
				System.out.println("eseguzione comando esterno in corso");

				// Get input streams
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

				// write command standard output in a log file
				String s;
				while ((s = stdInput.readLine()) != null) 
					{
            		IO.writeOUT(logFileName, s);
					}
            
				}    
			catch (Exception e)
				{
					e.printStackTrace(System.err);
					System.err.println("Errore durante l'eseguzione");
				}
		}
		else
		{
			//TODO linux
		}
	}
}


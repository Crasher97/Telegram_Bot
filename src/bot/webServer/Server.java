package bot.webServer;

import bot.*;
import fi.iki.elonen.NanoHTTPD;
import org.apache.commons.ssl.KeyMaterial;
import org.apache.commons.ssl.SSLServer;
import org.apache.commons.ssl.TrustMaterial;

import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Server extends NanoHTTPD
{
	private static Server server;

	/*
		To generate SSL certificate use OpenSSL with the following command:
		1) Generate the certificate:
		openssl req -new -x509 -nodes -out server.crt -keyout server.key

		2)Set a password:
		openssl rsa -des3 -in server.key -out server.key.new
		mv server.key.new server.key
	 */


	/**
	 * Constructor for web server
	 *
	 * @param certificateChain SSL Certificate
	 * @param privateKey       SSL Certificate private key
	 * @param certPassword     SSL Certificate password
	 * @param serverPort       Server port
	 */
	private Server(String certificateChain, String privateKey, String certPassword, int serverPort)
	{
		super("0.0.0.0", serverPort);
		this.makeSecure(makeSSLServerSocketFactory(certificateChain, privateKey, certPassword), null);
	}

	/**
	 * Make a SSLServerSocketFactory from the given certificate
	 *
	 * @param certificateChain SSL Certificate
	 * @param privateKey       SSL Certificate private key
	 * @param password         SSL Certificate password
	 * @return An SSLServerSocketFactory built from the given certificate
	 */
	public SSLServerSocketFactory makeSSLServerSocketFactory(String certificateChain, String privateKey, String password)
	{
		SSLServerSocketFactory sslServerSocketFactory = null;
		try
		{
			SSLServer server = new SSLServer();
			char[] passwordC = password.toCharArray();
			KeyMaterial km = new KeyMaterial(certificateChain, privateKey, passwordC);
			server.setKeyMaterial(km);
			server.setCheckHostname(false);
			server.setCheckExpiry(true);
			server.setCheckCRL(true);
			server.addTrustMaterial(TrustMaterial.TRUST_ALL);
			sslServerSocketFactory = server.getSSLContext().getServerSocketFactory();
		}
		catch (Exception e)
		{
			Log.stackTrace(e.getStackTrace());
		}
		return sslServerSocketFactory;
	}

	/**
	 * Method that handle the connection to the server and extract the JSON data from the POST
	 *
	 * @param session
	 * @return
	 */
	@Override
	public Response serve(IHTTPSession session)
	{
		Map<String, String> files = new HashMap<String, String>();
		Method method = session.getMethod();
		if (Method.PUT.equals(method) || Method.POST.equals(method))
		{
			try
			{
				session.parseBody(files);
			}
			catch (IOException | ResponseException e)
			{
				Log.stackTrace(e.getStackTrace());
			}
		}

		String update = files.get("postData");
		if (update != null)
		{

			ArrayList<Message> message = UpdatesReader.parseJSON(update);
			for (Message msg : message)
			{
				Main.messageProcessThread(msg);
				try
				{
					Thread.sleep(20);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}

		return newFixedLengthResponse(null);
	}

	/**
	 * Start the server with the config found in the setting file
	 *
	 * @return true if the server is started successfully else false
	 */
	public static boolean startServer()
	{
		String certificateChain = Setting.readSetting("Certificate", "WebHookServer");
		String privateKey = Setting.readSetting("Private_Key", "WebHookServer");
		String certPassword = Setting.readSetting("Certificate_Password", "WebHookServer");
		int serverPort = Integer.parseInt(Setting.readSetting("WebHook_Port", "WebHook"));

		server = new Server(certificateChain, privateKey, certPassword, serverPort);

		try
		{
			server.start(NanoHTTPD.SOCKET_READ_TIMEOUT, true);
			return true;
		}
		catch (IOException e)
		{
			Log.stackTrace(e.getStackTrace());
			return false;
		}
	}

	/**
	 * Stop the server
	 */
	public static void stopServer()
	{
		server.stop();
	}
}
package bot.webServer;

import bot.Log;
import bot.Setting;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Server
{
	public static void startWebServer() {
		try
		{
			HttpServer server = HttpServer.create(new InetSocketAddress(Integer.parseInt(Setting.readSetting("WebHook_Port", "WebHook"))), 0);
			server.createContext("/" + Setting.readSetting("WebHook_Local", "WebHook"), new MyHandler());
			server.setExecutor(null);
			server.start();
		}
		catch(Exception e)
		{
			Log.stackTrace(e.getStackTrace());
		}
	}

	static class MyHandler implements HttpHandler
	{
		@Override
		public void handle(HttpExchange t) throws IOException
		{
			String response = "OK";
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
			getQuery(t.getRequestURI().getQuery());
		}

		public void getQuery(String query)
		{
			System.out.println(query);
		}
	}
}

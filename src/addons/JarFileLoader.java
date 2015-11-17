package addons;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import bot.log.Log;
import bot.botType.Message;

public class JarFileLoader extends ClassLoader
{
	/**
	 * Execute the method load in all the addons
	 */
	public static void loadJarFile()
	{
		invokeClassMethod("Main", "load");
	}

	/**
	 * Execute a method in a class of all the jar file in the addons directory
	 *
	 * @param classBinName Class name
	 * @param methodName   Method name
	 */
	public static void invokeClassMethod(String classBinName, String methodName)
	{
		int addonsNumber = 0;
		File dir = new File("addons/");
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null)
		{
			for (File file : directoryListing)
			{
				// Per ogni file presente nella cartella addons
				try
				{
					if (file.toURI().toURL().toString().endsWith(".jar"))
					{
						URL url = file.toURI().toURL();
						URL[] urls = new URL[]{url};

						// Crea il Class Loader
						ClassLoader classLoader = new URLClassLoader(urls);

						Class<?> loadedMyClass = classLoader.loadClass(classBinName);

						//System.out.println("Loaded class name: " + loadedMyClass.getName() + " From jar: " + file.getName());

						// Create a new instance from the loaded class
						Constructor<?> constructor = loadedMyClass.getConstructor();
						Object myClassObject = constructor.newInstance();

						// Getting the target method from the loaded class and
						// invoke it using its name
						try
						{
							Method method = loadedMyClass.getMethod(methodName);
							//System.out.println("Invoked method name: " + method.getName());
							Log.config("Addons " + file.getName() + " Caricato con successo");
							method.invoke(myClassObject);
							addonsNumber++;
						}
						catch (NoSuchMethodException em)
						{
							Log.error("Metodo " + methodName + " non trovato nel file " + file.getName());
						}

						((URLClassLoader) classLoader).close();
					}

				}
				catch (ClassNotFoundException e)
				{
					Log.error("Classe " + classBinName + " non trovata, impossibile caricare addons " + file.getName());
				}
				catch (Exception e)
				{
					Log.stackTrace(e.getStackTrace());
				}
			}
			Log.info(addonsNumber + " addons have been loaded");
		}

	}

	/**
	 * Call an addon's method
	 *
	 * @param jarName      Name of the addon's jar file
	 * @param classBinName Class name
	 * @param methodName   Method name
	 * @param message      Message
	 */
	public static void invokeClassMethod(String jarName, String classBinName, String methodName, Message message)
	{
		File file = new File("addons/" + jarName);

		// Per ogni file presente nella cartella addons
		try
		{
			if (file.toURI().toURL().toString().endsWith(".jar"))
			{
				URL url = file.toURI().toURL();
				URL[] urls = new URL[]{url};

				// Crea il Class Loader
				ClassLoader classLoader = new URLClassLoader(urls);

				Class<?> loadedMyClass = classLoader.loadClass(classBinName);

				Log.info("Loaded class name: " + loadedMyClass.getName() + " From jar: " + file.getName());

				// Create a new instance from the loaded class
				Constructor<?> constructor = loadedMyClass.getConstructor();
				Object myClassObject = constructor.newInstance();

				// Getting the target method from the loaded class and
				// invoke it using its name
				try
				{
					Method method = loadedMyClass.getMethod(methodName, Message.class);
					Log.info("Invoked method name: " + method.getName());
					method.invoke(myClassObject, message);
				}
				catch (NoSuchMethodException em)
				{
					Log.stackTrace(em.getStackTrace());
					Log.error("Metodo " + methodName + " non trovato nel file " + file.getName());
				}
				((URLClassLoader) classLoader).close();
			}

		}
		catch (ClassNotFoundException e)
		{
			Log.stackTrace(e.getStackTrace());
			Log.error("Classe " + classBinName + " non trovata nell addons " + file.getName());
		}
		catch (Exception e)
		{
			Log.stackTrace(e.getStackTrace());
		}
	}

}

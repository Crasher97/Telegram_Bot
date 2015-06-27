package addons;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class JarFileLoader extends ClassLoader
{
	public static void loadJarFile()
	{
		String classBinName = "Main";
		String methodName = "main";
			
		File dir = new File("addons\\");
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
						URL[] urls = new URL[] { url };

						// Crea il Class Loader
						ClassLoader classLoader = new URLClassLoader(urls);

						Class<?> loadedMyClass = classLoader.loadClass(classBinName);

						System.out.println("Loaded class name: " + loadedMyClass.getName() + " From jar: " + file.getName());
						
						// Create a new instance from the loaded class
						Constructor<?> constructor = loadedMyClass.getConstructor();
						Object myClassObject = constructor.newInstance();

						// Getting the target method from the loaded class and
						// invoke it using its name
						try
						{
							Method method = loadedMyClass.getMethod(methodName);
							System.out.println("Invoked method name: " + method.getName());
							method.invoke(myClassObject);
						}
						catch(NoSuchMethodException em)
						{
							System.err.println("Metodo Main non trovato nel file " + file.getName());
						}
						((URLClassLoader) classLoader).close();
					}

				}
				catch (ClassNotFoundException e)
				{
					//e.printStackTrace();
					System.err.println("Classe " + classBinName + " non trovata, impossibile caricare addons " + file.getName());
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}

		}

	}
}

# Telegram Bot
This project is a bot based on addons.
To get our project working you need to download from here(github) and then compile yourself.

How to create your addon:
1. Download codes
2. Open with you IDE
3. Create a new module that has as dependency our code
4. Create a class named "Main" that **implement** addons.Addon 
5. Create new method with this signature: public void load()
6. In this method you have to do one of things below:
 * Create a waiting command: This is a command that still wait until user send param (Ex. "/commad" then bot wait for a futere message with only "param") SEE POINT A
 * Create a simple command: In this case param is together with commad (Ex. "/command param") SEE POINT B

Your method load contanis information about what bot have to do when your command is used.
Command signature consists in:

new command(commandName, JarName, ClassWhereMethosIs, MethodToBeInvoked);

Then only for waiting command you can add parameters that will be sent by bot, at the invocation of the command, throught a custom keyboard.

7.)Develop your addon.

ADDON EXAMPLE(2015-11-22): https://github.com/BITeam/Telegram_Bot_Addon_Example




A.	Ex of waiting command

	public void load()
	{

		WaitingCommand img = new WaitingCommand("img", "ImageUploader.jar", "ImageUploader", "upload"); //CREATING WAITING COMMAND

		img.addParam("random"); //ADDING PARAMETER

		img.addParam("nasa");

		Commands.addCommand(img); //ADDING COMMAND IN COLLECTION

		Help.addHelp("img", "Get random image: %0A/img random%0AGet nasa pic of the day:%0A/img nasa"); //ADDING HELP FOR COMMAND addHelp(commandName,help);

	}
}

B.	Ex of simple command

	```public void load()
	{

		Command cmd = new Command("data", "data.jar", "Main", "functionData");

		Commands.addCommand(cmd);

	}```
 



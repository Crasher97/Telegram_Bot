package GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import bot.Log;
import bot.Main;
import bot.Setting;

public class Gui implements ActionListener
	{
		private JFrame frame;
		private JTextField botId;
		private JTextField ownerId;
		private JTextArea OUT;
		private JScrollPane stdOUT;
		private JTextArea ERR;
		private JScrollPane stdERR;
		private PrintStream printStreamOUT;
		private PrintStream printStreamERR;
		private Thread thread;
		private boolean stop;
		private JButton button;
		private static Gui gui;
		private JPanel panel;
		
		/**
		 * 
		 */
		public static void startGui()
		{
			String[] settings = Setting.readLastSettings();
			if(settings.length >= 2 && settings[0] != null && settings[1] != null)
				{
					Main.setBotId(settings[0]);
					Main.setOwnerId(settings[1]);
					Log.warn("idCode & ownerId are loaded from setting file");
				}
			gui = new Gui();
			gui.makeGui();
		}
		
		/**
		 * Constructor of class Gui
		 */
		private Gui()
			{
				stop = false;
				
				OUT = new JTextArea(10, 50);
				stdOUT = new JScrollPane(OUT);
				stdOUT.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				
				ERR = new JTextArea(10, 50);
				stdERR = new JScrollPane(ERR);
				stdERR.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				
				botId = new JTextField();
				ownerId = new JTextField();
				printStreamOUT = new PrintStream(
						new CustomOutputStream(OUT));
				printStreamERR = new PrintStream(
						new CustomOutputStream(ERR));
				button = new JButton("Start");
				
				panel = new JPanel(new GridLayout(5,2));
				panel.setBounds(10, 10, 25, 25);
			}

		public void makeGui()
			{
				frame = new JFrame("Telegram Bot");
				frame.setSize(75, 75);
			    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			    frame.setLayout(new FlowLayout());
			    
			   
				
				addjLabel("Bot ID :");
				botId.setEditable(true);
				panel.add(botId);
				botId.setText(bot.Main.getIdCode());
				
				
				addjLabel("Bot Owner ID :");
				ownerId.setEditable(true);
				panel.add(ownerId);
				ownerId.setText(bot.Main.getOwner());
				
				
				addjLabel("CONSOLE :");
				OUT.setEditable(false);
			    panel.add(stdOUT);
				
				
				addjLabel("ERROR CONSOLE :");
				ERR.setEditable(false);
				panel.add(stdERR);
				
			
				addjLabel("START :");
				button.addActionListener(this);
				panel.add(button);
				frame.getContentPane().add(panel, BorderLayout.CENTER);
				
				System.setOut(printStreamOUT);
				System.setErr(printStreamERR);
				frame.pack();
				frame.setVisible(true);
			}
		
		/**
		 * An interface action has been performed. Find out what it was and
		 * handle it.
		 * 
		 * @param event
		 *            The event that has occured.
		 */
		public void actionPerformed(ActionEvent event)
			{
				if(stop != true)
					{
						thread = new Thread(new Runnable() {
							public void run()
								{
									if(botId.getText() != null && ownerId.getText() != null)
									{
										String[] args = { botId.getText(), ownerId.getText()};
										saveConfiguration(botId.getText(), ownerId.getText());
										bot.Main.main(args);
									}
								}
						});
						thread.start();
						stop = true;
						button.setText("Stop");
						frame.pack();
					}
				else
					{
						thread.interrupt();
						stop = false;
						button.setText("Start");
						frame.pack();
					}
			}
		
		/**
		 * Change text inside botUrl textArea
		 * @param botUrl
		 */
		public void setBotUrl(String botUrl)
		{
			this.botId.setText(botUrl);
		}
		
		/**
		 * Change text inside ownerId textArea
		 * @param ownerId
		 */
		public void setOwner0Id(String ownerId)
		{
			this.ownerId.setText(ownerId);
		}
		
		/**
		 * Save confuguration for next start
		 * @param botUrl
		 * @param ownerId
		 */
		public void saveConfiguration(String botUrl, String ownerId)
		{
			//SAVE BOT URL CONFIGURATION
			if(bot.Setting.settingExist("Bot_ID", "Main"))
				{
					bot.Setting.editSetting("Bot_ID", botUrl, "Main");
				}
			else
				{
					bot.Setting.addSetting("Bot_ID", botUrl, "Main");
				}
			
			//SAVE OWNER CONFIGURATION
			if(bot.Setting.settingExist("Owner_ID", "Main"))
				{
					bot.Setting.editSetting("Owner_ID", ownerId, "Main");
				}
			else
				{
					bot.Setting.addSetting("Owner_ID", ownerId, "Main");
				}
		}
		
		public void addjLabel(String name)
		{
			JLabel label = new JLabel(name);
			label.setVisible(true);
			panel.add(label);
		}
		/**
		 * Return last botUrl used
		 * @return lastBotUrl
		 */
		@Deprecated
		public String lastBotUrl()
		{
			if(bot.Setting.settingExist("Bot_ID", "Main"))
				{
					return bot.Setting.readSetting("Bot_ID", "Main");
				}
			else
				return "";
		}
		
		/**
		 * Return last owner used
		 * @return lastOwner
		 */
		@Deprecated
		public String lastOwner()
			{
				if(bot.Setting.settingExist("Owner_ID", "Main"))
					{
						return bot.Setting.readSetting("Owner_ID", "Main");
					}
				else
					return "";
			}
	}
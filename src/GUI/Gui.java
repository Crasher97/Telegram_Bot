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

public class Gui implements ActionListener
	{
		private JFrame frame;
		private JTextField botUrl;
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

		public Gui()
			{
				stop = false;
				
				OUT = new JTextArea(7, 75);
				stdOUT = new JScrollPane(OUT);
				stdOUT.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				
				ERR = new JTextArea(7, 75);
				stdERR = new JScrollPane(ERR);
				stdERR.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				
				botUrl = new JTextField();
				ownerId = new JTextField();
				printStreamOUT = new PrintStream(
						new CustomOutputStream(OUT));
				printStreamERR = new PrintStream(
						new CustomOutputStream(ERR));
				button = new JButton("Start");
				makeGui();
				frame.setVisible(true);
			}

		public void makeGui()
			{
				frame = new JFrame("Telegram Bot");
				frame.setSize(75, 75);
			    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			    frame.setLayout(new FlowLayout());
			    
			    JPanel panel = new JPanel(new GridLayout(5,2));
			    panel.setBounds(10, 10, 25, 25);
				
				JLabel label1 = new JLabel("Bot Url :");
				label1.setVisible(true);
				panel.add(label1);
				botUrl.setEditable(true);
				panel.add(botUrl);
				
				
				JLabel label2 = new JLabel("Owner ID :");
				label2.setVisible(true);
				panel.add(label2);
				ownerId.setEditable(true);
				ownerId.setText("84985065"); //TODO DELETE
				panel.add(ownerId);
				
				
				JLabel label3 = new JLabel("CONSOLE");
				label3.setVisible(true);
				panel.add(label3);
				OUT.setEditable(false);
			    panel.add(stdOUT);
				
				
				JLabel label4 = new JLabel("ERROR CONSOLE");
				label4.setVisible(true);
				panel.add(label4);
				ERR.setEditable(false);
				panel.add(stdERR);
				
				JLabel label5 = new JLabel("START:");
				label5.setVisible(true);
				panel.add(label5);button.addActionListener(this);
				panel.add(button);
				frame.getContentPane().add(panel, BorderLayout.CENTER);
				
				System.setOut(printStreamOUT);
				System.setErr(printStreamERR);
				frame.pack();
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
									String[] args = { botUrl.getText(), ownerId.getText()};
									bot.Main.main(args);
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
	}

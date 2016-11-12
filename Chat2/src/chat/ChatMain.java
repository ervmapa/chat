package chat;

import javax.swing.JOptionPane;

/**
 * Program entry point. A simple chat application that lets you chat with
 * all others with the same application. In this version there is only one
 * global channel and users can not create or join other channels. 
 * 
 * @author Mats Palm (original by Thomas Ejnefjäll)
 * @version 0.1
 */
public class ChatMain {
	/**
	 * Program entry point
	 * 
	 * @param args a name to use in the chat, if no name is provided via main 
	 * 			   the program will prompt the user for one
	 */
	public static void main(String[] args) {
		String userName = "";

		if (args.length > 0) {
			userName = args[0];
		}

		while(userName == null || userName.length() < 1) {
			userName = JOptionPane.showInputDialog(null, "Enter your name", "Name", JOptionPane.QUESTION_MESSAGE);
		}
		
		/* A UDPChatCommunicator is something that:
		 * 
		 * - is both a Chatsubscriber and ChatPublisher
		 * - as ChatPublisher, listens for incoming messages on the network and propagates the messages to Chatsubscribers (GUI)
		 * - as Chatsubscriber, receives messages from a ChatPublisher (GUI) and sends them out on the network
		 */
		UDPChatCommunicator udpcomm = new UDPChatCommunicator();
		
		/* A ChatGUI is something that:
		 * 
		 * - is both a Chatsubscriber and ChatPublisher
		 * - as ChatPublisher, allows a user to enter message in  a Gui, and propagates the messages to Chatsubscribers (UDPChatCommunicator)
		 * - as Chatsubscriber, receives messages from a ChatPublisher (UDPChatCommunicator) and shows it in the Gui
		 */		
		ChatGUI chatgui = new ChatGUI(userName);
		
		// Register gui-subscriber/observer to the udpcomm-Publisher/subject  
		udpcomm.registerSubscriber(chatgui);
		
		// Register udpcomm-subscriber/observer to the gui-Publisher/subject  
		chatgui.registerSubscriber(udpcomm);
		
		chatgui.setVisible(true);

	}
}
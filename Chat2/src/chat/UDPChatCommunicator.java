package chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;
import chatInterface.ChatPublisher;
import chatInterface.ChatSubscriber;
import logging.Logger;

/**
 * The communicator handles the network traffic between all chat clients.
 * Messages are sent and received via the UDP protocol which may lead to 
 * messages being lost.
 * 
 * @author Mats Palm (original från Thomas Ejnefjäll)
 */
public class UDPChatCommunicator implements Runnable, ChatPublisher, ChatSubscriber {
	private final int DATAGRAM_LENGTH = 100;
	private final int PORT = 6789;
	private final String MULTICAST_ADDRESS = "228.28.28.28";
	private ChatPublisher _chatPublisher;
    private Logger logger = Logger.getInstance();
    private final String TAG = this.getClass().getName();
    private String message;
    
	// List of chatSubscribers 
	private List<ChatSubscriber> chatSubscribers = new ArrayList<ChatSubscriber>();

	/**
	 * Constructor
	 */
	public UDPChatCommunicator() {
		System.setProperty("java.net.preferIPv4Stack" , "true");
		startListen();
	}

	/**
	 * Register the UDPChatCommunicator (ChatSubscriber part) to a chatPublisher
	 * 
	 * @param publisher The ChatPublisher to contact (should be the ChatGui)
	 */
	public void setChatPublisher(ChatPublisher publisher) {
		_chatPublisher = publisher;
		_chatPublisher.registerSubscriber(this);
	}

	/**
	 * Sends the chat message as a multicast packet on the network
	 * 
	 * @param message Text message to send
	 * @throws IOException If there is an IO error
	 */
	public void sendMessage(String message) throws IOException {
		
		DatagramSocket socket = new DatagramSocket();		
		String toSend = message;
		byte[] b = toSend.getBytes();

		DatagramPacket datagram = new DatagramPacket(b, b.length, 
				InetAddress.getByName(MULTICAST_ADDRESS), PORT);

		socket.send(datagram);
		socket.close();
	}
	
	/**
	 * Starts to listen for messages from other clients
	 */
	public void startListen() {
		new Thread(this).start();	
	}
	
	/**
	 * Listens for messages from other clients
	 * 
	 * @throws IOException If there is an IO error
	 */
	private void listenForMessages() throws IOException {
		byte[] b = new byte[DATAGRAM_LENGTH];
		DatagramPacket datagram = new DatagramPacket(b, b.length);
		
		try(MulticastSocket socket = new MulticastSocket(PORT)) {
		//try(MulticastSocket socket = new MulticastSocket(99999)) { // for debug
			socket.joinGroup(InetAddress.getByName(MULTICAST_ADDRESS));
			logger.log(TAG, "Listening on " + MULTICAST_ADDRESS + ":" + PORT);

			while(true) {
				socket.receive(datagram); 
				message = new String(datagram.getData());
				message = message.substring(0, datagram.getLength());
				datagram.setLength(b.length); 
				
				notifySubscriber();


			}
		} 
	}
	
	// ----------- runnable interface implementation ----------- 

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		try {
			this.listenForMessages();
		} catch (Exception e) {
			logger.log(TAG, "ERROR: " + e.toString());
		} 
	}
	
	// ----------- ChatPublisher interface implementation ----------- 

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerSubscriber(ChatSubscriber subscriber) {
		chatSubscribers.add(subscriber);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeSubscriber(ChatSubscriber subscriber) {
		int i = chatSubscribers.indexOf(subscriber);
		if (i >= 0) {
			chatSubscribers.remove(i);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void notifySubscriber() {
		
		/* If a message is received from the network
		 * then push the message to all our subscribers
		 */
		for (ChatSubscriber subscriber : chatSubscribers) {
			subscriber.doUpdate(message);
		}
	}
	

	// ----------- ChatSubscriber interface implementation ----------- 
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doUpdate(String message) {
		try {
			logger.log(TAG, message);
			sendMessage(message);
		} catch (IOException e) {
			// Log exceptions 
			logger.log(TAG, "ERROR: " + e.toString());
		}
		
	}
}
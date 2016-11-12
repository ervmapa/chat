package chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import chatInterface.ChatPublisher;
import chatInterface.ChatSubscriber;
import logging.Logger;

/**
 * The main GUI for a simple chat application. 
 * 
 * @author Mats Palm (original Thomas Ejnefjäll)
 */
public class ChatGUI extends JFrame implements ChatSubscriber, ChatPublisher {

	private static final long serialVersionUID = -6901406569465760897L;
	private JTextArea _chatArea, _messageArea;
	private JButton _sendButton;
	private String _user;
    private Logger logger = Logger.getInstance();
    private final String TAG = this.getClass().getName();
    
	// List of chatSubscribers 
	private List<ChatSubscriber> _chatSubscribers = new ArrayList<ChatSubscriber>();

	/**
	 * Creates a ChatGUI
	 * 
	 * @param userName the name to use in the chat
	 */
	public ChatGUI(String userName) {
		this.setTitle("Simple Chat - " + userName);
		_user = userName;
		this.initializeGUI();
		this.addGUIListeners();		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * Register the ChatGUI (ChatSubscriber part) to a chatPublisher
	 * 
	 * @param publisher The ChatPublisher to contact (should be the UDPChatCommunicator)
	 */	
	public void setChatPublisher(ChatPublisher publisher) {
		publisher.registerSubscriber(this);
	}
	
	/**
	 * Initializes the GUI
	 */
	private void initializeGUI() {
		_chatArea = new JTextArea(25, 1);
		_messageArea = new JTextArea(3, 10);
		_sendButton = new JButton("Send");
		_messageArea.setLineWrap(true);
		_messageArea.setBorder(BorderFactory.createLineBorder(Color.black));
		_chatArea.setEnabled(false);
		_chatArea.setLineWrap(true);

		Container contentPane = this.getContentPane();

		contentPane.add(_chatArea, BorderLayout.NORTH);
		contentPane.add(_messageArea, BorderLayout.WEST);
		contentPane.add(_sendButton, BorderLayout.CENTER);

		this.setSize(200, 500);
		this.setResizable(false);
	}
	
	/**
	 * Adds GUI related listeners
	 */
	private void addGUIListeners() {
		_sendButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}});
		_messageArea.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {				
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (_messageArea.getText().length() > 0) {
						_messageArea.setText(_messageArea.getText().substring(0, _messageArea.getText().length() - 1));
						sendMessage();
					}
				}
			}});		
	}
	
	/**
	 * Send the message in the messageArea to the registered ChatSubscribers
	 * After that clear the messageArea
	 */
	private void sendMessage() {
			
		notifySubscriber();
		_messageArea.setText("");
		_messageArea.grabFocus();			

	}

	/**
	 * Informs the user that an error has occurred and exits the application
	 */
	public void error() {		
		JOptionPane.showMessageDialog(this, "An error has occured and the application will close", "Error", JOptionPane.WARNING_MESSAGE);

		this.setVisible(false);
		this.dispose();
		System.exit(ERROR);		
	}
	
	// ----------- ChatPublisher interface implementation ----------- 

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public void registerSubscriber(ChatSubscriber chatSubscriber) {
		_chatSubscribers.add(chatSubscriber);
		
	}
	 
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeSubscriber(ChatSubscriber chatSubscriber) {
		int i = _chatSubscribers.indexOf(chatSubscriber);
		if (i >= 0) {
			_chatSubscribers.remove(i);
		}		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void notifySubscriber() {
		for (ChatSubscriber subscriber : _chatSubscribers) {
			subscriber.doUpdate(_user + ": " +_messageArea.getText());
		}
	}
	
	// ----------- ChatSubscriber interface implementation ----------- 
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doUpdate(String message) {
		_chatArea.append(message + "\n");
		logger.log(TAG, message);
	}

}
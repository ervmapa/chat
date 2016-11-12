package chatInterface;

/**
 * A ChatPublisher (subject) interface is implemented by a class that allows
 * chatSubscribers (observers) to register.
 * 
 * @author Mats Palm
 */
public interface ChatPublisher {

	/**
	 * Called when a subscriber wants to register itself in order to receive
	 * chat messages.
	 * 
	 * @param subscriber
	 *            A class implementing the ChatSubscriber (observer) interface
	 */
	public void registerSubscriber(ChatSubscriber subscriber);

	/**
	 * Called when a subscriber wants to remove itself from receiving messages
	 * from the Publisher
	 * 
	 * @param subscriber
	 *            A class implementing the ChatSubscriber (observer) interface
	 */
	public void removeSubscriber(ChatSubscriber subscriber);

	/**
	 * Called when a the publisher wants to notifications to all observers
	 */
	public void notifySubscriber();

		
}
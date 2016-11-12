package chatInterface;

/**
 * A ChatSubscriber (observer) interface is implemented by a class that wants to
 * get messages from a ChatPublisher (Subject).
 * 
 * @author Mats Palm
 */
public interface ChatSubscriber {
	
	/**
	 * Called by the ChatPublisher wants to push a update the ChatSubscriber 
	 * with a new message
	 * 
	 * @param message
	 *            The message from the ChatPublisher            
	 */	
	public void doUpdate(String message);
	
}

package logging;

/**
 * Interface to handle log messages
 * 
 * @author Mats Palm
 */
public interface LoggerFormat {
	
	/**
	 * Logs a message
	 * 
	 * @param tag The TAG
	 * @param logMessage The message to log
	 */
	public void log(String tag, String logMessage);

}

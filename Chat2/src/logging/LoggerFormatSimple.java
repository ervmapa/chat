package logging;

/**
 * Logs a message to system out.
 * 
 * @author Mats Palm
 */
public class LoggerFormatSimple implements LoggerFormat {

	/**
	 *  {@inheritDoc}
	 */
	@Override
	public void log(String tag, String logMessage) {
		System.out.println(tag + " " + logMessage);
	}

}

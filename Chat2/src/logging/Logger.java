package logging;

/**
 * A singleton class that can be used by other classes when they want to print log messages
 *  
 * @author Mats Palm
 */
public class Logger {
	
	// Create a Logger object when class is loaded
	private static Logger _logger = new Logger();
	
	// The default LoggerFormat to use
	private static LoggerFormat _loggerFormat = new LoggerFormatTimestamp();
	
	/**
	 * Constructor. It is private, so other classes can not 
	 * create LoggerObjects using "new Logger()" 
	 */
	private Logger() {
	}

	/**
	 * This method is used by other classes to get a reference to the 
	 * one and only Logger
	 * 
	 * @return _logger The LoggerObject
	 */
	public static Logger getInstance() {
		return _logger;
	}
	
	/**
	 * Method to set LoggerFormat
	 * 
	 * @param LoggerFormat
	 *            A class that implements the LoggerFormat interface
	 */
	public static void setLoggingFormat(LoggerFormat loggerFormat) {
		_loggerFormat = loggerFormat;
	}
	
	/**
	 * Send a message to the LoggerFormat
	 * 
	 * @param tag
	 *            A prefix to the logMessage
	 * @param logMessage
	 *            The message to be logged
	 */
	public void log(String tag, String logMessage) {
		_loggerFormat.log(tag, logMessage);
	}

	
}
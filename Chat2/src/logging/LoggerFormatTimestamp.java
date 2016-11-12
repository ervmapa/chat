package logging;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Logs a message to system out with time stamp.
 * 
 * @author Mats Palm
 */
public class LoggerFormatTimestamp implements LoggerFormat {

	/**
	 *  {@inheritDoc}
	 */
	@Override
	public void log(String tag, String logMessage) {
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		System.out.println(String.format("%-19s %-25s %s" ,date, tag, logMessage ));
	}

}

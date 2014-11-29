package tsBot;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

	public static String createDateString(Timestamp timestamp, String pattern) {
		Date date = (Date) timestamp;
		DateFormat df = new SimpleDateFormat(pattern);
		return df.format(date);

	}

	public static String createDateString(Timestamp timestamp) {
		return createDateString(timestamp, "dd.MM.yyyy");
	}
}

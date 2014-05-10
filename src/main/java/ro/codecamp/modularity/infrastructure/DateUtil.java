package ro.codecamp.modularity.infrastructure;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	private static final SimpleDateFormat JS_DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

	private static final SimpleDateFormat UK_DATE_FORMAT = new SimpleDateFormat(
			"dd/MM/yyyy");

	private DateUtil() {
	}

	public static String formatJSDate(Date date) {
		synchronized (JS_DATE_FORMAT) {
			return JS_DATE_FORMAT.format(date);
		}
	}

	public static Date parseJSDate(String date) {
		try {
			synchronized (JS_DATE_FORMAT) {
				return JS_DATE_FORMAT.parse(date);
			}
		} catch (ParseException ex) {
			throw new RuntimeException(ex);
		}
	}

	public static String formatUKDate(Date date) {
		synchronized (UK_DATE_FORMAT) {
			return UK_DATE_FORMAT.format(date);
		}
	}

}

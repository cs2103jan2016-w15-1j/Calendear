// @@author A0126513N
package calendear.parser;

import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.ocpsoft.prettytime.nlp.PrettyTimeParser;


public class DateParser {
	
	private static final String ERROR_DATE_FORMAT = "\"%1$s\" is not a valid date and time description";
	
	public static GregorianCalendar parse(String timeStr) 
	throws ParseException {
		PrettyTimeParser parser = new PrettyTimeParser();
		List<Date> dates =  parser.parse(timeStr);
		if (dates.size() == 0){
			throw new ParseException(String.format(ERROR_DATE_FORMAT, timeStr), 0);
		}
		GregorianCalendar res = new GregorianCalendar();
		res.setTime(dates.get(0));
		return res;
	}
}

package calendear.parser;

import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.ocpsoft.prettytime.nlp.PrettyTimeParser;


public class DateParser {
	
	public static GregorianCalendar parse(String timeStr) 
	throws ParseException {
		PrettyTimeParser parser = new PrettyTimeParser();
		List<Date> dates =  parser.parse(timeStr);
		if (dates.size() == 0){
			throw new ParseException("\"" + timeStr +"\"" + "is not a valid date and time description", 0);
		}
		GregorianCalendar res = new GregorianCalendar();
		res.setTime(dates.get(0));
		return res;
	}
}

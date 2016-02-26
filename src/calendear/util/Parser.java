package calendear.util;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.ParseException;

public class Parser {
	
	private static final String CMD_STR_ADD = ".add";
	private static final String CMD_STR_DISPLAY = ".display";
	private static final String CMD_STR_UPDATE = ".update";
	private static final String CMD_STR_DELETE = ".delete";
	private static final String CMD_STR_SEARCH = ".search";
	private static final String CMD_STR_SORT = ".sort";
	private static final String CMD_STR_MARK = ".mark";
	private static final String CMD_STR_DONE = ".done";
	private static final String CMD_STR_UNDO = ".undo";
	private static final String CMD_STR_TAG = ".tag";
	private static final String CMD_STR_LINK_GOOGLE = ".linkGoogle";
	private static final String CMD_STR_EXIT = ".exit";
	
//	private static final String PATTERN_DATE_FORMAT = 
//								"\\b(\\d){1,2}([:\\-/]\\d\\d[:\\-/]||(\\w){3})\\d\\d";
	private static final String PATTERN_ADD_CMD = 
								"(\\.add +)(.+)(by +)(\\b(\\d){1,2}([:\\-/]\\d\\d[:\\-/]||(\\w){3})\\d\\d)";
	
	public static Command parse(String rawInput){	
		String input = rawInput.trim();
		String[] words = input.split(" ");
		return parseCommand(words, rawInput);
	}
	
	public static Command parseCommand(String[] words, String rawInput){
		
		switch (words[0]){
			case CMD_STR_ADD:
				return parseAddCmd(words, rawInput);
			case CMD_STR_DISPLAY:
				return parseDisplayCmd(words, rawInput);
			case CMD_STR_UPDATE:
				return parseUpdateCmd(words, rawInput);
			case CMD_STR_DELETE:
				return parseDeleteCmd(words, rawInput);
			case CMD_STR_SEARCH:
				return parseSearchCmd(words, rawInput);
			case CMD_STR_SORT:
				return parseSortCmd(words, rawInput);
			case CMD_STR_MARK:
				return parseMarkCmd(words, rawInput);
			case CMD_STR_DONE:
				return parseDoneCmd(words, rawInput);
			case CMD_STR_UNDO:
				return parseUndoCmd(words, rawInput);
			case CMD_STR_TAG:
				return parseTagCmd(words, rawInput);
			case CMD_STR_LINK_GOOGLE:
				return parseLinkGoogleCmd(words, rawInput);
			case CMD_STR_EXIT:
				return parseExitCmd(words, rawInput);
			default:
				return parseInvalidCmd(words, rawInput);
		}
	}
	
	private static Command parseAddCmd(String[] words, String rawInput){
		
		
		Pattern pattern = Pattern.compile(PATTERN_ADD_CMD);
		Matcher matcher = pattern.matcher(rawInput);
		matcher.find();
		GregorianCalendar time;
		try {
			time = DateParser.parse(matcher.group(4));
		} catch (ParseException e) {
			return new CommandInvalid(rawInput);
		}
		Task task = new Task (matcher.group(2), time);
		return new CommandAdd(task);

	}
	
	private static Command parseDisplayCmd(String[] words, String rawInput){
		if (words[1].equals("!")){
			return new CommandDisplay(true);
		} else {
			return new CommandDisplay(false);
		}
	}
	
	private static Command parseUpdateCmd(String[] words, String rawInput){
		return null;
	}
	
	private static Command parseDeleteCmd(String[] words, String rawInput){
		int index = Integer.parseInt(words[1]);
		return new CommandDelete(index);
	}
	
	private static Command parseSearchCmd(String[] words, String rawInput){
		return null;
	}
	
	private static Command parseSortCmd(String[] words, String rawInput){
		return null;
	}
	
	private static Command parseMarkCmd(String[] words, String rawInput){
		int index = Integer.parseInt(words[1]);
		return new CommandMark(index);
	}
	
	private static Command parseDoneCmd(String[] words, String rawInput){
		int index = Integer.parseInt(words[1]);
		return new CommandDone(index);
	}
	
	private static Command parseUndoCmd(String[] words, String rawInput){
		return new CommandUndo();
	}
	
	private static Command parseTagCmd(String[] words, String rawInput){
		int index = Integer.parseInt(words[1]);
		String tagName = words[2];
		return new CommandTag(index, tagName);
	}
	
	private static Command parseLinkGoogleCmd(String[] words, String rawInput){
		return new CommandLinkGoogle();
	}
	
	private static Command parseExitCmd(String[] words, String rawInput){
		return new CommandExit();
	}
	
	private static Command parseInvalidCmd(String[] words, String rawInput){
		return new CommandInvalid(rawInput);
	}
	
}

class DateParser {
	
	public static final SimpleDateFormat dateFormatterType1 = new SimpleDateFormat("dd-MM-yyyy");
	
	public static GregorianCalendar parse(String timeStr)
	throws ParseException {
		timeStr.trim();
		GregorianCalendar result = new GregorianCalendar();
		result.setTime(dateFormatterType1.parse(timeStr));
		return result;
	}
}

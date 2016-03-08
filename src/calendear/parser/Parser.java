package calendear.parser;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import calendear.util.*;

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
	
	private static final String IMPORTANT = "!";
	
//	private static final String PATTERN_DATE_FORMAT = 
//								"\\b(\\d){1,2}([:\\-/]\\d\\d[:\\-/]||(\\w){3})\\d\\d";
	private static final String PATTERN_ADD_DEADLINE = "(\\.add +)(.+)(\\.by +)(.+)";
	private static final String PATTERN_ADD_EVENT = "(\\.add +)(.+)(\\.from +)(.+)(\\.to)(.+)";
	private static final String PATTERN_ADD_FLOATING = "(\\.add +)(.+)";
	private static final String PATTERN_UPDATE_NAME_BY_INDEX = "(\\.update +)(\\d+) +(.+)";
	private static final String PATTERN_UPDATE_MULTIPLE_FIELD_BY_INDEX = 
	"(\\.update) +(\\d+) +"
	+ "(?:(?:(\\.name) *([^\\.]+)) *|(?:(\\.by) *([^\\.]+)) *|(?:(\\.from) *([^\\.]+)) *"
	+ "|(?:(\\.to) *([^\\.]+)) *|(?:(\\.float) *()) *|(?:(\\.at) *([^\\.]+)) *"
	+ "|(?:(\\.note) *([^\\.]+)) *|(?:(\\.tag) *([^\\.]+)) *|(?:(\\.mark) *()) *|(?:(\\.done) *()) *)+";
	private static final int NUM_OF_UPDATE_KEYWORD = 10;
	private static final int NUM_OF_TASK_ATTRIBUTES = 9;
	
	public static Command parse(String rawInput){	
		String input = rawInput.trim();
		String[] words = input.split(" ");
		return parseCommand(words, rawInput);
	}
	
	private static Command parseCommand(String[] words, String rawInput){
		
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
		
		Pattern pattern = Pattern.compile(PATTERN_ADD_DEADLINE);
		Matcher matcher = pattern.matcher(rawInput);
		if (matcher.find()) {
			return parseAddDeadline(rawInput, matcher);
		}
		
		pattern = Pattern.compile(PATTERN_ADD_EVENT);
		matcher = pattern.matcher(rawInput);
		if (matcher.find()) {
			return parseAddEvent(rawInput, matcher);
		}
		
		pattern = Pattern.compile(PATTERN_ADD_FLOATING);
		matcher = pattern.matcher(rawInput);
		if (matcher.find()) {
			return parseAddFloating(rawInput, matcher);
		}
		
		return new CommandInvalid(rawInput);
		
	}
	
	// .add <name> .by dd/MM/yy HH:mm
	private static Command parseAddDeadline(String rawInput, Matcher matcher) {
		GregorianCalendar time;
		try {
			time = DateParser.parse(matcher.group(4));
		} catch (ParseException e) {
			return new CommandInvalid(rawInput);
		}
		Task task = new Task (matcher.group(2), time);
		return new CommandAdd(task);
	}
	
	// .add <name> .from dd/MM/yy HH:mm .to dd/MM/yy HH:mm
	private static Command parseAddEvent(String rawInput, Matcher matcher) {
		GregorianCalendar startTime, endTime;
		try {
			startTime = DateParser.parse(matcher.group(4));
			endTime = DateParser.parse(matcher.group(6));
		} catch (ParseException e) {
			return new CommandInvalid(rawInput);
		}
		Task task = new Task (matcher.group(2), startTime, endTime);
		return new CommandAdd(task);
	}
	
	// .add <name>
	private static Command parseAddFloating(String rawInput, Matcher matcher) {
		Task task = new Task (matcher.group(2));
		return new CommandAdd(task);
	}
	
	private static Command parseDisplayCmd(String[] words, String rawInput){
		if (words.length>1 && words[1].equals(IMPORTANT)){
			return new CommandDisplay(true);
		} else {
			return new CommandDisplay(false);
		}
	}
	
	// .update <index> <new name>
	private static Command parseUpdateCmd(String[] words, String rawInput){
		Pattern pattern = Pattern.compile(PATTERN_UPDATE_NAME_BY_INDEX);
		Matcher matcher = pattern.matcher(rawInput);
		if (matcher.find()){
			try {	
				int index = Integer.parseInt(matcher.group(2));
				String newName = matcher.group(3);
				return new CommandUpdate(index, newName);
			}
			catch (NumberFormatException e){
				return new CommandInvalid(rawInput);
			}
		}
		
		return new CommandInvalid(rawInput);
	}
	
	/** 
	 *  parseUpdateCmd2 by Jiyun
	 * 
	 **/
	//.update <index> <header1:content\\r\\n header2:content\\r\\n ...>
	private static Command parseUpdateCmd2(String[] words, String rawInput){
			Pattern pattern = Pattern.compile(PATTERN_UPDATE_NAME_BY_INDEX);
			Matcher matcher = pattern.matcher(rawInput);
			if (matcher.find()){
				try {	
					int index = Integer.parseInt(matcher.group(2));
					String newInfo = matcher.group(3);
					int[] updateChecklist = new int[NUM_OF_TASK_ATTRIBUTES];
					String[] newInfoContentOnly = new String[NUM_OF_TASK_ATTRIBUTES];
					String[] newInfoArr = newInfo.split("\\r?\\n");
					for(int i=0;i<newInfoArr.length;i++){
						String[] line = newInfoArr[i].split(":");
						if(line[0].equalsIgnoreCase("name")){
							updateChecklist[0]=1;
							newInfoContentOnly[0]= line[1];
						}
						else if(line[0].equalsIgnoreCase("type")){
							updateChecklist[1]=1;
							newInfoContentOnly[1]= line[1];
						}
						else if(line[0].equalsIgnoreCase("starttime")){
							updateChecklist[2]=1;
							newInfoContentOnly[2]= line[1];
						}
						else if(line[0].equalsIgnoreCase("endtime")){
							updateChecklist[3]=1;
							newInfoContentOnly[3]= line[1];
						}
						else if(line[0].equalsIgnoreCase("location")){
							updateChecklist[4]=1;
							newInfoContentOnly[4]= line[1];
						}
						else if(line[0].equalsIgnoreCase("note")){
							updateChecklist[5]=1;
							newInfoContentOnly[5]= line[1];
						}
						else if(line[0].equalsIgnoreCase("tag")){
							updateChecklist[6]=1;
							newInfoContentOnly[6]= line[1];
						}
						else if(line[0].equalsIgnoreCase("important")){
							updateChecklist[7]=1;
							newInfoContentOnly[7]= line[1];
						}
						else if(line[0].equalsIgnoreCase("finished")){
							updateChecklist[8]=1;
							newInfoContentOnly[8]= line[1];
						}
					
						
					}
					return new CommandUpdate(index, updateChecklist, newInfoContentOnly);
				}
				catch (NumberFormatException e){
					return new CommandInvalid(rawInput);
				}
			}
			
			return new CommandInvalid(rawInput);
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
	
	public static final SimpleDateFormat dateFormatterType1 = new SimpleDateFormat("dd/MM/yy HH:mm");
	
	public static GregorianCalendar parse(String timeStr)
	throws ParseException {
		timeStr.trim();
		GregorianCalendar result = new GregorianCalendar();
		result.setTime(dateFormatterType1.parse(timeStr));
		return result;
	}
}

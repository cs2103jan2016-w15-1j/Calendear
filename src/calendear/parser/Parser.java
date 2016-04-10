// @@author Dinh Viet Thang
package calendear.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import calendear.util.*;

import java.text.ParseException;

public class Parser {
	
	private static final String ERROR_WRONG_CMD_TYPE = "Wrong command type";
	private static final String ERROR_WRONG_CMD_FORMAT = "invalid command format";
	private static final int FORTH_GROUP = 4;
	private static final int THIRD_GROUP = 3;
	private static final String ERROR_WRONG_NUMBER_FORMAT = "Please enter the task id as a number";
	private static final int TWO = 2;
	private static final int THIRD_WORD_INDEX = 2;
	private static final int ONE = 1;
	private static final String ERROR_DELETE_INDEX_NOT_SPECIFIED = "Please provide task index to delete";
	private static final String ERROR_MARK_INDEX_NOT_SPECIFIED = "Please provide task index to mark";
	private static final int SECOND_GROUP = 2;
	private static final int SECOND_WORD_INDEX = 1;
	private static final int FIRST_WORD_INDEX = 0;
	private static final String PATTERN_SPACES = " +";
	
	private static final String CMD_STR_ADD = "add";
	private static final String CMD_STR_DISPLAY = "display";
	private static final String CMD_STR_UPDATE = "update";
	private static final String CMD_STR_DELETE = "delete";
	private static final String CMD_STR_SEARCH = "search";
	private static final String CMD_STR_SORT = "sort";
	private static final String CMD_STR_MARK = "important";
	private static final String CMD_STR_DONE = "done";
	private static final String CMD_STR_UNDO = "undo";
	private static final String CMD_STR_TAG = "tag";
	private static final String CMD_STR_LINK_GOOGLE = "linkGoogle";
	private static final String CMD_STR_EXIT = "exit";
	private static final String CMD_STR_REDO = "redo";
	private static final String CMD_STR_CLEAR = "clear";
	private static final String CMD_STR_SAVE = "save";
	private static final String CMD_STR_HELP = "help";
	private static final String LOAD_FROM_GOOGLE = "syncGoogle";
	private static final String EMPTY = "";
	//when using regex and regex-related methods like String.split() and String.replaceAll()
	//the "." is treated as metacharacter so you have to include the escape character "\\"
	private static final String INPUT_ESCAPE_CHARACTER = "\\.";
	private static final String PATTERN_ESCAPE_CHARACTER = "_";
	
	private static final String IMPORTANT = "important";
	private static final String NAME = "name";
	private static final String BY = "by";
	private static final String FROM = "from";
	private static final String TO = "to";
	private static final String FLOAT = "float";
	private static final String AT = "at";
	private static final String NOTE = "note";
	private static final String TAG = "tag";
	private static final String MARK = "important";
	private static final String DONE = "done";
	
	private static final String NEGATIVE_LOOKAHEAD_KEYWORDS = 
	"((?:.(?!\\b" + NAME + "\\b|\\b" + BY + "\\b|\\b" + FROM + "\\b|\\b" + TO 
	+ "\\b|\\b" + FLOAT + "\\b|\\b" + AT + "\\b|\\b" + NOTE + "\\b|\\b"+ TAG 
	+"\\b|\\b"+ IMPORTANT +"\\b|\\b"+ DONE +"\\b))+)";
	private static final String PATTERN_ADD = 
	"(\\b" + CMD_STR_ADD + "\\b) +" + NEGATIVE_LOOKAHEAD_KEYWORDS + PATTERN_SPACES
	+ "(?:(?:(\\b" + NAME + "\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"		//represent the groups name and <newName>
	+ "|(?:(\\b" + BY + "\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"			//represent the groups by and <dateAndTime>
	+ "|(?:(\\b" + FROM + "\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"			//represent the groups from and <dateAndTime>
	+ "|(?:(\\b" + TO + "\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"			//represent the groups to and <dateAndTime>
	+ "|(?:(\\b" + FLOAT + "\\b) *()) *"										//represent the group float
	+ "|(?:(\\b"+ AT + "\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"				//represent the groups at and <newLocation>
	+ "|(?:(\\b"+ NOTE + "\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"			//represent the groups note and <newNote>
	+ "|(?:(\\b" + TAG + "\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"			//represent the groups tag and <newTag>
	+ "|(?:(\\b" + IMPORTANT + "\\b) *()) *"									//represent the group important
	+ "|(?:(\\b"+ DONE + "\\b) *()) *)+";										//represent the group done
	private static final String PATTERN_ADD_FLOAT = "(\\b" + CMD_STR_ADD +"\\b) +(.+)";
	private static final String PATTERN_UPDATE_BY_INDEX = 
	"(\\bupdate\\b) +(\\d+) +"													//represent the groups update and <taskID>
	+ "(?:(?:(\\b" + NAME + "\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"		//represent the groups name and <newName>
	+ "|(?:(\\b" + BY + "\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"			//represent the groups by and <dateAndTime>
	+ "|(?:(\\b" + FROM + "\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"			//represent the groups from and <dateAndTime>
	+ "|(?:(\\b" + TO + "\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"			//represent the groups to and <dateAndTime>
	+ "|(?:(\\b" + FLOAT + "\\b) *()) *"										//represent the group float
	+ "|(?:(\\b"+ AT + "\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"				//represent the groups at and <newLocation>
	+ "|(?:(\\b"+ NOTE + "\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"			//represent the groups note and <newNote>
	+ "|(?:(\\b" + TAG + "\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"			//represent the groups tag and <newTag>
	+ "|(?:(\\b" + IMPORTANT + "\\b) *()) *"									//represent the group important
	+ "|(?:(\\b"+ DONE + "\\b) *()) *)+";										//represent the group done
	private static final String PATTERN_SEARCH = 
	"(\\bsearch\\b) +()"				//represent the groups update and <taskID>
	+ "(?:(?:(\\b" + NAME + "\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"		//represent the groups name and <newName>
	+ "|(?:(\\b" + BY + "\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"			//represent the groups by and <dateAndTime>
	+ "|(?:(\\b" + FROM + "\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"			//represent the groups from and <dateAndTime>
	+ "|(?:(\\b" + TO + "\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"			//represent the groups to and <dateAndTime>
	+ "|(?:(\\b" + FLOAT + "\\b) *()) *"										//represent the group float
	+ "|(?:(\\b"+ AT + "\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"				//represent the groups at and <newLocation>
	+ "|(?:(\\b"+ NOTE + "\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"			//represent the groups note and <newNote>
	+ "|(?:(\\b" + TAG + "\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"			//represent the groups tag and <newTag>
	+ "|(?:(\\b" + IMPORTANT + "\\b) *()) *"									//represent the group important
	+ "|(?:(\\b"+ DONE + "\\b) *()) *)+";										//represent the group done
	private static final String PATTERN_SAVE = "(\\b" + CMD_STR_SAVE + "\\b) +(.+)";
	
	private static final int NUM_OF_UPDATE_KEYWORD = 10;
	private static final int NUM_OF_TASK_ATTRIBUTES = 9;
	
	
	public static Command parse(String rawInput){	
		rawInput = rawInput.trim();
		rawInput = changeEscapeCharacter(rawInput);
		String[] words = rawInput.split(PATTERN_SPACES);
		try {
			return parseCommand(words, rawInput);
		}
		catch (NumberFormatException e) {
			return new CommandInvalid(ERROR_WRONG_NUMBER_FORMAT);
		}
	}
	
	private static Command parseCommand(String[] words, String rawInput) throws NumberFormatException {
		
		switch (words[FIRST_WORD_INDEX]){
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
			case CMD_STR_REDO:
				return parseRedoCmd(words, rawInput);
			case CMD_STR_CLEAR:
				return parseClearCmd(words, rawInput);
			case CMD_STR_TAG:
				return parseTagCmd(words, rawInput);
			case CMD_STR_LINK_GOOGLE:
				return parseLinkGoogleCmd(words, rawInput);
			case CMD_STR_SAVE:
				return parseSave(words, rawInput);
			case LOAD_FROM_GOOGLE:
				return parseLoadToGoogleCmd(words, rawInput);
			case CMD_STR_HELP:
				return parseHelpCmd(words, rawInput);
			case CMD_STR_EXIT:
				return parseExitCmd(words, rawInput);
			default:
				return parseInvalidCmd(words, rawInput);
		}
	}
	
	private static Command parseSave(String[] words, String rawInput) {
		Pattern pattern = Pattern.compile(PATTERN_SAVE);
		Matcher matcher = pattern.matcher(rawInput);
		if (matcher.find()){
			String filePath = matcher.group(SECOND_GROUP);
			filePath = changeBackEscapeCharacter(filePath);
			return new CommandSave(filePath);
		}
		return new CommandInvalid(rawInput);
	}

	private static Command parseAddCmd(String[] words, String rawInput){
		Pattern pattern = Pattern.compile(PATTERN_ADD);
		Matcher matcher = pattern.matcher(rawInput);
		if (matcher.find()){
			try {	
				String name = removeEscapeCharacter(matcher.group(SECOND_GROUP));
				boolean[] checkList = new boolean[NUM_OF_TASK_ATTRIBUTES];
				Object[] newInfo = new Object[NUM_OF_TASK_ATTRIBUTES];
				makeCheckList(matcher, checkList, newInfo);
				return new CommandAdd(name, checkList, newInfo);
			}
			catch (ParseException e){
				return new CommandInvalid(rawInput);
			}
		}
		
		pattern = Pattern.compile(PATTERN_ADD_FLOAT);
		matcher = pattern.matcher(rawInput);
		if (matcher.find()){
			String name = removeEscapeCharacter(matcher.group(SECOND_GROUP));
			boolean[] checkList = new boolean[NUM_OF_TASK_ATTRIBUTES];
			Object[] newInfo = new Object[NUM_OF_TASK_ATTRIBUTES];
			checkList[CommandUpdate.CODE_UPDATE_NAME] = true;
			newInfo[CommandUpdate.CODE_UPDATE_NAME] = name;
			checkList[CommandUpdate.CODE_UPDATE_TYPE] = true;
			newInfo[CommandUpdate.CODE_UPDATE_TYPE] = TASK_TYPE.FLOATING;
			return new CommandAdd(name, checkList, newInfo);
		}
		return new CommandInvalid(rawInput);
	}
	
	private static Command parseDisplayCmd(String[] words, String rawInput){
		if (words.length > ONE && words[SECOND_WORD_INDEX].equals(IMPORTANT)){
			return new CommandDisplay(true);
		} else {
			return new CommandDisplay(false);
		}
	}
	
	// .update <index> <new name>
	private static Command parseUpdateCmd(String[] words, String rawInput){
		Pattern pattern = Pattern.compile(PATTERN_UPDATE_BY_INDEX);
		Matcher matcher = pattern.matcher(rawInput);
		if (matcher.find()){
			try {	
				int index = Integer.parseInt(matcher.group(SECOND_GROUP));
				boolean[] checkList = new boolean[NUM_OF_TASK_ATTRIBUTES];
				Object[] newInfo = new Object[NUM_OF_TASK_ATTRIBUTES];
				makeCheckList(matcher, checkList, newInfo);
				return new CommandUpdate(index, checkList, newInfo);
			}
			catch (NumberFormatException e){
				return new CommandInvalid(ERROR_WRONG_NUMBER_FORMAT);
			}
			catch (ParseException e){
				return new CommandInvalid(rawInput);
			}
		}
		
		return new CommandInvalid(ERROR_WRONG_CMD_FORMAT);
	}
	
	private static void makeCheckList(Matcher matcher, boolean[] checkList, Object[] newInfo) 
	throws ParseException {
		for (int i=0; i<NUM_OF_UPDATE_KEYWORD; i++){
			if (matcher.group(THIRD_GROUP + TWO*i) != null){
				/* for both int[]updateChesklist and String[]newInfo 
				[0:name][1:type][2:starttime]
				[3:endtime][4:location][5:note]
				[6:tag][7:important][8:finished]
				*/
				String argument;
				switch (matcher.group(THIRD_GROUP + TWO*i)){
					case NAME:
						checkList[CommandUpdate.CODE_UPDATE_NAME] = true;
						argument = removeEscapeCharacter(matcher.group(FORTH_GROUP + TWO*i));
						newInfo[CommandUpdate.CODE_UPDATE_NAME] = argument;
						break;
					case BY:
						checkList[CommandUpdate.CODE_UPDATE_TYPE] = true;
						newInfo[CommandUpdate.CODE_UPDATE_TYPE] = TASK_TYPE.DEADLINE;
						checkList[CommandUpdate.CODE_UPDATE_END_TIME] = true;
						argument = removeEscapeCharacter(matcher.group(FORTH_GROUP + TWO*i));
						newInfo[CommandUpdate.CODE_UPDATE_END_TIME] = DateParser.parse(argument);
						break;
					case FROM:
						checkList[CommandUpdate.CODE_UPDATE_TYPE] = true;
						newInfo[CommandUpdate.CODE_UPDATE_TYPE] = TASK_TYPE.EVENT;
						checkList[CommandUpdate.CODE_UPDATE_START_TIME] = true;
						argument = removeEscapeCharacter(matcher.group(FORTH_GROUP + TWO*i));
						newInfo[CommandUpdate.CODE_UPDATE_START_TIME] = DateParser.parse(argument);
						break;
					case TO:
						checkList[CommandUpdate.CODE_UPDATE_TYPE] = true;
						newInfo[CommandUpdate.CODE_UPDATE_TYPE] = TASK_TYPE.EVENT;
						checkList[CommandUpdate.CODE_UPDATE_END_TIME] = true;
						argument = removeEscapeCharacter(matcher.group(FORTH_GROUP + TWO*i));
						newInfo[CommandUpdate.CODE_UPDATE_END_TIME] = DateParser.parse(argument);
						break;
					case FLOAT:
						checkList[CommandUpdate.CODE_UPDATE_TYPE] = true;
						newInfo[CommandUpdate.CODE_UPDATE_TYPE] = TASK_TYPE.FLOATING;
						break;
					case AT:
						checkList[CommandUpdate.CODE_UPDATE_LOCATION] = true;
						argument = removeEscapeCharacter(matcher.group(FORTH_GROUP + TWO*i));
						newInfo[CommandUpdate.CODE_UPDATE_LOCATION] = argument;
						break;
					case NOTE:
						checkList[CommandUpdate.CODE_UPDATE_NOTE] = true;
						argument = removeEscapeCharacter(matcher.group(FORTH_GROUP + TWO*i));
						newInfo[CommandUpdate.CODE_UPDATE_NOTE] = argument;
						break;
					case TAG:
						checkList[CommandUpdate.CODE_UPDATE_TAG] = true;
						argument = removeEscapeCharacter(matcher.group(FORTH_GROUP + TWO*i));
						newInfo[CommandUpdate.CODE_UPDATE_TAG] = argument;
						break;
					case MARK:
						checkList[CommandUpdate.CODE_UPDATE_IMPORTANT] = true;
						newInfo[CommandUpdate.CODE_UPDATE_IMPORTANT] = true;
						break;
					case DONE:
						checkList[CommandUpdate.CODE_UPDATE_FINISHED] = true;
						newInfo[CommandUpdate.CODE_UPDATE_FINISHED] = true;
						break;
				}
			}
		}
	}
	
	private static Command parseDeleteCmd(String[] words, String rawInput){
		if (words.length > ONE) {
			int index = Integer.parseInt(words[SECOND_WORD_INDEX]);
			return new CommandDelete(index);
		} else {
			return new CommandInvalid(ERROR_DELETE_INDEX_NOT_SPECIFIED);
		}
		
	}
	
	private static Command parseSearchCmd(String[] words, String rawInput){
		Pattern pattern = Pattern.compile(PATTERN_SEARCH);
		Matcher matcher = pattern.matcher(rawInput);
		if (matcher.find()){
			try {	
				boolean[] checkList = new boolean[NUM_OF_TASK_ATTRIBUTES];
				Object[] newInfo = new Object[NUM_OF_TASK_ATTRIBUTES];
				makeCheckList(matcher, checkList, newInfo);
				return new CommandSearch(checkList, newInfo);
			}
			catch (ParseException e){
				return new CommandInvalid(rawInput);
			}
		}
		
		return new CommandInvalid(rawInput);
	}
	
	private static Command parseSortCmd(String[] words, String rawInput){
		return null;
	}
	
	private static Command parseMarkCmd(String[] words, String rawInput) {
		if (words.length > ONE) {
			try {	
				int index = Integer.parseInt(words[SECOND_WORD_INDEX]);
				return new CommandMark(index);
			} catch (NumberFormatException e){
				return new CommandInvalid(ERROR_WRONG_NUMBER_FORMAT);
			}
		} else {
			return new CommandInvalid(ERROR_MARK_INDEX_NOT_SPECIFIED);
		}	
	}
	
	private static Command parseDoneCmd(String[] words, String rawInput){
		if (words.length > ONE) {
			try {
				int index = Integer.parseInt(words[SECOND_WORD_INDEX]);
				return new CommandDone(index);
			} catch (NumberFormatException e){
				return new CommandInvalid(ERROR_WRONG_NUMBER_FORMAT);
			}
		} else {
			return new CommandInvalid(ERROR_MARK_INDEX_NOT_SPECIFIED);
		}	
	}
	
	private static Command parseUndoCmd(String[] words, String rawInput){
		return new CommandUndo();
	}
	
	private static Command parseRedoCmd(String[] words, String rawInput){
		return new CommandRedo();
	}
	
	private static Command parseClearCmd(String[] words, String rawInput){
		return new CommandClear();
	}
	
	private static Command parseTagCmd(String[] words, String rawInput) {
		if (words.length > TWO) {
			try {
				int index = Integer.parseInt(words[SECOND_WORD_INDEX]);
				String tagName = words[THIRD_WORD_INDEX];
				return new CommandTag(index, tagName);
			} catch (NumberFormatException e){
				return new CommandInvalid(ERROR_WRONG_NUMBER_FORMAT);
			}
		} else {
			return new CommandInvalid(ERROR_MARK_INDEX_NOT_SPECIFIED);
		}	
	}
	
	private static Command parseLinkGoogleCmd(String[] words, String rawInput){
		return new CommandLinkGoogle();
	}
	
	private static Command parseLoadToGoogleCmd(String[] words, String rawInput){
		return new CommandLoadFromGoogle();
	}
	
	private static Command parseHelpCmd(String[] words, String rawInput){
		return new CommandHelp();
	}
	
	private static Command parseExitCmd(String[] words, String rawInput){
		return new CommandExit();
	}
	
	private static Command parseInvalidCmd(String[] words, String rawInput){
		return new CommandInvalid(ERROR_WRONG_CMD_TYPE);
	}
	
	private static String removeEscapeCharacter(String rawInput){
		return rawInput.replaceAll(PATTERN_ESCAPE_CHARACTER, EMPTY);
	}
	
	private static String changeEscapeCharacter(String rawInput){
		return rawInput.replaceAll(INPUT_ESCAPE_CHARACTER, PATTERN_ESCAPE_CHARACTER);
	}
	
	private static String changeBackEscapeCharacter(String rawInput){
		return rawInput.replaceAll(PATTERN_ESCAPE_CHARACTER, INPUT_ESCAPE_CHARACTER);
	}
	
}

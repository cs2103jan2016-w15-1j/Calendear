# Dinh Viet Thang
###### /src/calendear/parser/DateParser.java
``` java
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
```
###### /src/calendear/parser/Parser.java
``` java
package calendear.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import calendear.util.*;

import java.text.ParseException;

public class Parser {
	
	private static final int SECOND_WORD_INDEX = 1;
	private static final int FIRST_WORD_INDEX = 0;
	private static final String PATTERN_SPACES = " +";
	private static final String ADD = "add";
	private static final String DISPLAY = "display";
	private static final String UPDATE = "update";
	private static final String DELETE = "delete";
	private static final String SEARCH = "search";
	private static final String SORT = "sort";
	private static final String CMD_STR_MARK = "important";
	private static final String CMD_STR_DONE = "done";
	private static final String UNDO = "undo";
	private static final String CMD_STR_TAG = "tag";
	private static final String LINK_GOOGLE = "linkGoogle";
	private static final String EXIT = "exit";
	private static final String REDO = "redo";
	private static final String CLEAR = "clear";
	private static final String SAVE = "save";
	private static final String HELP = "help";
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
	"(\\b" + ADD + "\\b) +" + NEGATIVE_LOOKAHEAD_KEYWORDS + PATTERN_SPACES
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
	private static final String PATTERN_ADD_FLOAT = "(\\b" + ADD +"\\b) +(.+)";
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
	private static final String PATTERN_SAVE = "(\\b" + SAVE + "\\b) +(.+)";
	
	private static final int NUM_OF_UPDATE_KEYWORD = 10;
	private static final int NUM_OF_TASK_ATTRIBUTES = 9;
	
	public static Command parse(String rawInput){	
		rawInput = rawInput.trim();
		rawInput = changeEscapeCharacter(rawInput);
		String[] words = rawInput.split(PATTERN_SPACES);
		return parseCommand(words, rawInput);
	}
	
	private static Command parseCommand(String[] words, String rawInput){
		
		switch (words[FIRST_WORD_INDEX]){
			case ADD:
				return parseAddCmd(words, rawInput);
			case DISPLAY:
				return parseDisplayCmd(words, rawInput);
			case UPDATE:
				return parseUpdateCmd(words, rawInput);
			case DELETE:
				return parseDeleteCmd(words, rawInput);
			case SEARCH:
				return parseSearchCmd(words, rawInput);
			case SORT:
				return parseSortCmd(words, rawInput);
			case CMD_STR_MARK:
				return parseMarkCmd(words, rawInput);
			case CMD_STR_DONE:
				return parseDoneCmd(words, rawInput);
			case UNDO:
				return parseUndoCmd(words, rawInput);
			case REDO:
				return parseRedoCmd(words, rawInput);
			case CLEAR:
				return parseClearCmd(words, rawInput);
			case CMD_STR_TAG:
				return parseTagCmd(words, rawInput);
			case LINK_GOOGLE:
				return parseLinkGoogleCmd(words, rawInput);
			case SAVE:
				return parseSave(words, rawInput);
			case LOAD_FROM_GOOGLE:
				return parseLoadToGoogleCmd(words, rawInput);
			case HELP:
				return parseHelpCmd(words, rawInput);
			case EXIT:
				return parseExitCmd(words, rawInput);
			default:
				return parseInvalidCmd(words, rawInput);
		}
	}
	
	private static Command parseSave(String[] words, String rawInput) {
		if (words.length<=2){
			Pattern pattern = Pattern.compile(PATTERN_SAVE);
			Matcher matcher = pattern.matcher(rawInput);
			if (matcher.find()){
				String filePath = matcher.group(2);
				filePath = changeBackEscapeCharacter(filePath);
				return new CommandSave(filePath);
			}
			return new CommandInvalid(rawInput);
		} else {
			return new CommandInvalid(rawInput);
		}
	}

	private static Command parseAddCmd(String[] words, String rawInput){
		Pattern pattern = Pattern.compile(PATTERN_ADD);
		Matcher matcher = pattern.matcher(rawInput);
		if (matcher.find()){
			try {	
				String name = removeEscapeCharacter(matcher.group(2));
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
			String name = removeEscapeCharacter(matcher.group(2));
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
		if (words.length>1 && words[1].equals(IMPORTANT)){
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
				int index = Integer.parseInt(matcher.group(2));
				boolean[] checkList = new boolean[NUM_OF_TASK_ATTRIBUTES];
				Object[] newInfo = new Object[NUM_OF_TASK_ATTRIBUTES];
				makeCheckList(matcher, checkList, newInfo);
				return new CommandUpdate(index, checkList, newInfo);
			}
			catch (NumberFormatException e){
				return new CommandInvalid(matcher.group(2) + " is not a valid index");
			}
			catch (ParseException e){
				return new CommandInvalid(rawInput);
			}
		}
		
		return new CommandInvalid(rawInput);
	}
	
	private static void makeCheckList(Matcher matcher, boolean[] checkList, Object[] newInfo) 
	throws ParseException {
		for (int i=0; i<NUM_OF_UPDATE_KEYWORD; i++){
			if (matcher.group(3 + 2*i) != null){
				/* for both int[]updateChesklist and String[]newInfo 
				[0:name][1:type][2:starttime]
				[3:endtime][4:location][5:note]
				[6:tag][7:important][8:finished]
				*/
				String argument;
				switch (matcher.group(3 + 2*i)){
					case NAME:
						checkList[CommandUpdate.CODE_UPDATE_NAME] = true;
						argument = removeEscapeCharacter(matcher.group(4+2*i));
						newInfo[CommandUpdate.CODE_UPDATE_NAME] = argument;
						break;
					case BY:
						checkList[CommandUpdate.CODE_UPDATE_TYPE] = true;
						newInfo[CommandUpdate.CODE_UPDATE_TYPE] = TASK_TYPE.DEADLINE;
						checkList[CommandUpdate.CODE_UPDATE_END_TIME] = true;
						argument = removeEscapeCharacter(matcher.group(4+2*i));
						newInfo[CommandUpdate.CODE_UPDATE_END_TIME] = DateParser.parse(argument);
						break;
					case FROM:
						checkList[CommandUpdate.CODE_UPDATE_TYPE] = true;
						newInfo[CommandUpdate.CODE_UPDATE_TYPE] = TASK_TYPE.EVENT;
						checkList[CommandUpdate.CODE_UPDATE_START_TIME] = true;
						argument = removeEscapeCharacter(matcher.group(4+2*i));
						newInfo[CommandUpdate.CODE_UPDATE_START_TIME] = DateParser.parse(argument);
						break;
					case TO:
						checkList[CommandUpdate.CODE_UPDATE_TYPE] = true;
						newInfo[CommandUpdate.CODE_UPDATE_TYPE] = TASK_TYPE.EVENT;
						checkList[CommandUpdate.CODE_UPDATE_END_TIME] = true;
						argument = removeEscapeCharacter(matcher.group(4+2*i));
						newInfo[CommandUpdate.CODE_UPDATE_END_TIME] = DateParser.parse(argument);
						break;
					case FLOAT:
						checkList[CommandUpdate.CODE_UPDATE_TYPE] = true;
						newInfo[CommandUpdate.CODE_UPDATE_TYPE] = TASK_TYPE.FLOATING;
						break;
					case AT:
						checkList[CommandUpdate.CODE_UPDATE_LOCATION] = true;
						argument = removeEscapeCharacter(matcher.group(4+2*i));
						newInfo[CommandUpdate.CODE_UPDATE_LOCATION] = argument;
						break;
					case NOTE:
						checkList[CommandUpdate.CODE_UPDATE_NOTE] = true;
						argument = removeEscapeCharacter(matcher.group(4+2*i));
						newInfo[CommandUpdate.CODE_UPDATE_NOTE] = argument;
						break;
					case TAG:
						checkList[CommandUpdate.CODE_UPDATE_TAG] = true;
						argument = removeEscapeCharacter(matcher.group(4+2*i));
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
		int index = Integer.parseInt(words[SECOND_WORD_INDEX]);
		return new CommandDelete(index);
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
	
	private static Command parseRedoCmd(String[] words, String rawInput){
		return new CommandRedo();
	}
	
	private static Command parseClearCmd(String[] words, String rawInput){
		return new CommandClear();
	}
	
	private static Command parseTagCmd(String[] words, String rawInput){
		int index = Integer.parseInt(words[1]);
		String tagName = words[2];
		return new CommandTag(index, tagName);
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
		return new CommandInvalid(rawInput);
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
```
###### /src/calendear/util/CMD_TYPE.java
``` java
package calendear.util;

public enum CMD_TYPE {
	ADD, DISPLAY, UPDATE, DELETE, SEARCH, SORT, 
	MARK, DONE, UNDO, REDO, TAG, LINK_GOOGLE, EXIT, INVALID,
	LOAD_FROM_GOOGLE, CLEAR, SAVE, HELP
}
```
###### /src/calendear/util/Command.java
``` java
package calendear.util;

public abstract class Command {

	protected CMD_TYPE type;
	
	public CMD_TYPE getType(){
		return type;
	}

}
```
###### /src/calendear/util/CommandAdd.java
``` java
package calendear.util;

public class CommandAdd extends Command {
	
	private Task task;
	private boolean[] addChecklist;
	private Object[] newInfo;
	
	public CommandAdd(Task task){
		type = CMD_TYPE.ADD;
		this.task = task;
	}
	
	public CommandAdd (String name, boolean[] addChecklist, Object[] newInfo){
		type = CMD_TYPE.ADD;
		this.addChecklist = addChecklist;
		this.newInfo = newInfo;
		addChecklist[CommandUpdate.CODE_UPDATE_NAME] = true;
		newInfo[CommandUpdate.CODE_UPDATE_NAME] = name;
	}
	
	public Task getTask(){
		return task;
	}

	public void setTask(Task t) {
		task = t;	
	}
	
	public boolean[] getChecklist(){
		return addChecklist;
	}
	
	public Object[] getNewInfo(){
		return newInfo;
	}
	
//	public boolean equals(CommandAdd c){
//		boolean[] checkList = c.getChecklist();
//		Object[] newInfo = c.getNewInfo();
//		return Arrays.equals(checkList, this.addChecklist) && Arrays.equals(newInfo, this.newInfo);
//	}
	
}
```
###### /src/calendear/util/CommandDelete.java
``` java
package calendear.util;

public class CommandDelete extends Command {
	
	private int index;
	private Task _deletedTask;
	
	public CommandDelete(int index){
		type = CMD_TYPE.DELETE;
		this.index = index;
	}
	
	public int getIndex(){
		return index;
	}

	public void setDeletedTask(Task t) {
		_deletedTask = t;
	}
	
	public Task getDeletedTask(){
		return _deletedTask;
	}
	
}
```
###### /src/calendear/util/CommandDisplay.java
``` java
package calendear.util;

public class CommandDisplay extends Command {
	
	private boolean isOnlyImportantDisplayed;
	
	public CommandDisplay (boolean isOnlyImportantDisplayed){
		type = CMD_TYPE.DISPLAY;
		this.isOnlyImportantDisplayed = isOnlyImportantDisplayed;
	}
	
	public boolean isOnlyImportantDisplayed(){
		return isOnlyImportantDisplayed;
	}
	
}
```
###### /src/calendear/util/CommandDone.java
``` java
package calendear.util;

public class CommandDone extends Command {
	
	private int index;
	private boolean isDone;
	
	public CommandDone(int index){
		type = CMD_TYPE.DONE;
		this.index = index;
		this.isDone = true;
	}
	
	public CommandDone(int index, boolean isDone){
		type = CMD_TYPE.DONE;
		this.index = index;
		this.isDone = isDone;
	}
	
	public int getIndex(){
		return index;
	}
	
	public boolean isDone(){
		return this.isDone;
	}
	
	public void setIsDone(boolean isDone){
		this.isDone = isDone;
	}
}
```
###### /src/calendear/util/CommandExit.java
``` java
package calendear.util;

public class CommandExit extends Command {
	
	public CommandExit(){
		type = CMD_TYPE.EXIT;
	}
	
}
```
###### /src/calendear/util/CommandInvalid.java
``` java
package calendear.util;

public class CommandInvalid extends Command {
	
	private String command;
	
	public CommandInvalid(String command){
		type = CMD_TYPE.INVALID;
		this.command = command;
	}
	
	public String getCommand(){
		return command;
	}
}
```
###### /src/calendear/util/CommandLinkGoogle.java
``` java
package calendear.util;

public class CommandLinkGoogle extends Command {
	
	public CommandLinkGoogle(){
		type = CMD_TYPE.LINK_GOOGLE;
	}
	
}
```
###### /src/calendear/util/CommandLoadFromGoogle.java
``` java
package calendear.util;

import java.util.ArrayList;

public class CommandLoadFromGoogle extends Command {
	
	ArrayList<Task> undoList;
	
	public CommandLoadFromGoogle() {
		type = CMD_TYPE.LOAD_FROM_GOOGLE;
		undoList = new ArrayList<Task>();
	}
	
	public ArrayList<Task> getUndoList(){
		return undoList;
	}
	
	public void setUndoList(ArrayList<Task> list){
		this.undoList = list;
	}
	
}
```
###### /src/calendear/util/CommandMark.java
``` java
package calendear.util;

public class CommandMark extends Command {
	
	private int index;
	private boolean isImportant;
	
	public CommandMark(int index){
		type = CMD_TYPE.MARK;
		this.index = index;
		this.isImportant = true;
	}
	
	public CommandMark(int index, boolean importance){
		type = CMD_TYPE.MARK;
		this.index = index;
		this.isImportant = importance;
	}
	
	public int getIndex(){
		return index;
	}
	
	public boolean isImportant(){
		return this.isImportant;
	}
	
	public void setIsImportant(boolean isImportant){
		this.isImportant = isImportant;
	}
}
```
###### /src/calendear/util/CommandRedo.java
``` java
package calendear.util;

public class CommandRedo extends Command{
	
	public CommandRedo(){
		type = CMD_TYPE.REDO;
	}
}
```
###### /src/calendear/util/CommandSave.java
``` java
package calendear.util;

public class CommandSave extends Command {
	
	private String path;
	
	public CommandSave(String path){
		this.type = CMD_TYPE.SAVE;
		this.path = path;
	}
	
	public String getPath(){
		return path;
	}
	
}
```
###### /src/calendear/util/CommandSearch.java
``` java
package calendear.util;

/**
 * searchWith should be:
 * [string, 
	TASK_TYPE, 
	[GregorianCalendar, GregorianCalendar],
	[GregorianCalendar, GregorianCalendar],
	String,
	String,
	String,
	boolean,
	boolean]
 *
 */
public class CommandSearch extends Command {
	
	private String searchKey;
	private boolean[] toShow;
	private Object[] searchWith;
	
	public CommandSearch(String searchKey){
		type = CMD_TYPE.SEARCH;
		this.searchKey = searchKey;
	}
	
	public CommandSearch(boolean[] toShow, Object[] searchWith){
		assert(toShow.length == searchWith.length): "toShow is different length from searchWith in CommandSearch\n";
		this.toShow = toShow;
		this.searchWith = searchWith;
		this.type = CMD_TYPE.SEARCH;
	}
	
	public boolean[] getArrToShow(){
		return this.toShow;
	}
	
	public Object[] getArrSearchWith(){
		return this.searchWith;
	}
	
	public String getSearchKey(){
		return searchKey;
	}
}
```
###### /src/calendear/util/CommandTag.java
``` java
package calendear.util;


public class CommandTag extends Command {
	
	private int index;
	private String newTag;
	
	public CommandTag(int index, String newTag){
		type = CMD_TYPE.TAG;
		this.index = index;
		this.newTag = newTag;
	}
	
	public int getIndex(){
		return index;
	}
	
	public  String getTagName(){
		return newTag;
	}
	
	public void setTag(String newTag){
		this.newTag = newTag;
	}
}
```
###### /src/calendear/util/CommandUndo.java
``` java
package calendear.util;

public class CommandUndo extends Command {
	
	public CommandUndo(){
		type = CMD_TYPE.UNDO;
	}
	
}
```
###### /src/calendear/util/CommandUpdate.java
``` java
package calendear.util;

public class CommandUpdate extends Command {
	
	public static final int CODE_UPDATE_NAME = 0;
	public static final int CODE_UPDATE_TYPE = 1;
	public static final int CODE_UPDATE_START_TIME = 2;
	public static final int CODE_UPDATE_END_TIME = 3;
	public static final int CODE_UPDATE_LOCATION = 4;
	public static final int CODE_UPDATE_NOTE = 5;
	public static final int CODE_UPDATE_TAG = 6;
	public static final int CODE_UPDATE_IMPORTANT = 7;
	public static final int CODE_UPDATE_FINISHED = 8;
	
	private int index;
	
	/* for both int[]updateChesklist and String[]newInfo 
	[0:name][1:type][2:starttime]
	[3:endtime][4:location][5:note]
	[6:tag][7:important][8:finished]
	*/
	private boolean[] updateChecklist;
	private Object[] newInfo;
	
	
			
	public CommandUpdate (int index, String newName){
		type = CMD_TYPE.UPDATE;
		this.index = index;
	}
	
	public CommandUpdate (int index, boolean[] updateChecklist, Object[] newInfo){
		type = CMD_TYPE.UPDATE;
		this.index = index;
		this.updateChecklist = updateChecklist;
		this.newInfo = newInfo;
	}
	
	public int getIndex(){
		return index;
	}

	
	public boolean[] getChecklist(){
		return updateChecklist;
	}
	
	public Object[] getNewInfo(){
		return newInfo;
	}
	
}
```
###### /src/calendear/util/EditDistance.java
``` java
package calendear.util;

public class EditDistance {
	
	/**
	 * 
	 * Compute the LevenshteinDistance between 2 CharSequence. 
	 * This distance measure how much 2 string are different from each other.
	 * 
	 * Informally, the Levenshtein distance between two words is the minimum number
	 * of single-character edits (i.e. insertions, deletions or substitutions) required
	 * to change one word into the other. Inserting 1 character to one string will have 
	 * the same effect on Levenshtein distance as deleting 1 character in another string.
	 * 
	 * For more information https://en.wikipedia.org/wiki/Levenshtein_distance.
	 * 
	 * @param string1
	 * @param string2
	 * @return the Levenshtein distance between string1 and string2
	 */
    public static int computeEditDistance(CharSequence string1, CharSequence string2) {      
        //dynamic programming memo table
    	int[][] distance = new int[string1.length() + 1][string2.length() + 1];        

    	//The subproblem is finding edit distance of string1.substring(0,i) and string2.substring(0,j)
    	//The subproblem state can be described by 2 argument i, j.

    	//initialize base row
        for (int i = 0; i <= string1.length(); i++) {                                 
            distance[i][0] = i;                     
        }
        //initialize base column
        for (int j = 1; j <= string2.length(); j++) {                            
            distance[0][j] = j;                     
        }
                                                                                 
        for (int i = 1; i <= string1.length(); i++) {                                 
            for (int j = 1; j <= string2.length(); j++) {                             
                int caseDeleteString1Endpoint = distance[i - 1][j] + 1;
                int caseDeleteString2Endpoint = distance[i][j - 1] + 1;
                //We compare the end point of 2 string and add 1 substitution if they are not the same
                int caseNoDeletion = distance[i - 1][j - 1] + 
                		((string1.charAt(i - 1) == string2.charAt(j - 1)) ? 0 : 1);
            	distance[i][j] = minimum(caseDeleteString1Endpoint, caseDeleteString2Endpoint, caseNoDeletion);
            }
        }
                
        return distance[string1.length()][string2.length()];                           
    }
    
    private static int minimum(int a, int b, int c) {                            
        return Math.min(Math.min(a, b), c);                                      
    }                                                                            
    
}
```
###### /src/calendear/util/Task.java
``` java
package calendear.util;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Date;
import java.util.Calendar;
import java.util.TimeZone;

import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.client.util.DateTime;

import calendear.parser.DateParser;

public class Task {
	
	public static final SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, dd MMM yyyy, HH:mm");
	//private static final String OBJ_SEPERATOR = System.getProperty("line.separator");
	private static final String EMPTY = "";
	private static final String OBJ_SEPERATOR = ".";
	private static final String PATTERN_OBJ_SEPERATOR = "\\.";
	private static final String GOOGLE_EVENT_SEPERATOR = "-___-";
	private static final String IMPORTANT = "important";
	private static final String NOT_IMPORTANT = "not important";
	private static final String FINISHED = "finished";
	private static final String NOT_FINISHED = "not finished";
	private static final String STR_DEADLINE = "Deadline";
	private static final String STR_EVENT = "Event";
	private static final String STR_FLOATING = "Floating";
	
	private static final int SAVING_INDEX_NAME = 0;
	private static final int SAVING_INDEX_TYPE = 1;
	private static final int SAVING_INDEX_GOOGLE_ID = 2;
	private static final int SAVING_INDEX_START_TIME = 3;
	private static final int SAVING_INDEX_END_TIME = 4;
	private static final int SAVING_INDEX_LOCATION = 5;
	private static final int SAVING_INDEX_NOTE = 6;
	private static final int SAVING_INDEX_TAG = 7;
	private static final int SAVING_INDEX_IMPORTANT = 8;
	private static final int SAVING_INDEX_FINISHED = 9;
	
	private String name;
	private String googleEventId;
	private TASK_TYPE type;
	private GregorianCalendar startTime;
	private GregorianCalendar endTime;
	private String location;
	private String note;
	private String tag;
	private boolean isImportant;
	private boolean isFinished = false;
	
	public Task(){
		//adding task with infoList and newData
	}
	public Task(String name) {
		type = TASK_TYPE.FLOATING;
		this.name = name.trim();
	}
	
	public Task(String name, GregorianCalendar deadline) {
		type = TASK_TYPE.DEADLINE;
		this.name = name.trim();
		this.endTime = deadline;
	}
	
	public Task(String name, GregorianCalendar startTime, GregorianCalendar endTime) {
		type = TASK_TYPE.EVENT;
		this.name = name.trim();
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	public String getName() {
		return name;
	}
	
	public String getEventId() {
		return googleEventId;
	}
	
	public TASK_TYPE getType() {
		return type;
	}
	
	public String getTypeStr(){
		switch (type){
			case DEADLINE: return STR_DEADLINE;
			case EVENT: return STR_EVENT;
			default : return STR_FLOATING;
		}
	}
	
	public GregorianCalendar getStartTime() {
		return startTime;
	}
	
	public GregorianCalendar getEndTime() {
		return endTime;
	}
	
	public String getStartTimeStr() {
		if (startTime == null) {
			return EMPTY;
		}
		return dateFormatter.format(startTime.getTime());
	}
	
	public String getEndTimeStr() {
		if (endTime == null) {
			return EMPTY;
		}
		return dateFormatter.format(endTime.getTime());
	}
	
	public String getLocation() {
		if (location == null) {
			return EMPTY;
		}
		return location;
	}
	
	public String getNote() {
		if (note == null) {
			return EMPTY;
		}
		return note;
	}
	
	public String getTag(){
		if (tag == null) {
			return EMPTY;
		}
		return tag;
	}
	
	public boolean isImportant(){
		return isImportant;
	}
	
	private String getImportantStr(){
		if (isImportant) {
			return IMPORTANT;
		}
		else {
			return NOT_IMPORTANT;
		}
	}
	
	public boolean isFinished(){
		return isFinished;
	}
	
	private String getFinishedStr(){
		if (isFinished) {
			return FINISHED;
		}
		else {
			return NOT_FINISHED;
		}
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setEventId(String eventId) {
		this.googleEventId = eventId;
	}
	
	public void setStartTime(GregorianCalendar time) {
		this.startTime = time;
	}
	
	/**
```
###### /src/calendear/util/Task.java
``` java
	
	public void setEndTime(GregorianCalendar time) {
		this.endTime = time;
	}
	
	/**
```
###### /src/calendear/util/Task.java
``` java
	
	public void setType (TASK_TYPE type) {
		this.type = type;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public void setNote(String note){
		this.note = note;
	}
	
	public void setTag(String tag){
		this.tag = tag;
	}
	
	public void setIsImportant(boolean isImportant){
		this.isImportant = isImportant;
	}
	
	public void setIsFinished(boolean isFinished){
		this.isFinished = isFinished;
	}
	
	/**
```
###### /src/calendear/util/Task.java
``` java
	
	public String toSaveable() {
		String res;
		res = name + OBJ_SEPERATOR;
		res += getTypeStr() + OBJ_SEPERATOR;
		res += googleEventId + OBJ_SEPERATOR;
		res += getStartTimeStr() + OBJ_SEPERATOR;
		res += getEndTimeStr() + OBJ_SEPERATOR;
		res += location + OBJ_SEPERATOR;
		res += note + OBJ_SEPERATOR;
		res += getTag() + OBJ_SEPERATOR;
		res += getImportantStr() + OBJ_SEPERATOR;
		res += getFinishedStr() + OBJ_SEPERATOR;
		return res;
	}
```
###### /src/calendear/util/Task.java
``` java
	public static Task parseSaveable(String allString) throws ParseException {
		String[] members = allString.split(PATTERN_OBJ_SEPERATOR);
		String typeStr = members[SAVING_INDEX_TYPE];
		Task res;
		switch (typeStr){
			case STR_DEADLINE:
				res = parseDeadline(members);
				break;
			case STR_EVENT:
				res = parseEvent(members);
				break;
			case STR_FLOATING:
				res = parseFloat(members);
				break;
			default:
				throw new ParseException("type name not defined", 0);
		}
		parseOptionalAttribute(res, members);
		return res;
	}

	private static void parseOptionalAttribute(Task res, String[] members) {
		if (members[SAVING_INDEX_GOOGLE_ID].equals(EMPTY)){
			res.setEventId(null);
		} else {
			res.setEventId(members[SAVING_INDEX_GOOGLE_ID]);
		}
		
		if (members[SAVING_INDEX_TAG].equals(EMPTY)){
			res.setTag(null);
		} else {
			res.setTag(members[SAVING_INDEX_TAG]);
		}
		
		if (members[SAVING_INDEX_LOCATION].equals(EMPTY)){
			res.setEventId(null);
		} else {
			res.setLocation(members[SAVING_INDEX_LOCATION]);
		}
		
		if (members[SAVING_INDEX_NOTE].equals(EMPTY)){
			res.setEventId(null);
		} else {
			res.setNote(members[SAVING_INDEX_NOTE]);
		}
		
		if (members[SAVING_INDEX_IMPORTANT].equals(IMPORTANT)){
			res.setIsFinished(true);
		} else {
			res.setIsImportant(false);
		}
		
		if (members[SAVING_INDEX_FINISHED].equals(FINISHED)){
			res.setIsFinished(true);
		} else {
			res.setIsFinished(false);
		}
	}
	private static Task parseDeadline(String[] members) throws ParseException {
		String name = members[SAVING_INDEX_NAME];
		String endTimeStr = members[SAVING_INDEX_END_TIME];
		GregorianCalendar endTime = DateParser.parse(endTimeStr);
		return new Task(name, endTime);
	}
	
	private static Task parseEvent(String[] members) throws ParseException {
		String name = members[SAVING_INDEX_NAME];
		String startTimeStr = members[SAVING_INDEX_START_TIME];
		GregorianCalendar startTime = DateParser.parse(startTimeStr);
		String endTimeStr = members[SAVING_INDEX_END_TIME];
		GregorianCalendar endTime = DateParser.parse(endTimeStr);
		return new Task(name, startTime, endTime);
	}
	
	private static Task parseFloat(String[] members) {
		String name = members[SAVING_INDEX_NAME];
		return new Task(name);
	}
	
```
###### /src/calendear/util/TASK_TYPE.java
``` java
package calendear.util;

public enum TASK_TYPE {
	EVENT, DEADLINE, FLOATING, RECURRING
}
```
###### /src/test/ParserTest.java
``` java
package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;
import org.junit.internal.ArrayComparisonFailure;

import calendear.parser.Parser;
import calendear.util.Command;
import calendear.util.CommandAdd;
import calendear.util.CommandDelete;
import calendear.util.CommandDisplay;
import calendear.util.CommandUpdate;
import calendear.util.TASK_TYPE;

public class ParserTest {
	
	private static final int CHECKLIST_SIZE = 9;
	/* for both int[]updateChesklist and String[]newInfo 
	[0:name][1:type][2:starttime]
	[3:endtime][4:location][5:note]
	[6:tag][7:important][8:finished]
	*/
	private static final int INDEX_NAME = 0;
	private static final int INDEX_TYPE = 1;
	private static final int INDEX_START_TIME = 2;
	private static final int INDEX_END_TIME = 3;
	private static final int INDEX_LOCATION = 4;
	private static final int INDEX_NOTE = 5;
	private static final int INDEX_TAG = 6;
	private static final int INDEX_IMPORTANT = 7;
	private static final int INDEX_FINISHED = 8;

	private ArrayList<String> caseDescriptions = new ArrayList<String>();
	private ArrayList<String> rawInputs = new ArrayList<String>();
	private ArrayList<Command> expectedOutputs = new ArrayList<Command>();
	
	@Before
	public void setUp() throws Exception {
		addCaseAddDeadline();
		addCaseAddDeadlineWithOptions();
		addCaseAddInvalid();
		addCaseAddEvent();
		addCaseAddEventWithOptions();
		addCaseAddFloat();
		addCaseAddFloatwithOption();
		addCaseDisplay();
		addCaseDisplayImportant();
		addCaseUpdateWithoutChangeTaskType();
		addCaseUpdateToDeadline();
		addCaseUpdateToEvent();
		addCaseUpdateToFloat();
		addCaseDelete();
	}

	private void addCaseAddInvalid() {
		// TODO Auto-generated method stub
		caseDescriptions.add("add floating task with minimum parameter");
		rawInputs.add("add read the lord of the ring");
		String name = "read the lord of the ring";
		boolean[] checklist = new boolean[CHECKLIST_SIZE];
		Object[] newInfo = new Object[CHECKLIST_SIZE];
		checklist[INDEX_NAME] = true;
		newInfo[INDEX_NAME] = name;
		checklist[INDEX_TYPE] = true;
		newInfo[INDEX_TYPE] = TASK_TYPE.FLOATING;
		expectedOutputs.add(new CommandAdd(name, checklist, newInfo));
	}

	private void addCaseAddDeadline() {
		caseDescriptions.add("add deadline task with minimum parameter");
		rawInputs.add("add visit garden .by the bay by 3/21/16 5:30");
		String name = "visit garden by the bay";
		GregorianCalendar deadline = new GregorianCalendar(2016, Calendar.MARCH, 21, 5, 30);
		boolean[] checklist = new boolean[CHECKLIST_SIZE];
		Object[] newInfo = new Object[CHECKLIST_SIZE];
		checklist[INDEX_NAME] = true;
		newInfo[INDEX_NAME] = name;
		checklist[INDEX_TYPE] = true;
		newInfo[INDEX_TYPE] = TASK_TYPE.DEADLINE;
		checklist[INDEX_END_TIME] = true;
		newInfo[INDEX_END_TIME] = deadline;
		expectedOutputs.add(new CommandAdd(name, checklist, newInfo));
	}

	private void addCaseAddDeadlineWithOptions() {
		caseDescriptions.add("add deadline task with options");
		rawInputs.add("add visit friendssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss at garden .by the bay by 3/21/16 5:30 important note bring cakes");
		String name = "visit friendssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss";
		GregorianCalendar deadline = new GregorianCalendar(2016, Calendar.MARCH, 21, 5, 30);
		boolean[] checklist = new boolean[CHECKLIST_SIZE];
		Object[] newInfo = new Object[CHECKLIST_SIZE];
		checklist[INDEX_NAME] = true;
		newInfo[INDEX_NAME] = name;
		checklist[INDEX_TYPE] = true;
		newInfo[INDEX_TYPE] = TASK_TYPE.DEADLINE;
		checklist[INDEX_END_TIME] = true;
		newInfo[INDEX_END_TIME] = deadline;
		checklist[INDEX_IMPORTANT] = true;
		newInfo[INDEX_IMPORTANT] = true;
		checklist[INDEX_LOCATION] = true;
		newInfo[INDEX_LOCATION] = "garden by the bay";
		checklist[INDEX_NOTE] = true;
		newInfo[INDEX_NOTE] = "bring cakes";
		expectedOutputs.add(new CommandAdd(name, checklist, newInfo));
	}

	private void addCaseAddEvent() {
		caseDescriptions.add("add event task with minimum parameter");
		rawInputs.add("add visit garden .by the bay from 3/21/16 5:30pm to 21 Mar 2016 20:00");
		String name = "visit garden by the bay";
		GregorianCalendar startTime = new GregorianCalendar(2016, Calendar.MARCH, 21, 17, 30);
		GregorianCalendar endTime = new GregorianCalendar(2016, Calendar.MARCH, 21, 20, 0);
		boolean[] checklist = new boolean[CHECKLIST_SIZE];
		Object[] newInfo = new Object[CHECKLIST_SIZE];
		checklist[INDEX_NAME] = true;
		newInfo[INDEX_NAME] = name ;
		checklist[INDEX_TYPE] = true;
		newInfo[INDEX_TYPE] = TASK_TYPE.EVENT;
		checklist[INDEX_START_TIME] = true;
		newInfo[INDEX_START_TIME] = startTime;
		checklist[INDEX_END_TIME] = true;
		newInfo[INDEX_END_TIME] = endTime;
		expectedOutputs.add(new CommandAdd(name, checklist, newInfo));
	}

	private void addCaseAddEventWithOptions() {
		caseDescriptions.add("add event task with options");
		rawInputs.add("add visit friends from 3/21/16 5:30pm to 21 Mar 2016 20:00 at garden .by the bay note bring cakes important");
		String name = "visit friends";
		GregorianCalendar startTime = new GregorianCalendar(2016, Calendar.MARCH, 21, 17, 30);
		GregorianCalendar endTime = new GregorianCalendar(2016, Calendar.MARCH, 21, 20, 0);
		boolean[] checklist = new boolean[CHECKLIST_SIZE];
		Object[] newInfo = new Object[CHECKLIST_SIZE];
		checklist[INDEX_NAME] = true;
		newInfo[INDEX_NAME] = name ;
		checklist[INDEX_TYPE] = true;
		newInfo[INDEX_TYPE] = TASK_TYPE.EVENT;
		checklist[INDEX_START_TIME] = true;
		newInfo[INDEX_START_TIME] = startTime;
		checklist[INDEX_END_TIME] = true;
		newInfo[INDEX_END_TIME] = endTime;
		checklist[INDEX_IMPORTANT] = true;
		newInfo[INDEX_IMPORTANT] = true;
		checklist[INDEX_LOCATION] = true;
		newInfo[INDEX_LOCATION] = "garden by the bay";
		checklist[INDEX_NOTE] = true;
		newInfo[INDEX_NOTE] = "bring cakes";
		expectedOutputs.add(new CommandAdd(name, checklist, newInfo));
	}

	private void addCaseAddFloat() {
		caseDescriptions.add("add floating task with minimum parameter");
		rawInputs.add("add read the lord of the ring");
		String name = "read the lord of the ring";
		boolean[] checklist = new boolean[CHECKLIST_SIZE];
		Object[] newInfo = new Object[CHECKLIST_SIZE];
		checklist[INDEX_NAME] = true;
		newInfo[INDEX_NAME] = name;
		checklist[INDEX_TYPE] = true;
		newInfo[INDEX_TYPE] = TASK_TYPE.FLOATING;
		expectedOutputs.add(new CommandAdd(name, checklist, newInfo));
	}

	private void addCaseAddFloatwithOption() {
		// TODO Auto-generated method stub
		
	}

	private void addCaseDisplay() {
		caseDescriptions.add("standard display");
		rawInputs.add(" display  ");
		expectedOutputs.add(new CommandDisplay(false));
	}

	private void addCaseDisplayImportant() {
		caseDescriptions.add("display important only");
		rawInputs.add("display  important");
		expectedOutputs.add(new CommandDisplay(true));
	}

	private void addCaseUpdateWithoutChangeTaskType() {
		caseDescriptions.add("update without changing type and time of task");
		rawInputs.add("update 2 done name visit friends at garden .by the bay important note bring cakes");
		String name = "visit friends";
		boolean[] checklist = new boolean[CHECKLIST_SIZE];
		Object[] newInfo = new Object[CHECKLIST_SIZE];
		checklist[INDEX_NAME] = true;
		newInfo[INDEX_NAME] = name;
		checklist[INDEX_IMPORTANT] = true;
		newInfo[INDEX_IMPORTANT] = true;
		checklist[INDEX_LOCATION] = true;
		newInfo[INDEX_LOCATION] = "garden by the bay";
		checklist[INDEX_NOTE] = true;
		newInfo[INDEX_NOTE] = "bring cakes";
		checklist[INDEX_FINISHED] = true;
		newInfo[INDEX_FINISHED] = true;
		expectedOutputs.add(new CommandUpdate(2, checklist, newInfo));
	}
	
	private void addCaseUpdateToDeadline() {
		caseDescriptions.add("update and change type to deadline task with options");
		rawInputs.add("update 2 done name visit friends tag some blatag at garden .by the bay by 3/21/16 5:30 important note bring cakes");
		String name = "visit friends";
		GregorianCalendar deadline = new GregorianCalendar(2016, Calendar.MARCH, 21, 5, 30);
		boolean[] checklist = new boolean[CHECKLIST_SIZE];
		Object[] newInfo = new Object[CHECKLIST_SIZE];
		checklist[INDEX_NAME] = true;
		newInfo[INDEX_NAME] = name;
		checklist[INDEX_TYPE] = true;
		newInfo[INDEX_TYPE] = TASK_TYPE.DEADLINE;
		checklist[INDEX_END_TIME] = true;
		newInfo[INDEX_END_TIME] = deadline;
		checklist[INDEX_IMPORTANT] = true;
		newInfo[INDEX_IMPORTANT] = true;
		checklist[INDEX_LOCATION] = true;
		newInfo[INDEX_LOCATION] = "garden by the bay";
		checklist[INDEX_NOTE] = true;
		newInfo[INDEX_NOTE] = "bring cakes";
		checklist[INDEX_FINISHED] = true;
		newInfo[INDEX_FINISHED] = true;
		checklist[INDEX_TAG] = true;
		newInfo[INDEX_TAG] = "some blatag";
		expectedOutputs.add(new CommandUpdate(2, checklist, newInfo));
	}

	private void addCaseUpdateToEvent() {
		caseDescriptions.add("update deadline task with options");
		rawInputs.add("update 15 from 3/21/16 5:30pm at garden .by the bay tag some .tag name visit friends note bring cakes to 21 Mar 2016 20:00 important");
		String name = "visit friends";
		GregorianCalendar startTime = new GregorianCalendar(2016, Calendar.MARCH, 21, 17, 30);
		GregorianCalendar endTime = new GregorianCalendar(2016, Calendar.MARCH, 21, 20, 0);
		boolean[] checklist = new boolean[CHECKLIST_SIZE];
		Object[] newInfo = new Object[CHECKLIST_SIZE];
		checklist[INDEX_NAME] = true;
		newInfo[INDEX_NAME] = name ;
		checklist[INDEX_TYPE] = true;
		newInfo[INDEX_TYPE] = TASK_TYPE.EVENT;
		checklist[INDEX_START_TIME] = true;
		newInfo[INDEX_START_TIME] = startTime;
		checklist[INDEX_END_TIME] = true;
		newInfo[INDEX_END_TIME] = endTime;
		checklist[INDEX_IMPORTANT] = true;
		newInfo[INDEX_IMPORTANT] = true;
		checklist[INDEX_TAG] = true;
		newInfo[INDEX_TAG] = "some tag";
		checklist[INDEX_LOCATION] = true;
		newInfo[INDEX_LOCATION] = "garden by the bay";
		checklist[INDEX_NOTE] = true;
		newInfo[INDEX_NOTE] = "bring cakes";
		expectedOutputs.add(new CommandUpdate(15, checklist, newInfo));
	}

	private void addCaseUpdateToFloat() {
		caseDescriptions.add("update and change task type to float with options");
		rawInputs.add("update 15 at garden .by the bay float tag some .tag name visit friends note bring cakes important");
		String name = "visit friends";
		boolean[] checklist = new boolean[CHECKLIST_SIZE];
		Object[] newInfo = new Object[CHECKLIST_SIZE];
		checklist[INDEX_NAME] = true;
		newInfo[INDEX_NAME] = name ;
		checklist[INDEX_TYPE] = true;
		newInfo[INDEX_TYPE] = TASK_TYPE.FLOATING;
		checklist[INDEX_IMPORTANT] = true;
		newInfo[INDEX_IMPORTANT] = true;
		checklist[INDEX_TAG] = true;
		newInfo[INDEX_TAG] = "some tag";
		checklist[INDEX_LOCATION] = true;
		newInfo[INDEX_LOCATION] = "garden by the bay";
		checklist[INDEX_NOTE] = true;
		newInfo[INDEX_NOTE] = "bring cakes";
		expectedOutputs.add(new CommandUpdate(15, checklist, newInfo));
	}

	private void addCaseDelete() {
		caseDescriptions.add("delete by index");
		rawInputs.add("delete 10");
		expectedOutputs.add(new CommandDelete(10));
	}

	@Test
	public void test() {
		for (int i=0; i<rawInputs.size(); i++){
			String description = caseDescriptions.get(i);
			switch (expectedOutputs.get(i).getType()){
				case ADD:
					assertAddCmd(i, description);
					break;
				case DISPLAY:
					assertDisplayCmd(i, description);
					break;
				case UPDATE:
					assertUpdateCmd(i, description);
					break;
				case DELETE:
					assertDeleteCmd(i, description);
					break;
				case EXIT:
					assertExitCmd(i, description);
					break;
				case INVALID:
					assertInvalidCmd(i, description);
					break;


			}
		}
	}

	private void assertAddCmd(int i, String description) throws ArrayComparisonFailure {
		CommandAdd expected = (CommandAdd) expectedOutputs.get(i);
		CommandAdd actual = (CommandAdd) Parser.parse(rawInputs.get(i));
		boolean[] expectedChecklist = expected.getChecklist();
		boolean[] actualChecklist = actual.getChecklist();
		Object[] expectedNewInfo = expected.getNewInfo();
		Object[] actualNewInfo = actual.getNewInfo();
		assertArrayEquals(description, expectedChecklist, actualChecklist);
		assertArrayEquals(description, expectedNewInfo, actualNewInfo);
	}
	
	private void assertDisplayCmd(int i, String description) {
		CommandDisplay expected = (CommandDisplay) expectedOutputs.get(i);
		CommandDisplay actual = (CommandDisplay) Parser.parse(rawInputs.get(i));
		assertEquals(description, expected.isOnlyImportantDisplayed(), actual.isOnlyImportantDisplayed());
	}

	private void assertUpdateCmd(int i, String description) {
		CommandUpdate expected = (CommandUpdate) expectedOutputs.get(i);
		CommandUpdate actual = (CommandUpdate) Parser.parse(rawInputs.get(i));
		boolean[] expectedChecklist = expected.getChecklist();
		boolean[] actualChecklist = actual.getChecklist();
		Object[] expectedNewInfo = expected.getNewInfo();
		Object[] actualNewInfo = actual.getNewInfo();
		assertArrayEquals(description, expectedChecklist, actualChecklist);
		assertArrayEquals(description, expectedNewInfo, actualNewInfo);
	}

	private void assertDeleteCmd(int i, String description) {
		CommandDelete expected = (CommandDelete) expectedOutputs.get(i);
		CommandDelete actual = (CommandDelete) Parser.parse(rawInputs.get(i));
		assertEquals(description, expected.getIndex(), actual.getIndex());
	}

	private void assertExitCmd(int i, String description) {
		// TODO Auto-generated method stub
		
	}

	private void assertInvalidCmd(int i, String description) {
		// TODO Auto-generated method stub
		
	}

}
```

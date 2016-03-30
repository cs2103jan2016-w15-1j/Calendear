package calendear.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import calendear.util.*;

import java.text.ParseException;

public class Parser {
	
	private static final String ADD = "add";
	private static final String DISPLAY = "display";
	private static final String UPDATE = "update";
	private static final String DELETE = "delete";
	private static final String SEARCH = "search";
	private static final String SORT = "sort";
	private static final String CMD_STR_MARK = "mark";
	private static final String CMD_STR_DONE = "done";
	private static final String UNDO = "undo";
	private static final String CMD_STR_TAG = "tag";
	private static final String LINK_GOOGLE = "linkGoogle";
	private static final String EXIT = "exit";
	private static final String REDO = "redo";
	private static final String LOAD_TO_GOOGLE = "loadToGoogle";
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
	"((?:.(?!\\bname\\b|\\bby\\b|\\bfrom\\b|\\bto\\b|\\bfloat\\b|\\bat\\b|\\bnote\\b|\\btag\\b|\\bimportant\\b|\\bdone\\b))+)";
	private static final String PATTERN_ADD = 
	"(\\badd\\b) +" + NEGATIVE_LOOKAHEAD_KEYWORDS + " +"				//represent the groups update and <taskID>
	+ "(?:(?:(\\bname\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"		//represent the groups name and <newName>
	+ "|(?:(\\bby\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"			//represent the groups by and <dateAndTime>
	+ "|(?:(\\bfrom\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"		//represent the groups from and <dateAndTime>
	+ "|(?:(\\bto\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"			//represent the groups to and <dateAndTime>
	+ "|(?:(\\bfloat\\b) *()) *"		//represent the group float
	+ "|(?:(\\bat\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"			//represent the groups at and <newLocation>
	+ "|(?:(\\bnote\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"		//represent the groups note and <newNote>
	+ "|(?:(\\btag\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"		//represent the groups tag and <newTag>
	+ "|(?:(\\bimportant\\b) *()) *"			//represent the group mark
	+ "|(?:(\\bdone\\b) *()) *)+";		//represent the group done
	private static final String PATTERN_ADD_FLOAT = "(\\badd\\b) +(.+)";
	private static final String PATTERN_UPDATE_BY_INDEX = 
	"(\\bupdate\\b) +(\\d+) +"			//represent the groups update and <taskID>
	+ "(?:(?:(\\bname\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"		//represent the groups name and <newName>
	+ "|(?:(\\bby\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"			//represent the groups by and <dateAndTime>
	+ "|(?:(\\bfrom\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"		//represent the groups from and <dateAndTime>
	+ "|(?:(\\bto\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"			//represent the groups to and <dateAndTime>
	+ "|(?:(\\bfloat\\b) *()) *"		//represent the group float
	+ "|(?:(\\bat\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"			//represent the groups at and <newLocation>
	+ "|(?:(\\bnote\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"		//represent the groups note and <newNote>
	+ "|(?:(\\btag\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"		//represent the groups tag and <newTag>
	+ "|(?:(\\bimportant\\b) *()) *"			//represent the group mark
	+ "|(?:(\\bdone\\b) *()) *)+";		//represent the group done
	private static final String PATTERN_SEARCH = 
	"(\\badd\\b) +"				//represent the groups update and <taskID>
	+ "(?:(?:(\\bname\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"		//represent the groups name and <newName>
	+ "|(?:(\\bby\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"			//represent the groups by and <dateAndTime>
	+ "|(?:(\\bfrom\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"		//represent the groups from and <dateAndTime>
	+ "|(?:(\\bto\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"			//represent the groups to and <dateAndTime>
	+ "|(?:(\\bfloat\\b) *()) *"		//represent the group float
	+ "|(?:(\\bat\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"			//represent the groups at and <newLocation>
	+ "|(?:(\\bnote\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"		//represent the groups note and <newNote>
	+ "|(?:(\\btag\\b) *"+ NEGATIVE_LOOKAHEAD_KEYWORDS +") *"		//represent the groups tag and <newTag>
	+ "|(?:(\\bimportant\\b) *()) *"			//represent the group mark
	+ "|(?:(\\bdone\\b) *()) *)+";		//represent the group done
	
	private static final int NUM_OF_UPDATE_KEYWORD = 10;
	private static final int NUM_OF_TASK_ATTRIBUTES = 9;
	
	public static Command parse(String rawInput){	
		rawInput = rawInput.trim();
		rawInput = changeEscapeCharacter(rawInput);
		String[] words = rawInput.split(" +");
		return parseCommand(words, rawInput);
	}
	
	private static Command parseCommand(String[] words, String rawInput){
		
		switch (words[0]){
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
			case CMD_STR_TAG:
				return parseTagCmd(words, rawInput);
			case LINK_GOOGLE:
				return parseLinkGoogleCmd(words, rawInput);
			case LOAD_TO_GOOGLE:
				return parseLoadToGoogleCmd(words, rawInput);
			case EXIT:
				return parseExitCmd(words, rawInput);
			default:
				return parseInvalidCmd(words, rawInput);
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
		int index = Integer.parseInt(words[1]);
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
	
	private static Command parseTagCmd(String[] words, String rawInput){
		int index = Integer.parseInt(words[1]);
		String tagName = words[2];
		return new CommandTag(index, tagName);
	}
	
	private static Command parseLinkGoogleCmd(String[] words, String rawInput){
		return new CommandLinkGoogle();
	}
	
	private static Command parseLoadToGoogleCmd(String[] words, String rawInput){
		return new CommandLoadToGoogle();
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
	
}

/** 
 *  parseUpdateCmd2 by Jiyun
 * 
 **/
//.update <index> <header1:content\\r\\n header2:content\\r\\n ...>
//private static Command parseUpdateCmd2(String[] words, String rawInput){
//		Pattern pattern = Pattern.compile(PATTERN_UPDATE_NAME_BY_INDEX);
//		Matcher matcher = pattern.matcher(rawInput);
//		if (matcher.find()){
//			try {	
//				int index = Integer.parseInt(matcher.group(2));
//				String newInfo = matcher.group(3);
//				int[] updateChecklist = new int[NUM_OF_TASK_ATTRIBUTES];
//				String[] newInfoContentOnly = new String[NUM_OF_TASK_ATTRIBUTES];
//				String[] newInfoArr = newInfo.split("\\r?\\n");
//				for(int i=0;i<newInfoArr.length;i++){
//					String[] line = newInfoArr[i].split(":");
//					if(line[0].equalsIgnoreCase("name")){
//						updateChecklist[0]=1;
//						newInfoContentOnly[0]= line[1];
//					}
//					else if(line[0].equalsIgnoreCase("type")){
//						updateChecklist[1]=1;
//						newInfoContentOnly[1]= line[1];
//					}
//					else if(line[0].equalsIgnoreCase("starttime")){
//						updateChecklist[2]=1;
//						newInfoContentOnly[2]= line[1];
//					}
//					else if(line[0].equalsIgnoreCase("endtime")){
//						updateChecklist[3]=1;
//						newInfoContentOnly[3]= line[1];
//					}
//					else if(line[0].equalsIgnoreCase("location")){
//						updateChecklist[4]=1;
//						newInfoContentOnly[4]= line[1];
//					}
//					else if(line[0].equalsIgnoreCase("note")){
//						updateChecklist[5]=1;
//						newInfoContentOnly[5]= line[1];
//					}
//					else if(line[0].equalsIgnoreCase("tag")){
//						updateChecklist[6]=1;
//						newInfoContentOnly[6]= line[1];
//					}
//					else if(line[0].equalsIgnoreCase("important")){
//						updateChecklist[7]=1;
//						newInfoContentOnly[7]= line[1];
//					}
//					else if(line[0].equalsIgnoreCase("finished")){
//						updateChecklist[8]=1;
//						newInfoContentOnly[8]= line[1];
//					}
//				
//					
//				}
//				return new CommandUpdate(index, updateChecklist, newInfoContentOnly);
//			}
//			catch (NumberFormatException e){
//				return new CommandInvalid(rawInput);
//			}
//		}
//		
//		return new CommandInvalid(rawInput);
//	}
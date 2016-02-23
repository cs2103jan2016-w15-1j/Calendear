package calendear.util;

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
	
	private String rawInput;
	private String[] words;
	
	public Parser(String input){	
		rawInput = input;
		input = input.trim();
		words = input.split(" ");
	}
	
	public Command parseCommand(){
		
		switch (words[0]){
			case CMD_STR_ADD:
				return parseAddCmd();
			case CMD_STR_DISPLAY:
				return parseDisplayCmd();
			case CMD_STR_UPDATE:
				return parseUpdateCmd();
			case CMD_STR_DELETE:
				return parseDeleteCmd();
			case CMD_STR_SEARCH:
				return parseSearchCmd();
			case CMD_STR_SORT:
				return parseSortCmd();
			case CMD_STR_MARK:
				return parseMarkCmd();
			case CMD_STR_DONE:
				return parseDoneCmd();
			case CMD_STR_UNDO:
				return parseUndoCmd();
			case CMD_STR_TAG:
				return parseTagCmd();
			case CMD_STR_LINK_GOOGLE:
				return parseLinkGoogleCmd();
			case CMD_STR_EXIT:
				return parseExitCmd();
			default:
				return parseInvalidCmd();
		}
	}
	
	private Command parseAddCmd(){
		return null;
	}
	
	private Command parseDisplayCmd(){
		return null;
	}
	
	private Command parseUpdateCmd(){
		return null;
	}
	
	private Command parseDeleteCmd(){
		return null;
	}
	
	private Command parseSearchCmd(){
		return null;
	}
	
	private Command parseSortCmd(){
		return null;
	}
	
	private Command parseMarkCmd(){
		return null;
	}
	
	private Command parseDoneCmd(){
		return null;
	}
	
	private Command parseUndoCmd(){
		return new CommandUndo();
	}
	
	private Command parseTagCmd(){
		return null;
	}
	
	private Command parseLinkGoogleCmd(){
		return new CommandLinkGoogle();
	}
	
	private Command parseExitCmd(){
		return new CommandExit();
	}
	
	private Command parseInvalidCmd(){
		return new CommandInvalid(rawInput);
	}
	
}








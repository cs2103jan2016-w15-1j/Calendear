package calendear.util;

enum CMD_TYPE {
	ADD, DISPLAY, UPDATE, DELETE, SEARCH, SORT, 
	MARK, DONE, UNDO, TAG, LINK_GOOGLE, EXIT, INVALID
};	

public class Command {
	
	protected CMD_TYPE type;
	

	public CMD_TYPE getType(){
		return type;
	}
}

class CommandAdd extends Command {
	
	private Task task;
	
	public CommandAdd(Task task){
		type = CMD_TYPE.ADD;
		this.task = task;
	}
	
	public Task getTask(){
		return task;
	}
	
	
	
}

class CommandDisplay extends Command {
	
	private boolean isOnlyImportantDisplayed;
	
	public CommandDisplay (boolean isOnlyImportantDisplayed){
		type = CMD_TYPE.DISPLAY;
		this.isOnlyImportantDisplayed = isOnlyImportantDisplayed;
	}
	
	public boolean isOnlyImportantDisplayed(){
		return isOnlyImportantDisplayed;
	}
	
}

class CommandUpdate extends Command {
	
	private int index;
	private String newName;
	
	public CommandUpdate (int index, String newName){
		type = CMD_TYPE.UPDATE;
		this.index = index;
		this.newName = newName;
	}
	
	public int getIndex(){
		return index;
	}
	
	public String getNewName(){
		return newName;
	}
	
}

class CommandDelete extends Command {
	
	private int index;
	
	public CommandDelete(int index){
		type = CMD_TYPE.DELETE;
		this.index = index;
	}
	
	public int getIndex(){
		return index;
	}
	
}

class CommandMark extends Command {
	
	private int index;
	
	public CommandMark(int index){
		type = CMD_TYPE.MARK;
		this.index = index;
	}
	
	public int getIndex(){
		return index;
	}
	
}

class CommandDone extends Command {
	
	private int index;
	
	public CommandDone(int index){
		type = CMD_TYPE.DONE;
		this.index = index;
	}
	
	public int getIndex(){
		return index;
	}
	
}

class CommandUndo extends Command {
	
	public CommandUndo(){
		type = CMD_TYPE.UNDO;
	}
	
}

class CommandTag extends Command {
	
	private int index;
	private String tagName;
	
	public CommandTag(int index, String tagName){
		type = CMD_TYPE.TAG;
		this.index = index;
		this.tagName = tagName;
	}
	
	public int getIndex(){
		return index;
	}
	
	public String getTagName(){
		return tagName;
	}
	
}

class CommandSearch extends Command {
	
	private String searchKey;
	
	public CommandSearch(String searchKey){
		type = CMD_TYPE.SEARCH;
		this.searchKey = searchKey;
	}
	
	public String getSearchKey(){
		return searchKey;
	}
	
}

class CommandLinkGoogle extends Command {
	
	public CommandLinkGoogle(){
		type = CMD_TYPE.LINK_GOOGLE;
	}
	
}

class CommandExit extends Command {
	
	public CommandExit(){
		type = CMD_TYPE.EXIT;
	}
	
}

class CommandInvalid extends Command {
	
	private String command;
	
	public CommandInvalid(String command){
		type = CMD_TYPE.INVALID;
		this.command = command;
	}
	
	public String getCommand(){
		return command;
	}
}




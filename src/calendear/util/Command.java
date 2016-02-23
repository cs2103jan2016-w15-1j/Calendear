package calendear.util;

enum CMD_TYPE {
	ADD, DISPLAY, UPDATE, DELETE, SEARCH, SORT, 
	MARK, DONE, UNDO, TAG, LINK_GOOGLE, EXIT
};	

public class Command {
	
	private CMD_TYPE type;
	private Task task;
	
	public Command(CMD_TYPE type, Task task){
		this.type = type;
		this.task = task;
	}
	
	public CMD_TYPE getType(){
		return type;
	}
	
	public Task getTask(){
		return task;
	}
}

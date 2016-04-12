// @@author A0126513N
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
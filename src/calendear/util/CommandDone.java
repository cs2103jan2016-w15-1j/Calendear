// @@author A0126513N
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
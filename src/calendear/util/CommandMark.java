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
}
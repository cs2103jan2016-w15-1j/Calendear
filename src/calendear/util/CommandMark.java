package calendear.util;

public class CommandMark extends Command {
	
	private int index;
	
	public CommandMark(int index){
		type = CMD_TYPE.MARK;
		this.index = index;
	}
	
	public int getIndex(){
		return index;
	}
	
}
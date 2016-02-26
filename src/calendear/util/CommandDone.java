package calendear.util;

public class CommandDone extends Command {
	
	private int index;
	
	public CommandDone(int index){
		type = CMD_TYPE.DONE;
		this.index = index;
	}
	
	public int getIndex(){
		return index;
	}
	
}
package calendear.util;

public class CommandDelete extends Command {
	
	private int index;
	
	public CommandDelete(int index){
		type = CMD_TYPE.DELETE;
		this.index = index;
	}
	
	public int getIndex(){
		return index;
	}
	
}
package calendear.util;

import java.util.ArrayList;

public class CommandLoadToGoogle extends Command {
	
	ArrayList<Task> undoList;
	
	public CommandLoadToGoogle() {
		type = CMD_TYPE.LOAD_TO_GOOGLE;
		undoList = new ArrayList<Task>();
	}
	
	public ArrayList<Task> getUndoList(){
		return undoList;
	}
	
	public void setUndoList(ArrayList<Task> list){
		this.undoList = list;
	}
	
}

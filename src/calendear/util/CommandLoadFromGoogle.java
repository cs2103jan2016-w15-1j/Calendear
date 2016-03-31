package calendear.util;

import java.util.ArrayList;

public class CommandLoadFromGoogle extends Command {
	
	ArrayList<Task> undoList;
	
	public CommandLoadFromGoogle() {
		type = CMD_TYPE.LOAD_FROM_GOOGLE;
		undoList = new ArrayList<Task>();
	}
	
	public ArrayList<Task> getUndoList(){
		return undoList;
	}
	
	public void setUndoList(ArrayList<Task> list){
		this.undoList = list;
	}
	
}

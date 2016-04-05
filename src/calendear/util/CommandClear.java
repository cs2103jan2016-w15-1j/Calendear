package calendear.util;

import java.util.ArrayList;

public class CommandClear extends Command{
	ArrayList<Task> listBeforeClear;
	
	public CommandClear(){
		type = CMD_TYPE.CLEAR;
		listBeforeClear = new ArrayList<Task>();
	}
	
	public void setBeforeList(ArrayList<Task> list){
		this.listBeforeClear = list;
	}
	
	public ArrayList<Task> getListBeforeClear(){
		return this.listBeforeClear;
	}
}

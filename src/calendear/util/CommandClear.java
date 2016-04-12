// @@author A0126513
package calendear.util;

import java.util.ArrayList;
/**
 * 
 * @author Wu XiaoXiao
 *
 */
public class CommandClear extends Command{
	ArrayList<Task> listBeforeClear;
	boolean isLoggedToGoogle;
	
	public CommandClear(){
		this.type = CMD_TYPE.CLEAR;
		this.listBeforeClear = new ArrayList<Task>();
		this.isLoggedToGoogle = false;
	}
	
	public void setBeforeList(ArrayList<Task> list){
		this.listBeforeClear = list;
	}
	
	public ArrayList<Task> getListBeforeClear(){
		return this.listBeforeClear;
	}
	
	public void setIsLoggedToGoogle(){
		this.isLoggedToGoogle = true;
	}
	
	public boolean isLoggedToGoogle(){
		return isLoggedToGoogle;
	}
}

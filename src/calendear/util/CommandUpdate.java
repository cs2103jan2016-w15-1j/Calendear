package calendear.util;

/**
 * 
 * @author Viet Thang
 * @
 *
 */
public class CommandUpdate extends Command {
	
	private int index;
	private String newName;
	
	public CommandUpdate (int index, String newName){
		type = CMD_TYPE.UPDATE;
		this.index = index;
		this.newName = newName;
	}
	
	public int getIndex(){
		return index;
	}
	
	public String getNewName(){
		return newName;
	}
	
}
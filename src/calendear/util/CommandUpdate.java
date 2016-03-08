package calendear.util;

/**
 * 
 * @author Viet Thang
 * 
 *
 */
public class CommandUpdate extends Command {
	
	private int index;
	private String newName;
	
	private int[] updateChecklist;
	private String[] newInfo;
	
	
			
	public CommandUpdate (int index, String newName){
		type = CMD_TYPE.UPDATE;
		this.index = index;
		this.newName = newName;
	}
	
	public CommandUpdate (int index, int[] updateChecklist, String[] newInfo){
		type = CMD_TYPE.UPDATE;
		this.updateChecklist = updateChecklist;
		this.newInfo = newInfo;
	}
	
	public int getIndex(){
		return index;
	}
	
	
	public String getNewName(){
		return newName;
	}
	
	public int[] getChecklist(){
		return updateChecklist;
	}
	
	public String[] getNewInfo(){
		return newInfo;
	}
}
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
	
	/* for both int[]updateChesklist and String[]newInfo 
	[0:name][1:type][2:starttime]
	[3:endtime][4:location][5:note]
	[6:tag][7:important][8:finished]
	*/
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
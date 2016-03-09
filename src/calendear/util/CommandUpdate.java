package calendear.util;

public class CommandUpdate extends Command {
	
	public static final int CODE_UPDATE_NAME = 0;
	public static final int CODE_UPDATE_TYPE = 1;
	public static final int CODE_UPDATE_START_TIME = 2;
	public static final int CODE_UPDATE_END_TIME = 3;
	public static final int CODE_UPDATE_LOCATION = 4;
	public static final int CODE_UPDATE_NOTE = 5;
	public static final int CODE_UPDATE_TAG = 6;
	public static final int CODE_UPDATE_IMPORTANT = 7;
	public static final int CODE_UPDATE_FINISHED = 8;
	
	private int index;
	
	/* for both int[]updateChesklist and String[]newInfo 
	[0:name][1:type][2:starttime]
	[3:endtime][4:location][5:note]
	[6:tag][7:important][8:finished]
	*/
	private boolean[] updateChecklist;
	private Object[] newInfo;
	
	
			
	public CommandUpdate (int index, String newName){
		type = CMD_TYPE.UPDATE;
		this.index = index;
	}
	
	public CommandUpdate (int index, boolean[] updateChecklist, Object[] newInfo){
		type = CMD_TYPE.UPDATE;
		this.index = index;
		this.updateChecklist = updateChecklist;
		this.newInfo = newInfo;
	}
	
	public int getIndex(){
		return index;
	}

	
	public boolean[] getChecklist(){
		return updateChecklist;
	}
	
	public Object[] getNewInfo(){
		return newInfo;
	}
}
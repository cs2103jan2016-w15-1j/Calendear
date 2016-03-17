package calendear.util;

public class CommandAdd extends Command {
	
	private Task task;
	private boolean[] addChecklist;
	private Object[] newInfo;
	
	public CommandAdd(Task task){
		type = CMD_TYPE.ADD;
		this.task = task;
	}
	
	public CommandAdd (String name, boolean[] addChecklist, Object[] newInfo){
		type = CMD_TYPE.ADD;
		this.addChecklist = addChecklist;
		this.newInfo = newInfo;
		addChecklist[CommandUpdate.CODE_UPDATE_NAME] = true;
		newInfo[CommandUpdate.CODE_UPDATE_NAME] = name;
	}
	
	public Task getTask(){
		return task;
	}

	public void setTask(Task t) {
		task = t;	
	}
	
	public boolean[] getChecklist(){
		return addChecklist;
	}
	
	public Object[] getNewInfo(){
		return newInfo;
	}
	
}
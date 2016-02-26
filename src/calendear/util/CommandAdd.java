package calendear.util;

public class CommandAdd extends Command {
	
	private Task task;
	
	public CommandAdd(Task task){
		type = CMD_TYPE.ADD;
		this.task = task;
	}
	
	public Task getTask(){
		return task;
	}

	public void setTask(Task t) {
		task = t;	
	}
	
	
	
}
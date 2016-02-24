package calendear.util;


/**
 * CommandState stores a command state, storing the 
 * task to be modified and the command which modifies it
 * 
 * this object should be created only after a command
 * that will modify the data is called
 * 
 * @author Wu XiaoXiao
 *
 */
public class CommandState {
	
	private Task previousTask_;
	private Command cmd_;
	
	//constructor
	public CommandState(Task prevTask, Command cmd){
		previousTask_ = prevTask;
		cmd_ = cmd;
	}
	
	//im allowing the input cuz it might be confusing without
	//but the command here can only be add, since their is no
	// other command that does not have a previous state
	public CommandState(CommandAdd cmd){
		cmd_ = cmd;
		previousTask_ = null;
	}
	
	//accessers
	public Task getPreviousTask(){
		return previousTask_;
	}
	
	public Command getPreviousCommand(){
		return cmd_;
	}
}

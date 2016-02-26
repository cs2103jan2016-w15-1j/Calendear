package calendear.command;

import calendear.util.*;

public class HistoryState {
	private Task beforeModification_;
	private CMD_TYPE t_;
	private int taskId_;
	
	public HistoryState(Task newTask, CMD_TYPE ty){
		beforeModification_ = newTask;
		t_ = ty;
		//taskId_ = newTask.getId();
	}
	
	public HistoryState(CMD_TYPE ty, int id){// for add
		//id
		taskId_ = id;
	}
	
	/**
	 * accessors
	 */
	public Task getTask(){
		return beforeModification_;
	}
	
	public CMD_TYPE getType(){
		return t_;
	}
	
	public int getId(){
		return taskId_;
	}
}

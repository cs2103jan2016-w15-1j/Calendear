package calendear.action;

import calendear.util.*;

public class HistoryState {
	private Task _beforeModification;
	private CMD_TYPE _t;
	private int _taskId;
	
	public HistoryState(Task newTask, CMD_TYPE ty){
		_beforeModification = newTask;
		_t = ty;
		//taskId_ = newTask.getId();
	}
	
	public HistoryState(CMD_TYPE ty, int id){// for add
		//id
		_taskId = id;
	}
	
	/**
	 * accessors
	 */
	public Task getTask(){
		return _beforeModification;
	}
	
	public CMD_TYPE getType(){
		return _t;
	}
	
	public int getId(){
		return _taskId;
	}
}

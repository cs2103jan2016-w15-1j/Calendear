package calendear.action;

import java.util.ArrayList;
import java.util.Stack;

import calendear.util.Command;
import calendear.util.CMD_TYPE;
import calendear.util.Task;
/**
 * 
 * @author Wu XiaoXiao
 *
 */
public class Action {
	private ArrayList<Task> _data;
	private Stack<Command> _previousData;//last in first out
	private Stack<Command> _previousCommand;
	
	
	//constructor
	public Action(){
		_data = new ArrayList<Task>();
		_previousData = new Stack<Command>();
		_previousCommand = new Stack<Command>();
	}
	
	public Action(ArrayList<Task> tasks) {
		_data = tasks;
		_previousData = new Stack<Command>();
		_previousCommand = new Stack<Command>();
	}
	/**
	 * returns the task that was added.
	 * id is arrayList.size() - 1
	 * @param c
	 * @return
	 */
	public Task add(Command c){
		Task addedTask = c.getTask();
		_data.add(addedTask);
		c.setTask(null);
		_previousData.push(c);
		return addedTask;
	}
	/**
	 * remove task with id
	 * @param id
	 */
	public Task delete(Command c){
		int id = c.getIndex();
		Task t = _data.get(id);
		c.setTask(t);
		_previousData.push(c);
		return t;
	}


	public void update(Command c){
		String newName = c.getNewName();
		int id = c.getIndex();
		Task toBeModified = _data.get(id);
		String oldName = toBeModified.getName();
		//TODO
	}
	
	public ArrayList display(){
		return _data;
	}
	
	public void search(String str){
		
	}
	
	public void undo(){
		
	}
	
	public void tag(){
		
	}
	
	public void mark(){
		
	}
}



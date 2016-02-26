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
	}
	
	public void add(Command c){
		Task addedTask = c.getTask();
		
	}
	/**
	 * remove task with id
	 * @param id
	 */
	public void delete(Command c){
		//data_.remove(id);
	}
	/**
	 * TODO
	 * @param id
	 */
	public void update(int id){
		
	}
	
	public void display(){
		
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

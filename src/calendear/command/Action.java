package calendear.command;

import java.util.LinkedList;
import java.util.Stack;

import calendear.util.Command;
import calendear.util.Task;

public class Action {
	private LinkedList<Task> data_;
	private Stack<HistoryState> previousData_;//last in first out
	private Stack<Command> previousCommand_;
	
	//constructor
	public Action(){
		data_ = new LinkedList<Task>();
		previousData_ = new Stack<HistoryState>();
	}
	
	public Action(ArrayList<Task> tasks) {
		
	}
	
	public void add(Task t){
		data_.add(t);
	}
	/**
	 * remove task with id
	 * @param id
	 */
	public void delete(int id){
		data_.remove(id);
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

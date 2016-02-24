package calendear.util;

import java.util.LinkedList;
import java.util.Stack;

public class Action {
	private LinkedList<Task> data_;
	private Stack<Command> previousData_;//last in first out
	
	//constructor
	public Action(){
		data_ = new LinkedList<Task>();
		previousData_ = new Stack<Command>();
	}
	
	public void add(Task t){
		data_.add(t);
		previousData_.push(new CommandAdd(t));
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

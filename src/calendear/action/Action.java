package calendear.action;

import java.util.ArrayList;
import java.util.Stack;

import calendear.util.Command;
import calendear.util.CMD_TYPE;
import calendear.util.Task;
import calendear.storage.DataManager;
/**
 * 
 * @author Wu XiaoXiao
 *
 */
public class Action {
	private ArrayList<Task> _data;
	private Stack<Command> _previousData;//last in first out
	private Stack<Command> _previousCommand;
	private DataManager _dm; 
	
	//constructor
	/**
	 * default constructor, not used.
	 */
	public Action(){
		_data = new ArrayList<Task>();
		_previousData = new Stack<Command>();
		_previousCommand = new Stack<Command>();
	}
	
	public Action(ArrayList<Task> tasks, DataManager dm) {
		_data = tasks;
		_previousData = new Stack<Command>();
		_previousCommand = new Stack<Command>();
		_dm = dm;
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
		_dm.updateData(_data);
		return addedTask;
	}
	/**
	 * remove task with id
	 * @param id
	 */
	public Task delete(Command c){
		int id = c.getIndex();
		Task t = _data.get(id);
		_data.set(id, null);
		c.setTask(t);
		_previousData.push(c);
		_dm.updateData(_data);
		return t;
	}


	public Task update(Command c){
		String newName = c.getNewName();
		int id = c.getIndex();
		Task toBeModified = _data.get(id);
		String oldName = toBeModified.getName();
		//TODO
		toBeModified.setName(newName);
		return toBeModified;
	}
	/**
	 * 
	 * @param c
	 * @return arrayList containing tasks to show, all null elements should not be shown
	 */
	public ArrayList display(Command c){
		if(c.isOnlyImportantDisplayed()){
			//prepares an arraylist where all non important tasks are represented as null
			ArrayList<Task> imp = new ArrayList<Task>();
			for(int i = 0; i< _data.size(); i++){
				Task temp = _data.get(i);
				if(temp.isImportant()){
					imp.add(temp);
				}else{
					imp.add(null);
				}
			}
		}else{
			return _data;
		}
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



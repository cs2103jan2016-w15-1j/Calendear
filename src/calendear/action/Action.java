package calendear.action;

import java.util.ArrayList;
import java.util.Stack;

import calendear.util.Command;
import calendear.util.CommandAdd;
import calendear.util.CommandDelete;
import calendear.util.CommandDisplay;
import calendear.util.CommandUpdate;
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
	private Stack<Command> _undoStack;
	private Stack<Command> _redoStack;
	private DataManager _dm; 
	
	//constructor
	/**
	 * default constructor, not used.
	 */
	public Action(){
		_data = new ArrayList<Task>();
		_undoStack = new Stack<Command>();
		_redoStack = new Stack<Command>();
	}
	
	public Action(String nameOfFile) {
		_undoStack = new Stack<Command>();
		_redoStack = new Stack<Command>();
		_dm = new DataManager(nameOfFile);
		_data = _dm.buildData();
	}
	
	public Action(ArrayList<Task> tasks, String nameOfFile) {
		_data = tasks;
		_undoStack = new Stack<Command>();
		_redoStack = new Stack<Command>();
		_dm = new DataManager(nameOfFile);
	}
	/**
	 * returns the task that was added.
	 * id is arrayList.size() - 1
	 * @param c
	 * @return
	 */
	public Task exeAdd(CommandAdd c){
		Task addedTask = c.getTask();
		_data.add(addedTask);
		c.setTask(null);
		_undoStack.push(c);
		_dm.updateData(getNoNullArr());
		return addedTask;
	}
	/**
	 * remove task with id
	 * @param id
	 */
	public Task exeDelete(CommandDelete c){
		int id = c.getIndex();
		Task t = _data.get(id);
		_data.set(id, null);
		c.setDeletedTask(t);
		_undoStack.push(c);
		_dm.updateData(getNoNullArr());
		return t;
	}


	public Task exeUpdate(CommandUpdate c){
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
	public ArrayList<Task> exeDisplay(CommandDisplay c){
		if(c.isOnlyImportantDisplayed()){
			//prepares an arraylist where all non important tasks are represented as null
			ArrayList<Task> imp = new ArrayList<Task>();
			for(int i = 0; i< _data.size(); i++){
				Task temp = _data.get(i);
				if(temp != null && temp.isImportant()){
					imp.add(temp);
				}else{
					imp.add(null);
				}
			}
			return imp;
		}else{
			return _data;
		}
	}
	
	public void exeSearch(String str){
		
	}
	
	public void exeUndo(){
		
	}
	
	public void exeTag(){
		
	}
	
	public void exeMark(){
		
	}
	
	public void exeSort(){
		
	}
	/**
	 * returns an array of data with null objects removed
	 * @return
	 */
	private ArrayList<Task> getNoNullArr(){
		ArrayList<Task> toReturn = new ArrayList<Task>();
		Task t=  null;
		for(int i = 0; i<_data.size(); i++){
			t = _data.get(i);
			if(t != null){
				toReturn.add(t);
			}
		}
		return toReturn;
	}
	
	
}



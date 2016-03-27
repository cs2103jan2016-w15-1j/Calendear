package calendear.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import calendear.util.*;
import calendear.storage.DataManager;
import calendear.storage.GoogleIO;
/**
 * 
 * @author Wu XiaoXiao
 *
 *methods in Action:
 *1. exeAdd: functioning
 *2. exeDelete: functioning
 *3. exeUpdate: functioning
 *4. exeDisplay: functioning
 *5. exeSearch: in progress
 *6. exeUndo: finished except for undo tag
 *7. exeRedo: finished except for undo tag
 *8. exeTag: done, but currently there's no way to save previous tag
 *9. exeToggleImportance: functioning
 *10. exeToggleDone: functioning
 *11. exeSort: has not started
 *12. exeLinkGoogle:
 *13. exeExit:
 */


public class Action {
	private static final Logger log= Logger.getLogger( "Action" );
	private ArrayList<Task> _data;
	private Stack<Command> _undoStack;
	private Stack<Command> _redoStack;
	private DataManager _dataManager; 
	
	final int NAME_ID = 0;
	final int TYPE_ID = 1;
	final int STARTT_ID = 2;
	final int ENDT_ID = 3;
	final int LOCATION_ID = 4;
	final int NOTE_ID = 5;
	final int TAG_ID = 6;
	final int IMP_ID = 7;//important
	final int COMP_ID = 8;//finished
	
	//constructor
	/**
	 * default constructor, not used.
	 */
	/*public Action(){
		_data = new ArrayList<Task>();
		_undoStack = new Stack<Command>();
		_redoStack = new Stack<Command>();
	}*/
	
	public Action(String nameOfFile) throws ParseException {
		_undoStack = new Stack<Command>();
		_redoStack = new Stack<Command>();
		_dataManager = new DataManager(nameOfFile);
		_data = _dataManager.getDataFromFile();
	}
	
	/*//not using
	public Action(ArrayList<Task> tasks, String nameOfFile) {
		_data = tasks;
		_undoStack = new Stack<Command>();
		_redoStack = new Stack<Command>();
		_dataManager = new DataManager(nameOfFile);
	}*/
	/**
	 * returns the task that was added.
	 * id is arrayList.size() - 1
	 * @param cmd
	 * @return task to be added
	 */
	//---------helper-------------
	//helper for add
	private Task addWithInfo(CommandAdd cmd){
		Task toReturn = new Task("");// "" is a stud
		boolean[] infoList = cmd.getChecklist();
		Object[] newData = cmd.getNewInfo();
		exchangeInfo(toReturn, infoList, newData);
		return toReturn;
	}
	
	/**
	 * called by CDLogic
	 * @param cmd
	 * @return task added to storage
	 */
	public Task exeAdd(CommandAdd cmd){
		assertCommandNotNull(cmd);
		String eventId; 
		Task addedTask = addWithoutSave(cmd);
		if(this._dataManager.isLogined()){
			eventId = this._dataManager.addTaskToGoogle(addedTask);
			addedTask.setEventId(eventId);
		}
		this._undoStack.push(cmd);
		this._dataManager.insertDataToFile(getNoNullArr());
		return addedTask;
	}
	/**
	 * add to _data, but not storage
	 * @param cmd
	 * @return added task
	 */
	private Task addWithoutSave(CommandAdd cmd) {
		Task addedTask = cmd.getTask();
		if(addedTask == null){
			addedTask = addWithInfo(cmd);
			assert(addedTask != null) : "task to add is null";
		}
		this._data.add(addedTask);
		cmd.setTask(null);
		return addedTask;
	}
	/**
	 * remove task with id
	 * @param id
	 */
	public Task exeDelete(CommandDelete cmd){
		assertCommandNotNull(cmd);
		int id = cmd.getIndex();
		Task t = _data.get(id);
		if(this._dataManager.isLogined() && t.getEventId() != null){
			
		}
		_data.set(id, null);
		cmd.setDeletedTask(t);
		_undoStack.push(cmd);
		_dataManager.insertDataToFile(getNoNullArr());
		return t;
	}

	/**[0:name][1:type][2:starttime]
		[3:endtime][4:location][5:note]
		[6:tag][7:important][8:finished]
	*/
	/**
	 * update contents of toUpdate with infoList and newData
	 * @param toUpdate
	 * @param infoList
	 * @param newData
	 */
	//helper class to exchange contents of CommandUpdate and task
	private void exchangeInfo(Task toUpdate, boolean[] infoList, Object[] newData){
		
		
		try{
			if(infoList[NAME_ID]){
				//name
				String oldName = toUpdate.getName();
				toUpdate.setName((String)newData[NAME_ID]);
				newData[NAME_ID] = oldName;
			}
			if(infoList[TYPE_ID]){
				//type
				TASK_TYPE oldType = toUpdate.getType();
				toUpdate.setType((TASK_TYPE)newData[TYPE_ID]);
				newData[TYPE_ID] = (Object)oldType;
			}
			if(infoList[STARTT_ID]){
				//start time
				GregorianCalendar oldStartTime = toUpdate.getStartTime();
				toUpdate.setStartTime((GregorianCalendar) newData[STARTT_ID]);
				newData[STARTT_ID] = (Object)oldStartTime;
			}
			if(infoList[ENDT_ID]){
				//end time
				GregorianCalendar oldEndTime = toUpdate.getEndTime();
				toUpdate.setEndTime((GregorianCalendar) newData[ENDT_ID]);
				newData[ENDT_ID] = (Object)oldEndTime;
			}
			if(infoList[LOCATION_ID]){
				String newLoc = (String)newData[LOCATION_ID];
				newData[LOCATION_ID] = toUpdate.getLocation();
				toUpdate.setLocation(newLoc);
			}
			if(infoList[NOTE_ID]){
				String newNote = (String)newData[NOTE_ID];
				newData[LOCATION_ID] = toUpdate.getLocation();
				toUpdate.setLocation(newNote);
			}
			if(infoList[TAG_ID]){
				//can be a series of tags, need to specify 
				// add/delete/replace
				//TODO
				String newTag = (String) newData[TAG_ID];
				toUpdate.setTag(newTag);
			}
			if(infoList[IMP_ID]){
				boolean isImportant = (boolean)newData[IMP_ID];
				newData[IMP_ID] = Boolean.toString(toUpdate.isImportant());
				toUpdate.markImportant(isImportant);
			}
			if(infoList[COMP_ID]){
				boolean isFinished = (boolean)newData[COMP_ID];
				newData[COMP_ID] = Boolean.toString(toUpdate.isFinished());
				toUpdate.setIsFinished(isFinished);
			}
		}catch (NullPointerException e){
			e.printStackTrace();
		}catch (ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
		}
	}
	/**
	 * updates toUpdate with cmd
	 * @param cmd
	 * @param toUpdate
	 */
	private void updateInformation(CommandUpdate cmd, Task toUpdate){
		boolean[] infoList = cmd.getChecklist();
		Object[] newData = cmd.getNewInfo();
		exchangeInfo(toUpdate, infoList, newData);
	}
	

	/**
	 * executes cmd (a command update)
	 * @param cmd
	 * @return
	 */
	public Task exeUpdate(CommandUpdate cmd){
		assertCommandNotNull(cmd);
		int changeId = cmd.getIndex();
		Task toUpdate = _data.get(changeId);
		if(this._dataManager.isLogined() && toUpdate.getEventId() != null){
			this._dataManager.updateTaskToGoogle(toUpdate);
		}
		updateInformation(cmd, toUpdate);
		_undoStack.add(cmd);
		this._dataManager.insertDataToFile(getNoNullArr());
		return toUpdate;
	}
	
	/**
	 * arrayList containing tasks to show, all null elements should not be shown
	 * @param cmd
	 * @return ArrayList<Task>
	 */
	public ArrayList<Task> exeDisplay(CommandDisplay cmd){
		assertCommandNotNull(cmd);
		if(cmd.isOnlyImportantDisplayed()){
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
	/**
	 * filters _data for tasks to be displayed according to toShow and searchWith
	 * @param toShow
	 * @param searchWith
	 * @return ArrayList<Task>
	 */
	private ArrayList<Task> exeDisplaySelectiveHelper(boolean[] toShow, Object[] searchWith){
		
		ArrayList<Task> show = new ArrayList<Task>();
		show.addAll(this._data);
		
		for(int i = 0; i<show.size(); i++){
			Task t = show.get(i);
			try{
				if(toShow[NAME_ID] && !t.getName().equals((String)searchWith[NAME_ID])){
					t = null;
				}
				if(toShow[TYPE_ID] && !t.getType().equals((TASK_TYPE)searchWith[TYPE_ID])){
					t = null;
				}
				if(toShow[STARTT_ID]){
				}
				if(toShow[ENDT_ID]){
				}
				if(toShow[LOCATION_ID]){
				}
				if(toShow[NOTE_ID]){
				}
				if(toShow[TAG_ID]){
				}
				if(toShow[IMP_ID] && !(t.isImportant() == (boolean)searchWith[IMP_ID])){
					t = null;
				}
				if(toShow[COMP_ID] && !(t.isFinished() == (boolean)searchWith[COMP_ID])){
					
				}
			}catch (NullPointerException e){
				e.printStackTrace();
			}catch (ArrayIndexOutOfBoundsException e){
				e.printStackTrace();
			}
		}
		
		return show;
	}
	
	public void exeSearch(String strToSearch){
		//TODO
	}
	
	/**
	 * ADD: removed last task in arraylist and store in cmdAdd
	 * UPDATE: exchanged data with data stored in cmdUpdate
	 * DELETE: replaced null at index with stored task
	 * SORT: -
	 * MARK: toggle importance of task
	 * DONE: toggle done-tag of task
	 * TAG: -
	 */
	
	public void exeUndo(){

		try{
			Command previousCmd = _undoStack.pop();
			CMD_TYPE cmdType = previousCmd.getType();
			switch(cmdType){
				case ADD:
					log.log(Level.FINE, "undo add", previousCmd);
					CommandAdd cmdAdd = (CommandAdd) previousCmd;//type casting, we are sure that cmdType == ADD
					int lastIndex = _data.size()-1;
					Task removed = _data.get(lastIndex);
					_data.remove(lastIndex);//removed from _data ArrayList
					cmdAdd.setTask(removed);//commandAdd in _redoStack will always contain the task.
					previousCmd = cmdAdd;//typecast back to be added to _redoStack
					
					break;
				case UPDATE:
					log.log(Level.FINE, "undo update", previousCmd);
					CommandUpdate cmdUpdate = (CommandUpdate) previousCmd;
					Task toUpdate = this._data.get(cmdUpdate.getIndex());
					updateInformation(cmdUpdate, toUpdate);
					break;
				case DELETE:
					log.log(Level.FINE, "undo delete", previousCmd);
					CommandDelete cmdDelete = (CommandDelete) previousCmd;
					int deleteIndex = cmdDelete.getIndex();
					Task toAddBack = cmdDelete.getDeletedTask();
					this._data.set(deleteIndex, toAddBack);
					break;
				case MARK:
					log.log(Level.FINE, "undo mark", previousCmd);
					CommandMark cmdMark = (CommandMark) previousCmd;
					int markIndex = cmdMark.getIndex();
					Task toMark = this._data.get(markIndex);
					toMark.markImportant(!toMark.isImportant());//toggles importance
					break;
				case DONE:
					log.log(Level.FINE, "undo done", previousCmd);
					CommandDone cmdDone = (CommandDone) previousCmd;
					int doneIndex = cmdDone.getIndex();
					Task done = this._data.get(doneIndex);
					done.setIsFinished(!done.isFinished());
					break;
				case TAG:
					//currently tag is a private string
					//TODO
					log.log(Level.FINE, "undo tag", previousCmd);
					CommandTag cmdTag = (CommandTag) previousCmd;
					int tagIndex = cmdTag.getIndex();
					Task tagged = this._data.get(tagIndex);
					tagged.setTag(cmdTag.getTagName());
					break;
				default:
					log.log(Level.SEVERE, "reached unreachable area in undo", previousCmd);
					throw new AssertionError(cmdType);
			}
			_redoStack.push(previousCmd);
			this._dataManager.insertDataToFile(getNoNullArr());
			log.log(Level.FINE, "pushed previousCmd to redoStack", previousCmd);
		}
		catch (EmptyStackException e){
			System.out.println("error: nothing to undo");
		}
	}
	
	/**
	 * execute each command for the user.
	 */
	public void exeRedo(){
		try{
		Command redoCmd = this._redoStack.pop();
		CMD_TYPE redoType = redoCmd.getType();
		
		switch (redoType){
		case ADD:
			log.log(Level.FINE, "redo add", redoCmd);
			CommandAdd cmdAdd = (CommandAdd) redoCmd;
			assert(cmdAdd.getTask() != null);//cmdAdd always contains the task
			exeAdd(cmdAdd);
			break;
		case UPDATE:
			log.log(Level.FINE, "redo update", redoCmd);
			CommandUpdate cmdUpdate = (CommandUpdate) redoCmd;
			exeUpdate(cmdUpdate);
			break;
		case DELETE:
			log.log(Level.FINE, "redo delete", redoCmd);
			CommandDelete cmdDelete = (CommandDelete) redoCmd;
			exeDelete(cmdDelete);
			break;
		case SORT:
			//TODO since after sorting the index will change, this should not happen
			break;
		case MARK:
			log.log(Level.FINE, "redo toggle Importance", redoCmd);
			CommandMark cmdMark = (CommandMark) redoCmd;
			exeToggleImportance(cmdMark);
			break;
		case DONE:
			log.log(Level.FINE, "redo toggle finished", redoCmd);
			CommandDone cmdDone = (CommandDone) redoCmd;
			exeToggleDone(cmdDone);
			break;
		case TAG:
			//TODO
			break;
		default:
			log.log(Level.SEVERE, "reached unreachable area in redo", redoCmd);
			throw new AssertionError(redoCmd);
		}
		this._dataManager.insertDataToFile(getNoNullArr());
		}
		catch (EmptyStackException e){
			System.out.println("error: nothing to redo");
		}
	}
	
	public void exeTag(CommandTag cmd){
		//TODO can not save previous tag
		int toTagIndex = cmd.getIndex();
		Task toTag = this._data.get(toTagIndex);
		toTag.setTag(cmd.getTagName());
		this._undoStack.push(cmd);
		this._dataManager.insertDataToFile(getNoNullArr());
	}
	
	public void exeToggleImportance(CommandMark cmd){//toggles importance
		int toMarkIndex = cmd.getIndex();
		Task toMark = this._data.get(toMarkIndex);
		toMark.markImportant(!toMark.isImportant());
		
		this._undoStack.push(cmd);
		this._dataManager.insertDataToFile(getNoNullArr());
	}
	
	public void exeToggleDone(CommandDone cmd){
		int toMarkDoneIndex = cmd.getIndex();
		Task toMarkDone = this._data.get(toMarkDoneIndex);
		toMarkDone.setIsFinished(!toMarkDone.isFinished());
		
		this._undoStack.push(cmd);
		this._dataManager.insertDataToFile(getNoNullArr());
	}
	
	public void exeSort(){
		//TODO
		this._dataManager.insertDataToFile(getNoNullArr());
	}
	
	/**
	 * @author Phang Chun Rong
	 */
	public void exeLinkGoogle() {
		this._dataManager.loginGoogle();
	}
	
	public void exeExit(){
		//TODO
		this._dataManager.insertDataToFile(getNoNullArr());
	}
	
	//--------------------------------------------------------------------------
	//helper
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
	
	private void assertCommandNotNull(Command cmd){
		assert(cmd != null): "Received null command";
	}
	
	public int getAmount(){
		return this._data.size();
	}
}



package calendear.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Stack;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import calendear.util.*;
import calendear.util.CMD_TYPE;
import calendear.storage.DataManager;
/**
 * 
 * @author Wu XiaoXiao
 *
 */
public class Action {
	private static final Logger log= Logger.getLogger( "Action" );
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
	
	public Action(String nameOfFile) throws ParseException {
		_undoStack = new Stack<Command>();
		_redoStack = new Stack<Command>();
		_dm = new DataManager(nameOfFile);
		_data = _dm.buildData();
	}
	
	//not using
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
	//helper for add
	private Task addWithInfo(CommandAdd c){
		Task toReturn = new Task("");// "" is a stud
		boolean[] infoList = c.getChecklist();
		Object[] newData = c.getNewInfo();
		exchangeInfo(toReturn, infoList, newData);
		return toReturn;
	}
	
	public Task exeAdd(CommandAdd c){
		assertCommandNotNull(c);
		Task addedTask = addWithoutSave(c);
		this._undoStack.push(c);
		this._dm.updateData(getNoNullArr());
		return addedTask;
	}

	private Task addWithoutSave(CommandAdd c) {
		Task addedTask = c.getTask();
		if(addedTask == null){
			addedTask = addWithInfo(c);
			assert(addedTask != null) : "task to add is null";
		}
		this._data.add(addedTask);
		c.setTask(null);
		return addedTask;
	}
	/**
	 * remove task with id
	 * @param id
	 */
	public Task exeDelete(CommandDelete c){
		assertCommandNotNull(c);
		int id = c.getIndex();
		Task t = _data.get(id);
		_data.set(id, null);
		c.setDeletedTask(t);
		_undoStack.push(c);
		_dm.updateData(getNoNullArr());
		return t;
	}
	/**[0:name][1:type][2:starttime]
		[3:endtime][4:location][5:note]
		[6:tag][7:important][8:finished]
	*/
	//helper class to exchange contents of CommandUpdate and task
	private void exchangeInfo(Task toUpdate, boolean[] infoList, Object[] newData){
		final int NAME_ID = 0;
		final int TYPE_ID = 1;
		final int STARTT_ID = 2;
		final int ENDT_ID = 3;
		final int LOCATION_ID = 4;
		final int NOTE_ID = 5;
		final int TAG_ID = 6;
		final int IMP_ID = 7;//important
		final int COMP_ID = 8;//finished
		
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
	
	private void updateInformation(CommandUpdate c, Task toUpdate){
		boolean[] infoList = c.getChecklist();
		Object[] newData = c.getNewInfo();
		exchangeInfo(toUpdate, infoList, newData);
	}
	


	public Task exeUpdate(CommandUpdate c){
		assertCommandNotNull(c);
		int changeId = c.getIndex();
		Task toUpdate = _data.get(changeId);
		updateInformation(c, toUpdate);
		_undoStack.add(c);
		this._dm.updateData(getNoNullArr());
		return toUpdate;
	}
	
	/**
	 * 
	 * @param c
	 * @return arrayList containing tasks to show, all null elements should not be shown
	 */
	public ArrayList<Task> exeDisplay(CommandDisplay c){
		assertCommandNotNull(c);
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
	
	public ArrayList<Task> exeDisplaySelectiveHelper(boolean[] toShow, Object[] searchWith){
		final int NAME_ID = 0;
		final int TYPE_ID = 1;
		final int STARTT_ID = 2;
		final int ENDT_ID = 3;
		final int LOCATION_ID = 4;
		final int NOTE_ID = 5;
		final int TAG_ID = 6;
		final int IMP_ID = 7;//important
		final int COMP_ID = 8;//finished
		
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
	
	public void exeSearch(String str){
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
				Task deleted = this._data.get(deleteIndex);
				cmdDelete.setDeletedTask(deleted);
				this._data.set(deleteIndex, null);
				break;
			case SORT:
				//TODO since after sorting the index will change, this should not happen
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
		this._dm.updateData(getNoNullArr());
		log.log(Level.FINE, "pushed previousCmd to redoStack", previousCmd);
	}
	
	/**
	 * execute each command for the user.
	 */
	public void exeRedo(){
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
		this._dm.updateData(getNoNullArr());
	}
	
	public void exeTag(CommandTag c){
		//TODO can not save previous tag
		int toTagIndex = c.getIndex();
		Task toTag = this._data.get(toTagIndex);
		toTag.setTag(c.getTagName());
		this._undoStack.push(c);
		this._dm.updateData(getNoNullArr());
	}
	
	public void exeToggleImportance(CommandMark c){//toggles importance
		int toMarkIndex = c.getIndex();
		Task toMark = this._data.get(toMarkIndex);
		toMark.markImportant(!toMark.isImportant());
		
		this._undoStack.push(c);
		this._dm.updateData(getNoNullArr());
	}
	
	public void exeToggleDone(CommandDone c){
		int toMarkDoneIndex = c.getIndex();
		Task toMarkDone = this._data.get(toMarkDoneIndex);
		toMarkDone.setIsFinished(!toMarkDone.isFinished());
		
		this._undoStack.push(c);
		this._dm.updateData(getNoNullArr());
	}
	
	public void exeSort(){
		//TODO
		this._dm.updateData(getNoNullArr());
	}
	
	public void exeExit(){
		//TODO
		this._dm.updateData(getNoNullArr());
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
	
	private void assertCommandNotNull(Command c){
		assert(c != null): "Received null command";
	}
	
	public int getAmount(){
		return this._data.size();
	}
}



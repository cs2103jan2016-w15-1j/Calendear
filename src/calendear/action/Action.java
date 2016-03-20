package calendear.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Stack;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import calendear.util.*;
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
	public Task exeAdd(CommandAdd c){
		assertCommandNotNull(c);
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
	private void updateInformation(CommandUpdate c, Task toUpdate){
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
			
			boolean[] u = c.getChecklist();//refactor to isChanged in the future
			Object[] i = c.getNewInfo();
			
			if(u[NAME_ID]){
				//name
				log.log(Level.FINE, "update name", c);
				String oldName = toUpdate.getName();
				toUpdate.setName((String)i[NAME_ID]);
				i[NAME_ID] = oldName;
			}
			if(u[TYPE_ID]){
				//type
				log.log(Level.FINE, "update type", c);
				TASK_TYPE oldType = toUpdate.getType();
				toUpdate.setType((TASK_TYPE)i[TYPE_ID]);
				i[TYPE_ID] = (Object)oldType;
			}
			if(u[STARTT_ID]){
				//start time
				log.log(Level.FINE, "update starttime", c);
				GregorianCalendar oldStartTime = toUpdate.getStartTime();
				toUpdate.setStartTime((GregorianCalendar) i[STARTT_ID]);
				i[STARTT_ID] = (Object)oldStartTime;
			}
			if(u[ENDT_ID]){
				//end time
				log.log(Level.FINE, "update endtime", c);
				GregorianCalendar oldEndTime = toUpdate.getEndTime();
				GregorianCalendar newEndTime = (GregorianCalendar) i[ENDT_ID];
				toUpdate.setEndTime(newEndTime);
				i[ENDT_ID] = (Object)oldEndTime;
			}
			if(u[LOCATION_ID]){
				String newLoc = (String)i[LOCATION_ID];
				i[LOCATION_ID] = toUpdate.getLocation();
				toUpdate.setLocation(newLoc);
			}
			if(u[NOTE_ID]){
				String newNote = (String)i[NOTE_ID];
				i[LOCATION_ID] = toUpdate.getLocation();
				toUpdate.setLocation(newNote);
			}
			if(u[TAG_ID]){
				//can be a series of tags, need to specify 
				// add/delete/replace
				String originalTag = toUpdate.getTag();
				toUpdate.setTag((String)i[TAG_ID]);
				//can not mutate tag in CommandTag yet
				//TODO
			}
			if(u[IMP_ID]){
				boolean isImportant = (boolean)i[IMP_ID];
				i[IMP_ID] = Boolean.toString(toUpdate.isImportant());
				toUpdate.markImportant(isImportant);
			}
			if(u[COMP_ID]){
				boolean isFinished = (boolean)i[COMP_ID];
				i[COMP_ID] = Boolean.toString(toUpdate.isFinished());
				toUpdate.setIsFinished(isFinished);
			}
			
			_undoStack.add(c);
			_dm.updateData(getNoNullArr());
		}catch (NullPointerException e){
			e.printStackTrace();
		}catch (ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
		}

	}

	public Task exeUpdate(CommandUpdate c){
		assertCommandNotNull(c);
		int changeId = c.getIndex();
		Task toUpdate = _data.get(changeId);
		
		updateInformation(c, toUpdate);
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
	
	public void exeSearch(String str){
		//TODO
	}
	
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
				log.log(Level.SEVERE, previousCmd.toString(), previousCmd);
				throw new AssertionError(cmdType);
		}
		_redoStack.push(previousCmd);
		this._dm.updateData(getNoNullArr());
		log.log(Level.FINE, "pushed previousCmd to redoStack", previousCmd);
	}
	
	public void exeTag(){
		this._dm.updateData(getNoNullArr());
	}
	
	public void exeMark(){
		this._dm.updateData(getNoNullArr());
	}
	
	public void exeSort(){
		this._dm.updateData(getNoNullArr());
	}
	
	public void exeExit(){
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
}



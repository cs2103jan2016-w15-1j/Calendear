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
 *12. exeLinkGoogle:
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
	
	final String TAG_SEPARATOR = " # ";
	
	//constructor

	/**
	 * constructor, takes in nameOfFile to store data in, nameOfFile should end with .txt
	 * @param nameOfFile
	 * @throws ParseException
	 */
	public Action(String nameOfFile) throws ParseException {
		this._undoStack = new Stack<Command>();
		this._redoStack = new Stack<Command>();
		this._dataManager = new DataManager(nameOfFile);
		this._data = _dataManager.getDataFromFile();
		this._data.add(0, null);
	}

	/**
	 * helper,
	 * returns the task that was added.
	 * id is arrayList.size() - 1
	 * @param cmd
	 * @return task to be added
	 */

	private Task addWithInfo(CommandAdd cmd){
		Task toReturn = new Task();
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
	 * helper,
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
		Task taskToDelete = this._data.get(id);
		if(this._dataManager.isLogined() && taskToDelete.getEventId() != null){
			this._dataManager.deleteTaskFromGoogle(taskToDelete);
			taskToDelete.setEventId(null);
		}
		this._data.set(id, null);
		cmd.setDeletedTask(taskToDelete);
		this._undoStack.push(cmd);
		this._dataManager.insertDataToFile(getNoNullArr());
			
		return taskToDelete;
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
				String newTag = (String) newData[TAG_ID];
				toUpdate.setTag(toUpdate.getTag() + TAG_SEPARATOR + newTag);
			}
			if(infoList[IMP_ID]){
				boolean isImportant = (boolean)newData[IMP_ID];
				newData[IMP_ID] = toUpdate.isImportant();
				toUpdate.setIsImportant(isImportant);
			}
			if(infoList[COMP_ID]){
				boolean isFinished = (boolean)newData[COMP_ID];
				newData[COMP_ID] = toUpdate.isFinished();
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
		updateInformation(cmd, toUpdate);
		if(this._dataManager.isLogined() && toUpdate.getEventId() != null){
			this._dataManager.updateTaskToGoogle(toUpdate);
		}
		_undoStack.add(cmd);
		this._dataManager.insertDataToFile(getNoNullArr());
		return toUpdate;
	}
	
	/**
	 * only display tasks which are not finished
	 * arrayList containing tasks to show, all null elements should not be shown
	 * @param cmd
	 * @return ArrayList<Task>
	 */
	public ArrayList<Task> exeDisplay(CommandDisplay cmd){
		assertCommandNotNull(cmd);
		ArrayList<Task> arr = new ArrayList<Task>();
		arr.addAll(this._data);
		if(cmd.isOnlyImportantDisplayed()){
			return filterWithImportance(true, arr);
		}else{
			return arr;
		}
	}
	/**
	 * filters _data for tasks to be displayed according to toShow and searchWith
	 * @param toShow
	 * @param searchWith
	 * @return ArrayList<Task>
	 */
	private ArrayList<Task> displaySelectiveHelper(boolean[] toShow, Object[] searchWith){
		
		ArrayList<Task> show = new ArrayList<Task>();
		show.addAll(this._data);
		
		for(int i = 0; i<show.size(); i++){
			Task task = show.get(i);
			try{
				if(toShow[NAME_ID] && !task.getName().contains((String)searchWith[NAME_ID])){
					task = null;
				}
				if(toShow[TYPE_ID] && !task.getType().equals((TASK_TYPE)searchWith[TYPE_ID])){
					task = null;
				}
				if(toShow[STARTT_ID]){
					GregorianCalendar[] comparingWith = (GregorianCalendar[])searchWith[STARTT_ID];
					assert(comparingWith.length == 2): "length of startTime comparision not 2\n";
					isStartTimeWithinRange(task, comparingWith[0], comparingWith[1]);
				}
				if(toShow[ENDT_ID]){
					GregorianCalendar[] comparingWith = (GregorianCalendar[])searchWith[ENDT_ID];
					assert(comparingWith.length == 2): "length of startTime comparision not 2\n";
					isEndTimeWithinRange(task, comparingWith[0], comparingWith[1]);
				}
				if(toShow[LOCATION_ID] &&!(task.getLocation().contains((String) searchWith[LOCATION_ID]))){
					task = null;
				
				}
				if(toShow[NOTE_ID] && !task.getNote().contains((String) searchWith[NOTE_ID])){
					task = null;
				}
				if(toShow[TAG_ID]){
					String[] tagList = task.getTag().split(TAG_SEPARATOR);
					boolean isTagged = false;
					for(int j = 0; j<tagList.length ;j++ ){
						if(tagList[i].equalsIgnoreCase((String) searchWith[TAG_ID])){
							isTagged = true;
							break;
						}
					}
					if(!isTagged){
						task = null;
					}
				}
				if(toShow[IMP_ID] && !(task.isImportant() == (boolean)searchWith[IMP_ID])){
					task = null;
				}
				if(toShow[COMP_ID] && !(task.isFinished() == (boolean)searchWith[COMP_ID])){
					task = null;
				}
			}catch (NullPointerException e){
				e.printStackTrace();
			}catch (ArrayIndexOutOfBoundsException e){
				e.printStackTrace();
			}
		}
		
		return show;
	}
	
	private boolean isStartTimeWithinRange(Task currentTask, GregorianCalendar start, GregorianCalendar end){
		if(currentTask != null 
				&& currentTask.getStartTime().compareTo(start) <= 0//before start
				&& currentTask.getStartTime().compareTo(end) >= 0){//after end
			currentTask = null;
			return false;
		}
		return true;
	}
	
	private boolean isEndTimeWithinRange(Task currentTask, GregorianCalendar start, GregorianCalendar end){
			if(currentTask != null 
					&& currentTask.getEndTime().compareTo(start) <= 0//before start
					&& currentTask.getEndTime().compareTo(end) >= 0){//after end
				currentTask = null;
				return false;
			}
		return true;
	}
	
	/**
	 * returns arraylist with !<isImportant> importance tasks as null
	 * @param isImportant
	 * @param dataToFilter
	 * @return
	 */
	private ArrayList<Task> filterWithImportance(boolean isImportant, ArrayList<Task> dataToFilter){
		ArrayList<Task> toDisplay = new ArrayList<Task>();
		toDisplay.addAll(dataToFilter);
		for(int i = 0; i<toDisplay.size(); i++){
			if(toDisplay.get(i) != null && toDisplay.get(i).isImportant() != isImportant){
				toDisplay.set(i, null);
			}
		}
		return toDisplay;
	}
	
	public ArrayList<Task> exeDisplayImportant(){
		return filterWithImportance(true, this._data);
	}
	
	public ArrayList<Task> exeDisplayNotImportant(){
		return filterWithImportance(false, this._data);
	}

	public ArrayList<Task> exeSearch(CommandSearch cmd){
		assertCommandNotNull(cmd);
		boolean[] toShow = cmd.getArrToShow();
		Object[] searchWith = cmd.getArrSearchWith();
		return displaySelectiveHelper(toShow, searchWith);
	}
	
	/**
	 * replaces !<isDone> completeness tasks with null
	 * @param isDone
	 * @param dataToFilter
	 * @return
	 */
	private ArrayList<Task> filterWithCompleteness(boolean isDone, ArrayList<Task> dataToFilter){
		ArrayList<Task> toDisplay = new ArrayList<Task>();
		toDisplay.addAll(dataToFilter);
		for(int i = 0; i<toDisplay.size(); i++){
			if(toDisplay.get(i) != null && toDisplay.get(i).isFinished() != isDone){
				toDisplay.set(i, null);
			}
		}
		return toDisplay;
	}
	
	public ArrayList<Task> exeDisplayNotDone(){
		return filterWithCompleteness(false, this._data);
	}	
	
	public ArrayList<Task> exeDisplayDone(){
		return filterWithCompleteness(true, this._data);
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
	
	public boolean exeUndo(){
		try{
			Command previousCmd = this._undoStack.pop();
			CMD_TYPE cmdType = previousCmd.getType();
			switch(cmdType){
				case ADD:
					log.log(Level.FINE, "undo add", previousCmd);
					CommandAdd cmdAdd = (CommandAdd) previousCmd;//type casting, we are sure that cmdType == ADD
					int lastIndex = this._data.size()-1;
					
					Task toBeRemoved = this._data.get(lastIndex);
					this._data.remove(lastIndex);//removed from _data ArrayList
					cmdAdd.setTask(toBeRemoved);//commandAdd in _redoStack will always contain the task.
					
					previousCmd = cmdAdd;//typecast back to be added to _redoStack
					
					if(this._dataManager.isLogined() && toBeRemoved.getEventId() != null){
						this._dataManager.deleteTaskFromGoogle(toBeRemoved);
					}
					
					break;
				case UPDATE:
					log.log(Level.FINE, "undo update", previousCmd);
					CommandUpdate cmdUpdate = (CommandUpdate) previousCmd;
					Task toUndoUpdate = this._data.get(cmdUpdate.getIndex());
					
					updateInformation(cmdUpdate, toUndoUpdate);
					
					previousCmd = cmdUpdate;
					
					if(this._dataManager.isLogined() && toUndoUpdate.getEventId() != null){
						this._dataManager.updateTaskToGoogle(toUndoUpdate);
					}
					
					break;
				case DELETE:
					log.log(Level.FINE, "undo delete", previousCmd);
					CommandDelete cmdDelete = (CommandDelete) previousCmd;
					int deleteIndex = cmdDelete.getIndex();
					Task toAddBack = cmdDelete.getDeletedTask();
					
					this._data.set(deleteIndex, toAddBack);
					
					if(this._dataManager.isLogined() && toAddBack.getEventId() != null){
						this._dataManager.addTaskToGoogle(toAddBack);
					}
					break;
				case MARK:
					log.log(Level.FINE, "undo mark", previousCmd);
					CommandMark cmdMark = (CommandMark) previousCmd;
					int markIndex = cmdMark.getIndex();
					Task toUndoMark = this._data.get(markIndex);
					
					boolean isImportantBefore = cmdMark.isImportant();
					boolean isCurrentlyImportant = toUndoMark.isImportant();
					
					toUndoMark.setIsImportant(isImportantBefore);//toggles importance
					cmdMark.setIsImportant(isCurrentlyImportant);
					
					previousCmd = cmdMark;
					
					if(this._dataManager.isLogined() && toUndoMark.getEventId() != null){
						this._dataManager.updateTaskToGoogle(toUndoMark);
					}
					break;
				case DONE:
					log.log(Level.FINE, "undo done", previousCmd);
					CommandDone cmdDone = (CommandDone) previousCmd;
					int doneIndex = cmdDone.getIndex();
					Task toUndoDone = this._data.get(doneIndex);
					
					boolean isOriginallyFinished = cmdDone.isDone();
					boolean isCurrentlyFinished = toUndoDone.isFinished();
					
					toUndoDone.setIsFinished(isOriginallyFinished);
					cmdDone.setIsDone(isCurrentlyFinished);
					
					previousCmd = cmdDone;

					if(this._dataManager.isLogined() && toUndoDone.getEventId() != null){
						this._dataManager.updateTaskToGoogle(toUndoDone);
					}
					break;
				case TAG:
					log.log(Level.FINE, "undo tag", previousCmd);
					CommandTag cmdTag = (CommandTag) previousCmd;
					int tagIndex = cmdTag.getIndex();
					Task toUndoTag = this._data.get(tagIndex);
					
					removeLastTag(toUndoTag);
					
					previousCmd = cmdTag;
					
					if(this._dataManager.isLogined() && toUndoTag.getEventId() != null){
						this._dataManager.updateTaskToGoogle(toUndoTag);
					}
					break;
					
				case LOAD_FROM_GOOGLE:
					log.log(Level.FINE, "undo loadfromgoogle", previousCmd);
					CommandLoadFromGoogle cmdLoadFromGoogle = (CommandLoadFromGoogle) previousCmd;
					this._data = cmdLoadFromGoogle.getUndoList();
					break;
				default:
					log.log(Level.SEVERE, "reached unreachable area in undo", previousCmd);
					throw new AssertionError(cmdType);
			}
			_redoStack.push(previousCmd);
			this._dataManager.insertDataToFile(getNoNullArr());
			log.log(Level.FINE, "pushed previousCmd to redoStack", previousCmd);
			return true;
		}
		catch (EmptyStackException e){
			return false;
		}
	}
	
	/**
	 * execute each command for the user.
	 */
	public boolean exeRedo(){
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
				case MARK:
					log.log(Level.FINE, "redo Importance", redoCmd);
					CommandMark cmdMark = (CommandMark) redoCmd;
					exeMarkImportance(cmdMark);
					break;
				case DONE:
					log.log(Level.FINE, "redo finished", redoCmd);
					CommandDone cmdDone = (CommandDone) redoCmd;
					exeMarkDone(cmdDone);
					break;
				case TAG:
					log.log(Level.FINE, "redo tag finished", redoCmd);
					CommandTag cmdTag = (CommandTag) redoCmd;
					exeTag(cmdTag);
					break;
				case LOAD_FROM_GOOGLE:
					
					log.log(Level.FINE, "redo loadtogoogle", redoCmd);
					CommandLoadFromGoogle cmdLoadFromGoogle = (CommandLoadFromGoogle) redoCmd;
					exeLoadTasksFromGoogle(cmdLoadFromGoogle);
					break;
				default:
					log.log(Level.SEVERE, "reached unreachable area in redo", redoCmd);
					throw new AssertionError(redoCmd);
			}
			this._dataManager.insertDataToFile(getNoNullArr());
			return true;
		}
		
		catch (EmptyStackException e){
			return false;
		}
	}
	
	public Task exeTag(CommandTag cmd){
		int toTagIndex = cmd.getIndex();
		Task toTag = this._data.get(toTagIndex);
		if(toTag.getTag() == null){
			toTag.setTag(cmd.getTagName());
		}else{
			toTag.setTag(toTag.getTag() + " # " + cmd.getTagName());
		}
		this._undoStack.push(cmd);
		this._dataManager.insertDataToFile(getNoNullArr());
		return toTag;
	}
	
	public Task exeMarkImportance(CommandMark cmd){//toggles importance
		int toMarkIndex = cmd.getIndex();
		Task toMark = this._data.get(toMarkIndex);
		boolean originalImportance = toMark.isImportant();
		toMark.setIsImportant(cmd.isImportant());
		cmd.setIsImportant(originalImportance);
		if(this._dataManager.isLogined() && toMark.getEventId() != null){
			this._dataManager.updateTaskToGoogle(toMark);
		}
		this._undoStack.push(cmd);
		this._dataManager.insertDataToFile(getNoNullArr());
		return toMark;
	}

	public Task exeMarkDone(CommandDone cmd){

		int toMarkDoneIndex = cmd.getIndex();
		Task toMarkDone = this._data.get(toMarkDoneIndex);
		boolean isOriginallyDone = toMarkDone.isFinished();
		toMarkDone.setIsFinished(cmd.isDone());
		cmd.setIsDone(isOriginallyDone);
		if(this._dataManager.isLogined() && toMarkDone.getEventId() != null){
			this._dataManager.updateTaskToGoogle(toMarkDone);
		}
		this._undoStack.push(cmd);
		this._dataManager.insertDataToFile(getNoNullArr());

		return toMarkDone;
	}
	
	
	private void exeAddAllToGoogle(){
		assert(this._dataManager.isLogined()): "called exeAddAllToGoogle without logging in\n";

		for(int i = 1; i<this._data.size(); i++){
			Task currentTask = this._data.get(i);
			if(currentTask != null 
					&& (currentTask.getEventId().equals(null) || currentTask.getEventId().equals("null"))){
				System.out.println("Trying to Add: " + i);
				String newEventId = this._dataManager.addTaskToGoogle(currentTask);
				currentTask.setEventId(newEventId);
			}
		}
		this._dataManager.insertDataToFile(getNoNullArr());
	}

	public ArrayList<Task> exeLoadTasksFromGoogle(CommandLoadFromGoogle cmd){
		if(!this._dataManager.isLogined()){
			System.out.println("user not logged in");
			return new ArrayList<Task>();
		}
		ArrayList<Task> originalTaskList = new ArrayList<Task>(this._data);
		cmd.setUndoList(originalTaskList);
		ArrayList<Task> loadedTasks = this._dataManager.getTasksFromGoogle();
		for(int i = 0; i< this._data.size(); i++){
			Task currentTask = this._data.get(i);
			for(int j = 0; j< loadedTasks.size(); j++){
				Task taskFromGoogle = loadedTasks.get(j);
				if(currentTask != null && currentTask.getEventId().equals(taskFromGoogle.getEventId())){
					currentTask.setName(taskFromGoogle.getName());
					currentTask.setStartTime(taskFromGoogle.getStartTime());
					currentTask.setEndTime(taskFromGoogle.getEndTime());
					loadedTasks.remove(j);
					j--;
					break;
				}
			}
		}
		this._data.addAll(loadedTasks);
		this._dataManager.insertDataToFile(getNoNullArr());
		this._undoStack.push(cmd);
		return this._data;
	}

	
	/**
	 * @author Phang Chun Rong
	 */
	public void exeLinkGoogle() {
		if (!this._dataManager.isLogined()) {
			this._dataManager.loginGoogle();
		}
		
		try {
			Thread.sleep(300);
		}
		catch (InterruptedException ex) {
			System.out.println(ex);
		}
		
		if (this._dataManager.isLogined()) {
			exeAddAllToGoogle();
		}
	}
	
	
	//--------------------------------------------------------------------------
	//helper
	/**
	 * returns an array of data with null objects removed
	 * @return ArrayList<Task>
	 */
	private ArrayList<Task> getNoNullArr(){
		ArrayList<Task> toReturn = new ArrayList<Task>();
		Task t=  null;
		for(int i = 0; i<this._data.size(); i++){
			t = this._data.get(i);
			if(t != null){
				toReturn.add(t);
			}
		}
		return toReturn;
	}
	
	/**
	 * assert that cmd is not null
	 * @param cmd
	 */
	private void assertCommandNotNull(Command cmd){
		assert(cmd != null): "Received null command";
	}
	/**
	 * 
	 * @return the size of _data
	 */
	public int getAmount(){
		return this._data.size();
	}
	
	private void removeLastTag(Task taggedTask){
		String[] tags = taggedTask.getTag().split(TAG_SEPARATOR);
		String newTag = new String();
		for(int i = 0; i<tags.length-2; i++){
			newTag += tags[i] + TAG_SEPARATOR;
		}
		newTag += tags[tags.length-2];
		taggedTask.setTag(newTag);
	}
	
	public void showData(){
		for(int i = 0; i< this._data.size(); i++){
			if(this._data.get(i) == null){
				System.out.println("null");
			}else{
				System.out.println(this._data.get(i).getName());
			}
		}
	}
	
	
}



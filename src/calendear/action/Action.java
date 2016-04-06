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
 * Class for taking care of main logic
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
 *12. exeLinkGoogle: done and functioning
 *13. exeClear: in construction
 */


public class Action {
	private static final int DATA_START_INDEX = 1;
	private static final String CLASS_NAME = "Action";
	private static final Logger log= Logger.getLogger( CLASS_NAME );
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
	 * @param commandAdd
	 * @return task to be added
	 */

	private Task addWithInfo(CommandAdd commandAdd){
		Task toReturn = new Task();
		boolean[] infoList = commandAdd.getChecklist();
		Object[] newData = commandAdd.getNewInfo();
		exchangeInfo(toReturn, infoList, newData);
		return toReturn;
	}
	
	/**
	 * called by CDLogic
	 * @param commandAdd
	 * @return task added to storage
	 */
	public Task exeAdd(CommandAdd commandAdd){
		assertCommandNotNull(commandAdd);
		String eventId; 
		Task addedTask = addWithoutSave(commandAdd);
		if(this._dataManager.isLogined()){
			eventId = this._dataManager.addTaskToGoogle(addedTask);
			addedTask.setEventId(eventId);
		}
		this._undoStack.push(commandAdd);
		this._dataManager.insertDataToFile(dataWithNullRemoved());
		this._redoStack.clear();
		return addedTask;
	}
	/**
	 * helper,
	 * add to _data, but not storage
	 * @param commandAdd
	 * @return added task
	 */
	private Task addWithoutSave(CommandAdd commandAdd) {
		Task addedTask = commandAdd.getTask();
		if(addedTask == null){
			addedTask = addWithInfo(commandAdd);
			assert(addedTask != null) : "task to add is null";
		}
		this._data.add(addedTask);
		commandAdd.setTask(null);
		return addedTask;
	}
	/**
	 * delete task with id
	 * @param commandDelete
	 * @return
	 */
	public Task exeDelete(CommandDelete commandDelete){
		assertCommandNotNull(commandDelete);
		int id = commandDelete.getIndex();
		Task taskToDelete = this._data.get(id);
		if(this._dataManager.isLogined() && taskToDelete.getEventId() != null){
			this._dataManager.deleteTaskFromGoogle(taskToDelete);
			taskToDelete.setEventId(null);
		}
		this._data.set(id, null);
		commandDelete.setDeletedTask(taskToDelete);
		this._undoStack.push(commandDelete);
		this._dataManager.insertDataToFile(dataWithNullRemoved());
		this._redoStack.clear();
			
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
				toUpdate.setType(TASK_TYPE.EVENT);// only even tasks have start time
			}
			if(infoList[ENDT_ID]){
				//end time
				GregorianCalendar oldEndTime = toUpdate.getEndTime();
				toUpdate.setEndTime((GregorianCalendar) newData[ENDT_ID]);
				newData[ENDT_ID] = (Object)oldEndTime;
				if(toUpdate.getType() == null || toUpdate.getType().equals(TASK_TYPE.FLOATING)){//originally float now deadline
					toUpdate.setType(TASK_TYPE.DEADLINE);
				}
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
	 * @param commandUpdate
	 * @param toUpdate
	 */
	private void updateInformation(CommandUpdate commandUpdate, Task toUpdate){
		boolean[] infoList = commandUpdate.getChecklist();
		Object[] newData = commandUpdate.getNewInfo();
		exchangeInfo(toUpdate, infoList, newData);
	}
	

	/**
	 * executes cmd (a command update)
	 * @param commandUpdate
	 * @return
	 */
	public Task exeUpdate(CommandUpdate commandUpdate){
		assertCommandNotNull(commandUpdate);
		int changeId = commandUpdate.getIndex();
		Task toUpdate = _data.get(changeId);
		updateInformation(commandUpdate, toUpdate);
		if(this._dataManager.isLogined() && toUpdate.getEventId() != null){
			this._dataManager.updateTaskToGoogle(toUpdate);
		}
		_undoStack.add(commandUpdate);
		this._dataManager.insertDataToFile(dataWithNullRemoved());
		this._redoStack.clear();
		return toUpdate;
	}
	
	/**
	 * only display tasks which are not finished
	 * arrayList containing tasks to show, all null elements should not be shown
	 * @param commandDisplay
	 * @return ArrayList<Task>
	 */
	public ArrayList<Task> exeDisplay(CommandDisplay commandDisplay){
		assertCommandNotNull(commandDisplay);
		ArrayList<Task> toDisplay = new ArrayList<Task>();
		toDisplay.addAll(this._data);
		if(commandDisplay.isOnlyImportantDisplayed()){
			return filterWithImportance(true, toDisplay);
		}else{
			return toDisplay;
		}
	}
	/**
	 * filters _data for tasks to be displayed according to toShow and searchWith
	 * @param toShow
	 * @param searchWith
	 * @return ArrayList<Task>
	 */
	private ArrayList<Task> displaySelectiveHelper(boolean[] toShow, Object[] searchWith){
		
		ArrayList<Task> toDisplay = new ArrayList<Task>();
		toDisplay.addAll(this._data);
		
		for(int i = 0; i<toDisplay.size(); i++){
			Task task = toDisplay.get(i);
			if(task == null){
				continue;
			}
			try{
				if(toShow[NAME_ID] && !withinDistance(task.getName(), (String)searchWith[NAME_ID])){
					toDisplay.set(i, null);
					continue;
				}
				if(toShow[TYPE_ID] && !task.getType().equals((TASK_TYPE)searchWith[TYPE_ID])){
					toDisplay.set(i, null);
					continue;
				}
				if(toShow[STARTT_ID]){
					GregorianCalendar comparingWithStart = (GregorianCalendar)searchWith[STARTT_ID];
					assert(comparingWithStart != null) : "start time null";
					if(!isStartTimeWithinRange(task, comparingWithStart)){
						toDisplay.set(i, null);
						continue;
					}
				}
				if(toShow[ENDT_ID]){
					GregorianCalendar comparingWithEnd = (GregorianCalendar)searchWith[ENDT_ID];
					assert(comparingWithEnd != null) : "end time null";
					if(!isEndTimeWithinRange(task, comparingWithEnd)){
						toDisplay.set(i, null);
						continue;
					}
				}
				if(toShow[LOCATION_ID] &&!withinDistance(task.getLocation(), (String)searchWith[LOCATION_ID])){
					toDisplay.set(i, null);
					continue;
				
				}
				if(toShow[NOTE_ID] && !withinDistance(task.getNote(), (String)searchWith[NOTE_ID])){
					toDisplay.set(i, null);
					continue;
				}
				if(toShow[TAG_ID]){
					if(task.getTag() == null){
						toDisplay.set(i, null);
					}else{
						String[] tagList = task.getTag().split(TAG_SEPARATOR);
						boolean isTagged = false;
						for(int j = 0; j<tagList.length ;j++ ){
							if(tagList[j].equalsIgnoreCase(((String) searchWith[TAG_ID]).trim())){
								isTagged = true;
								break;
							}
						}
						if(!isTagged){
							toDisplay.set(i, null);
							continue;
						}
					}
				}
				if(toShow[IMP_ID] && !(task.isImportant() == (boolean)searchWith[IMP_ID])){
					toDisplay.set(i, null);
					continue;
				}
				if(toShow[COMP_ID] && !(task.isFinished() == (boolean)searchWith[COMP_ID])){
					toDisplay.set(i, null);
				}
			}catch (NullPointerException e){
				e.printStackTrace();
			}catch (ArrayIndexOutOfBoundsException e){
				e.printStackTrace();
			}
		}
		
		return toDisplay;
	}
	
	private boolean isStartTimeWithinRange(Task task, GregorianCalendar startTimeToCompare){
		if(task == null){
			return false;
		}
		
		if(task.getStartTime() == null){
			if(task.getType() != null && task.getType().equals(TASK_TYPE.DEADLINE)){
				return true;//no one searches for deadline tasks with start time
			}else{
				return false;
			}
		}
		
		if(task.getStartTime().compareTo(startTimeToCompare) >= 0){
			return true;
		} 
		return false;
	}
	
	private boolean isEndTimeWithinRange(Task task, GregorianCalendar endTimeToCompare){
		if(task == null || task.getEndTime() == null){
			return false;
		}
		if(task.getEndTime().compareTo(endTimeToCompare) <= 0){
			return true;
		}
		return false;
	}
	
	private boolean withinDistance(String str1, String str2){
		String splitWith = " ";
		if(str1 == null || str2 == null){
			return false;
		}
		String[] strArr1 = str1.split(splitWith);
		String[] strArr2 = str2.split(splitWith);
		
		for(int i = 0; i<strArr1.length; i++){
			for(int j = 0; j<strArr2.length; j++){
				if(EditDistance.computeEditDistance(strArr1[i].trim(), strArr2[j].trim()) <= 2){
					return true;
				}
			}
		}
		return false;
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

	public ArrayList<Task> exeSearch(CommandSearch commandSearch){
		assertCommandNotNull(commandSearch);
		boolean[] toShow = commandSearch.getArrToShow();
		Object[] searchWith = commandSearch.getArrSearchWith();
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
			Command commandToUndo = this._undoStack.pop();
			CMD_TYPE commandType = commandToUndo.getType();
			switch(commandType){
				case ADD:
					log.log(Level.FINE, "undo add", commandToUndo);
					CommandAdd commandAdd = (CommandAdd) commandToUndo;//type casting, we are sure that cmdType == ADD
					int lastIndex = this._data.size()-1;
					
					Task toBeRemoved = this._data.get(lastIndex);
					this._data.remove(lastIndex);//removed from _data ArrayList
					commandAdd.setTask(toBeRemoved);//commandAdd in _redoStack will always contain the task.
					
					commandToUndo = commandAdd;//typecast back to be added to _redoStack
					
					if(this._dataManager.isLogined() && toBeRemoved.getEventId() != null){
						this._dataManager.deleteTaskFromGoogle(toBeRemoved);
					}
					
					break;
				case UPDATE:
					log.log(Level.FINE, "undo update", commandToUndo);
					CommandUpdate commandUpdate = (CommandUpdate) commandToUndo;
					Task toUndoUpdate = this._data.get(commandUpdate.getIndex());
					
					updateInformation(commandUpdate, toUndoUpdate);
					
					commandToUndo = commandUpdate;
					
					if(this._dataManager.isLogined() && toUndoUpdate.getEventId() != null){
						this._dataManager.updateTaskToGoogle(toUndoUpdate);
					}
					
					break;
				case DELETE:
					log.log(Level.FINE, "undo delete", commandToUndo);
					CommandDelete commandDelete = (CommandDelete) commandToUndo;
					int deleteIndex = commandDelete.getIndex();
					Task toAddBack = commandDelete.getDeletedTask();
					
					this._data.set(deleteIndex, toAddBack);
					
					if(this._dataManager.isLogined() && toAddBack.getEventId() != null){
						this._dataManager.addTaskToGoogle(toAddBack);
					}
					break;
				case MARK:
					log.log(Level.FINE, "undo mark", commandToUndo);
					CommandMark commandMark = (CommandMark) commandToUndo;
					int markIndex = commandMark.getIndex();
					Task toUndoMark = this._data.get(markIndex);
					
					boolean isImportantBefore = commandMark.isImportant();
					boolean isCurrentlyImportant = toUndoMark.isImportant();
					
					toUndoMark.setIsImportant(isImportantBefore);
					commandMark.setIsImportant(isCurrentlyImportant);
					
					commandToUndo = commandMark;
					
					if(this._dataManager.isLogined() && toUndoMark.getEventId() != null){
						this._dataManager.updateTaskToGoogle(toUndoMark);
					}
					break;
				case DONE:
					log.log(Level.FINE, "undo done", commandToUndo);
					CommandDone commandDone = (CommandDone) commandToUndo;
					int doneIndex = commandDone.getIndex();
					Task toUndoDone = this._data.get(doneIndex);
					
					boolean isOriginallyFinished = commandDone.isDone();
					boolean isCurrentlyFinished = toUndoDone.isFinished();
					
					toUndoDone.setIsFinished(isOriginallyFinished);
					commandDone.setIsDone(isCurrentlyFinished);
					
					commandToUndo = commandDone;

					if(this._dataManager.isLogined() && toUndoDone.getEventId() != null){
						this._dataManager.updateTaskToGoogle(toUndoDone);
					}
					break;
				case TAG:
					log.log(Level.FINE, "undo tag", commandToUndo);
					CommandTag cmdTag = (CommandTag) commandToUndo;
					int tagIndex = cmdTag.getIndex();
					Task toUndoTag = this._data.get(tagIndex);
					
					removeLastTag(toUndoTag);
					
					commandToUndo = cmdTag;
					
					if(this._dataManager.isLogined() && toUndoTag.getEventId() != null){
						this._dataManager.updateTaskToGoogle(toUndoTag);
					}
					break;
					
				case LOAD_FROM_GOOGLE:
					log.log(Level.FINE, "undo loadfromgoogle", commandToUndo);
					CommandLoadFromGoogle commandLoadFromGoogle = (CommandLoadFromGoogle) commandToUndo;
					this._data = commandLoadFromGoogle.getUndoList();
					break;
					
				case CLEAR:
					log.log(Level.FINE, "undo clear", commandToUndo);
					CommandClear commandClear = (CommandClear) commandToUndo;
					this._data = commandClear.getListBeforeClear();
					break;
				default:
					log.log(Level.SEVERE, "reached unreachable area in undo", commandToUndo);
					throw new AssertionError(commandType);
			}
			_redoStack.push(commandToUndo);
			this._dataManager.insertDataToFile(dataWithNullRemoved());
			log.log(Level.FINE, "pushed previousCmd to redoStack", commandToUndo);
			return true;
		}
		catch (EmptyStackException e){
			System.out.println("nothing to undo");
			return false;
		}
	}
	
	/**
	 * execute each command for the user.
	 */
	public boolean exeRedo(){
		try{
			Command commandToRedo = this._redoStack.pop();
			CMD_TYPE commandType = commandToRedo.getType();
			
			switch (commandType){
				case ADD:
					log.log(Level.FINE, "redo add", commandToRedo);
					CommandAdd commandAdd = (CommandAdd) commandToRedo;
					assert(commandAdd.getTask() != null);//cmdAdd always contains the task
					exeAdd(commandAdd);
					break;
				case UPDATE:
					log.log(Level.FINE, "redo update", commandToRedo);
					CommandUpdate commandUpdate = (CommandUpdate) commandToRedo;
					exeUpdate(commandUpdate);
					break;
				case DELETE:
					log.log(Level.FINE, "redo delete", commandToRedo);
					CommandDelete commandDelete = (CommandDelete) commandToRedo;
					exeDelete(commandDelete);
					break;
				case MARK:
					log.log(Level.FINE, "redo Importance", commandToRedo);
					CommandMark commandMark = (CommandMark) commandToRedo;
					exeMarkImportance(commandMark);
					break;
				case DONE:
					log.log(Level.FINE, "redo finished", commandToRedo);
					CommandDone commandDone = (CommandDone) commandToRedo;
					exeMarkDone(commandDone);
					break;
				case TAG:
					log.log(Level.FINE, "redo tag finished", commandToRedo);
					CommandTag commandTag = (CommandTag) commandToRedo;
					exeTag(commandTag);
					break;
				case LOAD_FROM_GOOGLE:
					
					log.log(Level.FINE, "redo loadtogoogle", commandToRedo);
					CommandLoadFromGoogle commandLoadFromGoogle = (CommandLoadFromGoogle) commandToRedo;
					exeLoadTasksFromGoogle(commandLoadFromGoogle);
					break;
				case CLEAR:
					
					log.log(Level.FINE, "redo clear", commandToRedo);
					CommandClear commandClear = (CommandClear) commandToRedo;
					exeClear(commandClear);
					break;
					
				default:
					log.log(Level.SEVERE, "reached unreachable area in redo", commandToRedo);
					throw new AssertionError(commandToRedo);
			}
			this._dataManager.insertDataToFile(dataWithNullRemoved());
			log.log(Level.FINE, "redo", commandToRedo);
			return true;
		}
		
		catch (EmptyStackException e){
			System.out.println("nothing to undo");
			return false;
		}
		
		catch (LogicException e){
			return false;
		}
	}
	
	public Task exeTag(CommandTag commandTag){
		int toTagIndex = commandTag.getIndex();
		Task taskToTag = this._data.get(toTagIndex);
		String tag = taskToTag.getTag();
		if(tag == null || tag.equals("")){
			taskToTag.setTag(commandTag.getTagName());
		}else{
			taskToTag.setTag(taskToTag.getTag() + " # " + commandTag.getTagName());
		}
		this._undoStack.push(commandTag);
		this._dataManager.insertDataToFile(dataWithNullRemoved());
		this._redoStack.clear();
		return taskToTag;
	}
	
	public Task exeMarkImportance(CommandMark commandMark){//marks importance
		int toMarkIndex = commandMark.getIndex();
		Task taskToMark = this._data.get(toMarkIndex);
		boolean originalImportance = taskToMark.isImportant();
		taskToMark.setIsImportant(commandMark.isImportant());
		commandMark.setIsImportant(originalImportance);
		if(this._dataManager.isLogined() && taskToMark.getEventId() != null){
			this._dataManager.updateTaskToGoogle(taskToMark);
		}
		this._undoStack.push(commandMark);
		this._dataManager.insertDataToFile(dataWithNullRemoved());
		this._redoStack.clear();
		return taskToMark;
	}

	public Task exeMarkDone(CommandDone commandDone){
		
		int toMarkDoneIndex = commandDone.getIndex();
		Task taskToMarkDone = this._data.get(toMarkDoneIndex);
		boolean isOriginallyDone = taskToMarkDone.isFinished();
		taskToMarkDone.setIsFinished(commandDone.isDone());
		commandDone.setIsDone(isOriginallyDone);
		if(this._dataManager.isLogined() && taskToMarkDone.getEventId() != null){
			this._dataManager.updateTaskToGoogle(taskToMarkDone);
		}
		this._undoStack.push(commandDone);
		this._dataManager.insertDataToFile(dataWithNullRemoved());
		this._redoStack.clear();
		
		return taskToMarkDone;
	}
	
	
	private void exeAddAllToGoogle(){
		assert(this._dataManager.isLogined()): "called exeAddAllToGoogle without logging in\n";

		for(int i = DATA_START_INDEX; i<this._data.size(); i++){
			Task currentTask = this._data.get(i);
			if(currentTask != null 
					&& (currentTask.getEventId() == null || currentTask.getEventId().equals("null"))){
				System.out.println("Trying to Add: " + currentTask.getName());
				String newEventId = this._dataManager.addTaskToGoogle(currentTask);
				currentTask.setEventId(newEventId);
			}
		}
		this._dataManager.insertDataToFile(dataWithNullRemoved());
	}

	public ArrayList<Task> exeLoadTasksFromGoogle(CommandLoadFromGoogle commandLoadFromGoogle) throws LogicException{
		if(!this._dataManager.isLogined()){
			System.out.println("user not logged in");
			throw new LogicException("user is not logged in");
		}
		ArrayList<Task> originalTaskList = new ArrayList<Task>(this._data);
		commandLoadFromGoogle.setUndoList(originalTaskList);
		ArrayList<Task> loadedTasks = this._dataManager.getTasksFromGoogle();
		for(int i = DATA_START_INDEX; i< this._data.size(); i++){
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
		this._dataManager.insertDataToFile(dataWithNullRemoved());
		this._undoStack.push(commandLoadFromGoogle);
		this._redoStack.clear();
		return this._data;
	}
	
	public boolean exeClear(CommandClear commandClear){
		ArrayList<Task> listToSave = new ArrayList<Task>(this._data);
		commandClear.setBeforeList(listToSave);
		this._data.clear();
		this._dataManager.insertDataToFile(dataWithNullRemoved());
		this._undoStack.push(commandClear);
		return true;
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
			System.out.println("logging in");
			exeAddAllToGoogle();
		}
	}
	
	public String exeSaveFile(CommandSave commandSave) {
		System.out.println("Saving...");
		String path = commandSave.getPath();
		return this._dataManager.changeFilePath(path);
	}
	
	
	//--------------------------------------------------------------------------
	//helper
	/**
	 * returns an array of data with null objects removed
	 * @return ArrayList<Task>
	 */
	private ArrayList<Task> dataWithNullRemoved(){
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
	 * @param command
	 */
	private void assertCommandNotNull(Command command){
		assert(command != null): "Received null command";
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
		final int SECOND_LAST_TAG_INDEX = tags.length-2;
		
		for(int i = 0; i<SECOND_LAST_TAG_INDEX; i++){
			newTag += tags[i] + TAG_SEPARATOR;
		}
		newTag += tags[SECOND_LAST_TAG_INDEX];
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



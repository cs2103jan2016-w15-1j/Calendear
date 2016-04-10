package calendear.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import calendear.util.*;
import calendear.storage.DataManager;

/**
 * 
 * @@author Wu XiaoXiao
 * Class for taking care of main logic
 *
 */

public class Action {
	private static final int DATA_START_INDEX = 1;
	private static final String CLASS_NAME = "Action";
	private static final Logger log= Logger.getLogger( CLASS_NAME );
	
	private ArrayList<Task> _data;
	private Stack<Command> _undoStack;
	private Stack<Command> _redoStack;
	private DataManager _dataManager; 
	
	static final int NAME_ID = 0;
	static final int TYPE_ID = 1;
	static final int STARTT_ID = 2;
	static final int ENDT_ID = 3;
	static final int LOCATION_ID = 4;
	static final int NOTE_ID = 5;
	static final int TAG_ID = 6;
	static final int IMP_ID = 7;//important
	static final int COMP_ID = 8;//finished
	static final int TOTAL_LEN = 9;
	
	static final String TAG_SEPARATOR = " # ";
	static final String EMPTY_STRING = "";
	static final int TRUE_START_ID = 1;
	static final String NULL_STRING = "null";
	
	private final LogicException endTimeBeforeStartTime;
	private final LogicException tryingToModifyDeletedTask;
	private final LogicException userIsNotLoggedIn;
	
	
	//constructor

	/**
	 * a constructor to create a new Action object with nameOfFile as the file to store the new data in
	 * nameOfFile should end with .txt
	 * @param nameOfFile
	 * @throws ParseException
	 */
	public Action(String nameOfFile) throws ParseException, IOException {
		this._undoStack = new Stack<Command>();
		this._redoStack = new Stack<Command>();
		this._dataManager = new DataManager(nameOfFile);
		this._data = _dataManager.getDataFromFile();
		this._data.add(0, null);
		this.endTimeBeforeStartTime = new LogicException("Error: end time before start time");
		this.tryingToModifyDeletedTask = new LogicException("Error: trying to modify deleted task");
		this.userIsNotLoggedIn = new LogicException("user is not logged in");
	}

	/**
	 * 
	 * this function takes a commandAdd that contains no task and only information in it 
	 * and creates the task that should be added, adds the task, and returns the task
	 * @param commandAdd
	 * @return task to be added
	 */

	private Task addWithInfo(CommandAdd commandAdd){
		Task toReturn = new Task();
		boolean[] infoList = commandAdd.getChecklist();
		Object[] newData = commandAdd.getNewInfo();
		if(infoList[TYPE_ID] == false){
			infoList[TYPE_ID] = true;
			newData[TYPE_ID] = TASK_TYPE.FLOATING;
		}
		boolean isUndo = false;
		exchangeInfo(toReturn, infoList, newData, isUndo);
		return toReturn;
	}
	
	/**
	 * a public method that adds a task into the action object
	 * @param commandAdd
	 * @return task added to storage
	 */
	public Task exeAdd(CommandAdd commandAdd) throws LogicException, IOException {
		assertCommandNotNull(commandAdd);
		String eventId; 
		Task addedTask = addWithoutSave(commandAdd);
		
		GregorianCalendar startTime = addedTask.getStartTime();
		GregorianCalendar endTime = addedTask.getEndTime();
		
		if(startTime != null && endTime!= null && startTime.compareTo(endTime) > 0){
			throw endTimeBeforeStartTime;
		}
		
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
	 * 
	 * takes a commandAdd, decide if the new task is already present in commandAdd or information should 
	 * be used to create the task, add the task, and returns the task
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
	 * delete task with information in commandDelete
	 * @param commandDelete
	 * @return
	 */
	public Task exeDelete(CommandDelete commandDelete) throws ArrayIndexOutOfBoundsException, 
														LogicException, IOException{
		assertCommandNotNull(commandDelete);
		
		int id = commandDelete.getIndex();
		
		if(id >= this._data.size()){
			throw new ArrayIndexOutOfBoundsException();
		}
		
		Task taskToDelete = this._data.get(id);
		
		if(taskToDelete == null){
			throw this.tryingToModifyDeletedTask;
		}
		
		if(this._dataManager.isLogined() && hasEventId(taskToDelete)){
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
	 * update contents of toUpdate(Task) with infoList and newData
	 * @param toUpdate
	 * @param infoList
	 * @param newData
	 */
	//helper class to exchange contents of CommandUpdate and task
	private void exchangeInfo(Task toUpdate, boolean[] infoList, Object[] newData, boolean isUndo){
		
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
				String oldTag = toUpdate.getTag();
				if(isUndo){
					toUpdate.setTag(newTag);
				}else{
					
					if(toUpdate.getTag() == null || toUpdate.getTag().equals("")){
						toUpdate.setTag(newTag);
					}else{
						toUpdate.setTag(toUpdate.getTag() + TAG_SEPARATOR + newTag);
					}
				}
				newData[TAG_ID] = oldTag;
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
	 * updates toUpdate with commandUpdate
	 * @param commandUpdate
	 * @param toUpdate
	 */
	private void updateInformation(CommandUpdate commandUpdate, Task toUpdate, boolean isUndo) throws LogicException{
		boolean[] infoList = commandUpdate.getChecklist();
		Object[] newData = commandUpdate.getNewInfo();
		if(!isUndo){
			if(infoList[STARTT_ID] && infoList[ENDT_ID]){
				GregorianCalendar startTime = (GregorianCalendar)newData[STARTT_ID];
				GregorianCalendar endTime = (GregorianCalendar)newData[ENDT_ID];
				if(endTime != null && startTime != null && endTime.compareTo(startTime) < 0){
					throw endTimeBeforeStartTime;
				}
			} else if(infoList[STARTT_ID]){
				GregorianCalendar startTime = (GregorianCalendar)newData[STARTT_ID];
				GregorianCalendar endTime = toUpdate.getEndTime();
				
				if(endTime != null && startTime != null && endTime.compareTo(startTime) < 0){
					throw endTimeBeforeStartTime;
				}
			} else if(infoList[ENDT_ID]){
				GregorianCalendar startTime = toUpdate.getStartTime();
				GregorianCalendar endTime = (GregorianCalendar)newData[ENDT_ID];
				
				if(endTime != null && startTime != null && endTime.compareTo(startTime) < 0){
					throw endTimeBeforeStartTime;
				}
			}
		}
		exchangeInfo(toUpdate, infoList, newData, isUndo);
	}
	

	/**
	 * executes a command update and returns the task that was updated
	 * @param commandUpdate
	 * @return
	 */
	public Task exeUpdate(CommandUpdate commandUpdate) throws LogicException, 
									ArrayIndexOutOfBoundsException, IOException {
		assertCommandNotNull(commandUpdate);
		int changeId = commandUpdate.getIndex();
		
		if(changeId >= this._data.size()){
			throw new ArrayIndexOutOfBoundsException();
		}
		
		Task toUpdate = _data.get(changeId);
		
		if(toUpdate == null){
			throw this.tryingToModifyDeletedTask;
		}
		boolean isUndo = false;
		updateInformation(commandUpdate, toUpdate, isUndo);
		if(this._dataManager.isLogined() && hasEventId(toUpdate)){
			this._dataManager.updateTaskToGoogle(toUpdate);
		}
		_undoStack.add(commandUpdate);
		this._dataManager.insertDataToFile(dataWithNullRemoved());
		this._redoStack.clear();
		return toUpdate;
	}
	
	/**
	 * display tasks which are not finished
	 * arrayList containing tasks to show, all null elements should not be shown
	 * @param commandDisplay
	 * @return ArrayList<Task>
	 */
	public ArrayList<Task> exeDisplay(CommandDisplay commandDisplay){
		assertCommandNotNull(commandDisplay);
		ArrayList<Task> toDisplay = new ArrayList<Task>();
		toDisplay.addAll(exeDisplayNotDone());
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
	private ArrayList<Task> displaySelectiveHelper(boolean[] toShow, Object[] searchWith) {
		
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
				if(toShow[LOCATION_ID] && !withinDistance(task.getLocation(), (String)searchWith[LOCATION_ID])){
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
						continue;
					}else{
						String[] tagList = task.getTag().split(TAG_SEPARATOR);
						boolean isTagged = false;
						isTagged = setIsTagged(searchWith, tagList, isTagged);
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
				assert(false): "null pointer in searchWith when toShow with the coresponding index is true\n";
			}catch (ArrayIndexOutOfBoundsException e){
				assert(false): "toShow and/or searchWith arrays are not the right size\n";
			}
		}
		
		return toDisplay;
	}

	private boolean setIsTagged(Object[] searchWith, String[] tagList,
			boolean isTagged) {
		for(int j = 0; j<tagList.length ;j++ ){
			if(tagList[j].equalsIgnoreCase(((String) searchWith[TAG_ID]).trim())){
				isTagged = true;
				break;
			}
		}
		return isTagged;
	}
	/**
	 * determines if start time of task is after startTimeToCompare
	 * @param task
	 * @param startTimeToCompare
	 * @return
	 */
	private boolean isStartTimeWithinRange(Task task, GregorianCalendar startTimeToCompare){
		if(task == null){
			return false;
		}
		
		if(task.getStartTime() == null){//is deadline task return true
			if(task.getType() != null && task.getType().equals(TASK_TYPE.DEADLINE)){
				return true;
			}else{
				return false;
			}
		}
		
		if(task.getStartTime().compareTo(startTimeToCompare) >= 0){
			return true;
		} 
		return false;
	}
	/**
	 * determines if the endtime of the task is before endTimeToCompare
	 * @param task
	 * @param endTimeToCompare
	 * @return
	 */
	private boolean isEndTimeWithinRange(Task task, GregorianCalendar endTimeToCompare){
		if(task == null || task.getEndTime() == null){
			return false;
		}
		if(task.getEndTime().compareTo(endTimeToCompare) <= 0){
			return true;
		}
		return false;
	}
	/**
	 * determines if 2 strings can be considered similar
	 * @param str1
	 * @param searchWith
	 * @return
	 */
	private boolean withinDistance(String str1, String searchWith){
		final String splitWith = " ";
		if(str1 == null || searchWith == null){
			return false;
		}
		if(str1.contains(searchWith)){
			return true;
		}
		String[] strArr1 = str1.split(splitWith);
		String[] strArr2 = searchWith.split(splitWith);
		
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
	 * returns arraylist with !<isImportant> importance tasks and replace them with null
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

	/**
	 * searchs the data with commandSearch and returns results
	 * @param commandSearch
	 * @return
	 */
	public ArrayList<Task> exeSearch(CommandSearch commandSearch){
		assertCommandNotNull(commandSearch);
		boolean[] toShow = commandSearch.getArrToShow();
		Object[] searchWith = commandSearch.getArrSearchWith();
		assert(toShow.length == TOTAL_LEN && searchWith.length == TOTAL_LEN) : "length of toshow and searchwith are not 8";
		return displaySelectiveHelper(toShow, searchWith);
	}
	
	/**
	 * replaces tasks that are !<isDone> with null
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


	/**
	 * undos the previous command, returns true if successful
	 * @return
	 */
	public boolean exeUndo() throws IOException {
		try{
			Command commandToUndo = this._undoStack.pop();
			CMD_TYPE commandType = commandToUndo.getType();
			switch(commandType){
				case ADD:
					log.log(Level.FINE, "undo add", commandToUndo);
					CommandAdd commandAdd = (CommandAdd) commandToUndo;
					int lastIndex = this._data.size()-1;
					
					Task toBeRemoved = this._data.get(lastIndex);
					this._data.remove(lastIndex);
					commandAdd.setTask(toBeRemoved);
					
					commandToUndo = commandAdd;
					
					if(this._dataManager.isLogined() && hasEventId(toBeRemoved)){
						this._dataManager.deleteTaskFromGoogle(toBeRemoved);
					}
					
					break;
				case UPDATE:
					log.log(Level.FINE, "undo update", commandToUndo);
					CommandUpdate commandUpdate = (CommandUpdate) commandToUndo;
					Task toUndoUpdate = this._data.get(commandUpdate.getIndex());
					
					boolean isUndo = true;
					
					try{
						updateInformation(commandUpdate, toUndoUpdate, isUndo);
						
						commandToUndo = commandUpdate;
						
						if(this._dataManager.isLogined() && hasEventId(toUndoUpdate)){
							this._dataManager.updateTaskToGoogle(toUndoUpdate);
						}
					}
					catch (LogicException e){
						assert(false): "encountered unencountable error";
					}
					
					break;
				case DELETE:
					log.log(Level.FINE, "undo delete", commandToUndo);
					CommandDelete commandDelete = (CommandDelete) commandToUndo;
					int deleteIndex = commandDelete.getIndex();
					Task toAddBack = commandDelete.getDeletedTask();
					
					this._data.set(deleteIndex, toAddBack);
					
					if(this._dataManager.isLogined() && hasEventId(toAddBack)){
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
					
					if(this._dataManager.isLogined() && hasEventId(toUndoMark)){
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

					if(this._dataManager.isLogined() && hasEventId(toUndoDone)){
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
					
					if(this._dataManager.isLogined() && hasEventId(toUndoTag)){
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
					ArrayList<Task> listBeforeClear = commandClear.getListBeforeClear();
					if(commandClear.isLoggedToGoogle()){//logged in when clear
						for(int i = TRUE_START_ID; i<listBeforeClear.size(); i++){
							Task currentTask = listBeforeClear.get(i);
							this._dataManager.addTaskToGoogle(currentTask);
						}
					}else{
						this._data = listBeforeClear;
					}
					
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
			return false;
		}
	}
	
	/**
	 * execute each command for the user.
	 */

	/**
	 * redos the previously undone command, returns true if successful
	 * @return
	 */
	public boolean exeRedo() throws IOException {
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
			return false;
		}
		
		catch (LogicException e){
			return false;
		}
	}
	

	/**
	 * tags a task using commandTag
	 * @param commandTag
	 * @return
	 * @throws LogicException
	 */
	public Task exeTag(CommandTag commandTag) throws LogicException, IOException {
		assertCommandNotNull(commandTag);
		
		int toTagIndex = commandTag.getIndex();
		if(toTagIndex >= this._data.size()){
			throw new ArrayIndexOutOfBoundsException();
		}
		
		Task taskToTag = this._data.get(toTagIndex);
		
		if(taskToTag == null){
			throw this.tryingToModifyDeletedTask;
		}
		
		String tag = taskToTag.getTag();
		if(tag == null || tag.equals("")){
			taskToTag.setTag(commandTag.getTagName());
		}else{
			taskToTag.setTag(taskToTag.getTag() + " # " + commandTag.getTagName());
		}
		if(this._dataManager.isLogined() && hasEventId(taskToTag)){
			this._dataManager.updateTaskToGoogle(taskToTag);
		}
		this._undoStack.push(commandTag);
		this._dataManager.insertDataToFile(dataWithNullRemoved());
		this._redoStack.clear();
		return taskToTag;
	}

	/**
	 * mark a task as important with commandMark
	 * @param commandMark
	 * @return
	 * @throws LogicException
	 */
	public Task exeMarkImportance(CommandMark commandMark) throws LogicException, IOException{
		assertCommandNotNull(commandMark);
		
		int toMarkIndex = commandMark.getIndex();
		
		if(toMarkIndex >= this._data.size()){
			throw new ArrayIndexOutOfBoundsException();
		}
		
		Task taskToMark = this._data.get(toMarkIndex);
		
		if(taskToMark == null){
			throw this.tryingToModifyDeletedTask;
		}
		
		boolean originalImportance = taskToMark.isImportant();
		taskToMark.setIsImportant(commandMark.isImportant());
		commandMark.setIsImportant(originalImportance);
		if(this._dataManager.isLogined() && hasEventId(taskToMark)){
			this._dataManager.updateTaskToGoogle(taskToMark);
		}
		this._undoStack.push(commandMark);
		this._dataManager.insertDataToFile(dataWithNullRemoved());
		this._redoStack.clear();
		return taskToMark;
	}


	/**
	 * mark a task as done with commandDone
	 * @param commandDone
	 * @return
	 * @throws LogicException
	 */
	public Task exeMarkDone(CommandDone commandDone) throws LogicException, IOException {
		assertCommandNotNull(commandDone);
		
		int toMarkDoneIndex = commandDone.getIndex();
		if(toMarkDoneIndex >= this._data.size()){
			throw new ArrayIndexOutOfBoundsException();
		}
		
		Task taskToMarkDone = this._data.get(toMarkDoneIndex);
		
		if( taskToMarkDone == null){
			throw this.tryingToModifyDeletedTask;
		}
		
		boolean isOriginallyDone = taskToMarkDone.isFinished();
		taskToMarkDone.setIsFinished(commandDone.isDone());
		commandDone.setIsDone(isOriginallyDone);
		if(this._dataManager.isLogined() && hasEventId(taskToMarkDone)){
			this._dataManager.updateTaskToGoogle(taskToMarkDone);
		}
		this._undoStack.push(commandDone);
		this._dataManager.insertDataToFile(dataWithNullRemoved());
		this._redoStack.clear();
		
		return taskToMarkDone;
	}
	
	
	private void exeAddAllToGoogle() throws IOException {
		assert(this._dataManager.isLogined()): "called exeAddAllToGoogle without logging in\n";

		for(int i = DATA_START_INDEX; i<this._data.size(); i++){
			Task currentTask = this._data.get(i);
			if(currentTask != null 
					&& !hasEventId(currentTask)){
				String newEventId = this._dataManager.addTaskToGoogle(currentTask);
				currentTask.setEventId(newEventId);
			}
		}
		this._dataManager.insertDataToFile(dataWithNullRemoved());
	}


	/**
	 * loads all tasks to google if user is logged in
	 * @param commandLoadFromGoogle
	 * @return
	 * @throws LogicException
	 */
	public ArrayList<Task> exeLoadTasksFromGoogle(CommandLoadFromGoogle commandLoadFromGoogle) 
			throws LogicException, IOException {
		if(!this._dataManager.isLogined()){
			throw this.userIsNotLoggedIn;
		}
		ArrayList<Task> originalTaskList = new ArrayList<Task>(this._data);
		commandLoadFromGoogle.setUndoList(originalTaskList);
		ArrayList<Task> loadedTasks = this._dataManager.getTasksFromGoogle();
		updateLocalTasks(loadedTasks);
		this._data.addAll(loadedTasks);
		this._dataManager.insertDataToFile(dataWithNullRemoved());
		this._undoStack.push(commandLoadFromGoogle);
		this._redoStack.clear();
		return this._data;
	}

	/**
	 * update tasks in calendear with tasks from google
	 */
	private void updateLocalTasks(ArrayList<Task> loadedTasks) {
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
	}
	
	public boolean exeClear(CommandClear commandClear) throws IOException {
		assertCommandNotNull(commandClear);
		
		ArrayList<Task> listToSave = new ArrayList<Task>(this._data);
		commandClear.setBeforeList(listToSave);
		if(this._dataManager.isLogined()){
			commandClear.setIsLoggedToGoogle();
			clearTasksFromGoogle();
		}else{
			this._data.clear();
			this._data.add(null);
		}
		this._dataManager.insertDataToFile(dataWithNullRemoved());
		this._undoStack.push(commandClear);
		return true;
	}

	private void clearTasksFromGoogle() throws IOException {
		Task currentTask;
		for(int i = TRUE_START_ID; i<this._data.size(); i++){
			currentTask = this._data.get(i);
			if(currentTask == null || !hasEventId(currentTask)){
				continue;
			}
			this._dataManager.deleteTaskFromGoogle(currentTask);
		}
	}
	//@@author
	
	/**
	 * @@author Phang Chun Rong
	 */
	public void exeLinkGoogle() throws IOException,Exception  {
		if (!this._dataManager.isLogined()) {
			this._dataManager.loginGoogle();
		}
		
		try {
			Thread.sleep(300);
		}
		catch (InterruptedException ex) {
		}
		
		if (this._dataManager.isLogined()) {
			exeAddAllToGoogle();
		}
	}
	

	public void exeSaveFile(CommandSave commandSave) throws IOException {
		String path = commandSave.getPath();
		this._dataManager.changeFilePath(path);
	}
	
	//@@author Wu XiaoXiao
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
	 * assert that a command is not null
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
		if(tags.length == 1){
			taggedTask.setTag(null);
		}else{
			final int SECOND_LAST_TAG_INDEX = tags.length-2;
			
			for(int i = 0; i<SECOND_LAST_TAG_INDEX; i++){
				newTag += tags[i] + TAG_SEPARATOR;
			}
			newTag += tags[SECOND_LAST_TAG_INDEX];
			taggedTask.setTag(newTag);
		}
	}

	
	private boolean hasEventId(Task task){
		String eventId = task.getEventId();
		return eventId != null && !eventId.equals(EMPTY_STRING) && !eventId.equals(NULL_STRING);
	}
	
}



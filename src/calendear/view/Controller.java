//@@author A0139060M

package calendear.view;
import calendear.action.CDLogic;
import calendear.action.LogicException;
import calendear.util.*;

import java.util.Scanner;
import java.text.ParseException;
import java.util.ArrayList;

import java.io.IOException;

/**
 * Controller for User Interaction
 * @author Phang Chun Rong
 */
public class Controller {
	private static final int DONE_ID = 8;
	private static final String MESSAGE_ERROR_GOOGLE = "Error Communicating with Google";
	private static final String MESSAGE_ERROR_GOOGLE_LOGIN = "Error authenticating with Google";
	private static final String MESSAGE_ERROR_INITIALISATION = "Error with file, please choose another file";
	private static final String MESSAGE_ERROR_FILE_CREATION = "Cannot Create File";
	
	private static Scanner _scanner;
	private CDLogic _cdLogic;
	
	public static void main(String[] args) {
		_scanner = new Scanner(System.in);
		
		String nameOfFile = getFileName();
		
		Controller controller = new Controller(nameOfFile);
		controller.startApplication();
	}
	
	public Controller(String nameOfFile) {
		try {
			instantiateLogic(nameOfFile);
		} catch (ParseException e) {
			View.displayError(MESSAGE_ERROR_INITIALISATION);
			System.exit(0);
		}
		catch (IOException ex) {
			View.displayError(MESSAGE_ERROR_INITIALISATION);
			System.exit(0);
		}
		View.displayWelcome();
	}
	
	public void instantiateLogic(String nameOfFile) throws ParseException, IOException {
		_cdLogic = new CDLogic(nameOfFile);
	}
	
	private void startApplication() {
	    while(true) {
	    	
	    	View.displayRequestForInput();
	    	
	    	String userCommand = _scanner.nextLine();
//	    	Parse Tokens
	    	Command command = _cdLogic.parseInput(userCommand);
//	    	Do Actions
	    	switch(command.getType()) {
		    	case ADD:	  
		    		try{
		    			Task addedTask = _cdLogic.exeAdd((CommandAdd) command);
		    			View.displayAdd(addedTask);
		    			break;
		    		}
		    		catch(IOException ex) {
		    			View.displayError(MESSAGE_ERROR_GOOGLE);
		    		}
		    		catch(LogicException e){
		    			View.displayError(e.getMessage());
		    			break;
		    		}
		    				  
	    		case DISPLAY: 
	    			try{
	    				ArrayList<Task> tasks = _cdLogic.exeDisplay((CommandDisplay) command);
	    				View.displayDisplayInLine(tasks);
	    			}
	    			catch(ArrayIndexOutOfBoundsException e){
	    				View.displayInvalid();
	    			}
	    			break;
	    					  
	    		case DELETE:  
	    			try{
	    				Task deletedTask = _cdLogic.exeDelete((CommandDelete) command);
	    				View.displayDelete(deletedTask);
	    			}
	    			catch (ArrayIndexOutOfBoundsException e){
	    				View.displayInvalid();
	    			}
	    			catch (IOException ex) {
	    				View.displayError(MESSAGE_ERROR_GOOGLE);
	    			}
	    			catch (LogicException e){
	    				View.displayError(e.getMessage());
	    			}
	    			break;
	    		
	    		case UPDATE:  
	    			try{
	    				Task updatedTask = _cdLogic.exeUpdate((CommandUpdate) command);
	    				View.displayUpdate(updatedTask);
	    				break;
	    			}
	    			catch (LogicException e){
	    				View.displayError(e.getMessage());
	    				break;
	    			}
	    			catch (IOException ex) {
	    				View.displayError(MESSAGE_ERROR_GOOGLE);
	    			}
	    			catch (ArrayIndexOutOfBoundsException e){
	    				View.displayInvalid();
	    				break;
	    			}
	    		
	    		case SEARCH:  
	    			CommandSearch commandSearch = (CommandSearch)command;
	    			ArrayList<Task> foundTasks = _cdLogic.exeSearch(commandSearch);
	    			
	    			if(commandSearch.getArrToShow()[DONE_ID]){
	    				View.displaySearchDoneInLine(foundTasks);
	    			}else{
	    				View.displaySearchInLine(foundTasks);
	    			}
	    			break;
	    		
	    		case MARK:	
	    			try{
	    				Task markedImportanceTask = _cdLogic.exeMarkImportant((CommandMark) command);
		    			View.displayMark(markedImportanceTask);
	    			}
	    			catch (ArrayIndexOutOfBoundsException e){
	    				View.displayInvalid();
	    			}
	    			catch (IOException ex) {
	    				View.displayError(MESSAGE_ERROR_GOOGLE);
	    			}
	    			catch (LogicException e){
	    				View.displayError(e.getMessage());
	    			}
	    				break;

	    		case TAG:	
	    			try{
	    				Task taggedTag = _cdLogic.exeTag((CommandTag) command);
	    				View.displayTag(taggedTag);
	    			}
	    			catch(ArrayIndexOutOfBoundsException e){
	    				View.displayInvalid();
	    			}
	    			catch(IOException ex) {
	    				View.displayError(MESSAGE_ERROR_GOOGLE);
	    			}
	    			catch (LogicException e){
	    				View.displayError(e.getMessage());
	    			}
	    				break;
	    		
	    		case LINK_GOOGLE: 
	    			try {
						_cdLogic.exeLinkGoogle();
					}
	    			
	    			catch(IOException ex) {
	    				View.displayError(MESSAGE_ERROR_GOOGLE);
	    			}
	    			catch(Exception ex) {
	    				View.displayError(MESSAGE_ERROR_GOOGLE_LOGIN);
	    			}
				  break;
	    						  

	    		case LOAD_FROM_GOOGLE:  
	    			try{
						ArrayList<Task> resultingList =  _cdLogic.exeLoadTasksFromGoogle((CommandLoadFromGoogle) command);
						View.displayDisplayInLine(resultingList);
						break;
					}
	    			catch (IOException ex) {
	    				View.displayError(MESSAGE_ERROR_GOOGLE);
	    			}
					catch (LogicException logicException){
						View.displayError(logicException.getMessage());
						break;
					}
	    			
	    		case DONE: 
	    			try {
					 	Task completedTask = _cdLogic.exeMarkDone((CommandDone) command);
					 	View.displayDone(completedTask);
				   	}
	    			catch(ArrayIndexOutOfBoundsException e){
	    				View.displayInvalid();
	    			}
	    			catch (IOException ex) {
	    				View.displayError(MESSAGE_ERROR_GOOGLE);
	    			}
	    			catch (LogicException e){
	    				View.displayError(e.getMessage());
	    			}
					break;
	    				
	    		case UNDO: 
	    			try {
						boolean undoSuccessful = _cdLogic.exeUndo();
						if(!undoSuccessful){
							View.displayError("Error: nothing to undo");
						}
						else{
							View.displayUndo();
						}
				   	}
					
					catch(IOException ex) {
						View.displayError(MESSAGE_ERROR_GOOGLE);
					}
				   	   
				   break;
	    				   
	    		case EXIT:  
	    			View.displayExit();
					System.exit(0);
					break;
	    					
	    		case REDO: 
	    			try {
						boolean redoSuccessful = _cdLogic.exeRedo();
						if(!redoSuccessful){
    					   View.displayError("Error: nothing to redo.");
    				   }
						else{
							View.displayRedo();
						}
				   }
				   catch(IOException ex) {
					   View.displayError(MESSAGE_ERROR_GOOGLE);
				   }
				   	   
				   break;
	    		
	    		case SAVE:  
	    			try {
						_cdLogic.exeSaveFile((CommandSave) command);
					}
					catch (IOException ex) {
						View.displayError(MESSAGE_ERROR_FILE_CREATION);
					}
					
					break;
	    				
	    		case CLEAR: 
	    			try {
						_cdLogic.exeClear((CommandClear) command);
					}
					catch(IOException ex) {
						View.displayError(MESSAGE_ERROR_GOOGLE);
					}
					
					break;
	    		
	    		case HELP:	
	    			View.displayHelp();
				    break;
	    		
	    		case INVALID:
	    			View.displayInvalid();
	    			break;
	    			
	    		default: 
	    				break;
	    	}
	    }
	}
	
	private static String getFileName() {
		View.displayFileNamePrompt();
		String name = _scanner.nextLine();
		
		return name;
	}
}

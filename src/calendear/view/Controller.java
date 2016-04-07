package calendear.view;
import calendear.action.CDLogic;
import calendear.action.LogicException;
import calendear.util.*;

import java.util.Scanner;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * 
 * @author Phang Chun Rong
 * Controller for User Interaction
 */
public class Controller {
	private static final int DONE_ID = 8;
	
	private Scanner _scanner;
	private CDLogic _cdLogic;
	
	public static void main(String[] args) {
		String nameOfFile = args[0];
		Controller controller = new Controller(nameOfFile);
		controller.startApplication();
	}
	
	public Controller(String nameOfFile) {
		try {
			instantiateLogic(nameOfFile);
		} catch (ParseException e) {
			System.out.println(e);
			System.out.println("file reading failed");
		}
		
		View.displayWelcome();
	}
	
	public void instantiateLogic(String nameOfFile) throws ParseException {
		_cdLogic = new CDLogic(nameOfFile);
	}
	
	private void startApplication() {
	    _scanner = new Scanner(System.in);
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
		    		catch(LogicException e){
		    			View.displayError(e.getMessage());
		    			break;
		    		}
		    				  
	    		case DISPLAY: ArrayList<Task> tasks = _cdLogic.exeDisplay((CommandDisplay) command);
	    					  View.displayDisplayInLine(tasks);
	    					  break;
	    					  
	    		case DELETE:  Task deletedTask = _cdLogic.exeDelete((CommandDelete) command);
	    					  View.displayDelete(deletedTask);
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
	    		
	    		case SEARCH:  
	    			CommandSearch commandSearch = (CommandSearch)command;
	    			ArrayList<Task> foundTasks = _cdLogic.exeSearch(commandSearch);
	    			
	    			if(commandSearch.getArrToShow()[DONE_ID]){
	    				View.displaySearchDoneInLine(foundTasks);
	    			}else{
	    				View.displaySearchInLine(foundTasks);
	    			}
	    			break;
	    		
	    		case MARK:	Task markedImportanceTask = _cdLogic.exeMarkImportant((CommandMark) command);
	    					View.displayMark(markedImportanceTask);
	    					break;

	    		case TAG:	Task taggedTag = _cdLogic.exeTag((CommandTag) command);
	    					View.displayTag(taggedTag);
	    					break;
	    		
	    		case LINK_GOOGLE: _cdLogic.exeLinkGoogle();
	    						  break;
	    						  
	    		case LOAD_FROM_GOOGLE:  try{
	    									ArrayList<Task> resultingList =  _cdLogic.exeLoadTasksFromGoogle((CommandLoadFromGoogle) command);
	    									View.displayDisplayInLine(resultingList);
	    									break;
	    								}
	    								catch (LogicException logicException){
	    									View.displayError(logicException.getMessage());
	    									break;
	    								}
	    			
	    		case DONE: Task completedTask = _cdLogic.exeMarkDone((CommandDone) command);
	    				   View.displayDone(completedTask);
	    				   break;
	    				
	    		case UNDO: boolean undoSuccessful = _cdLogic.exeUndo();
	    				   if(!undoSuccessful){
	    					   View.displayError("Error: nothing to undo");
	    				   }	   
	    				   break;
	    				   
	    		case EXIT:  View.displayExit();
	    					System.exit(0);
	    					break;
	    					
	    		case REDO: boolean redoSuccessful = _cdLogic.exeRedo();
	    				   if(!redoSuccessful){
	    					   View.displayError("Error: nothing to redo.");
	    				   }
	    				   
	    				   break;
	    		
	    		case SAVE:  String result = _cdLogic.exeSaveFile((CommandSave) command);
	    					System.out.println(result);
	    					break;
	    				
	    		case CLEAR: _cdLogic.exeClear((CommandClear) command);
	    				break;
	    		
	    		case HELP:	View.displayHelp();
	    				    break;
	    		default: 
	    				break;
	    	}
	    }
	}

}

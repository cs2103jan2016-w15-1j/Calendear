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
		    	case ADD:	  Task addedTask = _cdLogic.exeAdd((CommandAdd) command);
		    				  View.displayAdd(addedTask);
		    				  break;
		    				  
	    		case DISPLAY: ArrayList<Task> tasks = _cdLogic.exeDisplay((CommandDisplay) command);
	    					  View.displayDisplayInLine(tasks);
	    					  break;
	    					  
	    		case DELETE:  Task deletedTask = _cdLogic.exeDelete((CommandDelete) command);
	    					  View.displayDelete(deletedTask);
	    					  break;
	    		
	    		case UPDATE:  Task updatedTask = _cdLogic.exeUpdate((CommandUpdate) command);
	    					  View.displayUpdateInLine(updatedTask);
	    					  break;
	    		
	    		case SEARCH:	ArrayList<Task> foundTasks = _cdLogic.exeSearch((CommandSearch) command);
	    						View.displaySearchInLine(foundTasks);
	    				break;
	    		
	    		case MARK:	Task markedImportanceTask = _cdLogic.exeMarkImportant((CommandMark) command);
	    					View.displayMark(markedImportanceTask);
	    				break;

	    		case TAG:	Task taggedTag = _cdLogic.exeTag((CommandTag) command);
	    					View.displayTag(taggedTag);
	    				break;
	    		
	    		case LINK_GOOGLE: _cdLogic.exeLinkGoogle();
	    						  break;
	    						  
	    		case LOAD_FROM_GOOGLE:  
	    			try{
	    				ArrayList<Task> resultingList =  _cdLogic.exeLoadTasksFromGoogle((CommandLoadFromGoogle) command);
	    				View.displayDisplayInLine(resultingList);
	    				break;
	    			}
	    			catch (LogicException logicException){
	    				//TODO
	    				System.out.println(logicException.getMessage());
	    			}
	    			
	    		case DONE: Task completedTask = _cdLogic.exeMarkDone((CommandDone) command);
	    				   View.displayDone(completedTask);
	    				break;
	    				
	    		case UNDO: _cdLogic.exeUndo();
	    				break;
	    				   
	    		case EXIT:  View.displayExit();
	    					System.exit(0);
	    				break;
	    					
	    		case REDO: _cdLogic.exeRedo();
	    				break;
	    				
	    		case CLEAR: _cdLogic.exeClear((CommandClear) command);
	    				break;
	    		default: 
	    				break;
	    	}
	    }
	}

}

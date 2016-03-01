import calendear.action.Action;
import calendear.parser.Parser;
import calendear.util.CommandAdd;
import calendear.storage.DataManager;
import calendear.util.*;
import calendear.view.View;

import java.util.Scanner;
import java.util.ArrayList;

public class Controller {
	
	private static Scanner _scanner;
	private static Action _action;
	private static DataManager _dataManager;
	
	public static void main(String[] args) {
		String nameOfFile = args[0];
		
		createOrFindFile(nameOfFile);

		instantiateOrPopulateTasks();
		
		View.displayWelcome();
		
		repl();
	}
	
	public static void createOrFindFile(String nameOfFile) {
		_dataManager = new DataManager(nameOfFile);
	}
	
	public static void instantiateOrPopulateTasks() {
		ArrayList<Task> tasks = _dataManager.buildData();
		_action = new Action(tasks, _dataManager);
	}
	
	private static void repl() {
	    _scanner = new Scanner(System.in);

	    while(true) {
	    	View.displayRequestForInput();
	    	
	    	String userCommand = _scanner.nextLine();
	    	
//	    	Parse Tokens
	    	Command command = Parser.parse(userCommand);
//	    	Do Actions
	    	switch(command.getType()) {
		    	case ADD:	  Task addedTask = _action.exeAdd((CommandAdd) command);
		    				  View.displayAdd(addedTask);
		    				  break;
		    				  
	    		case DISPLAY: ArrayList<Task> tasks = _action.exeDisplay((CommandDisplay) command);
	    					  View.displayDisplay(tasks);
	    					  break;
	    					  
	    		case DELETE:  Task deletedTask = _action.exeDelete((CommandDelete) command);
	    					  View.displayDelete(deletedTask);
	    					  break;
	    		
	    		case UPDATE:  Task updatedTask = _action.exeUpdate((CommandUpdate) command);
	    					  View.displayUpdate(updatedTask);
	    					  break;
	    		
	    		case SEARCH:
	    				break;
	    		
	    		case MARK:
	    				break;
	    		
	    		case TAG:
	    				break;
	    		
	    		case LINK_GOOGLE:
	    				break;
	    		
	    		case DONE:
	    				break;
	    		
	    		case EXIT:  View.displayExit();
	    					System.exit(0);
	    					break;
	    		default:
	    				break;
	    	}
	    }
	}

}

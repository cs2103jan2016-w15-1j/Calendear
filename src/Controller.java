import calendear.action.Action;
import calendear.parser.Parser;
import calendear.util.CommandAdd;
import calendear.util.*;
import calendear.parser.Parser;
import calendear.view.View;

import java.util.Scanner;
import java.text.ParseException;
import java.util.ArrayList;

public class Controller {
	
	private static Scanner _scanner;
	private static Action _action;
	
	public static void main(String[] args) {
		String nameOfFile = args[0];
		
		try {
			instantiateActions(nameOfFile);
		} catch (ParseException e) {
			System.out.println("file reading failed");
		}
		
		View.displayWelcome();
		
		repl();
	}
	
	public static void instantiateActions(String nameOfFile) throws ParseException {
		_action = new Action(nameOfFile);
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

import calendear.action.CDLogic;

import calendear.util.*;

import calendear.view.View;

import java.util.Scanner;
import java.text.ParseException;
import java.util.ArrayList;

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
	    					  View.displayDisplay(tasks);
	    					  break;
	    					  
	    		case DELETE:  Task deletedTask = _cdLogic.exeDelete((CommandDelete) command);
	    					  View.displayDelete(deletedTask);
	    					  break;
	    		
	    		case UPDATE:  Task updatedTask = _cdLogic.exeUpdate((CommandUpdate) command);
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

import calendear.action.Action;
import calendear.util.CommandAdd;
import calendear.storage.DataManager;
import calendear.util.Task;
import calendear.util.Command;
import calendear.parser.Parser;
import calendear.util.CMD_TYPE;

import java.util.Scanner;
import java.util.ArrayList;

public class Controller {
	
	private static Scanner _scanner;
	private static Action _action;
	private static DataManager _dataManager;
	
	public static void main(String[] args) {
		String nameOfFile = args[0];
		
//		Create or set file
		createOrFindFile(nameOfFile);

//		instantiateOrPopulateTasks();
		
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
	    	String userCommand = _scanner.nextLine();
	    	
//	    	Parse Tokens
	    	Command command = Parser.parse(userCommand);
//	    	Do Actions
	    	switch(command.getType()) {
	    		case ADD: Task addedTask = _action.add((CommandAdd) command);
	    				  break;
	    		case DISPLAY: 
	    				break;
	    		case DELETE:
	    				break;
	    		case UPDATE:
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
	    		case EXIT:
	    				break;
	    		default:
	    				break;
	    	}
	    }
	}

}

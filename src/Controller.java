import calendear.action.Action;
import calendear.storage.DataManager;
import calendear.util.Task;

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

		instantiateOrPopulateTasks();
		
		repl();
	}
	
	public static void createOrFindFile(String nameOfFile) {
		_dataManager = new DataManager(nameOfFile);
	}
	
	public static void instantiateOrPopulateTasks() {
		ArrayList<Task> tasks = _dataManager.buildData();
		_action = new Action(tasks);
	}
	
	
	private static void repl() {
	    _scanner = new Scanner(System.in);

	    while(true) {
	    	String userCommand = _scanner.nextLine();
	    	
//	    	Parse Tokens

//	    	Do Actions
	    	
//	    	Render View
	    }
	}

}

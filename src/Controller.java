import calendear.command.Action;
import calendear.storage.DataManager;
import java.util.Scanner;

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
//		Check whether the data exists in this path
		
	}
	
	public static void instantiateOrPopulateTasks() {
		_action = new Action();
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

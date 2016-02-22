import java.util.Scanner;

public class Controller {
	
	private static Scanner _scanner;
	
	public static void main(String[] args) {
		String nameOfFile = args[0];
		
//		Create or set file
		createOrFindFile(nameOfFile);
		
//		Initialize state with existing or new file (Actions)
				
		repl();
	}
	
	public static void createOrFindFile(String nameOfFile) {
		
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

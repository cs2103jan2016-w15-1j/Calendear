package calendear.view;

//@@author Pan Jiyun

public class HelpTable {
	private static int LEN_SEG1 = 24;
	private static int LEN_SEG2 = 50;
	private static String format = "|%1$-"+LEN_SEG1+"s"+"|%2$-"+LEN_SEG2+"s"+"|\n";
	
	
	private static final String[] SEG1 = {"Add event","Add due task","Add floating task","Display","Delete","Done",
			"Update","Tag","Search","Undo","Redo","Link Google"};
	private static final String[] SEG2 = {"add <event name> from <start time> to <end time>",
											  "add <task name> by <due time>",
											  "add <task name>","display","delete <task ID>",
											  "done <task ID>","update <task ID> <attribute> <new content>...",
											  "tag <task ID> <tag>","search <attribute> <key word>",
											  "undo","redo","linkGoogle"};

	
	
	
	private static final int NUM_OF_CMD = 12;
	private static final String HEADER_HELP = "Help Table";
	
	public static String getHelpTable(){
		String inLine ="";
		inLine+=Line.borderLineWithWordsEqualCyan(HEADER_HELP);
		int i;
		for(i=0;i<(NUM_OF_CMD-1);i++){
			inLine += (String.format(format,SEG1[i],SEG2[i]));
			inLine += (Line.borderLineDash());
		}
		inLine += (String.format(format,SEG1[i],SEG2[i]));
		inLine += (Line.borderLineEqual());
		return inLine;
	}
	
}

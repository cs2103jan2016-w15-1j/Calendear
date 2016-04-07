package calendear.view;

//@@author Pan Jiyun

public class Line {
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	private static final int LEN_TOTAL = 77;
	private static final String BORDER_SIGN_STAR = "*";
	private static final String BORDER_SIGN_DASH = "-";
	private static final String BORDER_SIGN_EQUAL = "=";
	
	
	private static String formatRed = ANSI_RED+"%s"+ANSI_RESET;
	private static String formatYellow = ANSI_YELLOW+"%s"+ANSI_RESET;
	private static String formatCyan = ANSI_CYAN+"%s"+ANSI_RESET;
	private static String formatGreen = ANSI_GREEN+"%s"+ANSI_RESET;
	
	public static String borderLineStar(){
		String border ="";
		for(int i=0;i<LEN_TOTAL;i++){
			border+= BORDER_SIGN_STAR;
		}
		border+="\n";
		String nborder = String.format(formatGreen, border);
		return nborder;
	}
	

	public static String borderLineDash(){
		String border ="";
		for(int i=0;i<LEN_TOTAL;i++){
			border+= BORDER_SIGN_DASH;
		}
		border+="\n";
		String nborder = String.format(formatCyan, border);
		return nborder;
	}
	
	public static String borderLineEqual(){
		String border ="";
		for(int i=0;i<LEN_TOTAL;i++){
			border+= BORDER_SIGN_EQUAL;
		}
		border+="\n";
		String nborder = String.format(formatCyan, border);
		return nborder;
	}
	
	public static String borderLineWithWordsStarYellow(String s){
		int sLen = s.length();
		int lineLen = LEN_TOTAL -sLen;
		int segment1 = lineLen/2;
		int segment2 = lineLen -segment1;
		String line="";
		for(int i=0;i<segment1;i++){
			line+= BORDER_SIGN_STAR;
		}
		line+=s;
		for(int i=0;i<segment2;i++){
			line+= BORDER_SIGN_STAR;
		}
		line+="\n";
		String nLine = String.format(formatYellow, line);
		return nLine;
	}
	
	public static String borderLineWithWordsEqualCyan(String s){
		int sLen = s.length();
		int lineLen = LEN_TOTAL -sLen;
		int segment1 = lineLen/2;
		int segment2 = lineLen -segment1;
		String line="";
		for(int i=0;i<segment1;i++){
			line+= BORDER_SIGN_EQUAL;
		}
		line+=s;
		for(int i=0;i<segment2;i++){
			line+= BORDER_SIGN_EQUAL;
		}
		line+="\n";
		String nLine = String.format(formatCyan, line);
		return nLine;
	}
	

}

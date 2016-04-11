# Pan Jiyun
###### \src\calendear\view\Full.java
``` java
 *
 */
public class Full {
	
	private static final String HEADER_NAME = "Task name:";
	private static final String HEADER_TAG = "Tag:";
	private static final String HEADER_STARTTIME = "Start time:";
	private static final String HEADER_ENDTIME = "End time:";
	private static final String HEADER_DUETIME = "Due time:";
	private static final String HEADER_LOCATION = "Location:";
	private static final String HEADER_NOTE = "Note:";
	private static final String HEADER_IMPORTANCE = "Important:";
	private static final String HEADER_FINISHED = "Finished:";
	private static final String[] HEADERS_ARR_N = {HEADER_IMPORTANCE,HEADER_FINISHED,HEADER_NAME, 
			HEADER_TAG, HEADER_STARTTIME,HEADER_DUETIME,HEADER_ENDTIME,HEADER_LOCATION,HEADER_NOTE};
	
	private static final int ID_IMPO=0;
	private static final int ID_FINI=1;
	private static final int ID_NAME=2;
	private static final int ID_TAG=3;
	private static final int ID_STIME=4;
	private static final int ID_DTIME=5;
	private static final int ID_ETIME=6;
//	private static final int ID_LOCA=7;
//	private static final int ID_NOTE=8;
	
	
	
	
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_CYAN = "\u001B[36m";
	
	private static String formatCyan = ANSI_CYAN+"%s"+ANSI_RESET;
	
	private static final String[] HEADERS_ARR = addColor(HEADERS_ARR_N);
	
	private static final String MSG_YES = "Yes";
	private static final String MSG_NO = "No";
	
	
	private static String[] addColor(String[] arr){
		String[] colorArr= new String[arr.length];
		for(int i=0;i<arr.length;i++){
			colorArr[i] = String.format(formatCyan,arr[i]);
		}
		return colorArr;
	}
	
	public static String getMultipleTasks(ArrayList<Task> taskArr){
		String output="";
		for(int i=0;i<taskArr.size();i++){
			if(taskArr.get(i)!=null){
				output+=i+".\n";
				output+=getTask(taskArr.get(i))+"\n";
			}
		}
		return output;
	}
	
	
	
	
	public static String getTask(Task task){
		String details="";
		if(task.getName()!=null){
			details+=HEADERS_ARR[ID_NAME]+task.getName()+"\n";
		}
		if(task.getTag()!=null){
			details+=HEADERS_ARR[ID_TAG]+task.getTag()+"\n";
		}	
		if((task.getStartTimeStr()!=null)&&
				(!task.getType().equals(TASK_TYPE.DEADLINE))){
			details+=HEADERS_ARR[ID_STIME]+task.getStartTimeStr()+"\n";
		}
		if(task.getEndTimeStr()!=null){
			if(task.getType().equals(TASK_TYPE.DEADLINE)){
				details+=HEADERS_ARR[ID_DTIME]+task.getEndTimeStr()+"\n";
			}
			else{
				details+=HEADERS_ARR[ID_ETIME]+task.getEndTimeStr()+"\n";
			}
		}
	/*	if(task.getLocation()!=null){
			details+=HEADERS_ARR[ID_LOCA]+task.getLocation()+"\n";
		}
		if(task.getNote()!=null){
			details+=HEADERS_ARR[ID_NOTE]+task.getNote()+"\n";
		} */
		if(task.isImportant()){
			details+=HEADERS_ARR[ID_IMPO]+ MSG_YES+ "\n";
		}
		if(task.isFinished()){
			details+=HEADERS_ARR[ID_FINI]+MSG_YES+"\n";
		}
		else if(!task.isFinished()){
			details+=HEADERS_ARR[ID_FINI]+MSG_NO+"\n";
		}
		return details;
	}
}
```
###### \src\calendear\view\HelpTable.java
``` java
 *
 */

public class HelpTable {
	private static int LEN_SEG1 = 24;
	private static int LEN_SEG2 = 50;
	private static String format = "|%1$-"+LEN_SEG1+"s"+"|%2$-"+LEN_SEG2+"s"+"|\n";
	
	
	private static final String[] SEG1 = {"Add event","Add due task","Add floating task","Display","Delete","Done",
			"Update","Tag","Search","Undo","Redo","Link Google","Sync Google","Save"};
	private static final String[] SEG2 = {"add <event name> from <start time> to <end time>",
											  "add <task name> by <due time>",
											  "add <task name>","display","delete <task ID>",
											  "done <task ID>","update <task ID> <attribute> <new content>...",
											  "tag <task ID> <tag>","search <attribute> <key word>",
											  "undo","redo","linkGoogle","syncGoogle","save <new path>"};

	
	
	
	private static final String HEADER_HELP = "Help Table";
	
	public static String getHelpTable(){
		String inLine ="";
		inLine+=Line.borderLineWithWordsEqualCyan(HEADER_HELP);
		int i;
		for(i=0;i<(SEG1.length-1);i++){
			inLine += (String.format(format,SEG1[i],SEG2[i]));
			inLine += (Line.borderLineDash());
		}
		inLine += (String.format(format,SEG1[i],SEG2[i]));
		inLine += (Line.borderLineEqual());
		return inLine;
	}
	
}
```
###### \src\calendear\view\Line.java
``` java
 *
 */

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
```
###### \src\calendear\view\Pair.java
``` java
 *
 * @param <T>
 * @param <Task>
 */

public class Pair<T,Task> {
	private  Integer id;
	private  Task task;
	
	public Pair(Integer id, Task task){
		this.id = id;
		this.task = task;
	}
	
	public void setId(Integer id){
		this.id = id;
	}
	
	public void setTask(Task task){
		this.task = task;
	}
	
	public Integer getId(){
		return id;
	}
	
	public Task getTask(){
		return task;
	}
}
```
###### \src\calendear\view\PairComparator.java
``` java
 * 
 */

/**order of display:
 * incomplete important event with early start time
 * incomplete important deadline task with early end time
 * incomplete important floating task
 * incomplete event with early start time
 * incomplete deadline task with early end time
 * incomplete floating task
 * completed important event with early start time
 * completed important deadline task with early end time
 * completed important floating task 
 * completed event with early start time
 * completed deadline task with early end time
 * completed floating task
 */



public class PairComparator implements Comparator<Pair<Integer,Task>> {
	@Override
	public int compare(Pair<Integer,Task> p1,Pair<Integer,Task> p2){
		return compareComplete((Task)p1.getTask(),(Task)p2.getTask());
	}
	
	private int compareComplete(Task t1,Task t2){
		if(t1.isFinished()&&!t2.isFinished()){
			return 1;
		}
		else if(!t1.isFinished()&&t2.isFinished()) {
			return -1;
		}
		else{
			return compareImportance(t1,t2);
		}
	}
	
	private int compareImportance(Task t1,Task t2){
		if(t1.isImportant()&&!t2.isImportant()){
			return -1;
		}
		else if(!t1.isImportant()&&t2.isImportant() ) {
			return 1;
		}
		else{
			return compareType(t1,t2);
		}
	}
	
	private int compareType(Task t1,Task t2){
		int i;
		i = t1.getType().compareTo(t2.getType());
		if(i==0){
			if(t1.getType().equals(TASK_TYPE.DEADLINE)){
				return compareDeadline(t1,t2);
			}
			else if(t1.getType().equals(TASK_TYPE.EVENT)){
				return compareEvent(t1,t2);
			}
			else if(t1.getType().equals(TASK_TYPE.FLOATING)){
				return compareFloat(t1,t2);
			}
		}
		return i;
		
	}
	
	
	private int compareDeadline(Task t1, Task t2) {
		int j;
		j= t1.getEndTime().compareTo(t2.getEndTime());
		if(j==0){
			compareName(t1,t2);
		}
		return j;
	}

	private int compareEvent(Task t1, Task t2) {
		int j;
		j= t1.getStartTime().compareTo(t2.getStartTime());
		if(j==0){
			compareName(t1,t2);
		}
		return j;
	}

	private int compareFloat(Task t1, Task t2) {
		return compareName(t1,t2);
	}

	private int compareName(Task t1, Task t2){
		return (t1.getName().compareTo(t2.getName()));
	}
	
}
```
###### \src\calendear\view\Table.java
``` java
 * 
 */

/**
 * functions:
 * 1. return full table 
 * 2. return table with incomplete tasks only
 * 3. return table with completed tasks only
 * 4. return sorted array list of pairs
 * 5. return table format of one single task
 * 6. return attributes of one single task in an array list
 */

public class Table {
	
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	

	
	private static final String HEADER_NAME = "Task name:";
	private static final String HEADER_TAG = "Tag:";
	private static final String HEADER_STARTTIME = "Start time:";
	private static final String HEADER_ENDTIME = "End time:";
//	private static final String HEADER_LOCATION = "Location:";
//	private static final String HEADER_NOTE = "Note:";
	private static final String HEADER_IMPORTANCE = "Important:";
//	private static final String HEADER_FINISHED = "Finished:";
	private static final String[] HEADERS_ARR_N = {HEADER_IMPORTANCE/*,HEADER_FINISHED*/,HEADER_NAME, 
			HEADER_TAG, HEADER_STARTTIME,HEADER_ENDTIME/*,HEADER_LOCATION,HEADER_NOTE*/};

	public static final String MSG_WELCOME = ANSI_PURPLE+welcomeString("Welcome to Calendear!")+ANSI_RESET;
	private static final String MSG_YES = "Yes";
	
	private static final String S = " ";
	private static final int NUM_OF_ATTRI = 8;
	private static final int LEN_ID = 4;
	private static final int LEN_IMPO = 10;
//	private static final int LEN_FINI = 10;
	private static final int LEN_NAME = 16;
	private static final int LEN_TAG = 16;
	private static final int LEN_STIME = 12;
	private static final int LEN_ETIME = 12;
	private static final int LEN_LOCA = 16;
	private static final int LEN_NOTE = 16;
	private static final int NOT_ARR_LIST = -1;
	private static final int NUM_OF_TITLE_BAR = 7;
	private static final int LEN_TOTAL = LEN_ID+LEN_IMPO/*+LEN_FINI*/+
						LEN_NAME+LEN_STIME+LEN_ETIME+LEN_TAG+/*LEN_LOCA+LEN_NOTE+*/NUM_OF_TITLE_BAR;
	
	private static final int ID_IMPO=0;
	private static final int ID_FINI=1;
	private static final int ID_NAME=2;
	private static final int ID_TAG=3;
	private static final int ID_STIME=4;
	private static final int ID_ETIME=5;
	private static final int ID_LOCA=6;
	private static final int ID_NOTE=7;
	
	
	private static final int[] LEN_TITLE_ARR = {0/*,1*/,6,12,1,3,7,11};
	private static final String BORDER_INCOMPLETE = "Incomplete";
	private static final String BORDER_COMPLETE = "Completed";
	private static final String BORDER_SIGN2 = ">";
	private static final String BORDER_SIGN3 = "<";
	private static final String MSG_NO_INCOMPLETE = "You do not have any incomplete task\n";
	private static final String MSG_NO_COMPLETE = "You do not have any completed task\n";
	
	
	private static String format = "|%1$-"+LEN_ID+"s"+
									"|%2$-"+LEN_IMPO+"s"+
								//	"|%3$-"+LEN_FINI+"s"+//
									"|%3$-"+LEN_NAME+"s"+
									"|%4$-"+LEN_TAG+"s"+
									"|%5$-"+LEN_STIME+"s"+
									"|%6$-"+LEN_ETIME+"s"+
								//	"|%8$-"+LEN_NAME+"s"+
								//	"|%9$-"+LEN_NOTE+"s"+
									"|\n";
	

	
	
	/** 
	 * 1. return full table 
	 * @param taskArr
	 */ 
	public static String getMultipleTasksInFull(ArrayList<Task> taskArr){
		ArrayList<Pair<Integer,Task>> pairArr= getSortedList(taskArr);
		String output = titleLine()+Line.borderLineStar()
							+getIncompleteTaskStr(pairArr)
							+getCompletedTaskStr(pairArr);
		return output;
	}
	
	
	/** 
	 *  2. return table with incomplete tasks only
	 *  @param taskArr
	 */		
	public static String getMultipleTasksIncomplete(ArrayList<Task> taskArr){
		ArrayList<Pair<Integer,Task>> pairArr= getSortedList(taskArr);
		String output = titleLine()+Line.borderLineStar()+getIncompleteTaskStr(pairArr);
		return output;
	}
	
	
	/** 
	 *  3. return table with completed tasks only
	 *  @param taskArr
	 */
	public static String getMultipleTasksComplete(ArrayList<Task> taskArr){
		ArrayList<Pair<Integer,Task>> pairArr= getSortedList(taskArr);
		String output =titleLine()+Line.borderLineStar()+getCompletedTaskStr(pairArr);
		return output;
	}
	
	// to prepare strings of completed tasks and borderlines
	private static String getCompletedTaskStr(ArrayList<Pair<Integer,Task>> pairArr){
		String outputLower =Line.borderLineWithWordsStarYellow(BORDER_COMPLETE);
		String outputComplete="";
		for(int i=0;i<pairArr.size();i++){
			if(pairArr.get(i)!=null){
				if(pairArr.get(i).getTask().isFinished()){
					outputComplete+=getSingleTask(pairArr.get(i).getTask(),pairArr.get(i).getId());
					outputComplete+=Line.borderLineStar();
				}
			}
		}
		if (outputComplete.equals("")){
			outputComplete+=MSG_NO_COMPLETE+Line.borderLineStar();
		}
		return outputLower+outputComplete;
	}
	
	// to prepare strings of incomplete tasks and borderlines
	private static String getIncompleteTaskStr(ArrayList<Pair<Integer,Task>> pairArr){
		String outputUpper =Line.borderLineWithWordsStarYellow(BORDER_INCOMPLETE);
		String outputIncomplete="";
		for(int i=0;i<pairArr.size();i++){
			if(pairArr.get(i)!=null){
				if(!pairArr.get(i).getTask().isFinished()){
					outputIncomplete+=getSingleTask(pairArr.get(i).getTask(),pairArr.get(i).getId());
					outputIncomplete+=Line.borderLineStar();			
				}
			}
		}
		if (outputIncomplete.equals("")){
			outputIncomplete+=MSG_NO_INCOMPLETE+Line.borderLineStar();
		}
		return outputUpper+outputIncomplete;
	}
	
	// to prepare pair arrays for sorting
	private static ArrayList<Pair<Integer, Task>> formPairArr(ArrayList<Task> taskArr) {
		ArrayList<Pair<Integer, Task>> pairArr = new ArrayList<Pair<Integer, Task>>();
		for(int i=0;i<taskArr.size();i++){
			if(taskArr.get(i)!=null){
				pairArr.add(new Pair<Integer, Task>(i,taskArr.get(i)));
			}
		}
		return pairArr;
	}
	
	
	/** 
	 *  4. return sorted array list of pairs
	 *  @param taskArr
	 */
	public static ArrayList<Pair<Integer, Task>> getSortedList(ArrayList<Task> taskArr){
		ArrayList<Pair<Integer, Task>> pairArr = formPairArr(taskArr);
		Collections.sort(pairArr,new PairComparator());
		return pairArr;
	}
	
	
	/** 
	 *  5. return table format of one single task
	 *  @param task
	 *  @param id
	 */
	public static String getSingleTask(Task task,int id){
		ArrayList<String> detailsInArray = getDetailsArr(task);
		ArrayList<ArrayList<String>> arrayOfAttributesArr = formArrayOfAttributesArr(detailsInArray);
		ArrayList<ArrayList<String>> arrayOfLines = formLineArr(arrayOfAttributesArr);
		String taskInLine = formLineString(arrayOfLines,id);
		return taskInLine;
	}
	
	
	/** 
	 *  6. return attributes of one single task in an array list
	 *  @param task
	 */
	public static ArrayList<String> getDetailsArr(Task task){
		ArrayList<String> details= new ArrayList<String>();
		for(int i=0;i<NUM_OF_ATTRI;i++){
			details.add("");
		}
		if(task.isImportant()){
			details.set(ID_IMPO, MSG_YES);
		}
		if(task.isFinished()){
			details.set(ID_FINI, MSG_YES);
		}
		details.set(ID_NAME, task.getName());
		details.set(ID_TAG, task.getTag());
		details.set(ID_STIME, task.getStartTimeStr());
		details.set(ID_ETIME, task.getEndTimeStr());
		details.set(ID_LOCA, task.getLocation()) ;
		details.set(ID_NOTE, task.getNote());
		
		return details;
	}
	
	// divide each attribute in to segments to fit the space of table 
	private static ArrayList<ArrayList<String>> 
		formArrayOfAttributesArr( ArrayList<String> details){
		ArrayList<ArrayList<String>> arrayOfAttributesArr = new ArrayList<ArrayList<String>>();
		for(int i=0;i<NUM_OF_ATTRI;i++){
			arrayOfAttributesArr.add(new ArrayList<String>());
		}
		arrayOfAttributesArr.get(ID_IMPO).add(details.get(ID_IMPO));
		arrayOfAttributesArr.get(ID_FINI).add(details.get(ID_FINI));
		arrayOfAttributesArr.get(ID_NAME).addAll(formAttributesArr(LEN_NAME, details.get(ID_NAME)));
		arrayOfAttributesArr.get(ID_TAG).addAll(formAttributesArr(LEN_TAG, details.get(ID_TAG)));
		arrayOfAttributesArr.get(ID_STIME).addAll(formAttributesArr(LEN_STIME, details.get(ID_STIME)));
		arrayOfAttributesArr.get(ID_ETIME).addAll(formAttributesArr(LEN_ETIME, details.get(ID_ETIME)));
		arrayOfAttributesArr.get(ID_LOCA).addAll(formAttributesArr(LEN_LOCA, details.get(ID_LOCA)));
		arrayOfAttributesArr.get(ID_NOTE).addAll(formAttributesArr(LEN_NOTE, details.get(ID_NOTE)));
		
		return arrayOfAttributesArr;
	}
	
	
	// to ensure words are splited according to spaces
	private static ArrayList<String> 
		formAttributesArr(int len, String text){
		ArrayList<String> attriArr = new ArrayList<String>();
		if(text!=null){
		if(text.length()<=len){
			attriArr.add(text);
		}
		else{
			int begin = 0;
			int end =len;
			while(end<text.length()){
				int count =0;
				while(!(text.charAt(end)==' ')){
					end--;
					count++;
					if(count==len){
						end+=len;
						break;
					}
				}
				attriArr.add(text.substring(begin, end));
				begin = end;
				end+=len;
				if(end>=text.length()){
					attriArr.add(text.substring(begin));
				}
			}
		}
		}
		return attriArr;
		
	}
	
	
	// to ensure the height of table is set according to the longest attributes
	private static ArrayList<ArrayList<String>> 
		formLineArr( ArrayList<ArrayList<String>> details){
		int[] colLen = new int[NUM_OF_ATTRI];
		int maxLen = 0;
		for(int i=0;i<NUM_OF_ATTRI;i++){
			if(details.get(i)!=null){
				colLen[i]=details.get(i).size();
			if(colLen[i]>maxLen){
				maxLen=colLen[i];
			}
			}
		}
		ArrayList<ArrayList<String>> lineBlock = new ArrayList<ArrayList<String>>();
		for(int i=0;i<maxLen;i++){
			lineBlock.add(new ArrayList<String>());
		}
		for(int m=0;m<maxLen;m++){
			for(int n=0;n<NUM_OF_ATTRI;n++){
				if(m>=colLen[n]){
					lineBlock.get(m).add(S);
				}
				else{
					lineBlock.get(m).add(details.get(n).get(m));
				}
			}
		}
		return lineBlock;
	}

	// to form all the lines of a table
	private static String 
		formLineString(ArrayList<ArrayList<String>> arr,int id){
		String taskInLine ="";
		if(id == NOT_ARR_LIST){  // to form the first line with index number
			taskInLine += String.format(format,S,arr.get(0).get(ID_IMPO)/*,arr.get(0).get(ID_FINI)*/
				,arr.get(0).get(ID_NAME),arr.get(0).get(ID_TAG),arr.get(0).get(ID_STIME)
				,arr.get(0).get(ID_ETIME)/*,arr.get(0).get(ID_LOCA),arr.get(0).get(ID_NOTE)*/);
		}
		else{                   // to form the first line without index number
			taskInLine += String.format(format,id+".",arr.get(0).get(ID_IMPO)/*,arr.get(0).get(ID_FINI)*/
					,arr.get(0).get(ID_NAME),arr.get(0).get(ID_TAG),arr.get(0).get(ID_STIME)
					,arr.get(0).get(ID_ETIME)/*,arr.get(0).get(ID_LOCA),arr.get(0).get(ID_NOTE)*/);
		}
		for(int i=1;i<arr.size();i++){
			taskInLine += String.format(format," ",arr.get(i).get(ID_IMPO)/*,arr.get(i).get(ID_FINI)*/
					,arr.get(i).get(ID_NAME),arr.get(i).get(ID_TAG),arr.get(i).get(ID_STIME)
					,arr.get(i).get(ID_ETIME)/*,arr.get(i).get(ID_LOCA),arr.get(i).get(ID_NOTE)*/);
		}
		return taskInLine;
		
	}
	
	private static String titleLine(){
		String titleLine = Line.borderLineStar();
		titleLine+= getHeaderString(HEADERS_ARR_N);
				/*String.format(format," ",HEADERS_ARR[0],HEADERS_ARR[1],HEADERS_ARR[2], 
				HEADERS_ARR[3], HEADERS_ARR[4],HEADERS_ARR[5],HEADERS_ARR[6],HEADERS_ARR[7]);*/
		return titleLine;
	}
	
	private static String getHeaderString(String[] arr){
		String headerString= "|    ";
		for(int i=0;i<arr.length;i++){
			headerString += ("|"+ANSI_CYAN+arr[i]+ANSI_RESET+addSpace(LEN_TITLE_ARR[i]));
		}
		headerString+="|\n";
		return headerString;
	}
	
	private static String addSpace(int n){
		String space="";
		for(int i=0;i<n;i++){
			space +=" ";
		}
		return space;
	}
	
	
	private static String welcomeString(String s){
		int sLen = s.length();
		int lineLen = LEN_TOTAL -sLen;
		int segment1 = lineLen/2;
		int segment2 = lineLen -segment1;
		String line="";
		for(int i=0;i<segment1;i++){
			line+= BORDER_SIGN2;
		}
		line+=s;
		for(int i=0;i<segment2;i++){
			line+= BORDER_SIGN3;
		}
		line+="\n";
		return line;
	}
}
```
###### \src\calendear\view\View.java
``` java
 *
 */

public class View {
	
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31;1m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	
	private static String formatRed = ANSI_RED+"%s"+ANSI_RESET;
	private static String formatYellow = ANSI_YELLOW+"%s"+ANSI_RESET;
	
	private static final String MSG_FILENAME_PROMPT = "Enter Filename: ";
	private static final String MSG_ADD = "Added task:\n";
	private static final String MSG_DELETE = "Deleted task:\n";
	private static final String MSG_UPDATE ="Updated task:\n";
	private static final String MSG_MARK ="Marked task:\n";
	private static final String MSG_TAG ="Tagged task:\n";
	private static final String MSG_DONE = "Finished task:\n";
	private static final String MSG_UNDO ="Undo successfully.\n";
	private static final String MSG_DISPLAY = "Display tasks:\n";
	private static final String MSG_SORT = "Sort results:\n";
	private static final String MSG_SEARCH ="Search results:\n";
	private static final String MSG_SEARCH_DONE="Search results (done):\n";
	private static final String MSG_LINKGOOGLE ="Linked to google";
	private static final String MSG_EXIT ="Exited";
	private static final String MSG_INVALID ="Invalid command";
	private static final String MSG_REDO ="Redo successfully.\n";
	
	private static final int ID_ADD = 0;
	private static final int ID_DELETE = 1;
	private static final int ID_UPDATE =2;
	private static final int ID_MARK =3;
	private static final int ID_TAG =4;
	private static final int ID_DONE = 5;
	private static final int ID_UNDO =6;
	private static final int ID_DISPLAY = 7;
	private static final int ID_SORT = 8;
	private static final int ID_SEARCH =9;
	private static final int ID_SEARCH_DONE=10;
	private static final int ID_LINKGOOGLE =11;
	private static final int ID_EXIT =12;
	private static final int ID_INVALID =13;
	private static final int ID_REDO =14;
	
	
	private static final String[] MSG_ARR_N = {MSG_ADD,MSG_DELETE,MSG_UPDATE,MSG_MARK,MSG_TAG,MSG_DONE,
		MSG_UNDO,MSG_DISPLAY,MSG_SORT,MSG_SEARCH,MSG_SEARCH_DONE,
			MSG_LINKGOOGLE,MSG_EXIT,MSG_INVALID,MSG_REDO};
	
	private static final String[] MSG_ARR = addColor(MSG_ARR_N);	
	private static final String MSG_COMMAND = ANSI_PURPLE+"Please enter command:"+ANSI_RESET;
	private static final int NOT_ARR_LIST = -1;
	
	private static String[] addColor(String[] arr){
		String[] colorArr= new String[arr.length];
		for(int i=0;i<arr.length;i++){
			colorArr[i] = String.format(formatRed,arr[i]);
		}
		return colorArr;
	}
	
	public static void displayWelcome(){
		System.out.println(Table.MSG_WELCOME);
	}
	
	public static void displayRequestForInput(){
		System.out.println(MSG_COMMAND);
	}
	
	public static void displayAdd(Task task){
		System.out.println(MSG_ARR[ID_ADD]+ Full.getTask(task));
	}
	
	public static void displayAddInLine(Task task){
		System.out.println(MSG_ARR[ID_ADD]+ Table.getSingleTask(task,NOT_ARR_LIST));
	}
	
	public static void displayDelete(Task task){
		System.out.println(MSG_ARR[ID_DELETE]+ Full.getTask(task));
	}
	
	public static void displayDeleteInLine(Task task){
		System.out.println(MSG_ARR[ID_DELETE]+ Table.getSingleTask(task,NOT_ARR_LIST));
	}
	
	public static void displayUpdate(Task task){
		System.out.println(MSG_ARR[ID_UPDATE]+ Full.getTask(task));
	}
	
	public static void displayUpdateInLine(Task task){
		System.out.println(MSG_ARR[ID_UPDATE]+ Table.getSingleTask(task,NOT_ARR_LIST));
	}
	
	public static void displayMark(Task task){
		System.out.println(MSG_ARR[ID_MARK]+ Full.getTask(task));
	}
	
	public static void displayMarkInLine(Task task){
		System.out.println(MSG_ARR[ID_MARK]+ Table.getSingleTask(task,NOT_ARR_LIST));
	}
	
	public static void displayTag(Task task){
		System.out.println(MSG_ARR[ID_TAG]+ Full.getTask(task));
	}
	
	public static void displayTagInLine(Task task){
		System.out.println(MSG_ARR[ID_TAG]+ Table.getSingleTask(task,NOT_ARR_LIST));
	}
	
	public static void displayDone(Task task){
		System.out.println(MSG_ARR[ID_DONE]+ Full.getTask(task));
	}
	
	public static void displayDoneInLine(Task task){
		System.out.println(MSG_ARR[ID_DONE]+ Table.getSingleTask(task,NOT_ARR_LIST));
	}
	
	public static void displayUndo(){
		System.out.println(MSG_ARR[ID_UNDO]);
	}
	
	public static void displayRedo(){
		System.out.println(MSG_ARR[ID_REDO]);
	}
	
	public static void displayDisplay(ArrayList<Task> taskArr){
		System.out.println(MSG_ARR[ID_DISPLAY]+ Full.getMultipleTasks(taskArr));
	}
	
	public static void displayDisplayInLine(ArrayList<Task> taskArr){
		System.out.println(MSG_ARR[ID_DISPLAY]+ Table.getMultipleTasksIncomplete(taskArr));
	}
	
	public static void displayDisplayAllInLine(ArrayList<Task> taskArr){
		System.out.println(MSG_ARR[ID_DISPLAY]+ Table.getMultipleTasksInFull(taskArr));
	}
	
	public static void displaySort(ArrayList<Task> taskArr){
		System.out.println(MSG_ARR[ID_SORT]+ Full.getMultipleTasks(taskArr));
	}
	
	public static void displaySortInLine(ArrayList<Task> taskArr){
		System.out.println(MSG_ARR[ID_SORT]+ Table.getMultipleTasksIncomplete(taskArr));
	}
	
	public static void displaySearch(ArrayList<Task> taskArr){
		System.out.println(MSG_ARR[ID_SEARCH]+ Full.getMultipleTasks(taskArr));
	}
	
	public static void displaySearchInLine(ArrayList<Task> taskArr){
		System.out.println(MSG_ARR[ID_SEARCH]+ Table.getMultipleTasksInFull(taskArr));
	}
	
	public static void displaySearchDoneInLine(ArrayList<Task> taskArr){
		System.out.println(MSG_ARR[ID_SEARCH_DONE]+ Table.getMultipleTasksComplete(taskArr));
	}
	
	public static void displayLinkGoogle(){
		System.out.println(MSG_ARR[ID_LINKGOOGLE]);
	}
	
	public static void displayExit(){
		System.out.println(MSG_ARR[ID_EXIT]);
	}
	
	public static void displayInvalid(){
		System.out.println(MSG_ARR[ID_INVALID]);
	}
	
	public static void displayError(String msg){
		System.out.println(String.format(formatYellow,msg));
	}
	
	public static void displayHelp(){
		System.out.println(HelpTable.getHelpTable());
	}
	
	public static void displayFileNamePrompt() {
		System.out.print(MSG_FILENAME_PROMPT);
	}

}
```
###### \src\test\ActionTest.java
``` java

	@Test
	public void testSearchStartTime() throws ParseException, LogicException, IOException{
		Action action1 = new Action("action5.txt");
		action1.exeClear(new CommandClear());
		Task test1 = new Task("search startTime1", 
				new GregorianCalendar( 2016,4,2,13,0),
				new GregorianCalendar( 2016,4,2,15,0));
		Task test2 = new Task("search startTime2", 
				new GregorianCalendar( 2016,4,2,12,0),
				new GregorianCalendar( 2016,4,2,16,0));
		Task test3 = new Task("search startTime3", 
				new GregorianCalendar( 2016,4,1,13,0),
				new GregorianCalendar( 2016,4,2,15,0));
		
		action1.exeAdd(new CommandAdd(test1));
		action1.exeAdd(new CommandAdd(test2));
		action1.exeAdd(new CommandAdd(test3));
		
		boolean[] toShow = {false, false, true, false, false, false, false, false, false};
		Object[] searchWith = {null, null, new GregorianCalendar( 2016,4,2,11,0), null, null, null, null, null};
		
		CommandSearch commandSearch = new CommandSearch(toShow, searchWith);
		
		ArrayList<Task> expectedResult = new ArrayList<Task>();
		expectedResult.add(null);
		expectedResult.add(test1);
		expectedResult.add(test2);
		expectedResult.add(null);
		
		ArrayList<Task> testResult = action1.exeSearch(commandSearch);	

		assertEquals(expectedResult,testResult);
		
		action1.exeClear(new CommandClear());
	}
	
	@Test

	public void testSearchEndTime() throws ParseException, LogicException, IOException{
		Action action1 = new Action("action6.txt");
		action1.exeClear(new CommandClear());
		Task test1 = new Task("search startTime1", 
				new GregorianCalendar( 2016,4,2,13,0),
				new GregorianCalendar( 2016,4,2,15,0));
		Task test2 = new Task("search startTime2", 
				new GregorianCalendar( 2016,4,2,12,0),
				new GregorianCalendar( 2016,4,2,16,0));
		Task test3 = new Task("search startTime3", 
				new GregorianCalendar( 2016,4,1,13,0),
				new GregorianCalendar( 2016,4,2,15,0));
		
		action1.exeAdd(new CommandAdd(test1));
		action1.exeAdd(new CommandAdd(test2));
		action1.exeAdd(new CommandAdd(test3));
		
		boolean[] toShow = {false, false, false, true, false, false, false, false, false};
		Object[] searchWith = {null, null, null, new GregorianCalendar( 2016,4,2,15,30), null, null, null, null};
		
		CommandSearch commandSearch = new CommandSearch(toShow, searchWith);
		
		ArrayList<Task> expectedResult = new ArrayList<Task>();
		expectedResult.add(null);
		expectedResult.add(test1);
		expectedResult.add(null);
		expectedResult.add(test3);
		
		ArrayList<Task> testResult = action1.exeSearch(commandSearch);	

		assertEquals(expectedResult,testResult);
		
		action1.exeClear(new CommandClear());
	}
	
	@Test
	public void testSearchStartAndEndTime() throws ParseException, LogicException, IOException{
		Action action1 = new Action("action7.txt");
		action1.exeClear(new CommandClear());
		Task test1 = new Task("search startTime1", 
				new GregorianCalendar( 2016,1,1,13,0),
				new GregorianCalendar( 2016,1,30,15,0));
		Task test2 = new Task("search startTime2", 
				new GregorianCalendar( 2016,1,12,12,0),
				new GregorianCalendar( 2016,1,21,16,0));
		Task test3 = new Task("search startTime3", 
				new GregorianCalendar( 2016,1,15,15,0));
		
		action1.exeAdd(new CommandAdd(test1));
		action1.exeAdd(new CommandAdd(test2));
		action1.exeAdd(new CommandAdd(test3));
		
		boolean[] toShow = {false, false, true, true, false, false, false, false, false};
		Object[] searchWith = {null, null, new GregorianCalendar( 2016,1,10,11,0), 
				new GregorianCalendar( 2016,1,20,11,0), null, null, null, null};
		
		CommandSearch commandSearch = new CommandSearch(toShow, searchWith);
		
		ArrayList<Task> expectedResult = new ArrayList<Task>();
		expectedResult.add(null);
		expectedResult.add(null);
		expectedResult.add(null);
		expectedResult.add(test3);
		
		ArrayList<Task> testResult = action1.exeSearch(commandSearch);	

		assertEquals(expectedResult,testResult);
		
		action1.exeClear(new CommandClear());
	}
	
	@Test
	public void testSearchTag() throws ParseException, LogicException, IOException{
		Action action1 = new Action("action8.txt");
		action1.exeClear(new CommandClear());
		Task test1 = new Task("tag test1");
		Task test2 = new Task("tag test2");
		Task test3 = new Task("tag test3");
		
		test1.setTag("school");
		test2.setTag("school # social");
		test3.setTag("social");
		
		action1.exeAdd(new CommandAdd(test1));
		action1.exeAdd(new CommandAdd(test2));
		action1.exeAdd(new CommandAdd(test3));
		
		boolean[] toShow = {false, false, false, false, false, false, true, false, false};
		Object[] searchWith = {null, null, null, null, null, null, "school", null};
		
		CommandSearch commandSearch = new CommandSearch(toShow, searchWith);
		
		ArrayList<Task> expectedResult = new ArrayList<Task>();
		expectedResult.add(null);
		expectedResult.add(test1);
		expectedResult.add(test2);
		expectedResult.add(null);
		
		ArrayList<Task> testResult = action1.exeSearch(commandSearch);	


		assertEquals(expectedResult,testResult);
		
		action1.exeClear(new CommandClear());
	}
	
	@Test
	public void testSearchLocation() throws ParseException, LogicException, IOException{
		Action action1 = new Action("action9.txt");
		action1.exeClear(new CommandClear());
		Task test1 = new Task("location test1");
		Task test2 = new Task("location test2");
		Task test3 = new Task("location test3");
		
		test1.setLocation("school");
		test2.setLocation("home");
		test3.setLocation("shopping mall");
		
		action1.exeAdd(new CommandAdd(test1));
		action1.exeAdd(new CommandAdd(test2));
		action1.exeAdd(new CommandAdd(test3));
		
		boolean[] toShow = {false, false, false, false, true, false, false, false, false};
		Object[] searchWith = {null, null, null, null, "school", null, null, null};
		
		CommandSearch commandSearch = new CommandSearch(toShow, searchWith);
		
		ArrayList<Task> expectedResult = new ArrayList<Task>();
		expectedResult.add(null);
		expectedResult.add(test1);
		expectedResult.add(null);
		expectedResult.add(null);
		
		ArrayList<Task> testResult = action1.exeSearch(commandSearch);	


		assertEquals(expectedResult,testResult);
		
		action1.exeClear(new CommandClear());
	}
    
	
	@Test
	public void testUndoUpdate() throws ParseException, LogicException, IOException{
		Action action1 = new Action("action10.txt");
		action1.exeClear(new CommandClear());
		int nextIndex = action1.getAmount();
		
		Task task = new Task();
		task.setName(originalName);
		task.setType(originalType);
		task.setStartTime(originalStartTime);
		task.setEndTime(originalEndTime);
		task.setTag(originalTag);
		task.setIsImportant(originalImportance);
		task.setIsFinished(originalDone);
		
		action1.exeAdd(new CommandAdd(task));
		boolean[] chklst = {true, true, true, true, true, true, true, true, true};
		Object[] info = {finalName, finalType, finalStartTime, finalEndTime, finalLocation, finalNote, 
				finalTag, finalImportance, finalDone};
		CommandUpdate commandUpdate = new CommandUpdate(nextIndex, chklst, info);
		action1.exeUpdate(commandUpdate);
		action1.exeUndo();
		
		assertEquals(originalName,task.getName() );
		assertEquals(originalType,task.getType() );
		assertEquals(originalStartTime,task.getStartTime());
		assertEquals(originalEndTime,task.getEndTime());
		assertEquals(originalTag,task.getTag());
		assertEquals(originalImportance,task.isImportant());
		assertEquals(originalDone,task.isFinished());
		
		action1.exeClear(new CommandClear());
		
	}
	
	@Test
	public void testRedoUpdate() throws ParseException, LogicException, IOException{
		Action action1 = new Action("action10.txt");
		action1.exeClear(new CommandClear());
		int nextIndex = action1.getAmount();
		
		Task task = new Task();
		task.setName(originalName);
		task.setType(originalType);
		task.setStartTime(originalStartTime);
		task.setEndTime(originalEndTime);
		task.setTag(originalTag);
		task.setIsImportant(originalImportance);
		task.setIsFinished(originalDone);
		
		action1.exeAdd(new CommandAdd(task));
		boolean[] chklst = {true, true, true, true, true, true, true, true, true};
		Object[] info = {finalName, finalType, finalStartTime, finalEndTime, finalLocation, finalNote, 
				finalTag, finalImportance, finalDone};
		CommandUpdate commandUpdate = new CommandUpdate(nextIndex, chklst, info);
		action1.exeUpdate(commandUpdate);
		action1.exeUndo();
		
		
		assertEquals(originalName,task.getName() );
		assertEquals(originalType,task.getType() );
		assertEquals(originalStartTime,task.getStartTime());
		assertEquals(originalEndTime,task.getEndTime());
		assertEquals(originalTag,task.getTag());
		assertEquals(originalImportance,task.isImportant());
		assertEquals(originalDone,task.isFinished());
		
		action1.exeRedo();
		
		assertEquals(finalName, task.getName() );
		assertEquals(finalType,task.getType() );
		assertEquals(finalStartTime,task.getStartTime() );
		assertEquals(finalEndTime,task.getEndTime());
		assertEquals(finalTag, task.getTag());
		assertEquals(finalImportance,task.isImportant());
		assertEquals(finalDone, task.isFinished() );
		
	}
}
```
###### \src\test\ViewTest.java
``` java
 *
 */

public class ViewTest {
	@Test
	public void testTable(){
		ArrayList<Task> taskArr = new ArrayList<Task>();
		taskArr.add(new Task("go to school "));
		taskArr.get(0).setLocation("NUS NUS NUS NUS NUS");
		taskArr.get(0).setTag("study related ");
		taskArr.get(0).setNote("don't forget don't forget ");
		taskArr.get(0).setIsFinished(true);
		taskArr.get(0).setIsImportant(true);
		
		assertEquals(Table.getDetailsArr(taskArr.get(0)).get(7),taskArr.get(0).getNote());
		
	}
	
	@Test
	public void testSort(){
		ArrayList<Task> taskArr = new ArrayList<Task>();
		taskArr.add(new Task("learn violin "));
		taskArr.get(0).setIsFinished(false);
		taskArr.get(0).setIsImportant(false);
		
		taskArr.add(new Task("go for piano lesson ",
				new GregorianCalendar(2016,4,2,13,0),
				new GregorianCalendar(2016,4,2,15,0)));
		taskArr.get(1).setIsFinished(false);
		taskArr.get(1).setIsImportant(true);
		
		taskArr.add(new Task("hand in piano homework ",
				new GregorianCalendar(2016,4,1,15,0)));
		taskArr.get(2).setIsFinished(false);
		taskArr.get(2).setIsImportant(true);
		
		taskArr.add(new Task("buy piano book",
				new GregorianCalendar(2016,3,18,17,0)));
		taskArr.get(3).setIsFinished(true);
		taskArr.get(3).setIsImportant(false);
		
		taskArr.add(new Task("go shopping"));
		taskArr.get(4).setType(TASK_TYPE.FLOATING);
		taskArr.get(4).setIsFinished(true);
		taskArr.get(4).setIsImportant(false);
		
		taskArr.add(new Task("practice playing the piano ",
				new GregorianCalendar(2016,4,1,8,0),
				new GregorianCalendar(2016,4,1,10,0)));
		taskArr.get(5).setIsFinished(true);
		taskArr.get(5).setIsImportant(true);
		
		ArrayList<Pair<Integer,Task>> sortedList = Table.getSortedList(taskArr);
		String expectedResult = "120534";
		String testResult = "";
		for(int i=0;i<6;i++){
			testResult += String.valueOf(sortedList.get(i).getId());
		}
		assertEquals(expectedResult,testResult);
		
	}
	
	

}
```

# Pan Jiyun
###### /src/calendear/view/Full.java
``` java
public class Full {
	
	private static final String HEADER_NAME = "Task name:";
	private static final String HEADER_TAG = "Tag:";
	private static final String HEADER_STARTTIME = "Start time:";
	private static final String HEADER_ENDTIME = "End time:";
	private static final String HEADER_DUETIME = "Due time:";
	private static final String HEADER_RECURRING_ENDTIME = "Next due time:";
	private static final String HEADER_LOCATION = "Location:";
	private static final String HEADER_NOTE = "Note:";
	private static final String HEADER_IMPORTANCE = "Important:";
	private static final String HEADER_FINISHED = "Finished:";
	private static final String[] HEADERS_ARR_N = {HEADER_IMPORTANCE,HEADER_FINISHED,HEADER_NAME, 
			HEADER_TAG, HEADER_STARTTIME,HEADER_DUETIME,HEADER_ENDTIME,HEADER_LOCATION,HEADER_NOTE};
	private static final String S = " ";
	
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
			details+=HEADERS_ARR[2]+task.getName()+"\n";
		}
		if(task.getTag()!=null){
			details+=HEADERS_ARR[3]+task.getTag()+"\n";
		}	
		if((task.getStartTimeStr()!=null)&&
				(!task.getType().equals(TASK_TYPE.DEADLINE))){
			details+=HEADERS_ARR[4]+task.getStartTimeStr()+"\n";
		}
		if(task.getEndTimeStr()!=null){
			if(task.getType().equals(TASK_TYPE.DEADLINE)){
				details+=HEADERS_ARR[5]+task.getEndTimeStr()+"\n";
			}
			else{
				details+=HEADERS_ARR[6]+task.getEndTimeStr()+"\n";
			}
		}
		if(task.getLocation()!=null){
			details+=HEADERS_ARR[7]+task.getLocation()+"\n";
		}
		if(task.getNote()!=null){
			details+=HEADERS_ARR[8]+task.getNote()+"\n";
		}
		if(task.isImportant()){
			details+=HEADERS_ARR[0]+ MSG_YES+ "\n";
		}
		if(task.isFinished()){
			details+=HEADERS_ARR[1]+MSG_YES+"\n";
		}
		else if(!task.isFinished()){
			details+=HEADERS_ARR[1]+MSG_NO+"\n";
		}
		return details;
	}
}
```
###### /src/calendear/view/Pair.java
``` java

public class Pair<Integer,Task> {
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
###### /src/calendear/view/PairComparator.java
``` java

/* order of display:
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



public class PairComparator implements Comparator<Pair> {
	@Override
	public int compare(Pair p1,Pair p2){
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
		int i;
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
###### /src/calendear/view/Table.java
``` java

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
	
	private static String formatRed = ANSI_RED+"%s"+ANSI_RESET;
	private static String formatYellow = ANSI_YELLOW+"%s"+ANSI_RESET;
	private static String formatCyan = ANSI_CYAN+"%s"+ANSI_RESET;
	private static String formatGreen = ANSI_GREEN+"%s"+ANSI_RESET;
	
	private static final String HEADER_NAME = "Task name:";
	private static final String HEADER_TAG = "Tag:";
	private static final String HEADER_STARTTIME = "Start time:";
	private static final String HEADER_ENDTIME = "End time:";
	private static final String HEADER_DUETIME = "Due time:";
	private static final String HEADER_RECURRING_ENDTIME = "Next due time:";
	private static final String HEADER_LOCATION = "Location:";
	private static final String HEADER_NOTE = "Note:";
	private static final String HEADER_IMPORTANCE = "Important:";
	private static final String HEADER_FINISHED = "Finished:";
	private static final String[] HEADERS_ARR_N = {HEADER_IMPORTANCE/*,HEADER_FINISHED*/,HEADER_NAME, 
			HEADER_TAG, HEADER_STARTTIME,HEADER_ENDTIME/*,HEADER_LOCATION,HEADER_NOTE*/};

	public static final String MSG_WELCOME = ANSI_PURPLE+welcomeString("Welcome to Calendear!")+ANSI_RESET;
	private static final String MSG_COMMAND = ANSI_PURPLE+"Please enter command:"+ANSI_RESET;
	private static final String MSG_YES = "Yes";
	private static final String MSG_NO = "No";
	
	private static final String S = " ";
	private static final int NUM_OF_ATTRI = 8;
	private static final int LEN_ID = 4;
	private static final int LEN_IMPO = 10;
	private static final int LEN_FINI = 10;
	private static final int LEN_NAME = 16;
	private static final int LEN_STIME = 12;
	private static final int LEN_ETIME = 12;
	private static final int LEN_TAG = 16;
	private static final int LEN_LOCA = 16;
	private static final int LEN_NOTE = 16;
	private static final int NOT_ARR_LIST = -1;
	private static final int NUM_OF_TITLE_BAR = 7;
	private static final int LEN_TOTAL = LEN_ID+LEN_IMPO/*+LEN_FINI*/+
						LEN_NAME+LEN_STIME+LEN_ETIME+LEN_TAG+/*LEN_LOCA+LEN_NOTE+*/NUM_OF_TITLE_BAR;
	private static final int[] LEN_TITLE_ARR = {0/*,1*/,6,12,1,3,7,11};
	private static final String BORDER_SIGN = "*";
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
	
	public static ArrayList<String> getDetailsArr(Task task){
		ArrayList<String> details= new ArrayList<String>();
		for(int i=0;i<NUM_OF_ATTRI;i++){
			details.add("");
		}
		if(task.isImportant()){
			details.set(0, "Yes");
		}
		if(task.isFinished()){
			details.set(1, "Yes");
		}
		details.set(2, task.getName());
		details.set(3, task.getTag());
		details.set(4, task.getStartTimeStr());
		details.set(5, task.getEndTimeStr());
		details.set(6, task.getLocation()) ;
		details.set(7, task.getNote());
		
		return details;
	}
 	
	
	public static String getMultipleTasksInFull(ArrayList<Task> taskArr){
		ArrayList<Pair<Integer,Task>> pairArr = formPairArr(taskArr);
		Collections.sort(pairArr,new PairComparator());
		String outputUpper = titleLine()+borderLine()+borderLineWithWords(BORDER_INCOMPLETE);
		String outputLower =borderLineWithWords(BORDER_COMPLETE);
		String outputComplete="";
		String outputIncomplete="";
		for(int i=0;i<pairArr.size();i++){
			if(pairArr.get(i)!=null){
				if(pairArr.get(i).getTask().isFinished()){
					outputComplete+=getSingleTask(pairArr.get(i).getTask(),pairArr.get(i).getId());
					outputComplete+=borderLine();
				}
				else {
					outputIncomplete+=getSingleTask(pairArr.get(i).getTask(),pairArr.get(i).getId());
					outputIncomplete+=borderLine();
				}
			}
		}
		if (outputIncomplete.equals("")){
			outputIncomplete+=MSG_NO_INCOMPLETE+borderLine();
		}
		if (outputComplete.equals("")){
			outputComplete+=MSG_NO_COMPLETE+borderLine();
		}
		return outputUpper+outputIncomplete+outputLower+outputComplete;
	}
	
	public static String getMultipleTasksIncomplete(ArrayList<Task> taskArr){
		ArrayList<Pair<Integer,Task>> pairArr = formPairArr(taskArr);
		Collections.sort(pairArr,new PairComparator());
		String outputUpper = titleLine()+borderLine()+borderLineWithWords(BORDER_INCOMPLETE);
		String outputIncomplete="";
		for(int i=0;i<pairArr.size();i++){
			if(pairArr.get(i)!=null){
				if(!pairArr.get(i).getTask().isFinished()){
					outputIncomplete+=getSingleTask(pairArr.get(i).getTask(),pairArr.get(i).getId());
					outputIncomplete+=borderLine();
					
				}
			}
		}
		if (outputIncomplete.equals("")){
			outputIncomplete+=MSG_NO_INCOMPLETE+borderLine();
		}
		return outputUpper+outputIncomplete;
	}
	
	public static String getMultipleTasksComplete(ArrayList<Task> taskArr){
		ArrayList<Pair<Integer,Task>> pairArr = formPairArr(taskArr);
		Collections.sort(pairArr,new PairComparator());
		String outputLower =titleLine()+borderLine()+borderLineWithWords(BORDER_COMPLETE);
		String outputComplete="";
		for(int i=0;i<pairArr.size();i++){
			if(pairArr.get(i)!=null){
				if(pairArr.get(i).getTask().isFinished()){
					outputComplete+=getSingleTask(pairArr.get(i).getTask(),pairArr.get(i).getId());
					outputComplete+=borderLine();
				}
			}
		}
		if (outputComplete.equals("")){
			outputComplete+=MSG_NO_COMPLETE+borderLine();
		}
		return outputLower+outputComplete;
	}



	private static ArrayList<Pair<Integer, Task>> formPairArr(ArrayList<Task> taskArr) {
		ArrayList<Pair<Integer, Task>> pairArr = new ArrayList<Pair<Integer, Task>>();
		for(int i=0;i<taskArr.size();i++){
			if(taskArr.get(i)!=null){
				pairArr.add(new Pair<Integer, Task>(i,taskArr.get(i)));
			}
		}
		return pairArr;
	}

	public static String getTask(Task task,int id){
		ArrayList<String> detailsInArray = getDetailsArr(task);
		ArrayList<ArrayList<String>> arrayOfAttributesArr = formArrayOfAttributesArr(detailsInArray);
		ArrayList<ArrayList<String>> arrayOfLines = formLineArr(arrayOfAttributesArr);
		String taskInLine = titleLine()+borderLine()+formLineString(arrayOfLines,id)+borderLine();
		return taskInLine;
	}
	
	public static String getSingleTask(Task task,int id){
		ArrayList<String> detailsInArray = getDetailsArr(task);
		ArrayList<ArrayList<String>> arrayOfAttributesArr = formArrayOfAttributesArr(detailsInArray);
		ArrayList<ArrayList<String>> arrayOfLines = formLineArr(arrayOfAttributesArr);
		String taskInLine = formLineString(arrayOfLines,id);
		return taskInLine;
	}
	
	private static ArrayList<ArrayList<String>> 
		formArrayOfAttributesArr( ArrayList<String> details){
		ArrayList<ArrayList<String>> arrayOfAttributesArr = new ArrayList<ArrayList<String>>();
		for(int i=0;i<NUM_OF_ATTRI;i++){
			arrayOfAttributesArr.add(new ArrayList<String>());
		}
		arrayOfAttributesArr.get(0).add(details.get(0));
		arrayOfAttributesArr.get(1).add(details.get(1));
		arrayOfAttributesArr.get(2).addAll(formAttributesArr(LEN_NAME, details.get(2)));
		arrayOfAttributesArr.get(3).addAll(formAttributesArr(LEN_TAG, details.get(3)));
		arrayOfAttributesArr.get(4).addAll(formAttributesArr(LEN_STIME, details.get(4)));
		arrayOfAttributesArr.get(5).addAll(formAttributesArr(LEN_ETIME, details.get(5)));
		arrayOfAttributesArr.get(6).addAll(formAttributesArr(LEN_LOCA, details.get(6)));
		arrayOfAttributesArr.get(7).addAll(formAttributesArr(LEN_NOTE, details.get(7)));
		
		return arrayOfAttributesArr;
	}
	
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

	
	private static String 
		formLineString(ArrayList<ArrayList<String>> arr,int id){
		String taskInLine ="";
		if(id == NOT_ARR_LIST){
			taskInLine += String.format(format,S,arr.get(0).get(0)/*,arr.get(0).get(1)*/
				,arr.get(0).get(2),arr.get(0).get(3),arr.get(0).get(4)
				,arr.get(0).get(5)/*,arr.get(0).get(6),arr.get(0).get(7)*/);
		}
		else{
			taskInLine += String.format(format,id+".",arr.get(0).get(0)/*,arr.get(0).get(1)*/
					,arr.get(0).get(2),arr.get(0).get(3),arr.get(0).get(4)
					,arr.get(0).get(5)/*,arr.get(0).get(6),arr.get(0).get(7)*/);
		}
		for(int i=1;i<arr.size();i++){
			taskInLine += String.format(format," ",arr.get(i).get(0)/*,arr.get(i).get(1)*/
					,arr.get(i).get(2),arr.get(i).get(3),arr.get(i).get(4)
					,arr.get(i).get(5)/*,arr.get(i).get(6),arr.get(i).get(7)*/);
		}
		return taskInLine;
		
	}
	
	private static String titleLine(){
		String titleLine = borderLine();
		titleLine+= getHeaderString(HEADERS_ARR_N);
				/*String.format(format," ",HEADERS_ARR[0],HEADERS_ARR[1],HEADERS_ARR[2], 
				HEADERS_ARR[3], HEADERS_ARR[4],HEADERS_ARR[5],HEADERS_ARR[6],HEADERS_ARR[7]);*/
		return titleLine;
	}
	
	private static String borderLine(){
		String border ="";
		for(int i=0;i<LEN_TOTAL;i++){
			border+= BORDER_SIGN;
		}
		border+="\n";
		String nborder = String.format(formatGreen, border);
		return nborder;
	}
	
	private static String borderLineWithWords(String s){
		int sLen = s.length();
		int lineLen = LEN_TOTAL -sLen;
		int segment1 = lineLen/2;
		int segment2 = lineLen -segment1;
		String line="";
		for(int i=0;i<segment1;i++){
			line+= BORDER_SIGN;
		}
		line+=s;
		for(int i=0;i<segment2;i++){
			line+= BORDER_SIGN;
		}
		line+="\n";
		String nLine = String.format(formatYellow, line);
		return nLine;
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
		String nLine = String.format(formatRed, line);
		return nLine;
	}
}
```
###### /src/calendear/view/View.java
``` java

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
	
	private static final String MSG_ADD = "Added task:\n";
	private static final String MSG_DELETE = "Deleted task:\n";
	private static final String MSG_DISPLAY = "Display tasks:\n";
	private static final String MSG_SORT = "Sort results:\n";
	private static final String MSG_SEARCH ="Search results:\n";
	private static final String MSG_UPDATE ="Updated task:\n";
	private static final String MSG_MARK ="Marked task:\n";
	private static final String MSG_DONE = "Finished task:\n";
	private static final String MSG_TAG ="Tagged task:\n";
	private static final String MSG_UNDO ="Undo successfully.\n";
	private static final String MSG_LINKGOOGLE ="Linked to google";
	private static final String MSG_EXIT ="Exited";
	private static final String MSG_INVALID ="Invalid command";
	
	private static final String[] MSG_ARR_N = {MSG_ADD,MSG_DELETE,MSG_UPDATE,MSG_MARK,MSG_TAG,MSG_DONE,
		MSG_UNDO,MSG_DISPLAY,MSG_SORT,MSG_SEARCH,
			MSG_LINKGOOGLE,MSG_EXIT,MSG_INVALID};
	
	private static final String[] MSG_ARR = addColor(MSG_ARR_N);
	
	private static final String MSG_WELCOME = ANSI_PURPLE+"Welcome to calendear!"+ANSI_RESET;
	private static final String MSG_COMMAND = ANSI_PURPLE+"Please enter command:"+ANSI_RESET;
	private static final String MSG_YES = "Yes";
	private static final String MSG_NO = "No";
	
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
		System.out.println(MSG_ARR[0]+ Full.getTask(task));
	}
	
	public static void displayAddInLine(Task task){
		System.out.println(MSG_ARR[0]+ Table.getTask(task,NOT_ARR_LIST));
	}
	
	public static void displayDelete(Task task){
		System.out.println(MSG_ARR[1]+ Full.getTask(task));
	}
	
	public static void displayDeleteInLine(Task task){
		System.out.println(MSG_ARR[1]+ Table.getTask(task,NOT_ARR_LIST));
	}
	
	public static void displayUpdate(Task task){
		System.out.println(MSG_ARR[2]+ Full.getTask(task));
	}
	
	public static void displayUpdateInLine(Task task){
		System.out.println(MSG_ARR[2]+ Table.getTask(task,NOT_ARR_LIST));
	}
	
	public static void displayMark(Task task){
		System.out.println(MSG_ARR[3]+ Full.getTask(task));
	}
	
	public static void displayMarkInLine(Task task){
		System.out.println(MSG_ARR[3]+ Table.getTask(task,NOT_ARR_LIST));
	}
	
	public static void displayTag(Task task){
		System.out.println(MSG_ARR[4]+ Full.getTask(task));
	}
	
	public static void displayTagInLine(Task task){
		System.out.println(MSG_ARR[4]+ Table.getTask(task,NOT_ARR_LIST));
	}
	
	public static void displayDone(Task task){
		System.out.println(MSG_ARR[5]+ Full.getTask(task));
	}
	
	public static void displayDoneInLine(Task task){
		System.out.println(MSG_ARR[5]+ Table.getTask(task,NOT_ARR_LIST));
	}
	
	public static void displayUndo(boolean successful){
		if (successful){
			System.out.println(MSG_ARR[6]);
		}
	}
	
	public static void displayUndoInLine(Task task){
		System.out.println(MSG_ARR[6]+ Table.getTask(task,NOT_ARR_LIST));
	}
	
	public static void displayDisplay(ArrayList<Task> taskArr){
		System.out.println(MSG_ARR[7]+ Full.getMultipleTasks(taskArr));
	}
	
	public static void displayDisplayInLine(ArrayList<Task> taskArr){
		System.out.println(MSG_ARR[7]+ Table.getMultipleTasksIncomplete(taskArr));
	}
	
	public static void displaySort(ArrayList<Task> taskArr){
		System.out.println(MSG_ARR[8]+ Full.getMultipleTasks(taskArr));
	}
	
	public static void displaySortInLine(ArrayList<Task> taskArr){
		System.out.println(MSG_ARR[8]+ Table.getMultipleTasksIncomplete(taskArr));
	}
	
	public static void displaySearch(ArrayList<Task> taskArr){
		System.out.println(MSG_ARR[9]+ Full.getMultipleTasks(taskArr));
	}
	
	public static void displaySearchInLine(ArrayList<Task> taskArr){
		System.out.println(MSG_ARR[9]+ Table.getMultipleTasksIncomplete(taskArr));
	}
	
	
	public static void displayLinkGoogle(){
		System.out.println(MSG_ARR[10]);
	}
	
	public static void displayExit(){
		System.out.println(MSG_ARR[11]);
	}
	
	public static void displayInvalid(){
		System.out.println(MSG_ARR[12]);
	}
	
	public static void displayError(String msg){
		System.out.println(msg);
	}
	

}
```

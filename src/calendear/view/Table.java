package calendear.view;

import java.util.ArrayList;

import java.util.Collections;

import calendear.util.Task;


/**
 * Tables containing tasks for display
 * @@author A0107350U
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

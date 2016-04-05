package calendear.view;

import java.util.ArrayList;

import java.util.Collections;
import java.util.HashMap;

import calendear.util.Task;



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
 	
	
	public static String getMultipleTasks(ArrayList<Task> taskArr){
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

package calendear.view;

import java.util.ArrayList;

import calendear.util.Task;

//import calendear.util.Task;


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
	private static String formatCyan = ANSI_CYAN+"%s"+ANSI_RESET;
	private static String formatGreen = ANSI_GREEN+"%s"+ANSI_RESET;
	
	private static final String HEADER_NAME = "task name:";
	private static final String HEADER_TAG = "tag:";
	private static final String HEADER_STARTTIME = "start time:";
	private static final String HEADER_ENDTIME = "end time:";
	private static final String HEADER_DUETIME = "due time:";
	private static final String HEADER_RECURRING_ENDTIME = "next due time:";
	private static final String HEADER_LOCATION = "location:";
	private static final String HEADER_NOTE = "note:";
	private static final String HEADER_IMPORTANCE = "important:";
	private static final String HEADER_FINISHED = "finished:";
	private static final String[] HEADERS_ARR_N = {HEADER_IMPORTANCE/*,HEADER_FINISHED*/,HEADER_NAME, 
			HEADER_TAG, HEADER_STARTTIME,HEADER_ENDTIME/*,HEADER_LOCATION,HEADER_NOTE*/};

	private static final String MSG_WELCOME = ANSI_PURPLE+"welcome to calendear!"+ANSI_RESET;
	private static final String MSG_COMMAND = ANSI_PURPLE+"command:"+ANSI_RESET;
	private static final String MSG_YES = "yes";
	private static final String MSG_NO = "no";
	
	private static final String S = " ";
	private static final int NUM_OF_ATTRI = 8;
	private static final int LEN_ID = 2;
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
	private static final String BORDER_COMPLETE = "Complete";
	
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
		String headerString= "|  ";
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
			details.set(0, "√");
		}
		if(task.isFinished()){
			details.set(1, "√");
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
		String outputUpper = titleLine()+borderLine()+borderLineWithWords(BORDER_INCOMPLETE);
		String outputLower =borderLineWithWords(BORDER_COMPLETE);
		String outputComplete="";
		String outputIncomplete="";
		for(int i=0;i<taskArr.size();i++){
			if(taskArr.get(i)!=null){
				if(taskArr.get(i).isFinished()){
					outputComplete+=getSingleTask(taskArr.get(i),i);
					outputComplete+=borderLine();
				}
				else {
					outputIncomplete+=getSingleTask(taskArr.get(i),i);
					outputIncomplete+=borderLine();
				}
			}
		}
		if (outputIncomplete.equals("")){
			outputIncomplete+="You do not have incompleted task\n"+borderLine();
		}
		if (outputComplete.equals("")){
			outputComplete+="You do not have completed task\n"+borderLine();
		}
		return outputUpper+outputIncomplete+outputLower+outputComplete;
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
		String nLine = String.format(formatRed, line);
		return nLine;
	}
}

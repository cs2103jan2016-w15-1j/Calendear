package calendear.view;
import java.util.ArrayList;

import calendear.util.TASK_TYPE;
import calendear.util.Task;

//@@author Pan Jiyun
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
	/*	if(task.getLocation()!=null){
			details+=HEADERS_ARR[7]+task.getLocation()+"\n";
		}
		if(task.getNote()!=null){
			details+=HEADERS_ARR[8]+task.getNote()+"\n";
		} */
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

package calendear.view;
import java.util.ArrayList;

import calendear.util.TASK_TYPE;
import calendear.util.Task;


/**
 * Full attributes of tasks for display
 * @@author A0107350U
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

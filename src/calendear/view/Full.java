package calendear.view;
import java.util.ArrayList;

import calendear.util.TASK_TYPE;
import calendear.util.Task;


public class Full {
	
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
	private static final String[] HEADERS = {HEADER_IMPORTANCE,HEADER_FINISHED,HEADER_NAME, 
			HEADER_TAG, HEADER_STARTTIME,HEADER_ENDTIME,HEADER_LOCATION,HEADER_NOTE};
	private static final String S = " ";
	
	private static final String MSG_YES = "yes";
	private static final String MSG_NO = "no";
	
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
			details+=HEADER_NAME+task.getName()+"\n";
		}
		if(task.getTag()!=null){
			details+=HEADER_TAG+task.getTag()+"\n";
		}	
		if((task.getStartTimeStr()!=null)&&
				(!task.getType().equals(TASK_TYPE.DEADLINE))){
			details+=HEADER_STARTTIME+task.getStartTimeStr()+"\n";
		}
		if(task.getEndTimeStr()!=null){
			if(task.getType().equals(TASK_TYPE.DEADLINE)){
				details+=HEADER_DUETIME+task.getEndTimeStr()+"\n";
			}
			else{
				details+=HEADER_ENDTIME+task.getEndTimeStr()+"\n";
			}
		}
		if(task.getLocation()!=null){
			details+=HEADER_LOCATION+task.getLocation()+"\n";
		}
		if(task.getNote()!=null){
			details+=HEADER_NOTE+task.getNote()+"\n";
		}
		if(task.isImportant()){
			details+=HEADER_IMPORTANCE+ MSG_YES+ "\n";
		}
		if(task.isFinished()){
			details+=HEADER_FINISHED+MSG_YES+"\n";
		}
		else if(!task.isFinished()){
			details+=HEADER_FINISHED+MSG_NO+"\n";
		}
		return details;
	}
}

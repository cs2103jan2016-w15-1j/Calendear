package calendear.view;

import java.util.ArrayList;

import calendear.util.Task;

public class View {
	
	private static final String HEADER_NAME = "task name: ";
	private static final String HEADER_TAG = "tag:\n";
	private static final String HEADER_STARTTIME = "start time: ";
	private static final String HEADER_ENDTIME = "due time: ";
	private static final String HEADER_RECURRING_ENDTIME = "next due time:  ";
	private static final String HEADER_LOCATION = "location: ";
	private static final String HEADER_NOTE = "note: ";
	private static final String HEADER_IMPORTANCE = "important: ";
	private static final String HEADER_FINISHED = "finished: ";
	
	private static final String MSG_ADD = "added task:\n";
	private static final String MSG_DELETE = "deleted task:\n";
	private static final String MSG_DISPLAY = "display tasks:\n";
	private static final String MSG_SORT = "sort results:\n";
	private static final String MSG_SEARCH ="search results:\n";
	private static final String MSG_UPDATE ="updated task:\n";
	private static final String MSG_MARK ="marked task:\n";
	private static final String MSG_DONE = "finished task:\n";
	private static final String MSG_TAG ="tagged task:\n";
	private static final String MSG_UNDO ="undo task:\n";
	private static final String MSG_LINKGOOGLE ="linked to google";
	private static final String MSG_EXIT ="exited";
	private static final String MSG_INVALID ="invalid command";
	
	private static final String MSG_WELCOME = "welcome to calendear!";
	private static final String MSG_COMMAND = "command:";
	private static final String MSG_YES = "yes";
	private static final String MSG_NO = "no";
	
	public static String getDetailsOfTaskArr(ArrayList<Task> taskArr){
		String output="";
		for(int i=0;i<taskArr.size();i++){
			if(taskArr.get(i)!=null){
				output+=i+".\n";
				output+=getDetailsOfTask(taskArr.get(i))+"\n";
			}
		}
		return output;
	}
	
	public static String getDetailsOfTask(Task task){
		String details="";
		if(task.getName()!=null){
			details+=HEADER_NAME+task.getName()+"\n";
		}
		if(task.getTag()!=null){
			details+=HEADER_TAG+task.getTag()+"\n";
		}	
		if(task.getStartTime()!=null){
			details+=HEADER_STARTTIME+task.getStartTimeStr()+"\n";
		}
		if(task.getEndTime()!=null){
			details+=HEADER_STARTTIME+task.getEndTimeStr()+"\n";
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
	}

	
	

}

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
	
	public static void displayAdd(Task task){
		System.out.println(MSG_ADD+ getDetailsOfTask(task));
	}
	
	public static void displayDelete(Task task){
		System.out.println(MSG_DELETE+ getDetailsOfTask(task));
	}
	
	public static void displayUpdate(Task task){
		System.out.println(MSG_UPDATE+ getDetailsOfTask(task));
	}
	
	public static void displayMark(Task task){
		System.out.println(MSG_MARK+ getDetailsOfTask(task));
	}
	
	public static void displayTag(Task task){
		System.out.println(MSG_TAG+ getDetailsOfTask(task));
	}
	
	public static void displayDone(Task task){
		System.out.println(MSG_DONE+ getDetailsOfTask(task));
	}
	
	public static void displayUndo(Task task){
		System.out.println(MSG_UNDO+ getDetailsOfTask(task));
	}
	
	public static void displayDisplay(ArrayList<Task> taskArr){
		System.out.println(MSG_DISPLAY+ getDetailsOfTaskArr(taskArr));
	}
	
	public static void displaySort(ArrayList<Task> taskArr){
		System.out.println(MSG_SORT+ getDetailsOfTaskArr(taskArr));
	}
	
	public static void displaySearch(ArrayList<Task> taskArr){
		System.out.println(MSG_SEARCH+ getDetailsOfTaskArr(taskArr));
	}
	
	public static void displayLinkGoogle(){
		System.out.println(MSG_LINKGOOGLE);
	}
	
	public static void displayExit(){
		System.out.println(MSG_EXIT);
	}
	
	public static void displayInvalid(){
		System.out.println(MSG_INVALID);
	}
	
	
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
		return details;
	}
	
	
	

	
	

}

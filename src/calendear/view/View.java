package calendear.view;
import java.util.ArrayList;

import calendear.util.Task;

public class View {
	
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
	
	private static final int NOT_ARR_LIST = -1;
	
	
	public static void displayWelcome(){
		System.out.println(MSG_WELCOME);
	}
	
	public static void displayRequestForInput(){
		System.out.println(MSG_COMMAND);
	}
	
	public static void displayAdd(Task task){
		System.out.println(MSG_ADD+ Full.getTask(task));
	}
	
	public static void displayAddInLine(Task task){
		System.out.println(MSG_ADD+ Table.getTask(task,NOT_ARR_LIST));
	}
	
	public static void displayDelete(Task task){
		System.out.println(MSG_DELETE+ Full.getTask(task));
	}
	
	public static void displayDeleteInLine(Task task){
		System.out.println(MSG_DELETE+ Table.getTask(task,NOT_ARR_LIST));
	}
	
	public static void displayUpdate(Task task){
		System.out.println(MSG_UPDATE+ Full.getTask(task));
	}
	
	public static void displayUpdateInLine(Task task){
		System.out.println(MSG_UPDATE+ Table.getTask(task,NOT_ARR_LIST));
	}
	
	public static void displayMark(Task task){
		System.out.println(MSG_MARK+ Full.getTask(task));
	}
	
	public static void displayMarkInLine(Task task){
		System.out.println(MSG_MARK+ Table.getTask(task,NOT_ARR_LIST));
	}
	
	public static void displayTag(Task task){
		System.out.println(MSG_TAG+ Full.getTask(task));
	}
	
	public static void displayTagInLine(Task task){
		System.out.println(MSG_TAG+ Table.getTask(task,NOT_ARR_LIST));
	}
	
	public static void displayDone(Task task){
		System.out.println(MSG_DONE+ Full.getTask(task));
	}
	
	public static void displayDoneInLine(Task task){
		System.out.println(MSG_DONE+ Table.getTask(task,NOT_ARR_LIST));
	}
	
	public static void displayUndo(Task task){
		System.out.println(MSG_UNDO+ Full.getTask(task));
	}
	
	public static void displayUndoInLine(Task task){
		System.out.println(MSG_UNDO+ Table.getTask(task,NOT_ARR_LIST));
	}
	
	public static void displayDisplay(ArrayList<Task> taskArr){
		System.out.println(MSG_DISPLAY+ Full.getMultipleTasks(taskArr));
	}
	
	public static void displayDisplayInLine(ArrayList<Task> taskArr){
		System.out.println(MSG_DISPLAY+ Table.getMultipleTasks(taskArr));
	}
	
	public static void displaySort(ArrayList<Task> taskArr){
		System.out.println(MSG_SORT+ Full.getMultipleTasks(taskArr));
	}
	
	public static void displaySortInLine(ArrayList<Task> taskArr){
		System.out.println(MSG_SORT+ Table.getMultipleTasks(taskArr));
	}
	
	public static void displaySearch(ArrayList<Task> taskArr){
		System.out.println(MSG_SEARCH+ Full.getMultipleTasks(taskArr));
	}
	
	public static void displaySearchInLine(ArrayList<Task> taskArr){
		System.out.println(MSG_SEARCH+ Table.getMultipleTasks(taskArr));
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
	

}

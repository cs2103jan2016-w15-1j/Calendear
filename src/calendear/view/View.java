package calendear.view;
import java.util.ArrayList;
import calendear.util.Task;

//@@author Pan Jiyun

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
	private static String formatYellow = ANSI_YELLOW+"%s"+ANSI_RESET;
	
	private static final String MSG_ADD = "Added task:\n";
	private static final String MSG_DELETE = "Deleted task:\n";
	private static final String MSG_UPDATE ="Updated task:\n";
	private static final String MSG_MARK ="Marked task:\n";
	private static final String MSG_TAG ="Tagged task:\n";
	private static final String MSG_DONE = "Finished task:\n";
	private static final String MSG_UNDO ="Undo successfully.\n";
	private static final String MSG_DISPLAY = "Display tasks:\n";
	private static final String MSG_SORT = "Sort results:\n";
	private static final String MSG_SEARCH ="Search results:\n";
	private static final String MSG_SEARCH_DONE="Search results (done):\n";
	private static final String MSG_LINKGOOGLE ="Linked to google";
	private static final String MSG_EXIT ="Exited";
	private static final String MSG_INVALID ="Invalid command";
	
	private static final int ID_ADD = 0;
	private static final int ID_DELETE = 1;
	private static final int ID_UPDATE =2;
	private static final int ID_MARK =3;
	private static final int ID_TAG =4;
	private static final int ID_DONE = 5;
	private static final int ID_UNDO =6;
	private static final int ID_DISPLAY = 7;
	private static final int ID_SORT = 8;
	private static final int ID_SEARCH =9;
	private static final int ID_SEARCH_DONE=10;
	private static final int ID_LINKGOOGLE =11;
	private static final int ID_EXIT =12;
	private static final int ID_INVALID =13;
	
	
	private static final String[] MSG_ARR_N = {MSG_ADD,MSG_DELETE,MSG_UPDATE,MSG_MARK,MSG_TAG,MSG_DONE,
		MSG_UNDO,MSG_DISPLAY,MSG_SORT,MSG_SEARCH,MSG_SEARCH_DONE,
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
		System.out.println(MSG_ARR[ID_ADD]+ Full.getTask(task));
	}
	
	public static void displayAddInLine(Task task){
		System.out.println(MSG_ARR[ID_ADD]+ Table.getSingleTask(task,NOT_ARR_LIST));
	}
	
	public static void displayDelete(Task task){
		System.out.println(MSG_ARR[ID_DELETE]+ Full.getTask(task));
	}
	
	public static void displayDeleteInLine(Task task){
		System.out.println(MSG_ARR[ID_DELETE]+ Table.getSingleTask(task,NOT_ARR_LIST));
	}
	
	public static void displayUpdate(Task task){
		System.out.println(MSG_ARR[ID_UPDATE]+ Full.getTask(task));
	}
	
	public static void displayUpdateInLine(Task task){
		System.out.println(MSG_ARR[ID_UPDATE]+ Table.getSingleTask(task,NOT_ARR_LIST));
	}
	
	public static void displayMark(Task task){
		System.out.println(MSG_ARR[ID_MARK]+ Full.getTask(task));
	}
	
	public static void displayMarkInLine(Task task){
		System.out.println(MSG_ARR[ID_MARK]+ Table.getSingleTask(task,NOT_ARR_LIST));
	}
	
	public static void displayTag(Task task){
		System.out.println(MSG_ARR[ID_TAG]+ Full.getTask(task));
	}
	
	public static void displayTagInLine(Task task){
		System.out.println(MSG_ARR[ID_TAG]+ Table.getSingleTask(task,NOT_ARR_LIST));
	}
	
	public static void displayDone(Task task){
		System.out.println(MSG_ARR[ID_DONE]+ Full.getTask(task));
	}
	
	public static void displayDoneInLine(Task task){
		System.out.println(MSG_ARR[ID_DONE]+ Table.getSingleTask(task,NOT_ARR_LIST));
	}
	
	public static void displayUndo(boolean successful){
		if (successful){
			System.out.println(MSG_ARR[ID_UNDO]);
		}
	}
	
	public static void displayDisplay(ArrayList<Task> taskArr){
		System.out.println(MSG_ARR[ID_DISPLAY]+ Full.getMultipleTasks(taskArr));
	}
	
	public static void displayDisplayInLine(ArrayList<Task> taskArr){
		System.out.println(MSG_ARR[ID_DISPLAY]+ Table.getMultipleTasksIncomplete(taskArr));
	}
	
	public static void displayDisplayAllInLine(ArrayList<Task> taskArr){
		System.out.println(MSG_ARR[ID_DISPLAY]+ Table.getMultipleTasksInFull(taskArr));
	}
	
	public static void displaySort(ArrayList<Task> taskArr){
		System.out.println(MSG_ARR[ID_SORT]+ Full.getMultipleTasks(taskArr));
	}
	
	public static void displaySortInLine(ArrayList<Task> taskArr){
		System.out.println(MSG_ARR[ID_SORT]+ Table.getMultipleTasksIncomplete(taskArr));
	}
	
	public static void displaySearch(ArrayList<Task> taskArr){
		System.out.println(MSG_ARR[ID_SEARCH]+ Full.getMultipleTasks(taskArr));
	}
	
	public static void displaySearchInLine(ArrayList<Task> taskArr){
		System.out.println(MSG_ARR[ID_SEARCH]+ Table.getMultipleTasksInFull(taskArr));
	}
	
	public static void displaySearchDoneInLine(ArrayList<Task> taskArr){
		System.out.println(MSG_ARR[ID_SEARCH_DONE]+ Table.getMultipleTasksComplete(taskArr));
	}
	
	public static void displayLinkGoogle(){
		System.out.println(MSG_ARR[ID_LINKGOOGLE]);
	}
	
	public static void displayExit(){
		System.out.println(MSG_ARR[ID_EXIT]);
	}
	
	public static void displayInvalid(){
		System.out.println(MSG_ARR[ID_INVALID]);
	}
	
	public static void displayError(String msg){
		System.out.println(String.format(formatYellow,msg));
	}
	
	public static void displayHelp(){
		System.out.println(HelpTable.getHelpTable());
	}
	

}

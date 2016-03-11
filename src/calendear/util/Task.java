package calendear.util;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class Task {
	
	public static final SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, dd-MMM-yyyy, HH:mm");
	//private static final String OBJ_SEPERATOR = System.getProperty("line.separator");
	private static final String TAB = "    ";
	private static final String NULL = "null";
	private static final String OBJ_SEPERATOR = ".";
	private static final String IMPORTANT = "important";
	private static final String NOT_IMPORTANT = "not important";
	private static final String FINISHED = "finished";
	private static final String NOT_FINISHED = "not finished";
	private static final String STR_DEADLINE = "Deadline";
	private static final String STR_EVENT = "Event";
	private static final String STR_FLOATING = "Floating";
	
	private String name;
	private TASK_TYPE type;
	private GregorianCalendar startTime;
	private GregorianCalendar endTime;
	private String location;
	private String note;
	private String tag;
	private boolean isImportant;
	private boolean isFinished = false;
	
	public Task(String name) {
		type = TASK_TYPE.FLOATING;
		this.name = name;
	}
	
	public Task(String name, GregorianCalendar deadline) {
		type = TASK_TYPE.DEADLINE;
		this.name = name;
		this.endTime = deadline;
	}
	
	public Task(String name, GregorianCalendar startTime, GregorianCalendar endTime) {
		type = TASK_TYPE.EVENT;
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	public String getName() {
		return name;
	}
	
	public TASK_TYPE getType() {
		return type;
	}
	
	public String getTypeStr(){
		switch (type){
			case DEADLINE: return STR_DEADLINE;
			case EVENT: return STR_EVENT;
			default : return STR_FLOATING;
		}
	}
	
	public GregorianCalendar getStartTime() {
		return startTime;
	}
	
	public GregorianCalendar getEndTime() {
		return endTime;
	}
	
	public String getStartTimeStr() {
		if (startTime == null) {
			return NULL;
		}
		return dateFormatter.format(startTime.getTime());
	}
	
	public String getEndTimeStr() {
		if (endTime == null) {
			return NULL;
		}
		return dateFormatter.format(endTime.getTime());
	}
	
	public String getLocation() {
		return location;
	}
	
	public String getNote() {
		return note;
	}
	
	public String getTag(){
		return tag;
	}
	
	public boolean isImportant(){
		return isImportant;
	}
	
	private String getImportantStr(){
		if (isImportant) {
			return IMPORTANT;
		}
		else {
			return NOT_IMPORTANT;
		}
	}
	
	public boolean isFinished(){
		return isFinished;
	}
	
	private String getFinishedStr(){
		if (isFinished) {
			return FINISHED;
		}
		else {
			return NOT_FINISHED;
		}
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setStartTime(GregorianCalendar time) {
		this.startTime = time;
	}
	
	public void setEndTime(GregorianCalendar time) {
		this.endTime = time;
	}
	
	public void setType (TASK_TYPE type) {
		this.type = type;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public void writeNote(String note){
		this.note = note;
	}
	
	public void setTag(String tag){
		this.tag = tag;
	}
	
	public void markImportant(boolean isImportant){
		this.isImportant = isImportant;
	}
	
	public void setIsFinished(boolean isFinished){
		this.isFinished = isFinished;
	}
	
	public String toSaveable() {
		String res = "{" + OBJ_SEPERATOR;
		res += TAB + getName() + OBJ_SEPERATOR;
		res += TAB + getTypeStr() + OBJ_SEPERATOR;
		res += TAB + getStartTimeStr() + OBJ_SEPERATOR;
		res += TAB + getEndTimeStr() + OBJ_SEPERATOR;
		res += TAB + getLocation() + OBJ_SEPERATOR;
		res += TAB + getNote() + OBJ_SEPERATOR;
		res += TAB + getTag() + OBJ_SEPERATOR;
		res += TAB + getImportantStr() + OBJ_SEPERATOR;
		res += TAB + getFinishedStr() + OBJ_SEPERATOR;
		res += "}";
		return res;
	}
	
	public static Task parseSaveable(String allString) {
//		Scanner sc = new Scanner(allString);
//		sc.nextLine(); //pass the open bracket "{"
//		String name = sc.nextLine();
//		String typeStr = sc.nextLine();
//		String startTimeStr = sc.nextLine();
//		String endTimeStr = sc.nextLine();
//		String location = sc.nextLine();
//		String note = sc.nextLine();
//		String tag = sc.nextLine();
//		String importantStr = sc.nextLine();
//		String finishedStr = sc.nextLine();
//		sc.close();
//		Task res;
//		switch (typeStr){
//			case STR_DEADLINE:
//				res = parseDeadline(name, endTimeStr);
//				break;
//			case STR_EVENT:
//				res = parseEvent(name, endTimeStr);
//				break;
//			case STR_FLOAT:
//				res = parseFloat(name, endTimeStr);
//				break;
//			default:
//				throw new Parse
//		}
		return new Task("");
	}
	
}

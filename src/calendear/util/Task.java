package calendear.util;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

enum TASK_TYPE{
	EVENT, DEADLINE, FLOATING, RECURRING
};

public class Task {
	
	public static final SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, dd-MMM-yyyy, HH:mm");
	
	private String name;
	private TASK_TYPE type;
	private GregorianCalendar startTime;
	private GregorianCalendar endTime;
	private String location;
	private String note;
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
	
	public GregorianCalendar getStartTime() {
		return startTime;
	}
	
	public GregorianCalendar getEndTime() {
		return endTime;
	}
	
	public String getStartTimeStr() {
		return dateFormatter.format(startTime.getTime());
	}
	
	public String getEndTimeStr() {
		return dateFormatter.format(endTime.getTime());
	}
	
	public String getLocation() {
		return location;
	}
	
	public String getNote() {
		return note;
	}
	
	public boolean isImportant(){
		return isImportant;
	}
	
	public boolean isFinished(){
		return isFinished;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setStartTime(GregorianCalendar time) {
		this.startTime = time;
	}
	
	public void setEndTIme(GregorianCalendar time) {
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
	
	public void markImportant(boolean isImportant){
		this.isImportant = isImportant;
	}
	
	public void setIsFinished(boolean isFinished){
		this.isFinished = isFinished;
	}
	
}

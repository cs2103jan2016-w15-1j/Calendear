package calendear.util;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Scanner;

import org.ocpsoft.prettytime.shade.edu.emory.mathcs.backport.java.util.Arrays;

import calendear.parser.DateParser;

public class Task {
	
	public static final SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, dd MMM yyyy, HH:mm");
	//private static final String OBJ_SEPERATOR = System.getProperty("line.separator");
	private static final String EMPTY = "";
	private static final String OBJ_SEPERATOR = ".";
	private static final String PATTERN_OBJ_SEPERATOR = "\\.";
	private static final String IMPORTANT = "important";
	private static final String NOT_IMPORTANT = "not important";
	private static final String FINISHED = "finished";
	private static final String NOT_FINISHED = "not finished";
	private static final String STR_DEADLINE = "Deadline";
	private static final String STR_EVENT = "Event";
	private static final String STR_FLOATING = "Floating";
	
	private static final int SAVING_INDEX_NAME = 0;
	private static final int SAVING_INDEX_TYPE = 1;
	private static final int SAVING_INDEX_START_TIME = 2;
	private static final int SAVING_INDEX_END_TIME = 3;
	private static final int SAVING_INDEX_LOCATION = 4;
	private static final int SAVING_INDEX_NOTE = 5;
	private static final int SAVING_INDEX_TAG = 6;
	private static final int SAVING_INDEX_IMPORTANT = 7;
	private static final int SAVING_INDEX_FINISHED = 8;
	
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
		this.name = name.trim();
	}
	
	public Task(String name, GregorianCalendar deadline) {
		type = TASK_TYPE.DEADLINE;
		this.name = name.trim();
		this.endTime = deadline;
	}
	
	public Task(String name, GregorianCalendar startTime, GregorianCalendar endTime) {
		type = TASK_TYPE.EVENT;
		this.name = name.trim();
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
			return EMPTY;
		}
		return dateFormatter.format(startTime.getTime());
	}
	
	public String getEndTimeStr() {
		if (endTime == null) {
			return EMPTY;
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
		String res;
		res = getName() + OBJ_SEPERATOR;
		res += getTypeStr() + OBJ_SEPERATOR;
		res += getStartTimeStr() + OBJ_SEPERATOR;
		res += getEndTimeStr() + OBJ_SEPERATOR;
		res += getLocation() + OBJ_SEPERATOR;
		res += getNote() + OBJ_SEPERATOR;
		res += getTag() + OBJ_SEPERATOR;
		res += getImportantStr() + OBJ_SEPERATOR;
		res += getFinishedStr() + OBJ_SEPERATOR;
		return res;
	}
	
	public static Task parseSaveable(String allString) throws ParseException {
		String[] members = allString.split(PATTERN_OBJ_SEPERATOR);
		String typeStr = members[SAVING_INDEX_TYPE];
		switch (typeStr){
			case STR_DEADLINE:
				return parseDeadline(members);
			case STR_EVENT:
				return parseEvent(members);
			case STR_FLOATING:
				return parseFloat(members);
			default:
				throw new ParseException("type name not defined", 0);
		}
	}

	private static Task parseDeadline(String[] members) throws ParseException {
		String name = members[SAVING_INDEX_NAME];
		String endTimeStr = members[SAVING_INDEX_END_TIME];
		GregorianCalendar endTime = DateParser.parse(endTimeStr);
		return new Task(name, endTime);
	}
	
	private static Task parseEvent(String[] members) throws ParseException {
		String name = members[SAVING_INDEX_NAME];
		String startTimeStr = members[SAVING_INDEX_START_TIME];
		GregorianCalendar startTime = DateParser.parse(startTimeStr);
		String endTimeStr = members[SAVING_INDEX_END_TIME];
		GregorianCalendar endTime = DateParser.parse(endTimeStr);
		return new Task(name, startTime, endTime);
	}
	
	private static Task parseFloat(String[] members) {
		String name = members[SAVING_INDEX_NAME];
		return new Task(name);
	}

}

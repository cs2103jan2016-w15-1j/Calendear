package calendear.util;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.Date;

import org.ocpsoft.prettytime.shade.edu.emory.mathcs.backport.java.util.Arrays;

import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.client.util.DateTime;

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
	private String googleEventId;
	private TASK_TYPE type;
	private GregorianCalendar startTime;
	private GregorianCalendar endTime;
	private String location;
	private String note;
	private String tag;
	private boolean isImportant;
	private boolean isFinished = false;
	
	public Task(){
		//adding task with infoList and newData
	}
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
	
	public String getEventId() {
		return googleEventId;
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
	
	public void setEventId(String eventId) {
		this.googleEventId = eventId;
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
	
	public void setNote(String note){
		this.note = note;
	}
	
	public void setTag(String tag){
		this.tag = tag;
	}
	
	public void setIsImportant(boolean isImportant){
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
	
	/**
	 * @author Phang Chun Rong
	 * @return Google Event
	 */
	public Event toGoogleEvent() {
		Event event = new Event();
		//TODO
		DateTime start;
		DateTime end;
		event.setSummary(name);
		if (location != null)
			event.setLocation(location);
		switch(type) {
			case EVENT:
				start = new DateTime(startTime.getTime(), startTime.getTimeZone());
				end = new DateTime(endTime.getTime(), endTime.getTimeZone());
				event.setStart(new EventDateTime().setDateTime(start));
				event.setEnd(new EventDateTime().setDateTime(end));
				break;
			case DEADLINE:
				end = new DateTime(endTime.getTime(), endTime.getTimeZone());
				event.setStart(new EventDateTime().setDateTime(end));
				event.setEnd(new EventDateTime().setDateTime(end));
				//If start and end time are required, set start time to be equal to end time or 1 second more.
				break;
			case FLOATING:
				//Set Start Time to be the time at this instance
				Date now = new Date();
				start = new DateTime(now.getTime());
				event.setStart(new EventDateTime().setDateTime(start));
				event.setEnd(new EventDateTime().setDateTime(start));				
				break;
			case RECURRING:
				break;
			default:
				break;
		}
		return event;
	}
	
	public static Task parseSaveable(String allString) throws ParseException {
		String[] members = allString.split(PATTERN_OBJ_SEPERATOR);
		String typeStr = members[SAVING_INDEX_TYPE];
		Task res;
		switch (typeStr){
			case STR_DEADLINE:
				res = parseDeadline(members);
				break;
			case STR_EVENT:
				res = parseEvent(members);
				break;
			case STR_FLOATING:
				res = parseFloat(members);
				break;
			default:
				throw new ParseException("type name not defined", 0);
		}
		parseOptionalAttribute(res, members);
		return res;
	}

	private static void parseOptionalAttribute(Task res, String[] members) {
		res.setLocation(members[SAVING_INDEX_LOCATION]);
		res.setNote(members[SAVING_INDEX_NOTE]);
		if (members[SAVING_INDEX_IMPORTANT].equals(IMPORTANT)){
			res.setIsFinished(true);
		} else {
			res.setIsImportant(false);
		}
		if (members[SAVING_INDEX_FINISHED].equals(FINISHED)){
			res.setIsFinished(true);
		} else {
			res.setIsFinished(false);
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

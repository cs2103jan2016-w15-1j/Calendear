package calendear.util;

import static org.junit.Assert.*;

import org.junit.Test;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.client.util.DateTime;
import java.util.GregorianCalendar;

/**
 * 
 * @author Phang Chun Rong
 *
 */
public class TaskTest {
	
	private static final String TASK_NAME = "Test Task";
	private static final String GOOGLE_EVENT_SEPERATOR = "-___-";
	
	private static final String STR_FLOATING = "Floating";
	private static final String STR_DEADLINE = "Deadline";
	private static final String STR_EVENT = "Event";
	
	private static final String FINISHED = "finished";
	private static final String NOT_FINISHED = "not finished";
	
	private static final GregorianCalendar END_TIME = new GregorianCalendar(2016, 4, 8, 14, 30);
	private static final GregorianCalendar START_TIME = new GregorianCalendar(2016, 4, 8, 13, 30);
	
	@Test
	public void floatingTaskToGoogleEvent() {	
		Task task = new Task(TASK_NAME);
		Event googleEventFromTask = task.toGoogleEvent();
		
		Event googleEvent = new Event();
		googleEvent.setSummary(TASK_NAME);
		googleEvent.setDescription(NOT_FINISHED + GOOGLE_EVENT_SEPERATOR + STR_FLOATING);
		
		assertEquals(googleEventFromTask.getSummary(), googleEvent.getSummary());
		assertEquals(googleEventFromTask.getDescription(), googleEvent.getDescription());
	}
	
	@Test
	public void deadlineTaskToGoogleEvent() {
		
		Task task = new Task(TASK_NAME, END_TIME);
		Event googleEventFromTask = task.toGoogleEvent();
		
		Event googleEvent = new Event();
		googleEvent.setSummary(TASK_NAME);
		googleEvent.setDescription(NOT_FINISHED + GOOGLE_EVENT_SEPERATOR + STR_DEADLINE);
		
		EventDateTime endEventDateTime = googleEventFromTask.getEnd();
		DateTime endDateTime = endEventDateTime.getDateTime();
		
		assertEquals(googleEventFromTask.getSummary(), googleEvent.getSummary());
		assertEquals(googleEventFromTask.getDescription(), googleEvent.getDescription());
		assertEquals(END_TIME.getTimeInMillis(), endDateTime.getValue());
	}
	
	@Test
	public void eventTaskToGoogleEvent() {
		Task task = new Task(TASK_NAME, START_TIME, END_TIME);
		task.setIsFinished(true);
		
		Event googleEventFromTask = task.toGoogleEvent();
		
		Event googleEvent = new Event();
		googleEvent.setSummary(TASK_NAME);
		googleEvent.setDescription(FINISHED + GOOGLE_EVENT_SEPERATOR + STR_EVENT);
		
		EventDateTime endEventDateTime = googleEventFromTask.getEnd();
		DateTime endDateTime = endEventDateTime.getDateTime();
		EventDateTime startEventDateTime = googleEventFromTask.getStart();
		DateTime startDateTime = startEventDateTime.getDateTime();
		
		assertEquals(googleEventFromTask.getSummary(), googleEvent.getSummary());
		assertEquals(googleEventFromTask.getDescription(), googleEvent.getDescription());
		assertEquals(START_TIME.getTimeInMillis(), startDateTime.getValue());
		assertEquals(END_TIME.getTimeInMillis(), endDateTime.getValue());

	}
	
	@Test
	public void normalGoogleEventToTask() {
		Event googleEvent = new Event();
		googleEvent.setSummary(TASK_NAME);
		DateTime start = new DateTime(START_TIME.getTime(), START_TIME.getTimeZone());
		DateTime end = new DateTime(END_TIME.getTime(), END_TIME.getTimeZone());
		
		googleEvent.setStart(new EventDateTime().setDateTime(start));
		googleEvent.setEnd(new EventDateTime().setDateTime(end));
		
		Task taskFromGoogleEvent = Task.parseGoogleEvent(googleEvent);
		
		assertEquals(taskFromGoogleEvent.getType(), TASK_TYPE.EVENT);
		assertEquals(taskFromGoogleEvent.getName(), TASK_NAME);
		assertEquals(taskFromGoogleEvent.getStartTime(), START_TIME);
		assertEquals(taskFromGoogleEvent.getEndTime(), END_TIME);
	}
	
	@Test
	public void floatingGoogleEventToTask() {
		Event googleEvent = new Event();
		googleEvent.setSummary(TASK_NAME);
		googleEvent.setDescription(NOT_FINISHED + GOOGLE_EVENT_SEPERATOR + STR_FLOATING);
		DateTime start = new DateTime(START_TIME.getTime(), START_TIME.getTimeZone());
		DateTime end = new DateTime(START_TIME.getTime(), START_TIME.getTimeZone());
		
		googleEvent.setStart(new EventDateTime().setDateTime(start));
		googleEvent.setEnd(new EventDateTime().setDateTime(end));		
		
		Task taskFromGoogleEvent = Task.parseGoogleEvent(googleEvent);
		
		assertEquals(taskFromGoogleEvent.getType(), TASK_TYPE.FLOATING);
		assertEquals(taskFromGoogleEvent.getName(), TASK_NAME);
		assertEquals(taskFromGoogleEvent.getStartTime(), null);
		assertEquals(taskFromGoogleEvent.getEndTime(), null);
		assertFalse(taskFromGoogleEvent.isFinished());
	}
	
	@Test
	public void deadlineGoogleEventToTask() {
		Event googleEvent = new Event();
		googleEvent.setSummary(TASK_NAME);
		googleEvent.setDescription(FINISHED + GOOGLE_EVENT_SEPERATOR + STR_DEADLINE);
		DateTime start = new DateTime(END_TIME.getTime(), END_TIME.getTimeZone());
		DateTime end = new DateTime(END_TIME.getTime(), END_TIME.getTimeZone());
		
		googleEvent.setStart(new EventDateTime().setDateTime(start));
		googleEvent.setEnd(new EventDateTime().setDateTime(end));
		
		Task taskFromGoogleEvent = Task.parseGoogleEvent(googleEvent);

		assertEquals(taskFromGoogleEvent.getType(), TASK_TYPE.DEADLINE);
		assertEquals(taskFromGoogleEvent.getName(), TASK_NAME);
		assertEquals(taskFromGoogleEvent.getStartTime(), null);
		assertEquals(taskFromGoogleEvent.getEndTime(), END_TIME);
		assertTrue(taskFromGoogleEvent.isFinished());		
	}

}

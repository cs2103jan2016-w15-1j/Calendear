package calendear.storage;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Iterator;
import java.util.ArrayList;

import calendear.util.Task;

/**
 * 
 * @author Phang Chun Rong
 *
 */
public class GoogleIO {

	  private static final String APPLICATION_NAME = "Calendear";
	  private static final String MESSAGE_ERROR = "Exception Caught";
	  private static final String MESSAGE_ENTER = "Enter";
	  private static final String MESSAGE_ENTER_SPACE = "Enter ";
	  private static final String MESSAGE_ERROR_CREDENTIALS = 
			  "Enter Client ID and Secret from "
			  + "https://code.google.com/apis/console/?api=calendar into "
			  + "calendar-cmdline-sample/src/main/resources/client_secrets.json";
	  
	  private static final String SECRETS_DIRECTORY = "/libs/client_secrets.json";
	  
	  /** Global instance of the HTTP transport. */
	  private static HttpTransport httpTransport;

	  /** Global instance of the JSON factory. */
	  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	  
	  private static com.google.api.services.calendar.Calendar client;
	  
	  /** Google Calendar ID for Calendear*/
	  private static String calendarID;
	  
	  private static boolean isLogined = false;
	  
	  /** Authorizes the installed application to access user's protected data. */
	  private static Credential authorize() throws Exception {
	    // load client secrets
	    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
	        new InputStreamReader(GoogleIO.class.getResourceAsStream(SECRETS_DIRECTORY)));
	    if (clientSecrets.getDetails().getClientId().startsWith(MESSAGE_ENTER)
	        || clientSecrets.getDetails().getClientSecret().startsWith(MESSAGE_ENTER_SPACE)) {
	      System.out.println(MESSAGE_ERROR_CREDENTIALS);
	      System.exit(1);
	    }
	    // set up authorization code flow
	    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
	        httpTransport, JSON_FACTORY, clientSecrets,
	        Collections.singleton(CalendarScopes.CALENDAR))
	        .build();
	    // authorize
	    return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	  }
	
	  public static void login() throws IOException, Exception{
		  // initialize the transport
		  httpTransport = GoogleNetHttpTransport.newTrustedTransport();

		  // authorization
		  Credential credential = authorize();

	      // set up global Calendar instance
	      client = new com.google.api.services.calendar.Calendar.Builder(
	          httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
	      
	      //Link to Calendear
	      calendarID = findOrCreateCalendar();
	      isLogined = true;

	  }
	  
	  public static boolean isLogined() {
		  return isLogined;
	  }
	  
	  public static String addEvent(Task task) throws IOException {
		  assert(isLogined == true);
		  
		  Event event = task.toGoogleEvent();
		  Event result = client.events().insert(calendarID, event).execute();
		  return result.getId();
	  
		}
		
	  public static void updateEvent(Task task) throws IOException{
		  assert(isLogined == true);
		  
		  String eventId = task.getEventId();
		  Event event = task.toGoogleEvent();
		  client.events().update(calendarID, eventId, event).execute();
	  }
		
	  public static void deleteEvent(Task task) throws IOException {
		  assert(isLogined == true);
		  
		  String eventId = task.getEventId();
		  client.events().delete(calendarID, eventId).execute();
	  }
		
	  public static ArrayList<Task> loadTasksFromGoogle() throws IOException {
		  assert(isLogined == true);
		  
		  ArrayList<Task> tasks = new ArrayList<Task>();
		  Events feed = client.events().list(calendarID).execute();
		  Iterable<Event> listOfEvents = feed.getItems();
		  for (Event event: listOfEvents) {
			  Task newTask = Task.parseGoogleEvent(event);
			  tasks.add(newTask);
		  }
		    
		  return tasks;
		}
		  
	  private static String findOrCreateCalendar() throws IOException {
		  CalendarList request = client.calendarList().list().execute();
		  Iterator<CalendarListEntry> iter = request.getItems().iterator();
		  while (iter.hasNext()) {
			  CalendarListEntry entry = iter.next();
			  String summary = entry.getSummary();
			  if (summary.equals(APPLICATION_NAME)) {
				  return entry.getId();
			  }
		  }
			
		  return createCalendar();
	  }
		
	  private static String createCalendar() throws IOException {
		  Calendar entry = new Calendar();
		  entry.setSummary(APPLICATION_NAME);
		  Calendar result = client.calendars().insert(entry).execute();
		  return result.getId();			
	  }
	  
}

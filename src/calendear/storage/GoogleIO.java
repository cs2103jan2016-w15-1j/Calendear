package calendear.storage;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Lists;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Iterator;

import calendear.util.Task;

/**
 * 
 * @author Phang Chun Rong
 *
 */
public class GoogleIO {
	/**
	   * Be sure to specify the name of your application. If the application name is {@code null} or
	   * blank, the application will log a warning. Suggested format is "MyCompany-ProductName/1.0".
	   */
	  private static final String APPLICATION_NAME = "Calendear";
	  private static final String MESSAGE_ERROR = "Exception Caught";
	  
	  /** Directory to store user credentials. */
	  private static final java.io.File DATA_STORE_DIR =
	      new java.io.File(System.getProperty("user.home"), ".store/calendear_credentials");

	  /**
	   * Global instance of the {@link DataStoreFactory}. The best practice is to make it a single
	   * globally shared instance across your application.
	   */
	  private static FileDataStoreFactory dataStoreFactory;
	  
	  /** Global instance of the HTTP transport. */
	  private static HttpTransport httpTransport;

	  /** Global instance of the JSON factory. */
	  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	  
	  private static com.google.api.services.calendar.Calendar client;
	  
	  /** Google Calendar ID for Calendear*/
	  private static String calendarID;

	  /** Authorizes the installed application to access user's protected data. */
	  private static Credential authorize() throws Exception {
	    // load client secrets
	    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
	        new InputStreamReader(GoogleIO.class.getResourceAsStream("/libs/client_secrets.json")));
	    if (clientSecrets.getDetails().getClientId().startsWith("Enter")
	        || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
	      System.out.println(
	          "Enter Client ID and Secret from https://code.google.com/apis/console/?api=calendar "
	          + "into calendar-cmdline-sample/src/main/resources/client_secrets.json");
	      System.exit(1);
	    }
	    // set up authorization code flow
	    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
	        httpTransport, JSON_FACTORY, clientSecrets,
	        Collections.singleton(CalendarScopes.CALENDAR)).setDataStoreFactory(dataStoreFactory)
	        .build();
	    // authorize
	    return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	  }
	
	  public static boolean login() {
		  try {
		      // initialize the transport
		      httpTransport = GoogleNetHttpTransport.newTrustedTransport();

		      // initialize the data store factory
		      dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);

		      // authorization
		      Credential credential = authorize();

		      // set up global Calendar instance
		      client = new com.google.api.services.calendar.Calendar.Builder(
		          httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
		      
		      //Link to Calendear
		      calendarID = findOrCreateCalendar();
		      
		      return true;

		    } catch (IOException e) {
		      System.err.println(e.getMessage());
		      return false;
		    } catch (Throwable t) {
		      t.printStackTrace();
		      return false;
		    }
		  
	  }
	  
		public static String addEvent(Task task) {
			try {
				Event event = task.toGoogleEvent();
				Event result = client.events().insert(calendarID, event).execute();
				return result.getId();
			}
			catch (IOException ex) {
				return MESSAGE_ERROR;
			}
			 
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
		
		private static String createCalendar() {
			try {
				Calendar entry = new Calendar();
				entry.setSummary(APPLICATION_NAME);
				Calendar result = client.calendars().insert(entry).execute();
				return result.getId();
			}
			catch (IOException ex) {
				System.out.println(MESSAGE_ERROR);
				return null;
			}
			
		}
	  
}

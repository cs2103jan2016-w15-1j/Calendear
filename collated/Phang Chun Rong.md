# Phang Chun Rong
###### /src/calendear/action/Action.java
``` java
	 */
	public void exeLinkGoogle() throws IOException,Exception  {
		if (!this._dataManager.isLogined()) {
			this._dataManager.loginGoogle();
		}
		
		try {
			Thread.sleep(300);
		}
		catch (InterruptedException ex) {
		}
		
		if (this._dataManager.isLogined()) {
			exeAddAllToGoogle();
		}
	}
	

	public void exeSaveFile(CommandSave commandSave) throws IOException {
		String path = commandSave.getPath();
		this._dataManager.changeFilePath(path);
	}
	
```
###### /src/calendear/action/CDLogic.java
``` java

package calendear.action;

import calendear.action.Action;
import calendear.parser.Parser;
import calendear.util.*;

import java.io.IOException;
import java.util.ArrayList;
import java.text.ParseException;

/**
```
###### /src/calendear/action/CDLogic.java
``` java

 * Facade Class for Logic Component
 * @author Phang Chun Rong
 */
public class CDLogic {
	
	private Action _action;
	
	public CDLogic(String nameOfFile) throws ParseException, IOException {
		_action = new Action(nameOfFile);
	}
	
	public Command parseInput(String userInput) {
		return Parser.parse(userInput);
	}
	
	public Task exeAdd(CommandAdd commandAdd) throws LogicException, IOException {
		Task addedTask = _action.exeAdd(commandAdd);
		return addedTask;
	}
	
	public ArrayList<Task> exeDisplay(CommandDisplay commandDisplay) {
		ArrayList<Task> tasks = _action.exeDisplay(commandDisplay);
		return tasks;
	}
	
	public Task exeUpdate(CommandUpdate commandUpdate) throws LogicException, IOException {
		Task task = _action.exeUpdate(commandUpdate);
		return task;
	}
	
	public Task exeDelete(CommandDelete commandDelete) throws LogicException, IOException {
		Task task = _action.exeDelete(commandDelete);
		return task;
	}
	
	public Task exeMarkImportant(CommandMark commandMark) throws LogicException, IOException {
		Task task = _action.exeMarkImportance(commandMark);
		return task;
	}
	
	public Task exeMarkDone(CommandDone commandDone) throws LogicException, IOException{
		Task task = _action.exeMarkDone(commandDone);
		return task;
	}
	
	public void exeLinkGoogle() throws IOException, Exception {
		_action.exeLinkGoogle();
	}
	
	public boolean exeUndo() throws IOException{
		return _action.exeUndo();
	}
	
	public boolean exeRedo() throws IOException{
		return _action.exeRedo();
	}
	
	public Task exeTag(CommandTag commandTag) throws LogicException, IOException {
		return _action.exeTag(commandTag);
	}
	
	public ArrayList<Task> exeSearch(CommandSearch commandSearch){
		return _action.exeSearch(commandSearch);
	}
	
	public ArrayList<Task> exeLoadTasksFromGoogle(CommandLoadFromGoogle commandLoadFromGoogle) 
			throws LogicException, IOException {
		return _action.exeLoadTasksFromGoogle(commandLoadFromGoogle);
	}
	
	public void exeSaveFile(CommandSave commandSave) throws IOException {
		_action.exeSaveFile(commandSave);
	}
	
	public boolean exeClear(CommandClear commandClear) throws IOException {
		return _action.exeClear(commandClear);
	}
}
```
###### /src/calendear/storage/DataManager.java
``` java
package calendear.storage;

import calendear.util.Task;

import java.text.ParseException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Facade Class to manage IO requirements. Depends on GoogleIO and FileIO.
 * @author Phang Chun Rong
 *
 */
public class DataManager {
		
	/**
	 * @return an ArrayList of tasks based on the stored file.
	 */
	public ArrayList<Task> getDataFromFile() throws ParseException, IOException {
		return FileIO.buildData();
	}

	/**
	 * Writes Tasks to File
	 * @param tasksList
	 */
	public void insertDataToFile(ArrayList<Task> tasksList) throws IOException {
		FileIO.updateData(tasksList);
	}

	/**
	 * Logs user into Google
	 */
	public void loginGoogle() throws IOException, Exception {
		GoogleIO.login();
	}
	
	public boolean isLogined() {
		return GoogleIO.isLogined();
	}
	
	/**
	 * Adds Calendear Tasks to Google using GoogleIO.
	 * @param task
	 * @return eventId
	 */
	public String addTaskToGoogle(Task task) throws IOException {
		return GoogleIO.addEvent(task);
	}
	
	/**
	 * Update Calendear Task to Google using GoogleIO.
	 * @param task
	 */
	public void updateTaskToGoogle(Task task) throws IOException {
		GoogleIO.updateEvent(task);
	}
	
	/**
	 * Delete Calendear Task to Google using GoogleIO.
	 * @param task
	 */
	public void deleteTaskFromGoogle(Task task) throws IOException {
		GoogleIO.deleteEvent(task);
	}
	
	/**
	 * Get Tasks that are saved in Google
	 * @return Tasks
	 */
	public ArrayList<Task> getTasksFromGoogle() throws IOException {
		return GoogleIO.loadTasksFromGoogle();
	}
	
	/**
	 * Change the file path that data is stored
	 * @param path
	 * @return 
	 */
	public void changeFilePath(String path) throws IOException {
		FileIO.changeFilePath(path);
	}
	
	/**
	 * DataManager constructor
	 * @param fileName
	 */
	public DataManager(String fileName) throws IOException{
		FileIO.createFile(fileName);
	}

}
```
###### /src/calendear/storage/FileIO.java
``` java
package calendear.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import calendear.util.Task;

/**
 * 
 * @author Phang Chun Rong
 *
 */
public class FileIO {
	
	private static String nameOfFile;
	private static File file;
	
	
	public static void createFile(String fileName) throws IOException {
		nameOfFile = fileName;
		file = new File(nameOfFile);
		file.createNewFile();
	    		
	}

	public static ArrayList<Task> buildData() throws ParseException, IOException {
		ArrayList<Task> tasks = new ArrayList<Task>();
		FileReader fileReader = new FileReader(file);

		BufferedReader bufferedReader = new BufferedReader(fileReader);	
		String line = null;
		while((line = bufferedReader.readLine()) != null) {
			Task newTask = Task.parseSaveable(line);
			tasks.add(newTask);
		}   
		
		fileReader.close();

		return tasks;
	}
	
	/**
	 * Takes in an ArrayList of Tasks and saves it into the database.
	 * @param tasksList
	 */
	public static void updateData(ArrayList<Task> tasksList) throws IOException {
		wipeFile(); // Clears file content first
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));

		for (int i=0; i < tasksList.size(); i++) {
			Task currentTask = tasksList.get(i);

			bw.write(currentTask.toSaveable());
			bw.newLine();
		}

		bw.close();
	}
	
	public static void changeFilePath(String path) throws IOException {
		File newFile = new File(path);
		newFile.createNewFile();
			
		copyData(newFile);
			
		nameOfFile = path;
		file = newFile;
	}
	
	private static void copyData(File newFile) throws IOException {
		FileReader fileReader = new FileReader(file);

		BufferedReader bufferedReader = 
            new BufferedReader(fileReader);
		String line = null;
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(newFile, true));

		while((line = bufferedReader.readLine()) != null) {
			bufferedWriter.write(line);
			bufferedWriter.newLine();
		}
		
		bufferedWriter.close();
		bufferedReader.close();
	}
	
	private static void wipeFile() throws IOException {
		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write("");
		fileWriter.close();
	}
}
```
###### /src/calendear/storage/GoogleIO.java
``` java

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
```
###### /src/calendear/util/Task.java
``` java
	 * @param startTime
	 */
	public void setStartTime(EventDateTime startTime) {
		DateTime dateTime = startTime.getDateTime();
		long timeValue;
		
		if (dateTime != null) {
			timeValue = dateTime.getValue();

		}
		else {
			dateTime = startTime.getDate();
			timeValue = dateTime.getValue() - NUMBER_MILLISECOND_EIGHT_HOURS;
		}
		
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(timeValue);
		this.startTime = cal;
	}
```
###### /src/calendear/util/Task.java
``` java
	 * @param endTime
	 */
	public void setEndTime(EventDateTime endTime) {
		DateTime dateTime = endTime.getDateTime();
		long timeValue;
		
		if (dateTime != null) {
			timeValue = dateTime.getValue();

		}
		else {
			dateTime = endTime.getDate();
			timeValue = dateTime.getValue() - NUMBER_MILLISECOND_EIGHT_HOURS;
		}
		
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(timeValue);
		this.endTime = cal;
	}
```
###### /src/calendear/util/Task.java
``` java
	 * @param strFinished
	 */
	public void setIsFinishedByString(String strFinished) {
		if (strFinished.equals(FINISHED)) {
			this.isFinished = true;
		}
		else {
			this.isFinished = false;
		}
	}
```
###### /src/calendear/util/Task.java
``` java
	 * @return Google Event
	 */
	public Event toGoogleEvent() {
		Event event = new Event();
		DateTime start;
		DateTime end;
		event.setSummary(name);
		event.setLocation(location);
		String description = getFinishedStr();
		switch(type) {
			case EVENT:
				start = new DateTime(startTime.getTime(), startTime.getTimeZone());
				end = new DateTime(endTime.getTime(), endTime.getTimeZone());
				event.setStart(new EventDateTime().setDateTime(start));
				event.setEnd(new EventDateTime().setDateTime(end));
				description = description + GOOGLE_EVENT_SEPERATOR + STR_EVENT;
				event.setDescription(description);
				break;
			case DEADLINE:
				end = new DateTime(endTime.getTime(), endTime.getTimeZone());
				event.setStart(new EventDateTime().setDateTime(end));
				event.setEnd(new EventDateTime().setDateTime(end));
				description = description + GOOGLE_EVENT_SEPERATOR + STR_DEADLINE;
				event.setDescription(description);
				break;
			case FLOATING:
				//Set Start Time to be the time at this instance
				Date now = new Date();
				start = new DateTime(now.getTime());
				event.setStart(new EventDateTime().setDateTime(start));
				event.setEnd(new EventDateTime().setDateTime(start));
				description = description + GOOGLE_EVENT_SEPERATOR + STR_FLOATING;
				event.setDescription(description);
				break;
			default:
				break;
		}
		return event;
	}
	
	/**
	 * @author Phang Chun Rong
	 * @param googleEvent
	 * @return Task
	 */
	public static Task parseGoogleEvent(Event googleEvent) {
		Task task = new Task();
		task.setName(googleEvent.getSummary());
		task.setEventId(googleEvent.getId());
		task.setLocation(googleEvent.getLocation());
		EventDateTime start;
		EventDateTime end;
		
		String description = googleEvent.getDescription();
		if (description != null) {
			String[] tokenizedDescription = description.split(GOOGLE_EVENT_SEPERATOR);
			//Means that this description is set programmatically by calendear
			if (tokenizedDescription.length == 2) {
				task.setIsFinishedByString(tokenizedDescription[0]);
				switch(tokenizedDescription[1]) {
					case STR_EVENT: start = googleEvent.getStart();
									end = googleEvent.getEnd();
									task.setStartTime(start);
									task.setEndTime(end);
									task.setType(TASK_TYPE.EVENT);
									break;
									
					case STR_DEADLINE: end = googleEvent.getEnd();
									   task.setEndTime(end);
									   task.setType(TASK_TYPE.DEADLINE);
									   break;
									   
					case STR_FLOATING: task.setType(TASK_TYPE.FLOATING);
					   				   break;
					default:
							break;
				}
			}
			else {
				start = googleEvent.getStart();
				end = googleEvent.getEnd();
				task.setStartTime(start);
				task.setEndTime(end);
				task.setType(TASK_TYPE.EVENT);
			}
		}
		else {
			start = googleEvent.getStart();
			end = googleEvent.getEnd();
			task.setStartTime(start);
			task.setEndTime(end);
			task.setType(TASK_TYPE.EVENT);
		}
		
		return task;
	}
```
###### /src/calendear/view/Controller.java
``` java

package calendear.view;
import calendear.action.CDLogic;
import calendear.action.LogicException;
import calendear.util.*;

import java.util.Scanner;
import java.text.ParseException;
import java.util.ArrayList;

import java.io.IOException;

/**
 * Controller for User Interaction
 * @author Phang Chun Rong
 */
public class Controller {
	private static final int DONE_ID = 8;
	private static final String MESSAGE_ERROR_GOOGLE = "Error Communicating with Google";
	private static final String MESSAGE_ERROR_GOOGLE_LOGIN = "Error authenticating with Google";
	private static final String MESSAGE_ERROR_INITIALISATION = "Error with file, please choose another file";
	private static final String MESSAGE_ERROR_FILE_CREATION = "Cannot Create File";
	
	private static Scanner _scanner;
	private CDLogic _cdLogic;
	
	public static void main(String[] args) {
		_scanner = new Scanner(System.in);
		
		String nameOfFile = getFileName();
		
		Controller controller = new Controller(nameOfFile);
		controller.startApplication();
	}
	
	public Controller(String nameOfFile) {
		try {
			instantiateLogic(nameOfFile);
		} catch (ParseException e) {
			View.displayError(MESSAGE_ERROR_INITIALISATION);
			System.exit(0);
		}
		catch (IOException ex) {
			View.displayError(MESSAGE_ERROR_INITIALISATION);
			System.exit(0);
		}
		View.displayWelcome();
	}
	
	public void instantiateLogic(String nameOfFile) throws ParseException, IOException {
		_cdLogic = new CDLogic(nameOfFile);
	}
	
	private void startApplication() {
	    while(true) {
	    	
	    	View.displayRequestForInput();
	    	
	    	String userCommand = _scanner.nextLine();
//	    	Parse Tokens
	    	Command command = _cdLogic.parseInput(userCommand);
//	    	Do Actions
	    	switch(command.getType()) {
		    	case ADD:	  
		    		try{
		    			Task addedTask = _cdLogic.exeAdd((CommandAdd) command);
		    			View.displayAdd(addedTask);
		    			break;
		    		}
		    		catch(IOException ex) {
		    			View.displayError(MESSAGE_ERROR_GOOGLE);
		    		}
		    		catch(LogicException e){
		    			View.displayError(e.getMessage());
		    			break;
		    		}
		    				  
	    		case DISPLAY: 
	    			try{
	    				ArrayList<Task> tasks = _cdLogic.exeDisplay((CommandDisplay) command);
	    				View.displayDisplayInLine(tasks);
	    			}
	    			catch(ArrayIndexOutOfBoundsException e){
	    				View.displayInvalid();
	    			}
	    			break;
	    					  
	    		case DELETE:  
	    			try{
	    				Task deletedTask = _cdLogic.exeDelete((CommandDelete) command);
	    				View.displayDelete(deletedTask);
	    			}
	    			catch (ArrayIndexOutOfBoundsException e){
	    				View.displayInvalid();
	    			}
	    			catch (IOException ex) {
	    				View.displayError(MESSAGE_ERROR_GOOGLE);
	    			}
	    			catch (LogicException e){
	    				View.displayError(e.getMessage());
	    			}
	    			break;
	    		
	    		case UPDATE:  
	    			try{
	    				Task updatedTask = _cdLogic.exeUpdate((CommandUpdate) command);
	    				View.displayUpdate(updatedTask);
	    				break;
	    			}
	    			catch (LogicException e){
	    				View.displayError(e.getMessage());
	    				break;
	    			}
	    			catch (IOException ex) {
	    				View.displayError(MESSAGE_ERROR_GOOGLE);
	    			}
	    			catch (ArrayIndexOutOfBoundsException e){
	    				View.displayInvalid();
	    				break;
	    			}
	    		
	    		case SEARCH:  
	    			CommandSearch commandSearch = (CommandSearch)command;
	    			ArrayList<Task> foundTasks = _cdLogic.exeSearch(commandSearch);
	    			
	    			if(commandSearch.getArrToShow()[DONE_ID]){
	    				View.displaySearchDoneInLine(foundTasks);
	    			}else{
	    				View.displaySearchInLine(foundTasks);
	    			}
	    			break;
	    		
	    		case MARK:	
	    			try{
	    				Task markedImportanceTask = _cdLogic.exeMarkImportant((CommandMark) command);
		    			View.displayMark(markedImportanceTask);
	    			}
	    			catch (ArrayIndexOutOfBoundsException e){
	    				View.displayInvalid();
	    			}
	    			catch (IOException ex) {
	    				View.displayError(MESSAGE_ERROR_GOOGLE);
	    			}
	    			catch (LogicException e){
	    				View.displayError(e.getMessage());
	    			}
	    				break;

	    		case TAG:	
	    			try{
	    				Task taggedTag = _cdLogic.exeTag((CommandTag) command);
	    				View.displayTag(taggedTag);
	    			}
	    			catch(ArrayIndexOutOfBoundsException e){
	    				View.displayInvalid();
	    			}
	    			catch(IOException ex) {
	    				View.displayError(MESSAGE_ERROR_GOOGLE);
	    			}
	    			catch (LogicException e){
	    				View.displayError(e.getMessage());
	    			}
	    				break;
	    		
	    		case LINK_GOOGLE: 
	    			try {
						_cdLogic.exeLinkGoogle();
					}
	    			
	    			catch(IOException ex) {
	    				View.displayError(MESSAGE_ERROR_GOOGLE);
	    			}
	    			catch(Exception ex) {
	    				View.displayError(MESSAGE_ERROR_GOOGLE_LOGIN);
	    			}
				  break;
	    						  

	    		case LOAD_FROM_GOOGLE:  
	    			try{
						ArrayList<Task> resultingList =  _cdLogic.exeLoadTasksFromGoogle((CommandLoadFromGoogle) command);
						View.displayDisplayInLine(resultingList);
						break;
					}
	    			catch (IOException ex) {
	    				View.displayError(MESSAGE_ERROR_GOOGLE);
	    			}
					catch (LogicException logicException){
						View.displayError(logicException.getMessage());
						break;
					}
	    			
	    		case DONE: 
	    			try {
					 	Task completedTask = _cdLogic.exeMarkDone((CommandDone) command);
					 	View.displayDone(completedTask);
				   	}
	    			catch(ArrayIndexOutOfBoundsException e){
	    				View.displayInvalid();
	    			}
	    			catch (IOException ex) {
	    				View.displayError(MESSAGE_ERROR_GOOGLE);
	    			}
	    			catch (LogicException e){
	    				View.displayError(e.getMessage());
	    			}
					break;
	    				
	    		case UNDO: 
	    			try {
						boolean undoSuccessful = _cdLogic.exeUndo();
						if(!undoSuccessful){
							View.displayError("Error: nothing to undo");
						}
						else{
							View.displayUndo();
						}
				   	}
					
					catch(IOException ex) {
						View.displayError(MESSAGE_ERROR_GOOGLE);
					}
				   	   
				   break;
	    				   
	    		case EXIT:  
	    			View.displayExit();
					System.exit(0);
					break;
	    					
	    		case REDO: 
	    			try {
						boolean redoSuccessful = _cdLogic.exeRedo();
						if(!redoSuccessful){
    					   View.displayError("Error: nothing to redo.");
    				   }
						else{
							View.displayRedo();
						}
				   }
				   catch(IOException ex) {
					   View.displayError(MESSAGE_ERROR_GOOGLE);
				   }
				   	   
				   break;
	    		
	    		case SAVE:  
	    			try {
						_cdLogic.exeSaveFile((CommandSave) command);
					}
					catch (IOException ex) {
						View.displayError(MESSAGE_ERROR_FILE_CREATION);
					}
					
					break;
	    				
	    		case CLEAR: 
	    			try {
						_cdLogic.exeClear((CommandClear) command);
					}
					catch(IOException ex) {
						View.displayError(MESSAGE_ERROR_GOOGLE);
					}
					
					break;
	    		
	    		case HELP:	
	    			View.displayHelp();
				    break;
	    		
	    		case INVALID:
	    			View.displayInvalid();
	    			break;
	    			
	    		default: 
	    				break;
	    	}
	    }
	}
	
	private static String getFileName() {
		View.displayFileNamePrompt();
		String name = _scanner.nextLine();
		
		return name;
	}
}
```
###### /src/test/DataManagerTest.java
``` java
package test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

import java.io.IOException;
import java.util.ArrayList;
import java.text.ParseException;

import calendear.util.Task;
import calendear.storage.*;

/**
 * Tests DataManager at a high level for writing and reading data from file.
 * @author Phang Chun Rong
 *
 */
public class DataManagerTest {
	
	private DataManager _dataManager;
	private ArrayList<Task> _testTasks;
	
	@Before
	public void setUpNewFile() {
		try {
			_dataManager = new DataManager("test_file.txt");
			
			_testTasks = new ArrayList<Task>();
			_testTasks.add(new Task("first task"));
			_testTasks.add(new Task("second task"));
			_testTasks.add(new Task("third task"));
			_dataManager.insertDataToFile(_testTasks);
		}
		catch (IOException ex) {
			System.out.println(ex);
		}
	}
	
	@Test
	public void testFileRead() throws ParseException, IOException {
		ArrayList<Task> tasksRead = _dataManager.getDataFromFile();
		assertEquals(tasksRead.size(), _testTasks.size());
	}

}
```
###### /src/test/TaskTest.java
``` java
package test;

import static org.junit.Assert.*;

import org.junit.Test;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.client.util.DateTime;
import java.util.GregorianCalendar;
import calendear.util.*;
/**
 * Unit Tests for Task
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
```

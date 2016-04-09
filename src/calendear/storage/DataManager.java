//@@author Phang Chun Rong
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

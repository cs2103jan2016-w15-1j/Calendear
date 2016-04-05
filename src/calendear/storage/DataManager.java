package calendear.storage;

import calendear.util.Task;


import java.text.ParseException;
import java.util.ArrayList;

/**
 * Facade Class to manage IO requirements. Depends on GoogleIO and FileIO.
 * @author Phang Chun Rong A0139060M
 *
 */
public class DataManager {
	
	private boolean _isLogined = false;
	
	/**
	 * @return an ArrayList of tasks based on the stored file.
	 */
	public ArrayList<Task> getDataFromFile() throws ParseException {
		return FileIO.buildData();
	}

	/**
	 * Writes Tasks to File
	 * @param tasksList
	 */
	public void insertDataToFile(ArrayList<Task> tasksList) {
		FileIO.updateData(tasksList);
	}

	/**
	 * Logs user into Google
	 */
	public void loginGoogle() {
		_isLogined = GoogleIO.login();
	}
	
	public boolean isLogined() {
		return _isLogined;
	}
	
	/**
	 * Adds Calendear Tasks to Google using GoogleIO.
	 * @param task
	 * @return eventId
	 */
	public String addTaskToGoogle(Task task) {
		return GoogleIO.addEvent(task);
	}
	
	/**
	 * Update Calendear Task to Google using GoogleIO.
	 * @param task
	 */
	public void updateTaskToGoogle(Task task) {
		GoogleIO.updateEvent(task);
	}
	
	/**
	 * Delete Calendear Task to Google using GoogleIO.
	 * @param task
	 */
	public void deleteTaskFromGoogle(Task task) {
		GoogleIO.deleteEvent(task);
	}
	
	/**
	 * Get Tasks that are saved in Google
	 * @return Tasks
	 */
	public ArrayList<Task> getTasksFromGoogle() {
		return GoogleIO.loadTasksFromGoogle();
	}
	
	/**
	 * Change the file path that data is stored
	 * @param path
	 * @return 
	 */
	public String changeFilePath(String path) {
		return FileIO.changeFilePath(path);
	}
	
	/**
	 * DataManager constructor
	 * @param fileName
	 */
	public DataManager(String fileName) {
		FileIO.createFile(fileName);
	}

}

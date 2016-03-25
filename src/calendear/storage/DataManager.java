package calendear.storage;

import calendear.util.Task;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Facade Class to manage IO requirements. Depends on GoogleIO and FileIO.
 * @author Phang Chun Rong A0139060M
 *
 */
public class DataManager {

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
		GoogleIO.login();
	}
	
	/**
	 * Adds Calendear Tasks to Google using GoogleIO.
	 * @param task
	 * @return eventId
	 */
	public String addTaskToGoogle(Task task) {
		return GoogleIO.addEvent(task);
	}
	
	//TODO
	public void deleteTaskFromGoogle(Task task) {
		
	}
	
	/**
	 * DataManager constructor
	 * @param fileName
	 */
	public DataManager(String fileName) {
		FileIO.createFile(fileName);
	}


}

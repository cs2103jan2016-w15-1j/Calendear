package calendear.storage;

import calendear.util.Task;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;

/**
 * DataManager class that handles file IO to flat file database
 * @author Phang Chun Rong A0139060M
 *
 */
public class DataManager {
	
	private String _nameOfFile;
	private File _file;

	/**
	 * @return an ArrayList of tasks based on the stored file.
	 */
	public ArrayList<Task> buildData() {
		ArrayList<Task> tasks = new ArrayList<Task>();
		try {
			FileReader fileReader = new FileReader(_file);

			BufferedReader bufferedReader = 
                new BufferedReader(fileReader);
			
			String line = null;
            while((line = bufferedReader.readLine()) != null) {
                Task newTask = Task.parseSaveable(line);
                tasks.add(newTask);
            }   
			fileReader.close();
		}
		catch(IOException ex) {
	    	System.out.println(ex);
		}
		return tasks;
	}
	
	/**
	 * Takes in an ArrayList of Tasks and saves it into the database.
	 * @param tasksList
	 */
	public void updateData(ArrayList<Task> tasksList) {
		wipeFile(); // Clears file content first
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(_file, true));

			for (int i=0; i < tasksList.size(); i++) {
				Task currentTask = tasksList.get(i);

				bw.write(currentTask.toSaveable());
				bw.newLine();
			}

			bw.close();
	    }
	    catch (IOException ex) {
	    	System.out.println(ex);
	    }
	}
	
	/**
	 *Clears the database on clear command
	 */
	public void wipeFile() {
		try {
			FileWriter fileWriter = new FileWriter(_file);
			fileWriter.write("");
			fileWriter.close();
		}
		catch(IOException ex) {
			System.out.println(ex);
		}	
	}

	
	/**
	 * DataManager constructor
	 * @param fileName
	 */
	public DataManager(String fileName) {
		try {
			_nameOfFile = fileName;
			_file = new File(_nameOfFile);
			_file.createNewFile();
	    }
	    catch(IOException ex) {
	    	System.out.println(ex);
	    }
	}


}

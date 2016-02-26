package calendear.storage;

import calendear.util.Task;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;

public class DataManager {
	
	private String _nameOfFile;
	private File _file;
	
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
	* DataManager constructor. 
	* Takes in a String as parameter for filename.
	*/
	public DataManager(String name) {
		try {
			_nameOfFile = name;
			_file = new File("./" + _nameOfFile);
			_file.createNewFile();
	    }
	    catch(IOException ex) {
	    	System.out.println(ex);
	    }
	}


}

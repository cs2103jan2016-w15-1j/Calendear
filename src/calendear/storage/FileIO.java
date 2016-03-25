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
 * @author Phang Chun Rong A0139060M
 *
 */
public class FileIO {
	private static String nameOfFile;
	private static File file;
	
	public static void createFile(String fileName) {
		try {
			nameOfFile = fileName;
			file = new File(nameOfFile);
			file.createNewFile();
	    }
	    catch(IOException ex) {
	    	System.out.println(ex);
	    }		
	}

	public static ArrayList<Task> buildData() throws ParseException {
		ArrayList<Task> tasks = new ArrayList<Task>();
		try {
			FileReader fileReader = new FileReader(file);

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
	public static void updateData(ArrayList<Task> tasksList) {
		wipeFile(); // Clears file content first
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));

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
	
	private static void wipeFile() {
		try {
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write("");
			fileWriter.close();
		}
		catch(IOException ex) {
			System.out.println(ex);
		}	
	}
}

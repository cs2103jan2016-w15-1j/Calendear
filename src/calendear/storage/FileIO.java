//@@author A0139060M
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
	
	/**
	 * Reads data from the database into and returns it as an ArrayList of Tasks
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
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
	 * @throws IOException
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

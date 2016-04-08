package calendear.storage;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

import java.util.ArrayList;
import java.text.ParseException;

import calendear.util.Task;

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
		_dataManager = new DataManager("test_file.txt");
		
		_testTasks = new ArrayList<Task>();
		_testTasks.add(new Task("first task"));
		_testTasks.add(new Task("second task"));
		_testTasks.add(new Task("third task"));
		_dataManager.insertDataToFile(_testTasks);
	}
	
	@Test
	public void testFileRead() throws ParseException {
		ArrayList<Task> tasksRead = _dataManager.getDataFromFile();
		assertEquals(tasksRead.size(), _testTasks.size());
	}

}

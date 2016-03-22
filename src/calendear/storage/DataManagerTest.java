package calendear.storage;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

import java.util.ArrayList;
import java.text.ParseException;

import calendear.util.Task;

public class DataManagerTest {
	
	private DataManager _dataManager;
	private ArrayList<Task> _testTasks;
	
	@Before
	public void setUpNewFile() {
		_dataManager = new DataManager("test_file.txt");
		_dataManager.wipeFile();
		
		_testTasks = new ArrayList<Task>();
		_testTasks.add(new Task("first task"));
		_testTasks.add(new Task("second task"));
		_testTasks.add(new Task("third task"));
		_dataManager.updateData(_testTasks);
	}
	
	@Test
	public void testFileRead() throws ParseException {
		ArrayList<Task> tasksRead = _dataManager.buildData();
		assertEquals(tasksRead.size(), _testTasks.size());
	}

}

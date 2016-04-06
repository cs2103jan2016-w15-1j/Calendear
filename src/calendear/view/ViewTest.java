
package calendear.view;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import calendear.util.Task;

public class ViewTest {
	@Test
	public void testTable(){
		ArrayList<Task> taskArr = new ArrayList<Task>();
		taskArr.add(new Task("go to school "));
		taskArr.get(0).setLocation("NUS NUS NUS NUS");
		taskArr.get(0).setTag("study related ");
		taskArr.get(0).setNote("Dont forget dont forget ");
		taskArr.get(0).setIsFinished(true);
		taskArr.get(0).setIsImportant(true);
		
		assertEquals(Table.getDetailsArr(taskArr.get(0)).get(7),taskArr.get(0).getNote());
		
	}
	
	@Test
	public void testSort(){
		
	}
	
	

}

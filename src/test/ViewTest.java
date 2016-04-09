
package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import calendear.view.*;
import org.junit.Test;

import calendear.util.TASK_TYPE;
import calendear.util.Task;
import calendear.view.Pair;
import calendear.view.Table;


/**
 * 
 * @@author Pan Jiyun
 *
 */

public class ViewTest {
	@Test
	public void testTable(){
		ArrayList<Task> taskArr = new ArrayList<Task>();
		taskArr.add(new Task("go to school "));
		taskArr.get(0).setLocation("NUS NUS NUS NUS NUS");
		taskArr.get(0).setTag("study related ");
		taskArr.get(0).setNote("don't forget don't forget ");
		taskArr.get(0).setIsFinished(true);
		taskArr.get(0).setIsImportant(true);
		
		assertEquals(Table.getDetailsArr(taskArr.get(0)).get(7),taskArr.get(0).getNote());
		
	}
	
	@Test
	public void testSort(){
		ArrayList<Task> taskArr = new ArrayList<Task>();
		taskArr.add(new Task("learn violin "));
		taskArr.get(0).setIsFinished(false);
		taskArr.get(0).setIsImportant(false);
		
		taskArr.add(new Task("go for piano lesson ",
				new GregorianCalendar(2016,4,2,13,0),
				new GregorianCalendar(2016,4,2,15,0)));
		taskArr.get(1).setIsFinished(false);
		taskArr.get(1).setIsImportant(true);
		
		taskArr.add(new Task("hand in piano homework ",
				new GregorianCalendar(2016,4,1,15,0)));
		taskArr.get(2).setIsFinished(false);
		taskArr.get(2).setIsImportant(true);
		
		taskArr.add(new Task("buy piano book",
				new GregorianCalendar(2016,3,18,17,0)));
		taskArr.get(3).setIsFinished(true);
		taskArr.get(3).setIsImportant(false);
		
		taskArr.add(new Task("go shopping"));
		taskArr.get(4).setType(TASK_TYPE.FLOATING);
		taskArr.get(4).setIsFinished(true);
		taskArr.get(4).setIsImportant(false);
		
		taskArr.add(new Task("practice playing the piano ",
				new GregorianCalendar(2016,4,1,8,0),
				new GregorianCalendar(2016,4,1,10,0)));
		taskArr.get(5).setIsFinished(true);
		taskArr.get(5).setIsImportant(true);
		
		ArrayList<Pair<Integer,Task>> sortedList = Table.getSortedList(taskArr);
		String expectedResult = "120534";
		String testResult = "";
		for(int i=0;i<6;i++){
			testResult += String.valueOf(sortedList.get(i).getId());
		}
		assertEquals(expectedResult,testResult);
		
	}
	
	

}

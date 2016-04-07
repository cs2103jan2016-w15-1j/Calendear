package calendear.action;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.*;

import org.junit.Test;

import calendear.util.*;

public class ActionTest {
	
	/**[0:name][1:type][2:start time]
	[3:end time][4:location][5:note]
	[6:tag][7:important][8:finished]
*/


	@Test
	public void testUpdate() throws ParseException, LogicException{
		Action action1 = new Action("action1.txt");
		int nextIndex = action1.getAmount();
		GregorianCalendar originalTime = new GregorianCalendar(01, 01, 2001);
		Task t1 = new Task("task2", originalTime);
		CommandAdd cA = new CommandAdd(t1);
		action1.exeAdd(cA);
		GregorianCalendar newTime = new GregorianCalendar(2, 1, 2001);
		String newName = "newName";
		GregorianCalendar newST = new GregorianCalendar(3, 3, 2003);
		String newTag = "newTag";
		boolean[] chklst = {true, false, true, true, false, false, false, false, false};
		Object[] objArr = {newName, null, null, (Object) newTime, null, null, null, null};
		CommandUpdate cU = new CommandUpdate(nextIndex, chklst, objArr);
		action1.exeUpdate(cU);
		assertEquals(t1.getEndTime(), newTime);
	}
	
	@Test
	public void testUndo() throws ParseException, LogicException{
		Action action1 = new Action("action2.txt");
		int nextIndex = action1.getAmount();
		GregorianCalendar originalTime = new GregorianCalendar(01, 01, 2001);
		Task t1 = new Task("task2", originalTime);
		CommandAdd cA = new CommandAdd(t1);
		action1.exeAdd(cA);
		GregorianCalendar newTime = new GregorianCalendar(2, 1, 2001);
		//System.out.println(nextIndex);
		boolean[] chklst = {false, false, false, true, false, false, false, false, false};
		Object[] objArr = {null, null, null, (Object) newTime, null, null, null, null};
		CommandUpdate cU = new CommandUpdate(nextIndex, chklst, objArr);
		action1.exeUpdate(cU);
		assertEquals(t1.getEndTime(), newTime);
		
		action1.exeUndo();
		assertEquals(t1.getEndTime(), originalTime);
	}
	
	@Test
	public void testUndoDelete() throws ParseException, LogicException{
		Action action1 = new Action("action2.txt");
		int nextIndex = action1.getAmount();
		GregorianCalendar originalTime = new GregorianCalendar(01, 01, 2001);
		Task t1 = new Task("task2", originalTime);
		CommandAdd cA = new CommandAdd(t1);
		action1.exeAdd(cA);
		int nextNextIndex = action1.getAmount(); 
		System.out.println(nextIndex + " ");
		
		action1.exeUndo();
		assertEquals(t1.getEndTime(), originalTime);
	}

	@Test
	public void testRedo() throws ParseException, LogicException{
		Action action1 = new Action("action3.txt");
		int nextIndex = action1.getAmount();
		GregorianCalendar originalTime = new GregorianCalendar(01, 01, 2001);
		Task t1 = new Task("task2", originalTime);
		CommandAdd cA = new CommandAdd(t1);
		action1.exeAdd(cA);
		GregorianCalendar newTime = new GregorianCalendar(2, 1, 2001);
		//System.out.println(nextIndex);
		boolean[] chklst = {false, false, false, true, false, false, false, false, false};
		Object[] objArr = {null, null, null, (Object) newTime, null, null, null, null};
		CommandUpdate cU = new CommandUpdate(nextIndex, chklst, objArr);
		action1.exeUpdate(cU);
		assertEquals(t1.getEndTime(), newTime);
		
		action1.exeUndo();
		assertEquals(t1.getEndTime(), originalTime);
		action1.exeRedo();
		assertEquals(t1.getEndTime(), newTime);
	}
	
	@Test
	public void testSearchName() throws ParseException, LogicException{
		Action action1 = new Action("action4.txt");
		int nextIndex = action1.getAmount();
		Task test1 = new Task("search test1 aaaa");
		Task test2 = new Task("search Test2 bbbb");
		Task test3 = new Task("search test3 cccc");
		
		action1.exeAdd(new CommandAdd(test1));
		action1.exeAdd(new CommandAdd(test2));
		action1.exeAdd(new CommandAdd(test3));
		
		boolean[] toShow = {true, false, false, false, false, false, false, false, false};
		Object[] searchWith = {"aaa", null, null, null, null, null, null, null};
		
		CommandSearch commandSearch = new CommandSearch(toShow, searchWith );
		
		ArrayList<Task> expectedResult = new ArrayList<Task>();
		expectedResult.add(null);
		expectedResult.add(test1);
		expectedResult.add(null);
		expectedResult.add(null);
		
		ArrayList<Task> testResult = action1.exeSearch(commandSearch);
		for(int i = 0; i< testResult.size(); i++){
			if(testResult.get(i) == null){
				continue;
			}
			System.out.println(i + " " + testResult.get(i).getName());
		}
		assertEquals(testResult,expectedResult);
	}
	
	@Test
	public void testSearchStartTime() throws ParseException, LogicException{
		Action action1 = new Action("action5.txt");
		Task test1 = new Task("search startTime1", 
				new GregorianCalendar( 2016,4,2,13,0),
				new GregorianCalendar( 2016,4,2,15,0));
		Task test2 = new Task("search startTime2", 
				new GregorianCalendar( 2016,4,2,12,0),
				new GregorianCalendar( 2016,4,2,16,0));
		Task test3 = new Task("search startTime3", 
				new GregorianCalendar( 2016,4,1,13,0),
				new GregorianCalendar( 2016,4,2,15,0));
		
		action1.exeAdd(new CommandAdd(test1));
		action1.exeAdd(new CommandAdd(test2));
		action1.exeAdd(new CommandAdd(test3));
		
		boolean[] toShow = {false, false, true, false, false, false, false, false, false};
		Object[] searchWith = {null, null, new GregorianCalendar( 2016,4,2,11,0), null, null, null, null, null};
		
		CommandSearch commandSearch = new CommandSearch(toShow, searchWith);
		
		ArrayList<Task> expectedResult = new ArrayList<Task>();
		expectedResult.add(null);
		expectedResult.add(test1);
		expectedResult.add(test2);
		expectedResult.add(null);
		
		ArrayList<Task> testResult = action1.exeSearch(commandSearch);	

		for(int i=0;i< testResult.size();i++){
			if(testResult.get(i)!=null){
				System.out.println(i + " " + testResult.get(i).getName());
			}
		}

		assertEquals(testResult,expectedResult);
	}
	
	@Test
	public void testSearchStartAndEndTime() throws ParseException, LogicException{
		Action action1 = new Action("action6.txt");
		Task test1 = new Task("search startTime1", 
				new GregorianCalendar( 2016,1,1,13,0),
				new GregorianCalendar( 2016,1,30,15,0));
		Task test2 = new Task("search startTime2", 
				new GregorianCalendar( 2016,1,12,12,0),
				new GregorianCalendar( 2016,1,21,16,0));
		Task test3 = new Task("search startTime3", 
				new GregorianCalendar( 2016,1,15,15,0));
		
		action1.exeAdd(new CommandAdd(test1));
		action1.exeAdd(new CommandAdd(test2));
		action1.exeAdd(new CommandAdd(test3));
		
		boolean[] toShow = {false, false, true, true, false, false, false, false, false};
		Object[] searchWith = {null, null, new GregorianCalendar( 2016,1,10,11,0), 
				new GregorianCalendar( 2016,1,20,11,0), null, null, null, null};
		
		CommandSearch commandSearch = new CommandSearch(toShow, searchWith);
		
		ArrayList<Task> expectedResult = new ArrayList<Task>();
		expectedResult.add(null);
		expectedResult.add(null);
		expectedResult.add(null);
		expectedResult.add(test3);
		
		ArrayList<Task> testResult = action1.exeSearch(commandSearch);	

		for(int i=0;i< testResult.size();i++){
			if(testResult.get(i)!=null){
				System.out.println(i + " " + testResult.get(i).getName());
			}
		}

		assertEquals(testResult,expectedResult);
	}
	
	@Test
	public void testSearchTag() throws ParseException, LogicException{
		Action action1 = new Action("action7.txt");
		Task test1 = new Task("tag test1");
		Task test2 = new Task("tag test2");
		Task test3 = new Task("tag test3");
		
		test1.setTag("school");
		test2.setTag("school # social");
		test3.setTag("social");
		
		action1.exeAdd(new CommandAdd(test1));
		action1.exeAdd(new CommandAdd(test2));
		action1.exeAdd(new CommandAdd(test3));
		
		boolean[] toShow = {false, false, false, false, false, false, true, false, false};
		Object[] searchWith = {null, null, null, null, null, null, "school", null};
		
		CommandSearch commandSearch = new CommandSearch(toShow, searchWith);
		
		ArrayList<Task> expectedResult = new ArrayList<Task>();
		expectedResult.add(null);
		expectedResult.add(test1);
		expectedResult.add(test2);
		expectedResult.add(null);
		
		ArrayList<Task> testResult = action1.exeSearch(commandSearch);	

		for(int i=0;i< testResult.size();i++){
			if(testResult.get(i)!=null){
				System.out.println(i + " " + testResult.get(i).getName());
			}
		}

		assertEquals(testResult,expectedResult);
	}

}

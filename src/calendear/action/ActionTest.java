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
	public void testSearch() throws ParseException, LogicException{
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
		
		ArrayList<Task> result = action1.exeSearch(commandSearch);
		for(int i = 0; i< result.size(); i++){
			if(result.get(i) == null){
				continue;
			}
			System.out.println(i + " " + result.get(i).getName());
		}
	}
	
}

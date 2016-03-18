package calendear.action;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.*;

import org.junit.Test;

import calendear.util.*;

public class ActionTest {

	@Test
	public void testUpdate() throws ParseException{
		Action action1 = new Action("action1.txt");
		Task t1 = new Task("task1", new GregorianCalendar(1,1,2001));
		CommandAdd cA = new CommandAdd(t1);
		action1.exeAdd(cA);
		GregorianCalendar newTime = new GregorianCalendar(2, 1, 2001);
		System.out.println(newTime.toString());
		int index = 0;
		boolean[] chklst = {false, false, false, true, false, false, false, false, false};
		Object[] objArr = {null, null, null, (Object) newTime, null, null, null, null};
		CommandUpdate cU = new CommandUpdate(index, chklst, objArr);
		action1.exeUpdate(cU);
		assertEquals(t1.getEndTime(), newTime);
	}


}

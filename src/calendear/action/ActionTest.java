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
		GregorianCalendar originalTime = new GregorianCalendar(01, 01, 2001);
		Task t1 = new Task("task2", originalTime);
		CommandAdd cA = new CommandAdd(t1);
		action1.exeAdd(cA);
		GregorianCalendar newTime = new GregorianCalendar(2, 1, 2001);
		int index = 0;//must start with new file, since index of t1 maynot be 0 other wise
		boolean[] chklst = {false, false, false, true, false, false, false, false, false};
		Object[] objArr = {null, null, null, (Object) newTime, null, null, null, null};
		CommandUpdate cU = new CommandUpdate(index, chklst, objArr);
		action1.exeUpdate(cU);
		assertEquals(t1.getEndTime(), newTime);
	}

}

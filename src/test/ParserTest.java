package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;
import org.junit.internal.ArrayComparisonFailure;

import calendear.parser.Parser;
import calendear.util.Command;
import calendear.util.CommandAdd;
import calendear.util.TASK_TYPE;

public class ParserTest {
	
	private static final int CHECKLIST_SIZE = 9;
	/* for both int[]updateChesklist and String[]newInfo 
	[0:name][1:type][2:starttime]
	[3:endtime][4:location][5:note]
	[6:tag][7:important][8:finished]
	*/
	private static final int INDEX_NAME = 0;
	private static final int INDEX_TYPE = 1;
	private static final int INDEX_START_TIME = 2;
	private static final int INDEX_END_TIME = 3;
	private static final int INDEX_LOCATION = 4;
	private static final int INDEX_NOTE = 5;
	private static final int INDEX_TAG = 6;
	private static final int INDEX_IMPORTANT = 7;
	private static final int INDEX_FINISHED = 8;

	private ArrayList<String> caseDescriptions = new ArrayList<String>();
	private ArrayList<String> rawInputs = new ArrayList<String>();
	private ArrayList<Command> expectedOutputs = new ArrayList<Command>();
	
	@Before
	public void setUp() throws Exception {
		addCaseAddDeadline();
		addCaseAddDeadlineWithOptions();
		addCaseAddEvent();
		addCaseAddEventWithOptions();
		addCaseAddFloat();
		addCaseAddFloatwithOption();
		addCaseDisplay();
		addCaseDisplayWithOptions();
		addCaseUpdate();
		addCaseUpdateWithOptions();
		addCaseDelete();
	}

	private void addCaseAddDeadline() {
		// TODO Auto-generated method stub
		caseDescriptions.add("add deadline task with minimum parameter");
		rawInputs.add("add visit garden .by the bay by 3/21/16 5:30");
		String name = "visit garden by the bay";
		GregorianCalendar deadline = new GregorianCalendar(2016, Calendar.MARCH, 21, 5, 30);
		boolean[] checklist = new boolean[CHECKLIST_SIZE];
		Object[] newInfo = new Object[CHECKLIST_SIZE];
		checklist[INDEX_NAME] = true;
		newInfo[INDEX_NAME] = name;
		checklist[INDEX_TYPE] = true;
		newInfo[INDEX_TYPE] = TASK_TYPE.DEADLINE;
		checklist[INDEX_END_TIME] = true;
		newInfo[INDEX_END_TIME] = deadline;
		expectedOutputs.add(new CommandAdd(name, checklist, newInfo));
	}

	private void addCaseAddDeadlineWithOptions() {
		// TODO Auto-generated method stub
		
	}

	private void addCaseAddEvent() {
		// TODO Auto-generated method stub
		
	}

	private void addCaseAddEventWithOptions() {
		// TODO Auto-generated method stub
		
	}

	private void addCaseAddFloat() {
		// TODO Auto-generated method stub
		
	}

	private void addCaseAddFloatwithOption() {
		// TODO Auto-generated method stub
		
	}

	private void addCaseDisplay() {
		// TODO Auto-generated method stub
		
	}

	private void addCaseDisplayWithOptions() {
		// TODO Auto-generated method stub
		
	}

	private void addCaseUpdate() {
		// TODO Auto-generated method stub
		
	}

	private void addCaseUpdateWithOptions() {
		// TODO Auto-generated method stub
		
	}

	private void addCaseDelete() {
		// TODO Auto-generated method stub
		
	}

	@Test
	public void test() {
		for (int i=0; i<rawInputs.size(); i++){
			String description = caseDescriptions.get(i);
			switch (expectedOutputs.get(i).getType()){
				case ADD:
					assertAddCmd(i, description);
					break;
				case DISPLAY:
					assertDisplayCmd(i, description);
					break;
				case UPDATE:
					assertUpdateCmd(i, description);
					break;
				case DELETE:
					assertDeleteCmd(i, description);
					break;
				case EXIT:
					assertExitCmd(i, description);
					break;
				case INVALID:
					assertInvalidCmd(i, description);
					break;


			}
		}
	}

	private void assertAddCmd(int i, String description) throws ArrayComparisonFailure {
		CommandAdd expected = (CommandAdd) expectedOutputs.get(i);
		CommandAdd actual = (CommandAdd) Parser.parse(rawInputs.get(i));
		boolean[] expectedChecklist = expected.getChecklist();
		boolean[] actualChecklist = actual.getChecklist();
		Object[] expectedNewInfo = expected.getNewInfo();
		Object[] actualNewInfo = actual.getNewInfo();
		assertArrayEquals(description, expectedChecklist, actualChecklist);
		assertArrayEquals(description, expectedNewInfo, actualNewInfo);
	}
	
	private void assertDisplayCmd(int i, String description) {
		// TODO Auto-generated method stub
		
	}

	private void assertUpdateCmd(int i, String description) {
		// TODO Auto-generated method stub
		
	}

	private void assertDeleteCmd(int i, String description) {
		// TODO Auto-generated method stub
		
	}

	private void assertExitCmd(int i, String description) {
		// TODO Auto-generated method stub
		
	}

	private void assertInvalidCmd(int i, String description) {
		// TODO Auto-generated method stub
		
	}

}

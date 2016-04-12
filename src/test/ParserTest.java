// @@author A0126513N
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
import calendear.util.CommandDelete;
import calendear.util.CommandDisplay;
import calendear.util.CommandExit;
import calendear.util.CommandInvalid;
import calendear.util.CommandLinkGoogle;
import calendear.util.CommandLoadFromGoogle;
import calendear.util.CommandRedo;
import calendear.util.CommandSave;
import calendear.util.CommandSearch;
import calendear.util.CommandTag;
import calendear.util.CommandUndo;
import calendear.util.CommandUpdate;
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
		addCaseInvalidAdd();
		addCaseAddEvent();
		addCaseAddEventWithOptions();
		addCaseAddFloat();
		addCaseAddFloatwithOption();
		addCaseDisplay();
		addCaseDisplayImportant();
		addCaseUpdateWithoutChangeTaskType();
		addCaseUpdateToDeadline();
		addCaseUpdateToEvent();
		addCaseUpdateToFloat();
		addCaseInvalidUpdate();
		addCaseDelete();
		addCaseInvalidDelete();
		addCaseExit();
		addCaseLinkGoogle();
		addCaseInvalidLinkGoogle();
		addCaseSyncGoogle();
		addCaseUndo();
		addCaseRedo();
		addCaseTag();
		addCaseInvalidTag();
		addCaseSearchByName();
		addCaseSearchMultipleCriteria();
		addCaseInvalidSearch();
		addCaseSave();
		addCaseNoKeyword();
	}

	private void addCaseNoKeyword() {
		caseDescriptions.add("user type in something which is not a command");
		rawInputs.add("can you understand this command?");
		expectedOutputs.add(new CommandInvalid("Wrong command type"));
	}

	private void addCaseSave() {
		caseDescriptions.add("save by full path");
		rawInputs.add("save C:\\Users\\Viet Thang\\Downloads\\textfile.txt");
		expectedOutputs.add(new CommandSave("C:\\Users\\Viet Thang\\Downloads\\textfile.txt"));
	}

	private void addCaseSearchByName() {
		caseDescriptions.add("search by name");
		rawInputs.add("search name visit friends                ");
		String name = "visit friends";
		boolean[] checklist = new boolean[CHECKLIST_SIZE];
		Object[] newInfo = new Object[CHECKLIST_SIZE];
		checklist[INDEX_NAME] = true;
		newInfo[INDEX_NAME] = name ;
		expectedOutputs.add(new CommandSearch(checklist, newInfo));
	}

	private void addCaseInvalidSearch() {
		caseDescriptions.add("search by name but forget to type name");
		rawInputs.add("search drink");
		expectedOutputs.add(new CommandInvalid("search drink"));
	}

	private void addCaseSearchMultipleCriteria() {
		caseDescriptions.add("search by multiple criteria");
		rawInputs.add("search name visit friends from 3/21/16 5:30pm to 21 Mar 2016 20:00 at garden .by the bay note bring cakes important");
		String name = "visit friends";
		GregorianCalendar startTime = new GregorianCalendar(2016, Calendar.MARCH, 21, 17, 30);
		GregorianCalendar endTime = new GregorianCalendar(2016, Calendar.MARCH, 21, 20, 0);
		boolean[] checklist = new boolean[CHECKLIST_SIZE];
		Object[] newInfo = new Object[CHECKLIST_SIZE];
		checklist[INDEX_NAME] = true;
		newInfo[INDEX_NAME] = name ;
		checklist[INDEX_TYPE] = true;
		newInfo[INDEX_TYPE] = TASK_TYPE.EVENT;
		checklist[INDEX_START_TIME] = true;
		newInfo[INDEX_START_TIME] = startTime;
		checklist[INDEX_END_TIME] = true;
		newInfo[INDEX_END_TIME] = endTime;
		checklist[INDEX_IMPORTANT] = true;
		newInfo[INDEX_IMPORTANT] = true;
		checklist[INDEX_LOCATION] = true;
		newInfo[INDEX_LOCATION] = "garden by the bay";
		checklist[INDEX_NOTE] = true;
		newInfo[INDEX_NOTE] = "bring cakes";
		expectedOutputs.add(new CommandSearch(checklist, newInfo));
	}

	private void addCaseInvalidTag() {
		caseDescriptions.add("case tag wrong format");
		rawInputs.add("tag two sometag");
		expectedOutputs.add(new CommandInvalid("Please enter the task id as a number"));
	}

	private void addCaseTag() {
		caseDescriptions.add("case tag");
		rawInputs.add("tag 2 sometag");
		expectedOutputs.add(new CommandTag(2, "sometag"));
	}

	private void addCaseInvalidLinkGoogle() {
		caseDescriptions.add("linkGoogle");
		rawInputs.add(" linkgoogle ");
		expectedOutputs.add(new CommandInvalid("Wrong command type"));
	}

	private void addCaseRedo() {
		caseDescriptions.add("redo");
		rawInputs.add("  redo");
		expectedOutputs.add(new CommandRedo());
	}

	private void addCaseUndo() {
		caseDescriptions.add("undo");
		rawInputs.add("undo ");
		expectedOutputs.add(new CommandUndo());
	}

	private void addCaseSyncGoogle() {
		caseDescriptions.add("syncGoogle");
		rawInputs.add(" syncGoogle ");
		expectedOutputs.add(new CommandLoadFromGoogle());
	}

	private void addCaseLinkGoogle() {
		caseDescriptions.add("linkGoogle");
		rawInputs.add(" linkGoogle ");
		expectedOutputs.add(new CommandLinkGoogle());
	}

	private void addCaseExit() {
		caseDescriptions.add("exit with some white spaces");
		rawInputs.add(" exit ");
		expectedOutputs.add(new CommandExit());
	}

	private void addCaseInvalidUpdate() {
		caseDescriptions.add("update without task id");
		rawInputs.add("update something with wrong date by tomorrow");
		expectedOutputs.add(new CommandInvalid("invalid command format"));
	}

	private void addCaseInvalidDelete() {
		caseDescriptions.add("delete with invalid parameter");
		rawInputs.add("delete bla");
		expectedOutputs.add(new CommandInvalid("Please enter the task id as a number"));
	}

	private void addCaseInvalidAdd() {
		caseDescriptions.add("add floating task with wrong datetime description");
		rawInputs.add("add something with wrong date by qwert");
		expectedOutputs.add(new CommandInvalid("add something with wrong date by qwert"));
	}

	private void addCaseAddDeadline() {
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
		caseDescriptions.add("add deadline task with options");
		rawInputs.add("add visit friendssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss at garden .by the bay by 3/21/16 5:30 important note bring cakes");
		String name = "visit friendssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss";
		GregorianCalendar deadline = new GregorianCalendar(2016, Calendar.MARCH, 21, 5, 30);
		boolean[] checklist = new boolean[CHECKLIST_SIZE];
		Object[] newInfo = new Object[CHECKLIST_SIZE];
		checklist[INDEX_NAME] = true;
		newInfo[INDEX_NAME] = name;
		checklist[INDEX_TYPE] = true;
		newInfo[INDEX_TYPE] = TASK_TYPE.DEADLINE;
		checklist[INDEX_END_TIME] = true;
		newInfo[INDEX_END_TIME] = deadline;
		checklist[INDEX_IMPORTANT] = true;
		newInfo[INDEX_IMPORTANT] = true;
		checklist[INDEX_LOCATION] = true;
		newInfo[INDEX_LOCATION] = "garden by the bay";
		checklist[INDEX_NOTE] = true;
		newInfo[INDEX_NOTE] = "bring cakes";
		expectedOutputs.add(new CommandAdd(name, checklist, newInfo));
	}

	private void addCaseAddEvent() {
		caseDescriptions.add("add event task with minimum parameter");
		rawInputs.add("add visit garden .by the bay from 3/21/16 5:30pm to 21 Mar 2016 20:00");
		String name = "visit garden by the bay";
		GregorianCalendar startTime = new GregorianCalendar(2016, Calendar.MARCH, 21, 17, 30);
		GregorianCalendar endTime = new GregorianCalendar(2016, Calendar.MARCH, 21, 20, 0);
		boolean[] checklist = new boolean[CHECKLIST_SIZE];
		Object[] newInfo = new Object[CHECKLIST_SIZE];
		checklist[INDEX_NAME] = true;
		newInfo[INDEX_NAME] = name ;
		checklist[INDEX_TYPE] = true;
		newInfo[INDEX_TYPE] = TASK_TYPE.EVENT;
		checklist[INDEX_START_TIME] = true;
		newInfo[INDEX_START_TIME] = startTime;
		checklist[INDEX_END_TIME] = true;
		newInfo[INDEX_END_TIME] = endTime;
		expectedOutputs.add(new CommandAdd(name, checklist, newInfo));
	}

	private void addCaseAddEventWithOptions() {
		caseDescriptions.add("add event task with options");
		rawInputs.add("add visit friends from 3/21/16 5:30pm to 21 Mar 2016 20:00 at garden .by the bay note bring cakes important");
		String name = "visit friends";
		GregorianCalendar startTime = new GregorianCalendar(2016, Calendar.MARCH, 21, 17, 30);
		GregorianCalendar endTime = new GregorianCalendar(2016, Calendar.MARCH, 21, 20, 0);
		boolean[] checklist = new boolean[CHECKLIST_SIZE];
		Object[] newInfo = new Object[CHECKLIST_SIZE];
		checklist[INDEX_NAME] = true;
		newInfo[INDEX_NAME] = name ;
		checklist[INDEX_TYPE] = true;
		newInfo[INDEX_TYPE] = TASK_TYPE.EVENT;
		checklist[INDEX_START_TIME] = true;
		newInfo[INDEX_START_TIME] = startTime;
		checklist[INDEX_END_TIME] = true;
		newInfo[INDEX_END_TIME] = endTime;
		checklist[INDEX_IMPORTANT] = true;
		newInfo[INDEX_IMPORTANT] = true;
		checklist[INDEX_LOCATION] = true;
		newInfo[INDEX_LOCATION] = "garden by the bay";
		checklist[INDEX_NOTE] = true;
		newInfo[INDEX_NOTE] = "bring cakes";
		expectedOutputs.add(new CommandAdd(name, checklist, newInfo));
	}

	private void addCaseAddFloat() {
		caseDescriptions.add("add floating task with minimum parameter");
		rawInputs.add("add read the lord of the ring");
		String name = "read the lord of the ring";
		boolean[] checklist = new boolean[CHECKLIST_SIZE];
		Object[] newInfo = new Object[CHECKLIST_SIZE];
		checklist[INDEX_NAME] = true;
		newInfo[INDEX_NAME] = name;
		checklist[INDEX_TYPE] = true;
		newInfo[INDEX_TYPE] = TASK_TYPE.FLOATING;
		expectedOutputs.add(new CommandAdd(name, checklist, newInfo));
	}

	private void addCaseAddFloatwithOption() {
		caseDescriptions.add("add floating task with options");
		rawInputs.add("add visit friends at garden .by the bay note bring cakes important");
		String name = "visit friends";
		boolean[] checklist = new boolean[CHECKLIST_SIZE];
		Object[] newInfo = new Object[CHECKLIST_SIZE];
		checklist[INDEX_NAME] = true;
		newInfo[INDEX_NAME] = name ;
		checklist[INDEX_IMPORTANT] = true;
		newInfo[INDEX_IMPORTANT] = true;
		checklist[INDEX_LOCATION] = true;
		newInfo[INDEX_LOCATION] = "garden by the bay";
		checklist[INDEX_NOTE] = true;
		newInfo[INDEX_NOTE] = "bring cakes";
		expectedOutputs.add(new CommandAdd(name, checklist, newInfo));
	}

	private void addCaseDisplay() {
		caseDescriptions.add("standard display");
		rawInputs.add(" display  ");
		expectedOutputs.add(new CommandDisplay(false));
	}

	private void addCaseDisplayImportant() {
		caseDescriptions.add("display important only");
		rawInputs.add("display  important");
		expectedOutputs.add(new CommandDisplay(true));
	}

	private void addCaseUpdateWithoutChangeTaskType() {
		caseDescriptions.add("update without changing type and time of task");
		rawInputs.add("update 2 done name visit friends at garden .by the bay important note bring cakes");
		String name = "visit friends";
		boolean[] checklist = new boolean[CHECKLIST_SIZE];
		Object[] newInfo = new Object[CHECKLIST_SIZE];
		checklist[INDEX_NAME] = true;
		newInfo[INDEX_NAME] = name;
		checklist[INDEX_IMPORTANT] = true;
		newInfo[INDEX_IMPORTANT] = true;
		checklist[INDEX_LOCATION] = true;
		newInfo[INDEX_LOCATION] = "garden by the bay";
		checklist[INDEX_NOTE] = true;
		newInfo[INDEX_NOTE] = "bring cakes";
		checklist[INDEX_FINISHED] = true;
		newInfo[INDEX_FINISHED] = true;
		expectedOutputs.add(new CommandUpdate(2, checklist, newInfo));
	}
	
	private void addCaseUpdateToDeadline() {
		caseDescriptions.add("update and change type to deadline task with options");
		rawInputs.add("update 2 done name visit friends tag some blatag at garden .by the bay by 3/21/16 5:30 important note bring cakes");
		String name = "visit friends";
		GregorianCalendar deadline = new GregorianCalendar(2016, Calendar.MARCH, 21, 5, 30);
		boolean[] checklist = new boolean[CHECKLIST_SIZE];
		Object[] newInfo = new Object[CHECKLIST_SIZE];
		checklist[INDEX_NAME] = true;
		newInfo[INDEX_NAME] = name;
		checklist[INDEX_TYPE] = true;
		newInfo[INDEX_TYPE] = TASK_TYPE.DEADLINE;
		checklist[INDEX_END_TIME] = true;
		newInfo[INDEX_END_TIME] = deadline;
		checklist[INDEX_IMPORTANT] = true;
		newInfo[INDEX_IMPORTANT] = true;
		checklist[INDEX_LOCATION] = true;
		newInfo[INDEX_LOCATION] = "garden by the bay";
		checklist[INDEX_NOTE] = true;
		newInfo[INDEX_NOTE] = "bring cakes";
		checklist[INDEX_FINISHED] = true;
		newInfo[INDEX_FINISHED] = true;
		checklist[INDEX_TAG] = true;
		newInfo[INDEX_TAG] = "some blatag";
		expectedOutputs.add(new CommandUpdate(2, checklist, newInfo));
	}

	private void addCaseUpdateToEvent() {
		caseDescriptions.add("update deadline task with options");
		rawInputs.add("update 15 from 3/21/16 5:30pm at garden .by the bay tag some .tag name visit friends note bring cakes to 21 Mar 2016 20:00 important");
		String name = "visit friends";
		GregorianCalendar startTime = new GregorianCalendar(2016, Calendar.MARCH, 21, 17, 30);
		GregorianCalendar endTime = new GregorianCalendar(2016, Calendar.MARCH, 21, 20, 0);
		boolean[] checklist = new boolean[CHECKLIST_SIZE];
		Object[] newInfo = new Object[CHECKLIST_SIZE];
		checklist[INDEX_NAME] = true;
		newInfo[INDEX_NAME] = name ;
		checklist[INDEX_TYPE] = true;
		newInfo[INDEX_TYPE] = TASK_TYPE.EVENT;
		checklist[INDEX_START_TIME] = true;
		newInfo[INDEX_START_TIME] = startTime;
		checklist[INDEX_END_TIME] = true;
		newInfo[INDEX_END_TIME] = endTime;
		checklist[INDEX_IMPORTANT] = true;
		newInfo[INDEX_IMPORTANT] = true;
		checklist[INDEX_TAG] = true;
		newInfo[INDEX_TAG] = "some tag";
		checklist[INDEX_LOCATION] = true;
		newInfo[INDEX_LOCATION] = "garden by the bay";
		checklist[INDEX_NOTE] = true;
		newInfo[INDEX_NOTE] = "bring cakes";
		expectedOutputs.add(new CommandUpdate(15, checklist, newInfo));
	}

	private void addCaseUpdateToFloat() {
		caseDescriptions.add("update and change task type to float with options");
		rawInputs.add("update 15 at garden .by the bay float tag some .tag name visit friends note bring cakes important");
		String name = "visit friends";
		boolean[] checklist = new boolean[CHECKLIST_SIZE];
		Object[] newInfo = new Object[CHECKLIST_SIZE];
		checklist[INDEX_NAME] = true;
		newInfo[INDEX_NAME] = name ;
		checklist[INDEX_TYPE] = true;
		newInfo[INDEX_TYPE] = TASK_TYPE.FLOATING;
		checklist[INDEX_IMPORTANT] = true;
		newInfo[INDEX_IMPORTANT] = true;
		checklist[INDEX_TAG] = true;
		newInfo[INDEX_TAG] = "some tag";
		checklist[INDEX_LOCATION] = true;
		newInfo[INDEX_LOCATION] = "garden by the bay";
		checklist[INDEX_NOTE] = true;
		newInfo[INDEX_NOTE] = "bring cakes";
		expectedOutputs.add(new CommandUpdate(15, checklist, newInfo));
	}

	private void addCaseDelete() {
		caseDescriptions.add("delete by index");
		rawInputs.add("delete 10");
		expectedOutputs.add(new CommandDelete(10));
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
				case TAG:
					assertTagCmd(i, description);
					break;
				case SEARCH:
					assertSearchCmd(i, description);
					break;
				case EXIT:
					assertExitCmd(i, description);
					break;
				case UNDO:
					assertUndoCmd(i, description);
					break;
				case REDO:
					assertRedoCmd(i, description);
					break;
				case LINK_GOOGLE:
					assertLinkGoogleCmd(i, description);
					break;
				case LOAD_FROM_GOOGLE:
					assertLoadFromGoogleCmd(i, description);
					break;
				case SAVE:
					assertSaveCmd(i, description);
					break;
				case INVALID:
					assertInvalidCmd(i, description);
					break;
				default :
					break;
			}
		}
	}

	private void assertSaveCmd(int i, String description) {
		CommandSave expected = (CommandSave) expectedOutputs.get(i);
		CommandSave actual = (CommandSave) Parser.parse(rawInputs.get(i));
		assertEquals(description, expected.getPath(), actual.getPath());
	}

	private void assertSearchCmd(int i, String description) {
		CommandSearch expected = (CommandSearch) expectedOutputs.get(i);
		CommandSearch actual = (CommandSearch) Parser.parse(rawInputs.get(i));
		boolean[] expectedChecklist = expected.getArrToShow();
		boolean[] actualChecklist = actual.getArrToShow();
		Object[] expectedNewInfo = expected.getArrSearchWith();
		Object[] actualNewInfo = actual.getArrSearchWith();
		assertArrayEquals(description, expectedChecklist, actualChecklist);
		assertArrayEquals(description, expectedNewInfo, actualNewInfo);
	}

	private void assertTagCmd(int i, String description) {
		CommandTag expected = (CommandTag) expectedOutputs.get(i);
		CommandTag actual = (CommandTag) Parser.parse(rawInputs.get(i));
		assertEquals(description, expected.getIndex(), actual.getIndex());
	}

	private void assertLoadFromGoogleCmd(int i, String description) {
		Command actual = Parser.parse(rawInputs.get(i));
		assertTrue(description, actual instanceof CommandLoadFromGoogle);
	}

	private void assertLinkGoogleCmd(int i, String description) {
		Command actual = Parser.parse(rawInputs.get(i));
		assertTrue(description, actual instanceof CommandLinkGoogle);
	}

	private void assertRedoCmd(int i, String description) {
		Command actual = Parser.parse(rawInputs.get(i));
		assertTrue(description, actual instanceof CommandRedo);
	}

	private void assertUndoCmd(int i, String description) {
		Command actual = Parser.parse(rawInputs.get(i));
		assertTrue(description, actual instanceof CommandUndo);
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
		CommandDisplay expected = (CommandDisplay) expectedOutputs.get(i);
		CommandDisplay actual = (CommandDisplay) Parser.parse(rawInputs.get(i));
		assertEquals(description, expected.isOnlyImportantDisplayed(), actual.isOnlyImportantDisplayed());
	}

	private void assertUpdateCmd(int i, String description) {
		CommandUpdate expected = (CommandUpdate) expectedOutputs.get(i);
		CommandUpdate actual = (CommandUpdate) Parser.parse(rawInputs.get(i));
		boolean[] expectedChecklist = expected.getChecklist();
		boolean[] actualChecklist = actual.getChecklist();
		Object[] expectedNewInfo = expected.getNewInfo();
		Object[] actualNewInfo = actual.getNewInfo();
		assertArrayEquals(description, expectedChecklist, actualChecklist);
		assertArrayEquals(description, expectedNewInfo, actualNewInfo);
	}

	private void assertDeleteCmd(int i, String description) {
		CommandDelete expected = (CommandDelete) expectedOutputs.get(i);
		CommandDelete actual = (CommandDelete) Parser.parse(rawInputs.get(i));
		assertEquals(description, expected.getIndex(), actual.getIndex());
	}

	private void assertExitCmd(int i, String description) {
		Command actual = Parser.parse(rawInputs.get(i));
		assertTrue(description, actual instanceof CommandExit);
	}

	private void assertInvalidCmd(int i, String description) {
		CommandInvalid expected = (CommandInvalid) expectedOutputs.get(i);
		CommandInvalid actual = (CommandInvalid) Parser.parse(rawInputs.get(i));
		assertEquals(description, expected.getErrorMessage(), actual.getErrorMessage());
	}

}

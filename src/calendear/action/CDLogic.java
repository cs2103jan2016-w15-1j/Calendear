package calendear.action;

import calendear.action.Action;
import calendear.parser.Parser;
import calendear.util.*;

import java.io.IOException;
import java.util.ArrayList;
import java.text.ParseException;

/**
 * @author Phang Chun Rong
 * Facade Class for Logic Component
 */
public class CDLogic {
	
	private Action _action;
	
	public CDLogic(String nameOfFile) throws ParseException, IOException {
		_action = new Action(nameOfFile);
	}
	
	public Command parseInput(String userInput) {
		return Parser.parse(userInput);
	}
	
	public Task exeAdd(CommandAdd commandAdd) throws LogicException, IOException {
		Task addedTask = _action.exeAdd(commandAdd);
		return addedTask;
	}
	
	public ArrayList<Task> exeDisplay(CommandDisplay commandDisplay) {
		ArrayList<Task> tasks = _action.exeDisplay(commandDisplay);
		return tasks;
	}
	
	public Task exeUpdate(CommandUpdate commandUpdate) throws LogicException, IOException {
		Task task = _action.exeUpdate(commandUpdate);
		return task;
	}
	
	public Task exeDelete(CommandDelete commandDelete) throws LogicException, IOException {
		Task task = _action.exeDelete(commandDelete);
		return task;
	}
	
	public Task exeMarkImportant(CommandMark commandMark) throws LogicException, IOException {
		Task task = _action.exeMarkImportance(commandMark);
		return task;
	}
	
	public Task exeMarkDone(CommandDone commandDone) throws LogicException, IOException{
		Task task = _action.exeMarkDone(commandDone);
		return task;
	}
	
	public void exeLinkGoogle() throws IOException, Exception {
		_action.exeLinkGoogle();
	}
	
	public boolean exeUndo() throws IOException{
		return _action.exeUndo();
	}
	
	public boolean exeRedo() throws IOException{
		return _action.exeRedo();
	}
	
	public Task exeTag(CommandTag commandTag) throws LogicException, IOException {
		return _action.exeTag(commandTag);
	}
	
	public ArrayList<Task> exeSearch(CommandSearch commandSearch){
		return _action.exeSearch(commandSearch);
	}
	
	public ArrayList<Task> exeLoadTasksFromGoogle(CommandLoadFromGoogle commandLoadFromGoogle) 
			throws LogicException, IOException {
		return _action.exeLoadTasksFromGoogle(commandLoadFromGoogle);
	}
	
	public void exeSaveFile(CommandSave commandSave) throws IOException {
		_action.exeSaveFile(commandSave);
	}
	
	public boolean exeClear(CommandClear commandClear) throws IOException {
		return _action.exeClear(commandClear);
	}
}
package calendear.action;

import calendear.action.Action;
import calendear.parser.Parser;
import calendear.util.*;

import java.util.ArrayList;
import java.text.ParseException;

/**
 * @author Phang Chun Rong
 * Facade Class for Logic Component
 */
public class CDLogic {
	
	private Action _action;
	
	public CDLogic(String nameOfFile) throws ParseException {
		_action = new Action(nameOfFile);
	}
	
	public Command parseInput(String userInput) {
		return Parser.parse(userInput);
	}
	
	public Task exeAdd(CommandAdd commandAdd) throws LogicException{
		Task addedTask = _action.exeAdd(commandAdd);
		return addedTask;
	}
	
	public ArrayList<Task> exeDisplay(CommandDisplay commandDisplay) {
		ArrayList<Task> tasks = _action.exeDisplay(commandDisplay);
		return tasks;
	}
	
	public Task exeUpdate(CommandUpdate commandUpdate) throws LogicException{
		Task task = _action.exeUpdate(commandUpdate);
		return task;
	}
	
	public Task exeDelete(CommandDelete commandDelete) throws LogicException{
		Task task = _action.exeDelete(commandDelete);
		return task;
	}
	
	public Task exeMarkImportant(CommandMark commandMark) throws LogicException{
		Task task = _action.exeMarkImportance(commandMark);
		return task;
	}
	
	public Task exeMarkDone(CommandDone commandDone) throws LogicException{
		Task task = _action.exeMarkDone(commandDone);
		return task;
	}
	
	public void exeLinkGoogle() {
		_action.exeLinkGoogle();
	}
	
	public boolean exeUndo(){
		return _action.exeUndo();
	}
	
	public boolean exeRedo(){
		return _action.exeRedo();
	}
	
	public Task exeTag(CommandTag commandTag) throws LogicException{
		return _action.exeTag(commandTag);
	}
	
	public ArrayList<Task> exeSearch(CommandSearch commandSearch){
		return _action.exeSearch(commandSearch);
	}
	
	public ArrayList<Task> exeLoadTasksFromGoogle(CommandLoadFromGoogle commandLoadFromGoogle) throws LogicException{
		return _action.exeLoadTasksFromGoogle(commandLoadFromGoogle);
	}
	
	public String exeSaveFile(CommandSave commandSave) {
		return _action.exeSaveFile(commandSave);
	}
	
	public boolean exeClear(CommandClear commandClear){
		return _action.exeClear(commandClear);
	}
}
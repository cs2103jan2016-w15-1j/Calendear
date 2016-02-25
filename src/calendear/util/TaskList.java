package calendear.util;

import java.util.ArrayList;

public class TaskList {
	private TaskList prevVersion;
	private ArrayList<Task> list;
	
	public TaskList(){
		list = new ArrayList<Task>();
		prevVersion = null;
	}
	
	public void addTask(Task t){
		list.add(t);
	}
	
	public void deleteTask(Task t){
		list.remove(t);
	}
	
	public void replaceTask(int index, Task t){
		list.set(index,t);
	}
	
	public void setPrevVersion(TaskList prevVersion){
		this.prevVersion = prevVersion;
	}
	
	public TaskList getPrevVersion(){
		return prevVersion;
	}

}

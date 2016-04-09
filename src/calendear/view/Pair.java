package calendear.view;


/**
 * Pair of index and task
 * @@author Pan Jiyun
 *
 * @param <T>
 * @param <Task>
 */

public class Pair<T,Task> {
	private  Integer id;
	private  Task task;
	
	public Pair(Integer id, Task task){
		this.id = id;
		this.task = task;
	}
	
	public void setId(Integer id){
		this.id = id;
	}
	
	public void setTask(Task task){
		this.task = task;
	}
	
	public Integer getId(){
		return id;
	}
	
	public Task getTask(){
		return task;
	}
}

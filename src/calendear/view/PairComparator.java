package calendear.view;
import java.util.Comparator;

import calendear.util.TASK_TYPE;
import calendear.util.Task;

//@@author Pan Jiyun

/**order of display:
 * incomplete important event with early start time
 * incomplete important deadline task with early end time
 * incomplete important floating task
 * incomplete event with early start time
 * incomplete deadline task with early end time
 * incomplete floating task
 * completed important event with early start time
 * completed important deadline task with early end time
 * completed important floating task 
 * completed event with early start time
 * completed deadline task with early end time
 * completed floating task
 */



public class PairComparator implements Comparator<Pair<Integer,Task>> {
	@Override
	public int compare(Pair<Integer,Task> p1,Pair<Integer,Task> p2){
		return compareComplete((Task)p1.getTask(),(Task)p2.getTask());
	}
	
	private int compareComplete(Task t1,Task t2){
		if(t1.isFinished()&&!t2.isFinished()){
			return 1;
		}
		else if(!t1.isFinished()&&t2.isFinished()) {
			return -1;
		}
		else{
			return compareImportance(t1,t2);
		}
	}
	
	private int compareImportance(Task t1,Task t2){
		if(t1.isImportant()&&!t2.isImportant()){
			return -1;
		}
		else if(!t1.isImportant()&&t2.isImportant() ) {
			return 1;
		}
		else{
			return compareType(t1,t2);
		}
	}
	
	private int compareType(Task t1,Task t2){
		int i;
		i = t1.getType().compareTo(t2.getType());
		if(i==0){
			if(t1.getType().equals(TASK_TYPE.DEADLINE)){
				return compareDeadline(t1,t2);
			}
			else if(t1.getType().equals(TASK_TYPE.EVENT)){
				return compareEvent(t1,t2);
			}
			else if(t1.getType().equals(TASK_TYPE.FLOATING)){
				return compareFloat(t1,t2);
			}
		}
		return i;
		
	}
	
	
	private int compareDeadline(Task t1, Task t2) {
		int j;
		j= t1.getEndTime().compareTo(t2.getEndTime());
		if(j==0){
			compareName(t1,t2);
		}
		return j;
	}

	private int compareEvent(Task t1, Task t2) {
		int j;
		j= t1.getStartTime().compareTo(t2.getStartTime());
		if(j==0){
			compareName(t1,t2);
		}
		return j;
	}

	private int compareFloat(Task t1, Task t2) {
		return compareName(t1,t2);
	}

	private int compareName(Task t1, Task t2){
		return (t1.getName().compareTo(t2.getName()));
	}
	
}

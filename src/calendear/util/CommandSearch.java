package calendear.util;

/**
 * searchWith should be:
 * [string, 
	TASK_TYPE, 
	[GregorianCalendar, GregorianCalendar],
	[GregorianCalendar, GregorianCalendar],
	String,
	String,
	String,
	boolean,
	boolean]
 * @author 
 *
 */
public class CommandSearch extends Command {
	
	private String searchKey;
	private boolean[] toShow;
	private Object[] searchWith;
	
	public CommandSearch(String searchKey){
		type = CMD_TYPE.SEARCH;
		this.searchKey = searchKey;
	}
	
	public CommandSearch(boolean[] toShow, Object[] searchWith){
		assert(toShow.length == searchWith.length): "toShow is different length from searchWith in CommandSearch\n";
		this.toShow = toShow;
		this.searchWith = searchWith;
	}
	
	public boolean[] getArrToShow(){
		return this.toShow;
	}
	
	public Object[] getArrSearchWith(){
		return this.searchWith;
	}
	
	public String getSearchKey(){
		return searchKey;
	}
}
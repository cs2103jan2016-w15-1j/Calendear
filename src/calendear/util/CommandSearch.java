package calendear.util;

public class CommandSearch extends Command {
	
	private String searchKey;
	
	public CommandSearch(String searchKey){
		type = CMD_TYPE.SEARCH;
		this.searchKey = searchKey;
	}
	
	public String getSearchKey(){
		return searchKey;
	}
	
}
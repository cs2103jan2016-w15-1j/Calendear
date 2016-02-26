package calendear.util;

public class CommandTag extends Command {
	
	private int index;
	private String tagName;
	
	public CommandTag(int index, String tagName){
		type = CMD_TYPE.TAG;
		this.index = index;
		this.tagName = tagName;
	}
	
	public int getIndex(){
		return index;
	}
	
	public String getTagName(){
		return tagName;
	}
	
}
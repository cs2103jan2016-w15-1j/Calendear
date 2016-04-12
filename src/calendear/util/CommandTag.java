// @@author A0126513
package calendear.util;


public class CommandTag extends Command {
	
	private int index;
	private String newTag;
	
	public CommandTag(int index, String newTag){
		type = CMD_TYPE.TAG;
		this.index = index;
		this.newTag = newTag;
	}
	
	public int getIndex(){
		return index;
	}
	
	public  String getTagName(){
		return newTag;
	}
	
	public void setTag(String newTag){
		this.newTag = newTag;
	}
}
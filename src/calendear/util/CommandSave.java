package calendear.util;

public class CommandSave extends Command {
	
	private String path;
	
	public CommandSave(String path){
		this.type = CMD_TYPE.SAVE;
		this.path = path;
	}
	
	public String getPath(){
		return path;
	}
	
}

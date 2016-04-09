// @@author Dinh Viet Thang
package calendear.util;

public class CommandInvalid extends Command {
	
	private String command;
	
	public CommandInvalid(String command){
		type = CMD_TYPE.INVALID;
		this.command = command;
	}
	
	public String getCommand(){
		return command;
	}
}
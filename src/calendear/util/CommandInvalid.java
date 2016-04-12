// @@author A0126513N
package calendear.util;

public class CommandInvalid extends Command {
	
	private String errorMessage;
	
	public CommandInvalid(String command){
		type = CMD_TYPE.INVALID;
		this.errorMessage = command;
	}
	
	public String getErrorMessage(){
		return errorMessage;
	}
}
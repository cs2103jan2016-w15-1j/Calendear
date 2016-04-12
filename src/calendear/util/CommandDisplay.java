// @@author A0126513
package calendear.util;

public class CommandDisplay extends Command {
	
	private boolean isOnlyImportantDisplayed;
	
	public CommandDisplay (boolean isOnlyImportantDisplayed){
		type = CMD_TYPE.DISPLAY;
		this.isOnlyImportantDisplayed = isOnlyImportantDisplayed;
	}
	
	public boolean isOnlyImportantDisplayed(){
		return isOnlyImportantDisplayed;
	}
	
}
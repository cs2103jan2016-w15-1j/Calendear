package calendear.util;

public class CommandSaveFile extends Command {
	
	private String _fileDirectory;
	
	public CommandSaveFile(String fileName) {
		this._fileDirectory = fileName;
		this.type = CMD_TYPE.SAVE_FILE;
	}
	
	public String getFileDirectory() {
		return _fileDirectory;
	}
}

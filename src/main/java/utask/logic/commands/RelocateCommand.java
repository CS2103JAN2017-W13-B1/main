//@@author A0138493W

package utask.logic.commands;
import java.io.File;

import utask.logic.commands.exceptions.CommandException;

public class RelocateCommand extends Command {
    
    public static final String COMMAND_WORD = "relocate";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": change the save file to a new data storage location \n"
            + "Parameters: [PATH] (Enter empty parameter will relocation to default location) \n"
            + "Example: \n"
            + "For Windows: " + COMMAND_WORD + "C:\\full\\path\\to\\destination\n"
            + "For Mac: " + COMMAND_WORD + " /Users/your_username/full/path/to/destination";
    
    public static final String MESSAGE_RELOCATE_TASK_SUCCESS = "The data storage file has been relocated: %1$s";
    public static final String MESSAGE_INVALID_PATH = "The path entered is invalid";
    
    public static String destinationPath;
    
    /**
     * Relocate Command for relocating back to default location.
     * */
    public RelocateCommand() {
        
    }
    
    /**
     * Relocate Command for relocating back to default location.
     * */
    public RelocateCommand(String path) {
        this.destinationPath = path;
    }
    
    @Override
    public CommandResult execute() throws CommandException {
        // TODO Auto-generated method stub
        return null;
    }

}

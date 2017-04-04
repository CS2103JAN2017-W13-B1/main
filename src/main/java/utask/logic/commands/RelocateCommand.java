//@@author A0138493W

package utask.logic.commands;
import utask.commons.core.EventsCenter;
import utask.staging.ui.events.FileRelocateEvent;

/**
 * Relocate the data file by the given location from user input
 */
public class RelocateCommand extends Command {

    public static final String COMMAND_WORD = "relocate";
    public static final String COMMAND_FORMAT = "[PATH (if not specify, relocate to \\data\\utask.xml)]";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": change the save file to a new data storage location \n"
            + "Parameters: " + COMMAND_FORMAT + " \n"
            + "Example: \n"
            + "For Windows: " + COMMAND_WORD + "C:\\full\\path\\to\\destination\n"
            + "For Mac: " + COMMAND_WORD + " /Users/your_username/full/path/to/destination";

    public static final String MESSAGE_RELOCATE_TASK_SUCCESS = "The data storage file has been relocated: %1$s";
    public static final String MESSAGE_INVALID_PATH = "The path entered is invalid";

    public static String destinationPath;
    private static final String DEFAULT_FOLDER_NAME = "data";

    /**
     * Relocate Command for relocating back to default location.
     * */
    public RelocateCommand() {
        this.destinationPath = DEFAULT_FOLDER_NAME;
    }

    /**
     * Relocate Command for relocating back to default location.
     * */
    public RelocateCommand(String path, boolean isValidPath) {
        if (isValidPath) {
            this.destinationPath = path;
        } else {
            this.destinationPath = DEFAULT_FOLDER_NAME;
        }
    }

    @Override
    public CommandResult execute() {
        assert model != null;
        EventsCenter.getInstance().post(new FileRelocateEvent(destinationPath));
        return new CommandResult(String.format(MESSAGE_RELOCATE_TASK_SUCCESS, destinationPath));
    }

}

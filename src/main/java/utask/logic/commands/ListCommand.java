package utask.logic.commands;

/**
 * Lists all tasks in the uTask to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";
    public static final String COMMAND_FORMAT = "";
    public static final String MESSAGE_SUCCESS = "Listed all tasks";

    @Override
    public CommandResult execute() {
        model.updateFilteredListToShowAll();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}

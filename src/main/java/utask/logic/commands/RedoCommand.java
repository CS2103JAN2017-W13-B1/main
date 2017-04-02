//@@author A0139996A
package utask.logic.commands;

import utask.commons.core.Messages;
import utask.logic.commands.exceptions.CommandException;
import utask.logic.commands.inteface.ReversibleCommand;

public class RedoCommand extends Command {

    public static final String COMMAND_WORD = "redo";

    public static final String COMMAND_FORMAT = "[INDEX (must be a positive integer)]";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Redo undone commands. "
            + "Parameters: " + COMMAND_FORMAT + "\n"
            + "Example: " + COMMAND_WORD + "\n"
            + COMMAND_WORD + " 1";

    public static final String MESSAGE_REDO_SUCCESS = "Redo: %1$s command";

    public final int index;

    public RedoCommand(int index) {
        this.index = index;
    }

    @Override
    public CommandResult execute() throws CommandException {
        if (index > model.getRedoCommandCount()) {
            throw new CommandException(Messages.MESSAGE_INVALID_REDO_RANGE);
        }

        ReversibleCommand redoCommand = model.getRedoCommand();

        try {
            redoCommand.redo();
            final ReversibleCommand undoCommand = redoCommand;
            model.addUndoCommand(undoCommand);
        } catch (Exception e) {
            //TODO: Is it sensible to add it back to the stack?
            //Will it work in the later in time of execution?
            model.addUndoCommand(redoCommand);
            throw new CommandException(Messages.MESSAGE_REDO_ERROR);
        }

        return new CommandResult(String.format(MESSAGE_REDO_SUCCESS, index));
    }
}

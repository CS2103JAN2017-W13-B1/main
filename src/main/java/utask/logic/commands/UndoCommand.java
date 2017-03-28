//@@author A0139996A
package utask.logic.commands;

import utask.commons.core.Messages;
import utask.logic.commands.exceptions.CommandException;
import utask.logic.commands.inteface.ReversibleCommand;

public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";

    public static final String COMMAND_FORMAT = "[INDEX (must be a positive integer)]";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Undo previous commands. "
            + "Parameters: " + COMMAND_FORMAT + "\n"
            + "Example: " + COMMAND_WORD + "\n"
            +  COMMAND_WORD + " 1";

    public static final String MESSAGE_UNDO_SUCCESS = "Undo: %1$s command";

    public final int index;

    public UndoCommand(int index) {
        this.index = index;
    }

    @Override
    public CommandResult execute() throws CommandException {
        if (index > model.getUndoCommandCount()) {
            throw new CommandException(Messages.MESSAGE_INVALID_UNDO_RANGE);
        }

        ReversibleCommand undoCommand = model.getUndoCommand();

        try {
            undoCommand.undo();
            final ReversibleCommand redoCommand = undoCommand;
            model.addRedoCommand(redoCommand);
        } catch (Exception e) {
            //TODO: Is it sensible to add it back to the stack?
            //Will it work in the later in time of execution?
            
            e.printStackTrace();
            
            model.addUndoCommand(undoCommand);
            throw new CommandException(Messages.MESSAGE_UNDO_ERROR);
        }

        return new CommandResult(String.format(MESSAGE_UNDO_SUCCESS, index));
    }
}

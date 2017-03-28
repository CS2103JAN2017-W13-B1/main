//@@author A0139996A
package utask.logic.parser;

import static utask.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Optional;

import utask.logic.commands.Command;
import utask.logic.commands.IncorrectCommand;
import utask.logic.commands.RedoCommand;

/**
 * Parses input arguments and creates a new UndoCommand object
 */
public class RedoCommandParser {
    /**
     * Parses the given {@code String} of arguments in the context of the UndoCommand
     * and returns an UndoCommand object for execution.
     */
    public Command parse(String args) {
        Optional<Integer> index = ParserUtil.parseIndex(args);
        if (index.isPresent() && index.get() > 0) {
            return new RedoCommand(index.get());
        } else if (!index.isPresent()) {
            return new RedoCommand(1);
        }

        return new IncorrectCommand(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, RedoCommand.MESSAGE_USAGE));
    }
}

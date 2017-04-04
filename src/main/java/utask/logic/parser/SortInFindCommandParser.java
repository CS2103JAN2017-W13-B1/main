//@@author A0139996A
package utask.logic.parser;

import static utask.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import utask.logic.commands.Command;
import utask.logic.commands.IncorrectCommand;
import utask.logic.commands.SortInFindCommand;

/**
 * Parses input arguments and creates a new SortInFindCommand object
 */
public class SortInFindCommandParser {
    /**
     * Parses the given {@code String} of arguments in the context of the SortCommandParser
     * and returns an SortInFindCommandParser object for execution.
     */
    public Command parse(String args) {
        String input = args.trim().toLowerCase();
        String column = ParserUtil.parseColumnAlphabetOfSortInFind(input);
        String orderBy = ParserUtil.parseColumnAlphabetOfSortInFind(input);

        if (!column.isEmpty()) {
            return new SortInFindCommand(column, orderBy);
        }

        return new IncorrectCommand(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortInFindCommand.MESSAGE_USAGE));
    }

}

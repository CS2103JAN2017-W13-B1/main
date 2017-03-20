//@@author A0138493W

package utask.logic.parser;

import static utask.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static utask.logic.parser.CliSyntax.KEYWORDS_ARGS_FORMAT;

import java.util.regex.Matcher;

import utask.logic.commands.Command;
import utask.logic.commands.IncorrectCommand;
import utask.logic.commands.SortCommand;

/**
 * Parses input arguments and creates a new SortCommandParser object
 */
public class SortCommandParser {
    /**
     * Parses the given {@code String} of arguments in the context of the SortCommandParser
     * and returns an SortCommandParser object for execution.
     */
    public Command parse(String args) {
        final Matcher matcher = KEYWORDS_ARGS_FORMAT.matcher(args.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        // keywords delimited by whitespace
        final String keywords = matcher.toString();
        return new SortCommand(keywords);
    }

}

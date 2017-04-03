//@@author A0139996A
package utask.logic.parser;

import utask.logic.commands.Command;
import utask.logic.commands.SortCommand;

/**
 * Parses input arguments and creates a new SortInFindCommand object
 */
public class SortInFindCommandParser {
    /**
     * Parses the given {@code String} of arguments in the context of the SortCommandParser
     * and returns an SortInFindCommandParser object for execution.
     */
    public Command parse(String args) {
        final String keyword =  args.replaceAll("\\s+", ""); //remove whitespace
        return new SortCommand(keyword);
    }

}

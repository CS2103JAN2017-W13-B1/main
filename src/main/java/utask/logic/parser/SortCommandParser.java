//@@author A0138493W

package utask.logic.parser;

import utask.logic.commands.Command;
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
        //remove whitespace
        final String keyword =  args.replaceAll("\\s+", "");
        return new SortCommand(keyword);
    }

}

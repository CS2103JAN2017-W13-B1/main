//@@author A0138493W
package utask.logic.parser;

import static utask.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utask.logic.commands.AliasCommand;
import utask.logic.commands.Command;
import utask.logic.commands.IncorrectCommand;
import utask.logic.commands.UnaliasCommand;
import utask.model.AliasCommandMap;

/**
 * Parses input arguments and creates a new UnaliasCommand object
 */
public class UnaliasCommandParser {
    private static final Pattern UNALIAS_REGEX = Pattern.compile("[\\w]+");

    public Command parse(AliasCommandMap aliasMap, String args) {
        final Matcher matcher = UNALIAS_REGEX.matcher(args.trim());
        if (matcher.matches()) {
            System.out.println(args.trim());
            return new UnaliasCommand(aliasMap, args.trim());
        }
        return new IncorrectCommand(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnaliasCommand.MESSAGE_USAGE));
    }
}

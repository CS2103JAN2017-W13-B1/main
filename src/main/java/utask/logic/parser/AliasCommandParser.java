//@@author A0138493W
package utask.logic.parser;

import static utask.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static utask.logic.parser.CliSyntax.PREFIX_ALIAS;

import java.util.Optional;

import utask.logic.commands.AliasCommand;
import utask.logic.commands.Command;
import utask.logic.commands.IncorrectCommand;
/**
 * Parses input arguments and creates a new AliasCommand object
 */
public class AliasCommandParser {
    /**
     * Parses the given {@code String} of arguments in the context of the AliasCommand
     * and returns an AliasCommand object for execution.
     */
    private static final String SPLITER_FOR_ALIAS = " /as";

    public Command parse(String args) {

        ArgumentTokenizer argsTokenizer = new ArgumentTokenizer(PREFIX_ALIAS);
        argsTokenizer.tokenize(args);

        Optional<String> alias = ParserUtil.parseAlias(args.trim().split(SPLITER_FOR_ALIAS)[0]);
        if (alias.isPresent()) {
            return new AliasCommand(alias.get(), argsTokenizer.getValue(PREFIX_ALIAS).get());
        }

        return new IncorrectCommand(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AliasCommand.MESSAGE_USAGE));
    }
}

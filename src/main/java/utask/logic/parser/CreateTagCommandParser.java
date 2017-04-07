package utask.logic.parser;

import static utask.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static utask.logic.parser.CliSyntax.PREFIX_TAGCOLOR;

import java.util.NoSuchElementException;

import utask.commons.exceptions.IllegalValueException;
import utask.logic.commands.Command;
import utask.logic.commands.CreateTagCommand;
import utask.logic.commands.IncorrectCommand;
// @@ author A0138423J
public class CreateTagCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * AddCommand and returns an AddCommand object for execution.
     */
    public Command parse(String args) {
        ArgumentTokenizer argsTokenizer = new ArgumentTokenizer(
                PREFIX_TAGCOLOR);
        argsTokenizer.tokenize(args);
        try {
            //TODO: This does not fit your [/color] design
            return new CreateTagCommand(argsTokenizer.getPreamble().get(),
                    argsTokenizer.getValue(PREFIX_TAGCOLOR).get());
        } catch (NoSuchElementException nsee) {
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                            CreateTagCommand.MESSAGE_USAGE));
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }

}

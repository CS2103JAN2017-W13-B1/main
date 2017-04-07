package utask.logic.parser;

import static utask.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static utask.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static utask.logic.parser.CliSyntax.PREFIX_DONE;
import static utask.logic.parser.CliSyntax.PREFIX_FREQUENCY;
import static utask.logic.parser.CliSyntax.PREFIX_TAG;
import static utask.logic.parser.CliSyntax.PREFIX_TIMESTAMP;

import java.util.NoSuchElementException;

import utask.commons.exceptions.IllegalValueException;
import utask.logic.commands.Command;
import utask.logic.commands.CreateCommand;
import utask.logic.commands.CreateDeadlineTaskCommand;
import utask.logic.commands.CreateEventTaskCommand;
import utask.logic.commands.CreateFloatingTaskCommand;
import utask.logic.commands.IncorrectCommand;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class CreateCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     */
    public Command parse(String args) {
        ArgumentTokenizer argsTokenizer =
                new ArgumentTokenizer(PREFIX_DEADLINE, PREFIX_TIMESTAMP, PREFIX_FREQUENCY, PREFIX_TAG, PREFIX_DONE);
        argsTokenizer.tokenize(args);
        try {
            if (argsTokenizer.getValue(PREFIX_TIMESTAMP).isPresent() &&
                    argsTokenizer.getValue(PREFIX_DEADLINE).isPresent()) {
                return new CreateEventTaskCommand(
                        argsTokenizer.getPreamble().get(),
                        argsTokenizer.getValue(PREFIX_DEADLINE).get(),
                        argsTokenizer.getValue(PREFIX_TIMESTAMP).get(),
                        argsTokenizer.tryGet(PREFIX_FREQUENCY),
                        ParserUtil.toSet(argsTokenizer.getAllValues(PREFIX_TAG)),
                        argsTokenizer.tryGet(PREFIX_DONE)
                );
            } else if (argsTokenizer.getValue(PREFIX_DEADLINE).isPresent()) {                
                return new CreateDeadlineTaskCommand(
                        argsTokenizer.getPreamble().get(),
                        argsTokenizer.getValue(PREFIX_DEADLINE).get(),
                        argsTokenizer.tryGet(PREFIX_FREQUENCY),
                        ParserUtil.toSet(argsTokenizer.getAllValues(PREFIX_TAG)),
                        argsTokenizer.tryGet(PREFIX_DONE)
                );
            } else {
                return new CreateFloatingTaskCommand(
                        argsTokenizer.getPreamble().get(),
                        argsTokenizer.tryGet(PREFIX_FREQUENCY),
                        ParserUtil.toSet(argsTokenizer.getAllValues(PREFIX_TAG)),
                        argsTokenizer.tryGet(PREFIX_DONE)
                );
            }
        } catch (NoSuchElementException nsee) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, CreateCommand.MESSAGE_USAGE));
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }
}

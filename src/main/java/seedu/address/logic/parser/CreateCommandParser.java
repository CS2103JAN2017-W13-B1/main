package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FREQUENCY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIMESTAMP;

import java.util.NoSuchElementException;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CreateCommand;
import seedu.address.logic.commands.CreateDeadlineTaskCommand;
import seedu.address.logic.commands.CreateEventTaskCommand;
import seedu.address.logic.commands.CreateFloatingTaskCommand;
import seedu.address.logic.commands.IncorrectCommand;

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
                new ArgumentTokenizer(PREFIX_DEADLINE, PREFIX_TIMESTAMP, PREFIX_FREQUENCY, PREFIX_TAG);
        argsTokenizer.tokenize(args);

        try {
            if (argsTokenizer.getValue(PREFIX_TIMESTAMP).isPresent() &&
                    argsTokenizer.getValue(PREFIX_DEADLINE).isPresent()) {
                return new CreateEventTaskCommand(
                        argsTokenizer.getPreamble().get(),
                        argsTokenizer.getValue(PREFIX_DEADLINE).get(),
                        argsTokenizer.getValue(PREFIX_TIMESTAMP).get(),
                        argsTokenizer.tryGet(PREFIX_FREQUENCY),
                        ParserUtil.toSet(argsTokenizer.getAllValues(PREFIX_TAG))
                );
            } else if (argsTokenizer.getValue(PREFIX_DEADLINE).isPresent()) {
                return new CreateDeadlineTaskCommand(
                        argsTokenizer.getPreamble().get(),
                        argsTokenizer.getValue(PREFIX_DEADLINE).get(),
                        argsTokenizer.tryGet(PREFIX_FREQUENCY),
                        ParserUtil.toSet(argsTokenizer.getAllValues(PREFIX_TAG))
                );
            } else {
                return new CreateFloatingTaskCommand(
                        argsTokenizer.getPreamble().get(),
                        argsTokenizer.tryGet(PREFIX_FREQUENCY),
                        ParserUtil.toSet(argsTokenizer.getAllValues(PREFIX_TAG))
                );
            }
        } catch (NoSuchElementException nsee) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, CreateCommand.MESSAGE_USAGE));
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }

}

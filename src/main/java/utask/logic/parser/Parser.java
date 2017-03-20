package utask.logic.parser;

import static utask.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static utask.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utask.logic.commands.ClearCommand;
import utask.logic.commands.Command;
import utask.logic.commands.CreateCommand;
import utask.logic.commands.DeleteCommand;
import utask.logic.commands.EditCommand;
import utask.logic.commands.ExitCommand;
import utask.logic.commands.FindCommand;
import utask.logic.commands.HelpCommand;
import utask.logic.commands.IncorrectCommand;
import utask.logic.commands.ListCommand;
import utask.logic.commands.SelectCommand;
import utask.staging.ui.stubs.SearchCommand;

/**
 * Parses user input.
 */
public class Parser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     */
    public Command parseCommand(String userInput) {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case CreateCommand.COMMAND_WORD:
            return new CreateCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case SelectCommand.COMMAND_WORD:
            return new SelectCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
            return new FindCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case SearchCommand.COMMAND_WORD:
            return new SearchCommand(arguments);

        default:
            return new IncorrectCommand(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}

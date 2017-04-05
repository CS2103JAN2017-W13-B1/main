//@@author A0138493W
package utask.logic.parser;

import static utask.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static utask.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.eventbus.Subscribe;

import utask.commons.core.EventsCenter;
import utask.commons.core.LogsCenter;
import utask.commons.exceptions.IllegalValueException;
import utask.logic.commands.AliasCommand;
import utask.logic.commands.ClearCommand;
import utask.logic.commands.Command;
import utask.logic.commands.CreateCommand;
import utask.logic.commands.CreateTagCommand;
import utask.logic.commands.DeleteCommand;
import utask.logic.commands.DoneCommand;
import utask.logic.commands.ExitCommand;
import utask.logic.commands.FindCommand;
import utask.logic.commands.HelpCommand;
import utask.logic.commands.IncorrectCommand;
import utask.logic.commands.ListCommand;
import utask.logic.commands.RedoCommand;
import utask.logic.commands.RelocateCommand;
import utask.logic.commands.SelectCommand;
import utask.logic.commands.SortCommand;
import utask.logic.commands.UndoCommand;
import utask.logic.commands.UndoneCommand;
import utask.logic.commands.UpdateCommand;
import utask.model.AliasCommandMap;
import utask.staging.ui.events.FindRequestEvent;
import utask.staging.ui.events.KeyboardEscapeKeyPressedEvent;
import utask.staging.ui.helper.SuggestionHelper;

/**
 * Parses user input.
 */
public class Parser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    private final Logger logger = LogsCenter.getLogger(SuggestionHelper.class);
    private boolean showExpandedSort = false;
    private AliasCommandMap aliasMap;

    public Parser() {
        aliasMap = new AliasCommandMap();
        EventsCenter.getInstance().registerHandler(this);
    }

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws IllegalValueException
     */
    public Command parseCommand(String userInput) {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");

        if (!isDefaultCommand(commandWord)) {
            try {
                commandWord = getDefaultCommand(commandWord);
            } catch (IllegalValueException e) {
                return new IncorrectCommand(e.getMessage());
            }
        }

        switch (commandWord) {

        case CreateCommand.COMMAND_WORD:
            return new CreateCommandParser().parse(arguments);

        case AliasCommand.COMMAND_WORD:
            return new AliasCommandParser().parse(aliasMap, arguments);

        case UpdateCommand.COMMAND_WORD:
            return new UpdateCommandParser().parse(arguments);

        case DoneCommand.COMMAND_WORD:
            return new DoneCommandParser().parse(arguments);

        case UndoneCommand.COMMAND_WORD:
            return new UndoneCommandParser().parse(arguments);

        case SelectCommand.COMMAND_WORD:
            return new SelectCommandParser().parse(arguments);

        case SortCommand.COMMAND_WORD:
            if (showExpandedSort) {
                return new SortInFindCommandParser().parse(arguments);
            } else {
                return new SortCommandParser().parse(arguments);
            }

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

        case UndoCommand.COMMAND_WORD:
            return new UndoCommandParser().parse(arguments);

        case RedoCommand.COMMAND_WORD:
            return new RedoCommandParser().parse(arguments);

        case RelocateCommand.COMMAND_WORD:
            return new RelocateCommandParser().parse(arguments);

        case CreateTagCommand.COMMAND_WORD:
            return new CreateTagCommandParser().parse(arguments);

        default:
            return new IncorrectCommand(MESSAGE_UNKNOWN_COMMAND);
        }

    }

    /*
     * This method is used to detect if the command is default command
     */
    private boolean isDefaultCommand (String command) {
        return aliasMap.getDefaultCommands().contains(command);
    }

    /*
     * This method is used to get default command if the command input is alias
     */
    private String getDefaultCommand (String alias) throws IllegalValueException {
        assert alias != null && !alias.isEmpty();
        if (!aliasMap.isAliasExist(alias)) {
            throw new IllegalValueException("The entered alias does not exist");
        }
        return aliasMap.getMappedCommand(alias);
    }

    @Subscribe
    private void handleFindRequestEvent(FindRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        showExpandedSort = true;
    }

    @Subscribe
    private void handleKeyboardEscapeKeyPressedEvent(KeyboardEscapeKeyPressedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        showExpandedSort = false;
    }
}

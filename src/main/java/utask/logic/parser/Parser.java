//@@author A0138493W
package utask.logic.parser;

import static utask.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static utask.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utask.logic.commands.AliasCommand;
import com.google.common.eventbus.Subscribe;

import utask.commons.core.EventsCenter;
import utask.commons.core.LogsCenter;
import utask.logic.commands.ClearCommand;
import utask.logic.commands.Command;
import utask.logic.commands.CreateCommand;
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
import utask.model.Model;
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

    private Model model;
    private final Logger logger = LogsCenter.getLogger(SuggestionHelper.class);
    private boolean showExpandedSort = false;
    
    public Parser(Model model) {
        this.model = model;

    public Parser() {
        EventsCenter.getInstance().registerHandler(this);
    }

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

        String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");

        if (!isDeafultCommand(commandWord)) {
            commandWord = getDefaultCommand(commandWord);
        }
        switch (commandWord) {

        case CreateCommand.COMMAND_WORD:
            return new CreateCommandParser().parse(arguments);

        case AliasCommand.COMMAND_WORD:
            return new AliasCommandParser().parse(arguments);

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

        default:
            return new IncorrectCommand(MESSAGE_UNKNOWN_COMMAND);
        }

    }

    /*
     * This method is used to detect if the command is default command
     */
    private boolean isDeafultCommand (String command) {
        return model.getDefaultCommandsSet().contains(command);
    }

    /*
     * This method is used to get default command if the command input is alias
     */
    private String getDefaultCommand (String command) {
        if (model.isAliasForDefaultCommandWord(command, CreateCommand.COMMAND_WORD)) {
            return CreateCommand.COMMAND_WORD;
        } else if (model.isAliasForDefaultCommandWord(command, AliasCommand.COMMAND_WORD)) {
            return AliasCommand.COMMAND_WORD;
        } else if (model.isAliasForDefaultCommandWord(command, UpdateCommand.COMMAND_WORD)) {
            return UpdateCommand.COMMAND_WORD;
        } else if (model.isAliasForDefaultCommandWord(command, DoneCommand.COMMAND_WORD)) {
            return DoneCommand.COMMAND_WORD;
        } else if (model.isAliasForDefaultCommandWord(command, UndoneCommand.COMMAND_WORD)) {
            return UndoneCommand.COMMAND_WORD;
        } else if (model.isAliasForDefaultCommandWord(command, SortCommand.COMMAND_WORD)) {
            return SortCommand.COMMAND_WORD;
        } else if (model.isAliasForDefaultCommandWord(command, DeleteCommand.COMMAND_WORD)) {
            return DeleteCommand.COMMAND_WORD;
        } else if (model.isAliasForDefaultCommandWord(command, ClearCommand.COMMAND_WORD)) {
            return ClearCommand.COMMAND_WORD;
        } else if (model.isAliasForDefaultCommandWord(command, FindCommand.COMMAND_WORD)) {
            return FindCommand.COMMAND_WORD;
        } else if (model.isAliasForDefaultCommandWord(command, ExitCommand.COMMAND_WORD)) {
            return ExitCommand.COMMAND_WORD;
        } else if (model.isAliasForDefaultCommandWord(command, HelpCommand.COMMAND_WORD)) {
            return HelpCommand.COMMAND_WORD;
        } else if (model.isAliasForDefaultCommandWord(command, UndoCommand.COMMAND_WORD)) {
            return UndoCommand.COMMAND_WORD;
        } else if (model.isAliasForDefaultCommandWord(command, RedoCommand.COMMAND_WORD)) {
            return RedoCommand.COMMAND_WORD;
        } else if (model.isAliasForDefaultCommandWord(command, RelocateCommand.COMMAND_WORD)) {
            return RelocateCommand.COMMAND_WORD;
        }
        return command;
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

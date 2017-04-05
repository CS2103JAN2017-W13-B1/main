//@@author A0138493W

package utask.logic.commands;

import utask.commons.exceptions.IllegalValueException;
import utask.logic.commands.exceptions.CommandException;
import utask.model.AliasCommandMap;

/*
 * Create an alias for a default command
 */
public class AliasCommand extends Command {

    public static final String COMMAND_WORD = "alias";
    public static final String COMMAND_FORMAT = "A /as B";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Create an alias for a command\n"
            + "Parameters: " + COMMAND_FORMAT + "\n"
            + "Example: " + COMMAND_WORD + " d /as delete";

    public static final String MESSAGE_CREATE_ALIAS_SUCCESS = "New alias %1$s added to command %2$s";
    public static final String MESSAGE_COMMAND_WORD_NOT_EXIST = "Command word %1$s is not exist";
    public static final String MESSAGE_ALIAS_CANNOT_BE_DEFAULT_COMMAND = "Alias %1$s cannot be default command";

    private final String alias;
    private final String defaultCommandWord;
    private AliasCommandMap aliasMap;

    public AliasCommand(AliasCommandMap aliasMap, String alias, String defaultCommandWord) {
        this.aliasMap = aliasMap;
        this.defaultCommandWord = defaultCommandWord;
        this.alias = alias;
    }

    @Override
    public CommandResult execute() throws CommandException {
        if (!aliasMap.getDefaultCommands().contains(defaultCommandWord)) {
            throw new CommandException(String.format(MESSAGE_COMMAND_WORD_NOT_EXIST, defaultCommandWord));
        }
        try {
            aliasMap.setAlias(alias, defaultCommandWord);
        } catch (IllegalValueException e) {
            throw new CommandException(String.format(MESSAGE_ALIAS_CANNOT_BE_DEFAULT_COMMAND, defaultCommandWord));
        }
        return new CommandResult(String.format(MESSAGE_CREATE_ALIAS_SUCCESS, alias, defaultCommandWord));
    }

}

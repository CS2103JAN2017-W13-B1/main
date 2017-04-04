//@@author A0138493W

package utask.logic.commands;

import utask.logic.commands.exceptions.CommandException;

public class AliasCommand extends Command {

    public static final String COMMAND_WORD = "alias";
    public static final String COMMAND_FORMAT = "A /as B";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Create an alias for a command\n"
            + "Parameters: " + COMMAND_FORMAT + "\n"
            + "Example: " + COMMAND_WORD + " d /as delete";

    public static final String MESSAGE_CREATE_ALIAS_SUCCESS = "New alias %1$s added to command %2$s";
    public static final String MESSAGE_COMMAND_WORD_NOT_EXIST = "Command word %1$s is not exist";

    private final String alias;
    private final String defaultCommandWord;

    public AliasCommand(String alias, String defaultCommandWord) {
        this.defaultCommandWord = defaultCommandWord;
        this.alias = alias;
    }

    @Override
    public CommandResult execute() throws CommandException {
        if (!model.getDefaultCommandsSet().contains(defaultCommandWord)) {
            throw new CommandException(String.format(MESSAGE_COMMAND_WORD_NOT_EXIST, defaultCommandWord));
        }

        return new CommandResult(String.format(MESSAGE_CREATE_ALIAS_SUCCESS, alias, defaultCommandWord));
    }

}

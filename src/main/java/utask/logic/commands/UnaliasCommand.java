//@@author A0138493W

package utask.logic.commands;

import utask.logic.commands.exceptions.CommandException;
import utask.model.AliasCommandMap;

public class UnaliasCommand extends Command {

    public static final String COMMAND_WORD = "unalias";
    public static final String COMMAND_FORMAT = "c";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Remove an alias\n"
            + "Parameters: " + COMMAND_FORMAT + "\n"
            + "Example: " + COMMAND_WORD + " c";

    public static final String MESSAGE_UNALIAS_SUCCESS = "Alias %1$s has been removed";
    public static final String MESSAGE_ALIAS_NOT_EXIST = "Alias %1$s is not exist";

    private final String alias;
    private AliasCommandMap aliasMap;

    public UnaliasCommand(AliasCommandMap aliasMap, String alias) {
        this.aliasMap = aliasMap;
        this.alias = alias;
    }

    @Override
    public CommandResult execute() throws CommandException {
        if (aliasMap.isAliasExist(alias)) {
            aliasMap.removeAlias(alias);
        } else {
            throw new CommandException(String.format(MESSAGE_ALIAS_NOT_EXIST, alias));
        }
        return new CommandResult(String.format(MESSAGE_UNALIAS_SUCCESS, alias));
    }

}

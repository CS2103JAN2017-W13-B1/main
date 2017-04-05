//@@author A0138493W

package utask.model;

import java.util.ArrayList;
import java.util.HashMap;

import utask.commons.exceptions.IllegalValueException;
import utask.staging.ui.helper.SuggestionHelper;

/*
 * Stores a HashMap of command maps aliases
 */
public class AliasCommandMap {
    private HashMap<String, String> commandToAliases;
    private final ArrayList<String> defaultCommands;
    
    public AliasCommandMap () {
        defaultCommands = (ArrayList<String>) SuggestionHelper.getInstance().getDefaultCommands();
    }

    /**
     * set a alias for a default command
     * @throws IllegalValueException
     */
    public void setAlias(String alias, String command) throws IllegalValueException {
        assert alias != null && !alias.isEmpty();
        if (defaultCommands.contains(alias)) {
            throw new IllegalValueException("Alias cannot be a default command");
        }
        if (!defaultCommands.contains(command)) {
            throw new IllegalValueException("Command is not recognized");
        }
        commandToAliases.put(alias, command);
    }
}
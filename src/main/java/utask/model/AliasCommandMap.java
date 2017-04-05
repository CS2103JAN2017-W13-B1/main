//@@author A0138493W

package utask.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utask.commons.exceptions.IllegalValueException;
import utask.staging.ui.helper.SuggestionHelper;

/*
 * Stores a HashMap of command maps aliases
 */
public class AliasCommandMap {
    private HashMap<String, String> aliasesToCommand;
    private final ArrayList<String> defaultCommands;

    public AliasCommandMap () {
        defaultCommands = (ArrayList<String>) SuggestionHelper.getInstance().getDefaultCommands();
        aliasesToCommand = new HashMap<String, String>();
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
        aliasesToCommand.put(alias, command);
    }

    public List<String> getDefaultCommands() {
        return defaultCommands;
    }

    public boolean isAliasExist (String alias) {
        return aliasesToCommand.containsKey(alias);
    }

    public String getMappedCommand (String alias) {
        return aliasesToCommand.get(alias);
    }

    public void removeAlias (String alias) {
        assert alias != null && !alias.isEmpty();
        assert aliasesToCommand.containsKey(alias);
        aliasesToCommand.remove(alias);
    }
}

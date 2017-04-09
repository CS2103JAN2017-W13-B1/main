//@@author A0138493W

package utask.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utask.commons.exceptions.IllegalValueException;
import utask.ui.helper.SuggestionHelper;

/*
 * Stores a HashMap of alias maps command
 */
public class AliasCommandMap {
    private HashMap<String, String> aliasesToCommand;
    private final ArrayList<String> defaultCommands;
    private static AliasCommandMap instance;

    private AliasCommandMap () {
        defaultCommands = (ArrayList<String>) SuggestionHelper.getInstance().getDefaultCommands();
        aliasesToCommand = new HashMap<String, String>();
    }

    public static AliasCommandMap getInstance() {
        if (instance == null) {
            instance = new AliasCommandMap();
        }
        return instance;
    }

    public void setAliasCommandMap(HashMap<String, String> aliasesToCommand) {
        this.aliasesToCommand = aliasesToCommand;
    }

    /**
     * set a alias for a default command
     * @throws IllegalValueException
     */
    public void setAlias(String alias, String command) throws IllegalValueException {
        assert alias != null && !alias.isEmpty();
        assert aliasesToCommand != null;
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

    public boolean isAliasExist(String alias) {
        assert aliasesToCommand != null;
        return aliasesToCommand.containsKey(alias);
    }

    public String getMappedCommand(String alias) {
        assert aliasesToCommand != null;
        return aliasesToCommand.get(alias);
    }

    public void removeAlias(String alias) {
        assert alias != null && !alias.isEmpty();
        assert aliasesToCommand != null;
        assert aliasesToCommand.containsKey(alias);
        aliasesToCommand.remove(alias);
    }

    public HashMap<String, String> getAliasMap() {
        assert aliasesToCommand != null;
        return aliasesToCommand;
    }
}

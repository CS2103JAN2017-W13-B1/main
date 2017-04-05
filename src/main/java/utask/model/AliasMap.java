//@@author A0138493W

package utask.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import utask.commons.exceptions.IllegalValueException;

/*
 * Stores a HashMap of command maps aliases
 */
public class AliasMap {

    private HashMap<String, ArrayList<String>> commandToAliases;

    public AliasMap () {
        setDefaultAliasMapping();
    }

    /*
     * Set default aliases for command
     */
    public void setDefaultAliasMapping() {
        HashMap<String, ArrayList<String>> defaultAliasMap = getDefaultAliasMapping();
        commandToAliases = new HashMap<String, ArrayList<String>>();
        for (String key : defaultAliasMap.keySet()) {
            commandToAliases.put(key, defaultAliasMap.get(key));
        }
    }

    /*
     * Returns default map of commands to aliases as a HashMap
     */
    private HashMap<String, ArrayList<String>> getDefaultAliasMapping() {
        HashMap<String, ArrayList<String>> defaultAliasMap = new HashMap<String, ArrayList<String>>();
        defaultAliasMap.put("alias", new ArrayList<String>());
        defaultAliasMap.put("clear", new ArrayList<String>());
        defaultAliasMap.put("create", new ArrayList<String>());
        defaultAliasMap.put("delete", new ArrayList<String>());
        defaultAliasMap.put("done", new ArrayList<String>());
        defaultAliasMap.put("exit", new ArrayList<String>());
        defaultAliasMap.put("find", new ArrayList<String>());
        defaultAliasMap.put("help", new ArrayList<String>());
        defaultAliasMap.put("redo", new ArrayList<String>());
        defaultAliasMap.put("relocate", new ArrayList<String>());
        defaultAliasMap.put("sort", new ArrayList<String>());
        defaultAliasMap.put("undo", new ArrayList<String>());
        defaultAliasMap.put("undone", new ArrayList<String>());
        defaultAliasMap.put("update", new ArrayList<String>());
        return defaultAliasMap;
    }

    /*
     * Return all the default commands as a String Set
     */
    public Set<String> getDefaultCommandsSet() {
        return getDefaultAliasMapping().keySet();
    }

    /**
     * set a alias for a default command
     * @throws IllegalValueException 
     */
    public void setAlias(String alias, String command) throws IllegalValueException {
        assert alias != null && !alias.isEmpty();
        assert(commandToAliases.get(command) != null);
        if (getDefaultCommandsSet().contains(alias)) {
            throw new IllegalValueException("Alias cannot be default command word");
        }
        ArrayList<String> aliases = commandToAliases.get(command);
        aliases.add(alias);
        commandToAliases.replace(command, aliases);
    }

    /*
     * Return true if the input alias exists for the given defaultCommandWord
     */
    public boolean isAliasForDefaultCommandWord(String alias, String defaultCommandWord) {
        return commandToAliases.get(defaultCommandWord).contains(alias);
    }
}

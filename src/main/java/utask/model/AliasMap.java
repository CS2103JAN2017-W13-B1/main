//@@author A0138493W

package utask.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

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
        defaultAliasMap.put("delete", new ArrayList<String>());
        defaultAliasMap.put("done", new ArrayList<String>());
        defaultAliasMap.put("exit", new ArrayList<String>());
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
    public Set<String> getDefaultCommands() {
        return getDefaultAliasMapping().keySet();
    }
}

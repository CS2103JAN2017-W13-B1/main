//@@author A0139996A
package utask.staging.ui.helper;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import utask.logic.commands.ClearCommand;
import utask.logic.commands.CreateCommand;
import utask.logic.commands.DeleteCommand;
import utask.logic.commands.DoneCommand;
import utask.logic.commands.ExitCommand;
import utask.logic.commands.FindCommand;
import utask.logic.commands.HelpCommand;
import utask.logic.commands.ListCommand;
import utask.logic.commands.RedoCommand;
import utask.logic.commands.RelocateCommand;
import utask.logic.commands.SelectCommand;
import utask.logic.commands.SortCommand;
import utask.logic.commands.UpdateCommand;

public class SuggestionHelper {

    private static final SortedMap<String, String> suggestionMap =  new TreeMap<String, String>();
    private static final StringBuilder sb;

    static {
        sb = new StringBuilder();
        suggestionMap.put(CreateCommand.COMMAND_WORD, CreateCommand.COMMAND_WORD + " " + CreateCommand.COMMAND_FORMAT);
        suggestionMap.put(ClearCommand.COMMAND_WORD, ClearCommand.COMMAND_WORD + " " + ClearCommand.COMMAND_FORMAT);
        suggestionMap.put(FindCommand.COMMAND_WORD, FindCommand.COMMAND_WORD + " " + FindCommand.COMMAND_FORMAT);
        suggestionMap.put(DeleteCommand.COMMAND_WORD, DeleteCommand.COMMAND_WORD + " " + DeleteCommand.COMMAND_FORMAT);
        suggestionMap.put(DoneCommand.COMMAND_WORD, DoneCommand.COMMAND_WORD + " " + DoneCommand.COMMAND_FORMAT);
        suggestionMap.put(UpdateCommand.COMMAND_WORD, UpdateCommand.COMMAND_WORD + " " + UpdateCommand.COMMAND_FORMAT);
        suggestionMap.put(ExitCommand.COMMAND_WORD, ExitCommand.COMMAND_WORD + " " + ExitCommand.COMMAND_FORMAT);
        suggestionMap.put(HelpCommand.COMMAND_WORD, HelpCommand.COMMAND_WORD + " " + HelpCommand.COMMAND_FORMAT);
        suggestionMap.put(ListCommand.COMMAND_WORD, ListCommand.COMMAND_WORD + " " + ListCommand.COMMAND_FORMAT);
        suggestionMap.put(RedoCommand.COMMAND_WORD, RedoCommand.COMMAND_WORD + " " + RedoCommand.COMMAND_FORMAT);
        suggestionMap.put(RelocateCommand.COMMAND_WORD, RelocateCommand.COMMAND_WORD
                          + " " + RelocateCommand.COMMAND_FORMAT);
        suggestionMap.put(SelectCommand.COMMAND_WORD, SelectCommand.COMMAND_WORD + " " + SelectCommand.COMMAND_FORMAT);
        suggestionMap.put(SortCommand.COMMAND_WORD, SortCommand.COMMAND_WORD + " " + SortCommand.COMMAND_FORMAT);
    }

    public static final String getInputSuggestionOfPreamble(String preamble) {
        Set<Entry<String, String>> results = filterPrefix(suggestionMap, preamble).entrySet();

        if (results.size() == 0) { //If don't recognise the command then don't show suggestion
            return "";
        } else if (results.size() == 1) {
            Entry<String, String> result = results.iterator().next();

            return result.getValue();
        } else { //results.size() > 2
            sb.setLength(0); // clears stringbuilder

            for (Map.Entry<String, String> entry : results) {
                sb.append(entry.getKey() + "  ");
            }

            return sb.toString();
        }
    }

    /*
     * Adapted from http://stackoverflow.com/questions/6713239/partial-search-in-hashmap
     * */
    private static <V> SortedMap<String, V> filterPrefix(SortedMap<String, V> baseMap, String prefix) {
        if (prefix.length() > 0) {
            char nextLetter = (char) (prefix.charAt(prefix.length() - 1) + 1);
            String end = prefix.substring(0, prefix.length() - 1) + nextLetter;
            return baseMap.subMap(prefix, end);
        }
        return baseMap;
    }
}

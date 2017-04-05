//@@author A0139996A
package utask.staging.ui.helper;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import utask.commons.core.EventsCenter;
import utask.commons.core.LogsCenter;
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
import utask.logic.commands.SortInFindCommand;
import utask.logic.commands.UpdateCommand;
import utask.staging.ui.events.FindRequestEvent;
import utask.staging.ui.events.KeyboardEscapeKeyPressedEvent;

public class SuggestionHelper {

    private final Logger logger = LogsCenter.getLogger(SuggestionHelper.class);
    private final SortedMap<String, String> suggestionMap =  new TreeMap<String, String>();
    private final StringBuilder sb;

    private static SuggestionHelper instance;

    private SuggestionHelper() {
        EventsCenter.getInstance().registerHandler(this);
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

        //Dynamic Suggested
        suggestionMap.put(SortCommand.COMMAND_WORD, SortCommand.COMMAND_WORD + " " + SortCommand.COMMAND_FORMAT);
    }

    public static SuggestionHelper getInstance() {
        if (instance == null) {
            instance = new SuggestionHelper();
        }

        return instance;
    }

    public final String getInputSuggestionOfPreamble(String preamble) {
        assert preamble != "" && preamble != null;

        Set<Entry<String, String>> results = filterPrefix(suggestionMap, preamble).entrySet();

        if (results.size() == 0) { //If don't recognize the command then show all possible commands
            return showAllSuggetions();
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

    private String showAllSuggetions() {
        sb.setLength(0); // clears stringbuilder

        for (Entry<String, String> entry : suggestionMap.entrySet()) {
            String command = entry.getKey();
            sb.append(command + "  ");
        }

        return sb.toString();
    }

    @Subscribe
    private void handleFindRequestEvent(FindRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        suggestionMap.put(SortInFindCommand.COMMAND_WORD, SortInFindCommand.COMMAND_WORD + " "
                        + SortInFindCommand.COMMAND_FORMAT);
    }

    @Subscribe
    private void handleKeyboardEscapeKeyPressedEvent(KeyboardEscapeKeyPressedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        suggestionMap.put(SortCommand.COMMAND_WORD, SortCommand.COMMAND_WORD + " " + SortCommand.COMMAND_FORMAT);
    }
}

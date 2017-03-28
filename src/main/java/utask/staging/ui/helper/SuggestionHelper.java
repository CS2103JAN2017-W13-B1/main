//@@author A0139996A
package utask.staging.ui.helper;

import java.util.HashMap;

import utask.logic.commands.CreateCommand;
import utask.logic.commands.FindCommand;

public class SuggestionHelper {

    private static final HashMap<String, String> suggestionMap = new HashMap<>();

    static {
        suggestionMap.put(CreateCommand.COMMAND_WORD, CreateCommand.COMMAND_WORD + " " + CreateCommand.COMMAND_FORMAT);
        suggestionMap.put(FindCommand.COMMAND_WORD, FindCommand.COMMAND_WORD + " " + CreateCommand.COMMAND_FORMAT);
    }

    //TODO: Upgrade to balanced tree for a more dynamic suggestion
    public static final String getInputSuggestionOfPreamble(String preamble) {
        //If we do not recognise the command, then don't show suggestion
        if (!suggestionMap.containsKey(preamble)) {
            return "";
        }

        return suggestionMap.get(preamble);
    }
}

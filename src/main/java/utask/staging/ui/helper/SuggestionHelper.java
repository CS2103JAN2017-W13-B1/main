//@@author A0139996A
package utask.staging.ui.helper;

import java.util.HashMap;

import utask.logic.commands.CreateCommand;

public class SuggestionHelper {

    private static final HashMap<String, String> inputSuggestion = new HashMap<>();

    static {
        inputSuggestion.put(CreateCommand.COMMAND_WORD, CreateCommand.COMMAND_FORMAT);
    }

    //TODO: Upgrade to balanced tree for a more dynamic suggestion
    public static final String getInputSuggestionOfPreamble(String preamble) {

        if (!inputSuggestion.containsKey(preamble)) {
            return "";
        }

        return inputSuggestion.get(preamble);
    }
}

package utask.staging.ui;

import java.util.HashMap;

public class UiDataDisplayHelper {

    private static final HashMap<String, String> inputSuggestion = new HashMap<>();

    static {
        inputSuggestion.put("create",
                "create NAME [/by DEADLINE] [/from START_TIME to END_TIME] [/repeat FREQUENCY] [/tag TAG...]");
        inputSuggestion.put("update",
                "update INDEX [/name NAME] [/by DEADLINE] [/from START_TIME to END_TIME] [/repeat FREQUENCY]"
                + " [/tag TAG...][/done YES|NO]");
        inputSuggestion.put("delete", "delete INDEX");
    }

    public static final String getInputSuggestionForPreamble(String preamble) {

        if (!inputSuggestion.containsKey(preamble)) {
            return "";
        }

        return inputSuggestion.get(preamble);
    }
}

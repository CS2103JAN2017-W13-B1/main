//@@author A0139996A
package utask.ui.helper;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import utask.logic.commands.ClearCommand;
import utask.logic.commands.CreateCommand;
import utask.staging.ui.helper.SuggestionHelper;

public class SuggestionHelperTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void known_commands_showCommandFormat() {
        String suggestion = SuggestionHelper.getInputSuggestionOfPreamble("create");
        String commandFormat = CreateCommand.COMMAND_WORD + " " + CreateCommand.COMMAND_FORMAT;
        assert (suggestion.equals(commandFormat));
    }

    @Test
    public void partial_commands_showPossibleCommandNames() {
        String suggestion = SuggestionHelper.getInputSuggestionOfPreamble("c").trim();
        String commandFormat = ClearCommand.COMMAND_WORD + "  " + CreateCommand.COMMAND_WORD;
        assert (suggestion.equals(commandFormat));
    }

    @Test
    public void unknown_commands_showAllPossibleCommandNames() {
        String suggestion = SuggestionHelper.getInputSuggestionOfPreamble("az123");
        assert (!suggestion.isEmpty());
    }

    @Test
    public void invalid_nullPreamble_notAllowed() {
        thrown.expect(AssertionError.class);
        SuggestionHelper.getInputSuggestionOfPreamble(null);
    }

    @Test
    public void invalid_emptyPreamble_notAllowed() {
        thrown.expect(AssertionError.class);
        SuggestionHelper.getInputSuggestionOfPreamble("");
    }
}

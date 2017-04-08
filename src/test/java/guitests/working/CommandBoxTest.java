package guitests.working;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import utask.ui.CommandBox;

public class CommandBoxTest extends UTaskGuiTest {

    private static final String CLEAR_COMMAND = "clear";
    private static final String LISTTAG_COMMAND = "listtag";
    private static final String COMMAND_THAT_SUCCEEDS = "select 3";
    private static final String COMMAND_THAT_FAILS = "invalid command";

    private ArrayList<String> defaultStyleOfCommandBox;
    private ArrayList<String> errorStyleOfCommandBox;

    @Before
    public void setUp() {
        defaultStyleOfCommandBox = new ArrayList<>(commandBox.getStyleClass());
        assertFalse("CommandBox default style classes should not contain error style class.",
                    defaultStyleOfCommandBox.contains(CommandBox.ERROR_TEXTFIELD_STYLE_CLASS));

        // build style class for error
        errorStyleOfCommandBox = new ArrayList<>(defaultStyleOfCommandBox);
        errorStyleOfCommandBox.add(CommandBox.ERROR_TEXTFIELD_STYLE_CLASS);
    }

    @Test
    public void commandBox_commandSucceeds_textClearedAndStyleClassRemainsTheSame() {
        commandBox.runCommand(COMMAND_THAT_SUCCEEDS);

        assertEquals("", commandBox.getCommandInput());
        assertEquals(defaultStyleOfCommandBox, commandBox.getStyleClass());
    }

    @Test
    public void commandBox_commandFails_textStaysAndErrorStyleClassAdded() {
        commandBox.runCommand(COMMAND_THAT_FAILS);

        assertEquals(COMMAND_THAT_FAILS, commandBox.getCommandInput());
        assertEquals(errorStyleOfCommandBox, commandBox.getStyleClass());
    }

    @Test
    public void commandBox_anyKey_textClearedAndErrorStyleClassRemoved() {
        commandBox.runCommand(COMMAND_THAT_FAILS);
        commandBox.hitArrowRightKey();
        assertEquals(COMMAND_THAT_FAILS, commandBox.getCommandInput());
        assertEquals(defaultStyleOfCommandBox, commandBox.getStyleClass());
    }

    @Test
    public void commandBox_commandSucceedsAfterFailedCommand_textClearedAndErrorStyleClassRemoved() {
        // add error style to simulate a failed command
        commandBox.getStyleClass().add(CommandBox.ERROR_TEXTFIELD_STYLE_CLASS);
        commandBox.runCommand(COMMAND_THAT_SUCCEEDS);

        assertEquals("", commandBox.getCommandInput());
        assertEquals(defaultStyleOfCommandBox, commandBox.getStyleClass());
    }

    //TODO
    @Test
    public void commandBox_canCycleCommand() {
        commandBox.runCommand(CLEAR_COMMAND);
        commandBox.runCommand(LISTTAG_COMMAND);
        commandBox.hitEscapeKey();
        commandBox.hitArrowUpKey();
//        assert(commandBox.getCommandInput().equals(LISTTAG_COMMAND));
//        commandBox.hitArrowUpKey();
//        assert(commandBox.getCommandInput().equals(LISTTAG_COMMAND));
        commandBox.hitArrowDownKey();
//        assert(commandBox.getCommandInput().equals(CLEAR_COMMAND));
    }
}

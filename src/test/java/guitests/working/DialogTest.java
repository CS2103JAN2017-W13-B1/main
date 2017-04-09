package guitests.working;

import org.junit.Before;
import org.junit.Test;

import utask.logic.commands.AliasCommand;
import utask.logic.commands.ClearCommand;
import utask.logic.commands.CreateCommand;
import utask.logic.commands.ListAliasCommand;
import utask.logic.commands.ListTagCommand;

public class DialogTest extends UTaskGuiTest {

    private static final String CREATE_ALIAS_C_AS_CREATE =
            AliasCommand.COMMAND_WORD + "c /as " + CreateCommand.COMMAND_WORD;

    @Before
    public void clear() {
        commandBox.runCommand(ClearCommand.COMMAND_WORD);
    }

    @Test
    public void show_listTagsDialogOnEmptyList_success() {
        commandBox.runCommand(ListTagCommand.COMMAND_WORD);
        commandBox.hitEscapeKey();
    }

    @Test
    public void show_listAliasDialogOnEmptyList_success() {
        commandBox.runCommand(ListAliasCommand.COMMAND_WORD);
        commandBox.hitEscapeKey();
    }

    @Test
    public void show_listTagsDialogOnNonEmptyList_success() {
        commandBox.runCommand(td.taskWithTags.getAddCommand());
        commandBox.runCommand(ListTagCommand.COMMAND_WORD);
        commandBox.hitEscapeKey();
    }

    @Test
    public void show_listAliasDialogOnNonEmptyList_success() {
        commandBox.runCommand(CREATE_ALIAS_C_AS_CREATE);
        commandBox.runCommand(ListAliasCommand.COMMAND_WORD);
        commandBox.hitEscapeKey();
    }
}

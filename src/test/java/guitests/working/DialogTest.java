package guitests.working;

import org.junit.Test;

import utask.logic.commands.ListAliasCommand;
import utask.logic.commands.ListTagCommand;

public class DialogTest extends UTaskGuiTest {

    @Test
    public void show_listTagsDialog_success() {
        commandBox.runCommand(ListTagCommand.COMMAND_WORD);
        commandBox.hitEscapeKey();
    }

    @Test
    public void show_listAliasDialog_success() {
        commandBox.runCommand(ListAliasCommand.COMMAND_WORD);
        commandBox.hitEscapeKey();
    }
}

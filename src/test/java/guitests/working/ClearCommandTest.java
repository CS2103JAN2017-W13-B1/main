package guitests.working;

import org.junit.Test;

public class ClearCommandTest extends UTaskGuiTest {

    @Test
    public void clear() {

        //verify a non-empty list can be cleared
//        assertTrue(future.isListMatching(td.getTypicalPersons()));
        assertClearCommandSuccess();

        //verify other commands can work after a clear command
        commandBox.runCommand(td.todo.getAddCommand());
        assert(todoListPanel.isListMatching(td.todo));
        commandBox.runCommand("delete 1");
        assertListSize(0);

        //verify clear command works when the list is empty
        assertClearCommandSuccess();
    }

    private void assertClearCommandSuccess() {
        commandBox.runCommand("clear");
        assertListSize(0);
        assertResultMessage("UTask has been cleared!");
    }
}

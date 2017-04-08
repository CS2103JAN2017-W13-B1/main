package guitests.working;

import org.junit.Test;

import guitests.UTaskGuiTest;
import utask.commons.core.Messages;
import utask.testutil.TestTask;

public class FindCommandTest extends UTaskGuiTest {
    private static final String consoleMessage = "Searching for [%s]\nFound %d task(s)\nPress [ESC] to return";
    @Test
    public void find_nonEmptyList() {
        assertFindResult("find", "Mark"); // no results
        assertFindResult("find", "Alice", td.d, td.g); // multiple results

        //find after deleting one result
        commandBox.runCommand("delete 1");
        assertFindResult("find", "Alice", td.g);
        commandBox.hitEscapeKey();
    }

    @Test
    public void find_emptyList() {
        commandBox.runCommand("clear");
        assertFindResult("find", "Jean"); // no results
        commandBox.hitEscapeKey();
    }

    @Test
    public void find_invalidCommand_fail() {
        commandBox.runCommand("findgeorge");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
        commandBox.hitEscapeKey();
    }

    private void assertFindResult(String command, String keywords, TestTask... expectedHits) {
        String runCommand = command + " " +  keywords;
        commandBox.runCommand(runCommand);
        assertSearchListSize(expectedHits.length);
        assertResultMessage(String.format(consoleMessage, keywords, expectedHits.length));
        //assertTrue(find.isListMatching(expectedHits));
    }
}

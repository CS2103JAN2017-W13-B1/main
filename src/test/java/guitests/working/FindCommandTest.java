package guitests.working;

import org.junit.Test;

import utask.commons.core.Messages;
import utask.logic.commands.SortCommand;
import utask.logic.commands.SortInFindCommand;
import utask.testutil.TestTask;

public class FindCommandTest extends UTaskGuiTest {
    private static final String UNKNOWN_COLUMN_ALPHABET = "z";
    private static final String UNKNOWN_SORTORDER = "asscemadfiong";
    private static final String consoleMessage = "Searching for [%s]\nFound %d task(s)\nPress [ESC] to return";
    @Test
    public void find_nonEmptyList() {
        assertFindResult("find", "Mark"); // no results
        assertFindResult("find", "Alice", td.d, td.g); // multiple results
        commandBox.runCommand("select 1");
        //find after deleting one result
        commandBox.runCommand("delete 1");
        assertFindResult("find", "Alice", td.g);
        commandBox.hitEscapeKey();
    }

    private static final String ASCENDING = "asc";
    private static final String DESCENDING = "dsc";
    private static final String[] COLUMNS = {"a", "b", "c", "d", "e", "f"};

    @Test
    public void find_andSort() {
        assertFindResult("find", "a");
        for (String column : COLUMNS) {
            commandBox.runCommand(SortCommand.COMMAND_WORD + " " + column + " " + ASCENDING);
            commandBox.runCommand(SortCommand.COMMAND_WORD + " " + column + " " + DESCENDING);
        }
        commandBox.hitEscapeKey();
    }

    @Test
    public void find_unknownSort() {
        assertFindResult("find", "a");

        //Unknown sort order
        commandBox.runCommand(SortInFindCommand.COMMAND_WORD + " " + COLUMNS[0] + " "  + UNKNOWN_SORTORDER);
        assertResultMessage(SortInFindCommand.MESSAGE_USAGE);

        //Unknown column alphabet
        commandBox.runCommand(SortCommand.COMMAND_WORD + " " + UNKNOWN_COLUMN_ALPHABET + " "  + ASCENDING);
        assertResultMessage(SortInFindCommand.MESSAGE_USAGE);
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

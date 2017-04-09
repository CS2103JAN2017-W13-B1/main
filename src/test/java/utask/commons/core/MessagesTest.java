package utask.commons.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
//@@author A0138423J
public class MessagesTest {
    @Test
    public void isValidMessageString() {
        assertEquals("Unknown command", Messages.MESSAGE_UNKNOWN_COMMAND);
        assertEquals("Tags names should be alphanumeric", Messages.MESSAGE_TAG_CONSTRAINTS);
        assertEquals("Sort order must be ASC or DSC", Messages.MESSAGE_INVALID_SORT_ORDER);
        assertEquals("The task index provided is invalid", Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        assertEquals("Unable to undo this many times", Messages.MESSAGE_INVALID_UNDO_RANGE);
        assertEquals("Unable to redo this many times", Messages.MESSAGE_INVALID_REDO_RANGE);
        assertEquals("An error has occurred while undo-ing", Messages.MESSAGE_UNDO_ERROR);
        assertEquals("An error has occurred while redo-ing", Messages.MESSAGE_REDO_ERROR);
        assertEquals("Invalid command format! \n%1$s", Messages.MESSAGE_INVALID_COMMAND_FORMAT);
        assertEquals("%1$d tasks listed!", Messages.MESSAGE_TASKS_LISTED_OVERVIEW);
        assertEquals("Searching for %s\nFound %d task(s)\nPress [ESC] to return", Messages.MESSAGE_SEARCH_RESULTS);
    }
}

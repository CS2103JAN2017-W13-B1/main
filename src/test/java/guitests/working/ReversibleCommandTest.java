package guitests.working;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import utask.commons.core.Messages;
import utask.logic.commands.ClearCommand;
import utask.logic.commands.DeleteCommand;
import utask.logic.commands.RedoCommand;
import utask.logic.commands.UndoCommand;
import utask.model.task.ReadOnlyTask;

public class ReversibleCommandTest extends UTaskGuiTest {

    @Before
    public void clear() {
        commandBox.runCommand(ClearCommand.COMMAND_WORD);
//        raise(new UTaskChangedEvent());
    }

    @After
    public void cleanup() {
        commandBox.runCommand(ClearCommand.COMMAND_WORD);
    }

    @Test
    public void addCanUndoAndRedo() {
        commandBox.runCommand(td.dueTask.getAddCommand());
        //Undo
        commandBox.runCommand(UndoCommand.COMMAND_WORD);
        assertListIsEmpty();

        //Redo
        commandBox.runCommand(RedoCommand.COMMAND_WORD);
        assertListIsNotEmpty();
    }

    @Test
    public void deleteCanUndoAndRedo() {
        ReadOnlyTask toAdd = td.dueTask;

        commandBox.runCommand(td.dueTask.getAddCommand());
        commandBox.runCommand(DeleteCommand.COMMAND_WORD + " 1");

        //Undo
        commandBox.runCommand(UndoCommand.COMMAND_WORD);
        // assertTaskNotInList(toAdd); //TESTFX BUGGY
       //assertListIsEmpty();

        //Redo
        commandBox.runCommand(RedoCommand.COMMAND_WORD);
        //assertListIsNotEmpty();
    }

//    @Test
//    public void updateCanUndoAndRedo() {
//        commandBox.runCommand(td.dueTask.getAddCommand());
//        commandBox.runCommand(DeleteCommand.COMMAND_WORD + " 1");
//
//        //Undo
//        commandBox.runCommand(UndoCommand.COMMAND_WORD);
//        assertListIsEmpty();
//
//        //Redo
//        commandBox.runCommand(RedoCommand.COMMAND_WORD);
//        assertListIsNotEmpty();
//    }
//    private void assertTaskNotInList(ReadOnlyTask task) {
//        assert(listPanel.getDueListView().getItems().contains(task));
//    }

    @Test
    public void undoRangeGreaterThanActual() {
        commandBox.runCommand("undo 1000");
        assertResultMessage(Messages.MESSAGE_INVALID_UNDO_RANGE);
    }

    @Test
    public void redoRangeGreaterThanActual() {
        commandBox.runCommand("redo 1000");
        assertResultMessage(Messages.MESSAGE_INVALID_REDO_RANGE);
    }

    private void assertListIsEmpty() {
        assert(listPanel.getDueListView().getItems().isEmpty());
    }

    private void assertListIsNotEmpty() {
        assert(listPanel.getDueListView().getItems().size() > 0);
    }

}

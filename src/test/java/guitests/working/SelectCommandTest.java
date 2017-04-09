package guitests.working;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import utask.model.task.ReadOnlyTask;

public class SelectCommandTest extends UTaskGuiTest {

    @Test
    public void selectPerson_nonEmptyList() {

        assertSelectionInvalid(100); // invalid index
        assertNoTaskSelected();

        //assertSelectionSuccess(1); // first person in the list
//        int personCount = td.getTypicalPersons().length;
//        assertSelectionSuccess(personCount); // last person in the list
//        int middleIndex = personCount / 2;
//        assertSelectionSuccess(middleIndex); // a person in the middle of the list
//
//        assertSelectionInvalid(personCount + 1); // invalid index
//        assertPersonSelected(middleIndex); // assert previous selection remains

        /* Testing other invalid indexes such as -1 should be done when testing the SelectCommand */
    }

    //CANNOT WORK TGT
    @Test
    public void selectPerson_emptyList() {
        commandBox.runCommand("clear");
        assertListSize(0);
        //assertSelectionInvalid(1); //invalid index
    }

    private void assertSelectionInvalid(int index) {
        commandBox.runCommand("select " + index);
        assertResultMessage("The task index provided is invalid");
    }

    private void assertSelectionSuccess(int index) {
        commandBox.runCommand("select " + index);
        assertResultMessage("Selected Task: " + index);
        assertTaskSelected(index);
    }

    private void assertTaskSelected(int index) {
        assertEquals(listPanel.getSelectedTaskOfTodoList().size(), 1);
        ReadOnlyTask selectedPerson = listPanel.getSelectedTaskOfTodoList().get(0);
        assertEquals(listPanel.getTaskInTodoList(index - 1), selectedPerson);
        //TODO: confirm the correct page is loaded in the Browser Panel
    }

    private void assertNoTaskSelected() {
        assert(listPanel.hasSelectionInListViews() == false);
    }

}

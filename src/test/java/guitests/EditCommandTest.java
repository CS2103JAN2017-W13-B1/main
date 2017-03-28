package guitests;

import static org.junit.Assert.assertTrue;
import static utask.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import org.junit.Test;

import guitests.guihandles.PersonCardHandle;
import utask.commons.core.Messages;
import utask.logic.commands.EditCommand;
import utask.model.tag.Tag;
import utask.model.task.Deadline;
import utask.model.task.Name;
import utask.testutil.TaskBuilder;
import utask.testutil.TestTask;

// TODO: reduce GUI tests by transferring some tests to be covered by lower level tests.
public class EditCommandTest extends UTaskGuiTest {

    // The list of persons in the person list panel is expected to match this list.
    // This list is updated with every successful call to assertEditSuccess().
    TestTask[] expectedPersonsList = td.getTypicalPersons();

//    @Test
//    public void edit_allFieldsSpecified_success() throws Exception {
//        String detailsToEdit = "Bobby p/91234567 e/bobby@gmail.com a/Block 123, Bobby Street 3 t/husband";
//        int addressBookIndex = 1;
//
//        TestPerson editedPerson = new PersonBuilder().withName("Bobby").withPhone("91234567")
//                .withEmail("bobby@gmail.com").withAddress("Block 123, Bobby Street 3").withTags("husband").build();
//
//        assertEditSuccess(addressBookIndex, addressBookIndex, detailsToEdit, editedPerson);
//    }

//    @Test
//    public void edit_notAllFieldsSpecified_success() throws Exception {
//        String detailsToEdit = "t/sweetie t/bestie";
//        int addressBookIndex = 2;
//
//        TestPerson personToEdit = expectedPersonsList[addressBookIndex - 1];
//        TestPerson editedPerson = new PersonBuilder(personToEdit).withTags("sweetie", "bestie").build();
//
//        assertEditSuccess(addressBookIndex, addressBookIndex, detailsToEdit, editedPerson);
//    }

    @Test
    public void edit_clearTags_success() throws Exception {
        String detailsToEdit = "/tag ";
        int addressBookIndex = 2;

        TestTask personToEdit = expectedPersonsList[addressBookIndex - 1];
        TestTask editedPerson = new TaskBuilder(personToEdit).withTags().build();

        assertEditSuccess(addressBookIndex, addressBookIndex, detailsToEdit, editedPerson);
    }

//    @Test
//    public void edit_findThenEdit_success() throws Exception {
//        commandBox.runCommand("find Free");
//
//        String detailsToEdit = "/name Not Free";
//        int filteredPersonListIndex = 1;
//        int addressBookIndex = 6;
//
//        TestTask personToEdit = expectedPersonsList[addressBookIndex - 1];
//        TestTask editedPerson = new TaskBuilder(personToEdit).withName("Not Free").build();
//
//        assertEditSuccess(filteredPersonListIndex, addressBookIndex, detailsToEdit, editedPerson);
//    }

    @Test
    public void edit_missingPersonIndex_failure() {
        commandBox.runCommand("update Bobby");
        assertResultMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
    }

    @Test
    public void edit_invalidPersonIndex_failure() {
        commandBox.runCommand("update 8 /name Bobby");
        assertResultMessage(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void edit_noFieldsSpecified_failure() {
        commandBox.runCommand("update 1");
        assertResultMessage(EditCommand.MESSAGE_NOT_EDITED);
    }

    @Test
    public void edit_invalidValues_failure() {
        commandBox.runCommand("update 1 /name *&");
        assertResultMessage(Name.MESSAGE_NAME_CONSTRAINTS);

        commandBox.runCommand("update 1 /by abcd");
        assertResultMessage(Deadline.MESSAGE_DEADLINE_CONSTRAINTS);

//        commandBox.runCommand("edit 1 /from !!!!");
//        assertResultMessage(Timestamp.MESSAGE_TIMESTAMP_CONSTRAINTS);

//        commandBox.runCommand("edit 1 /repeat !!!");
//        assertResultMessage(Frequency.MESSAGE_FREQUENCY_CONSTRAINTS);

        commandBox.runCommand("update 1 /tag *&");
        assertResultMessage(Tag.MESSAGE_TAG_CONSTRAINTS);
    }

//    @Test
//    public void edit_duplicatePerson_failure() {
//        commandBox.runCommand("edit 3 Alice Pauline p/85355255 e/alice@gmail.com "
//                                + "a/123, Jurong West Ave 6, #08-111 t/friends");
//        assertResultMessage(EditCommand.MESSAGE_DUPLICATE_PERSON);
//    }

    /**
     * Checks whether the edited person has the correct updated details.
     *
     * @param filteredPersonListIndex index of person to edit in filtered list
     * @param addressBookIndex index of person to edit in the address book.
     *      Must refer to the same person as {@code filteredPersonListIndex}
     * @param detailsToEdit details to edit the person with as input to the edit command
     * @param editedPerson the expected person after editing the person's details
     */
    private void assertEditSuccess(int filteredPersonListIndex, int addressBookIndex,
                                    String detailsToEdit, TestTask editedPerson) {
        commandBox.runCommand("update " + filteredPersonListIndex + " " + detailsToEdit);

        // confirm the new card contains the right data
        PersonCardHandle editedCard = personListPanel.navigateToPerson(editedPerson.getName().fullName);
        assertMatching(editedPerson, editedCard);

        // confirm the list now contains all previous persons plus the person with updated details
        expectedPersonsList[addressBookIndex - 1] = editedPerson;
        assertTrue(personListPanel.isListMatching(expectedPersonsList));
        assertResultMessage(String.format(EditCommand.MESSAGE_EDIT_TASK_SUCCESS, editedPerson));
    }
}

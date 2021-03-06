package utask.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static utask.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static utask.commons.core.Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX;
import static utask.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.eventbus.Subscribe;

import utask.commons.core.EventsCenter;
import utask.commons.core.Messages;
import utask.commons.events.model.UTaskChangedEvent;
import utask.commons.events.ui.JumpToListRequestEvent;
import utask.commons.events.ui.ShowHelpRequestEvent;
import utask.commons.exceptions.IllegalValueException;
import utask.logic.commands.AliasCommand;
import utask.logic.commands.ClearCommand;
import utask.logic.commands.CommandResult;
import utask.logic.commands.CreateCommand;
import utask.logic.commands.CreateTagCommand;
import utask.logic.commands.DeleteCommand;
import utask.logic.commands.DeleteTagCommand;
import utask.logic.commands.DoneCommand;
import utask.logic.commands.ExitCommand;
import utask.logic.commands.FindCommand;
import utask.logic.commands.HelpCommand;
import utask.logic.commands.ListAliasCommand;
import utask.logic.commands.ListCommand;
import utask.logic.commands.ListTagCommand;
import utask.logic.commands.RedoCommand;
import utask.logic.commands.RelocateCommand;
import utask.logic.commands.SelectCommand;
import utask.logic.commands.SortCommand;
import utask.logic.commands.SortInFindCommand;
import utask.logic.commands.UnaliasCommand;
import utask.logic.commands.UndoCommand;
import utask.logic.commands.UndoneCommand;
import utask.logic.commands.UpdateCommand;
import utask.logic.commands.UpdateTagCommand;
import utask.logic.commands.exceptions.CommandException;
import utask.model.Model;
import utask.model.ModelManager;
import utask.model.ReadOnlyUTask;
import utask.model.UTask;
import utask.model.tag.Tag;
import utask.model.tag.TagColorIndex;
import utask.model.tag.TagName;
import utask.model.tag.UniqueTagList;
import utask.model.tag.UniqueTagList.DuplicateTagException;
import utask.model.task.Deadline;
import utask.model.task.DeadlineTask;
import utask.model.task.EventTask;
import utask.model.task.FloatingTask;
import utask.model.task.Frequency;
import utask.model.task.Name;
import utask.model.task.ReadOnlyTask;
import utask.model.task.Status;
import utask.model.task.Task;
import utask.model.task.Timestamp;
import utask.storage.StorageManager;

public class LogicManagerTest {

    /**
     * See https://github.com/junit-team/junit4/wiki/rules#temporaryfolder-rule
     */
    @Rule
    public TemporaryFolder saveFolder = new TemporaryFolder();

    private Model model;
    private Logic logic;

    // These are for checking the correctness of the events raised
    private ReadOnlyUTask latestSavedUTask;
    private boolean helpShown;
    private int targetedJumpIndex;

    @Subscribe
    public void handleLocalModelChangedEvent(UTaskChangedEvent abce) {
        latestSavedUTask = new UTask(abce.data);
    }

    @Subscribe
    private void handleShowHelpRequestEvent(ShowHelpRequestEvent she) {
        helpShown = true;
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent je) {
        targetedJumpIndex = je.targetIndex;
    }

    @Before
    public void setUp() {
        model = new ModelManager();
        String tempAddressBookFile = saveFolder.getRoot().getPath()
                + "TempAddressBook.xml";
        String tempPreferencesFile = saveFolder.getRoot().getPath()
                + "TempPreferences.json";
        logic = new LogicManager(model,
                new StorageManager(tempAddressBookFile, tempPreferencesFile));
        EventsCenter.getInstance().registerHandler(this);

        latestSavedUTask = new UTask(model.getUTask()); // last
                                                        // saved
                                                        // assumed
                                                        // to
                                                        // be
                                                        // up
                                                        // to
                                                        // date
        helpShown = false;
        targetedJumpIndex = -1; // non yet
    }

    @After
    public void tearDown() {
        EventsCenter.clearSubscribers();
    }

    @Test
    public void execute_invalid() {
        String invalidCommand = "       ";
        assertCommandFailure(invalidCommand, String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
    }

    /**
     * Executes the command, confirms that a CommandException is not thrown and
     * that the result message is correct. Also confirms that both the 'address
     * book' and the 'last shown list' are as specified.
     *
     * @see #assertCommandBehavior(boolean, String, String, ReadOnlyUTask, List)
     */
    private void assertCommandSuccess(String inputCommand,
            String expectedMessage, ReadOnlyUTask expectedUTask,
            List<? extends ReadOnlyTask> expectedShownList) {
        assertCommandBehavior(false, inputCommand, expectedMessage,
                expectedUTask, expectedShownList);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that
     * the result message is correct. Both the 'UTask' and the 'last shown list'
     * are verified to be unchanged.
     *
     * @see #assertCommandBehavior(boolean, String, String, ReadOnlyUTask, List)
     */
    private void assertCommandFailure(String inputCommand,
            String expectedMessage) {
        UTask expectedUTask = new UTask(model.getUTask());
        List<ReadOnlyTask> expectedShownList = new ArrayList<>(
                model.getFilteredTaskList());
        assertCommandBehavior(true, inputCommand, expectedMessage,
                expectedUTask, expectedShownList);
    }

    /**
     * Executes the command, confirms that the result message is correct and
     * that a CommandException is thrown if expected and also confirms that the
     * following three parts of the LogicManager object's state are as
     * expected:<br>
     * - the internal address book data are same as those in the
     * {@code expectedAddressBook} <br>
     * - the backing list shown by UI matches the {@code shownList} <br>
     * - {@code expectedAddressBook} was saved to the storage file. <br>
     */
    private void assertCommandBehavior(boolean isCommandExceptionExpected,
            String inputCommand, String expectedMessage,
            ReadOnlyUTask expectedUTask,
            List<? extends ReadOnlyTask> expectedShownList) {

        try {
            CommandResult result = logic.execute(inputCommand);
            assertFalse("CommandException expected but was not thrown.",
                    isCommandExceptionExpected);
            assertEquals(expectedMessage, result.feedbackToUser);
        } catch (CommandException e) {
            assertTrue("CommandException not expected but was thrown.",
                    isCommandExceptionExpected);
            assertEquals(expectedMessage, e.getMessage());
        }

        // Confirm the ui display elements should contain the right data
        assertEquals(expectedShownList, model.getFilteredTaskList());

        // Confirm the state of data (saved and in-memory) is as expected
        assertEquals(expectedUTask, model.getUTask());
        assertEquals(expectedUTask, latestSavedUTask);
    }

    // @@ author A0138493W
    @Test
    public void execute_relocate_invalidPath() {
        String invalidPath = "this/path/is/invalid";
        assertCommandFailure("relocate " + invalidPath, String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, RelocateCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_relocate_defaultPathSuccess() {
        assertCommandSuccess("relocate", String
                .format(RelocateCommand.MESSAGE_RELOCATE_TASK_SUCCESS, "data"),
                new UTask(), Collections.emptyList());
    }

    @Test
    public void execute_relocate_success() {
        String validPath;
        if (System.getProperty("os.name").contains("Windows")) {
            validPath = "C:\\path\\to\\destination";
        } else {
            validPath = "/Users/James/Desktop";
        }
        assertCommandSuccess("relocate " + validPath,
                String.format(RelocateCommand.MESSAGE_RELOCATE_TASK_SUCCESS,
                        validPath),
                new UTask(), Collections.emptyList());
    }

    @Test
    public void execute_alias_invalidAliasFormat() {
        String invalidAlias = "%$#@";
        assertCommandFailure("alias " + invalidAlias + " /as create",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        AliasCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_alias_commandWordNotExist() {
        String notExistCommandword = "lalaland";
        assertCommandFailure("alias a /as " + notExistCommandword,
                String.format(AliasCommand.MESSAGE_COMMAND_WORD_NOT_EXIST,
                        notExistCommandword));
    }

    @Test
    public void execute_alias_aliasCannotBeDefaultCommandWord() {
        String defaultCommandword = "create";
        assertCommandFailure("alias " + defaultCommandword + " /as clear",
                String.format(
                        AliasCommand.MESSAGE_ALIAS_CANNOT_BE_DEFAULT_COMMAND,
                        defaultCommandword));
    }

    @Test
    public void execute_alias_successful() {
        String alias = "c";
        String defaultCommand = "create";
        assertCommandSuccess("alias " + alias + " /as " + defaultCommand,
                String.format(AliasCommand.MESSAGE_CREATE_ALIAS_SUCCESS, alias,
                        defaultCommand),
                new UTask(), Collections.emptyList());
    }

    @Test
    public void execute_unknownCommandWord() {
        String unknownCommand = "uicfhmowqewca";
        assertCommandFailure(unknownCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_alias_invalidUnaliasFormat() {
        String invalidAlias = "$%#^";
        assertCommandFailure("unalias " + invalidAlias, String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, UnaliasCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_unalias_aliasNotExist() {
        String alias = "c";
        assertCommandFailure("unalias " + alias,
                String.format(UnaliasCommand.MESSAGE_ALIAS_NOT_EXIST, alias));
    }

    @Test
    public void execute_unalias_successful() throws CommandException {
        String alias = "c";
        logic.execute("alias c /as create");
        assertCommandSuccess("unalias " + alias,
                String.format(UnaliasCommand.MESSAGE_UNALIAS_SUCCESS, alias),
                new UTask(), Collections.emptyList());
    }

    @Test
    public void execute_listalias_successful() throws CommandException {
        assertCommandSuccess("listalias",
                String.format(ListAliasCommand.MESSAGE_SUCCESS), new UTask(),
                Collections.emptyList());
    }
    // @@ author

    @Test
    public void execute_help() {
        assertCommandSuccess("help", HelpCommand.SHOWING_HELP_MESSAGE,
                new UTask(), Collections.emptyList());
        assertTrue(helpShown);
    }

    @Test
    public void execute_exit() {
        assertCommandSuccess("exit", ExitCommand.MESSAGE_EXIT_ACKNOWLEDGEMENT,
                new UTask(), Collections.emptyList());
    }

    @Test
    public void execute_clear() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        model.addTask(helper.generateEventTaskWithSeed(1));
        model.addTask(helper.generateEventTaskWithSeed(2));
        model.addTask(helper.generateEventTaskWithSeed(3));

        assertCommandSuccess("clear", ClearCommand.MESSAGE_SUCCESS, new UTask(),
                Collections.emptyList());
    }

    // @@ author A0138423J
    @Test
    public void execute_createtag_invalidcommandformatunsuccessful() {
        assertCommandFailure("createtag ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        CreateTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_createtag_invalidnameunsuccessful() {
        String tagName = "%^&*(";
        String color = "$%^&*";
        assertCommandFailure("createtag " + tagName + "/color " + color,
                Messages.MESSAGE_TAG_CONSTRAINTS);
    }

    // @@ author A0138493W
    @Test
    public void execute_clear_byAlias() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        model.addTask(helper.generateEventTaskWithSeed(1));
        logic.execute("alias c /as clear");
        assertCommandSuccess("c", ClearCommand.MESSAGE_SUCCESS, new UTask(),
                Collections.emptyList());
    }

    @Test
    public void execute_createtag_successful()
            throws DuplicateTagException, IllegalValueException {
        String tagName = "school";
        String color = "red";
        UTask expectedUT = new UTask();
        expectedUT.addTag(
                new Tag(new TagName(tagName), new TagColorIndex(color)));
        assertCommandSuccess("createtag " + tagName + " /color " + color,
                String.format(CreateTagCommand.MESSAGE_SUCCESS, tagName, color),
                expectedUT, expectedUT.getTaskList());
    }

    @Test
    public void execute_createtag_duplicateTag() throws CommandException {
        String tagName = "school";
        String color = "red";
        logic.execute("createtag " + tagName + " /color " + color);
        assertCommandFailure("createtag " + tagName + " /color " + color,
                CreateTagCommand.MESSAGE_DUPLICATE_TAG);
    }

    @Test
    public void execute_undoredo_createTagSuccessful() throws CommandException,
            DuplicateTagException, IllegalValueException {
        String tagName = "school";
        String color = "red";
        UTask expectedUT = new UTask();
        expectedUT.addTag(
                new Tag(new TagName(tagName), new TagColorIndex(color)));
        logic.execute("createtag " + tagName + " /color " + color);
        int index = 1;
        assertCommandSuccess("undo " + index,
                String.format(UndoCommand.MESSAGE_UNDO_SUCCESS, index),
                new UTask(), Collections.emptyList());
        assertCommandSuccess("redo " + index,
                String.format(RedoCommand.MESSAGE_REDO_SUCCESS, index),
                expectedUT, expectedUT.getTaskList());
    }

    @Test
    public void execute_listtag_successful() {
        assertCommandSuccess("listtag", ListTagCommand.MESSAGE_SUCCESS,
                new UTask(), Collections.emptyList());
    }

    @Test
    public void execute_updatetag_invalid() {
        assertCommandFailure("updatetag invalid@#",
                Messages.MESSAGE_TAG_CONSTRAINTS);
    }

    @Test
    public void execute_updatetag_successful() throws CommandException,
            DuplicateTagException, IllegalValueException {
        logic.execute("createtag tag /color blue");
        String tagName = "school";
        String color = "red";
        UTask expectedUT = new UTask();
        Tag updatedTag = new Tag(new TagName(tagName),
                new TagColorIndex(color));
        expectedUT.addTag(updatedTag);
        assertCommandSuccess(
                "updatetag tag /name" + tagName + " /color " + color,
                String.format(UpdateTagCommand.MESSAGE_SUCCESS, updatedTag),
                expectedUT, expectedUT.getTaskList());
    }

    @Test
    public void execute_deletetag_missingtag() {
        assertCommandFailure("deletetag", Messages.MESSAGE_TAG_CONSTRAINTS);
    }

    @Test
    public void execute_deletag_successful()
            throws CommandException, IllegalValueException {
        String tagName = " school";
        String color = "red";
        UTask expectedUT = new UTask();
        Tag deletedTag = new Tag(new TagName(tagName),
                new TagColorIndex(color));
        expectedUT.addTag(deletedTag);
        logic.execute("createtag" + tagName + " /color " + color);
        logic.execute("deletetag" + tagName);
        int index = 1;
    }

    @Test
    public void execute_undoredo_deleteTagSuccessful()
            throws CommandException, IllegalValueException {
        String tagName = " school";
        String color = "red";
        UTask expectedUT = new UTask();
        Tag deletedTag = new Tag(new TagName(tagName),
                new TagColorIndex(color));
        expectedUT.addTag(deletedTag);
        logic.execute("createtag" + tagName + " /color " + color);
        logic.execute("deletetag" + tagName);
        assertCommandSuccess("deletetag" + tagName,
                String.format(DeleteTagCommand.MESSAGE_SUCCESS, tagName),
                new UTask(), Collections.emptyList());
    }
    // @@ author

    @Test
    public void execute_create_invalidTaskData() {
        assertCommandFailure(
                "create Valid Name /by Aa;a /from 1830 to 2030 /repeat Every Monday /tag urgent",
                Deadline.MESSAGE_DEADLINE_CONSTRAINTS);
        assertCommandFailure(
                "create valid name /by 111111 /from aaa /repeat Every Monday /tag urgent",
                Timestamp.MESSAGE_TIMESTAMP_CONSTRAINTS);
        assertCommandFailure(
                "create valid name /by 111111 /from 0000 to 1200 /repeat Every Monday /tag ;a!!e",
                Messages.MESSAGE_TAG_CONSTRAINTS);
    }

    @Test
    public void execute_create_eventTaskSuccessful() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeAdded = helper.simpleTask();
        UTask expectedUT = new UTask();
        expectedUT.addTask(toBeAdded);
        logic.execute("createtag urgent /color CYAN");
        logic.execute("createtag assignment /color YELLOW");
        // execute command and verify result
        assertCommandSuccess(helper.generateEventCreateCommand(toBeAdded),
                String.format(CreateCommand.MESSAGE_SUCCESS, toBeAdded),
                expectedUT, expectedUT.getTaskList());
    }

    @Test
    public void execute_create_deadlineTaskSuccessful() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeAdded = helper.generateDeadlineTaskWithSeed(1);
        UTask expectedUT = new UTask();
        expectedUT.addTask(toBeAdded);
        // execute command and verify result
        assertCommandSuccess("create Task 1 /by 010120 /repeat Every 1",
                String.format(CreateCommand.MESSAGE_SUCCESS, toBeAdded),
                expectedUT, expectedUT.getTaskList());
    }

    @Test
    public void execute_create_floatingTaskSuccessful() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeAdded = helper.generateFloatingTaskWithSeed(1);
        UTask expectedUT = new UTask();
        expectedUT.addTask(toBeAdded);
        // execute command and verify result
        assertCommandSuccess("create Task 1 /repeat Every 1",
                String.format(CreateCommand.MESSAGE_SUCCESS, toBeAdded),
                expectedUT, expectedUT.getTaskList());
    }

    @Test
    public void execute_create_duplicateNotAllowed() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeAdded = helper.simpleTask();

        // setup starting state
        model.addTask(toBeAdded); // task already in internal address book
        // execute command and verify result
        assertCommandFailure(helper.generateEventCreateCommand(toBeAdded),
                CreateCommand.MESSAGE_DUPLICATE_TASK);
    }

    // @@ author A0138493W
    @Test
    public void execute_find_invalidArgsFormat() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FindCommand.MESSAGE_USAGE);
        assertCommandFailure("find ", expectedMessage);
    }

    @Test
    public void execute_find_onlyMatchesFullWordsInNames() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task pTarget1 = helper.generateTaskWithName("bla bla KEY bla");
        Task pTarget2 = helper.generateTaskWithName("bla KEY bla bceofeia");
        Task p1 = helper.generateTaskWithName("KE Y");
        Task p2 = helper.generateTaskWithName("KEYKEYKEY sduauo");

        List<Task> fourTasks = helper.generateTaskList(p1, pTarget1, p2,
                pTarget2);
        UTask expectedUT = helper.generateUTask(fourTasks);
        List<Task> expectedList = helper.generateTaskList(pTarget1, pTarget2);
        helper.addToModel(model, fourTasks);

        String keyword = "KEY";
        Set<String> mySet = new HashSet<String>(Arrays.asList(keyword));
        assertCommandSuccess("find " + keyword,
                String.format(Messages.MESSAGE_SEARCH_RESULTS, mySet.toString(),
                        expectedList.size()),
                expectedUT, expectedList);
    }

    @Test
    public void execute_find_completedTaskOnly() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task toBeAdded = helper.simpleTask();
        UTask expectedUT = new UTask();
        toBeAdded.setStatus(new Status("Complete"));
        expectedUT.addTask(toBeAdded);

        List<Task> compledTask = helper.generateTaskList(toBeAdded);
        helper.addToModel(model, compledTask);
        String keyword = "complete";
        Set<String> mySet = new HashSet<String>(Arrays.asList(keyword));
        assertCommandSuccess("find " + keyword,
                String.format(Messages.MESSAGE_SEARCH_RESULTS, mySet.toString(),
                        expectedUT.getTaskList().size()),
                expectedUT, expectedUT.getTaskList());
    }

    @Test
    public void execute_find_isNotCaseSensitive() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task p1 = helper.generateTaskWithName("bla bla KEY bla");
        Task p2 = helper.generateTaskWithName("bla KEY bla bceofeia");
        Task p3 = helper.generateTaskWithName("key key");
        Task p4 = helper.generateTaskWithName("KEy sduauo");

        List<Task> fourPersons = helper.generateTaskList(p3, p1, p4, p2);
        UTask expectedUT = helper.generateUTask(fourPersons);
        List<Task> expectedList = fourPersons;
        helper.addToModel(model, fourPersons);

        String keyword = "KEY";
        Set<String> mySet = new HashSet<String>(Arrays.asList(keyword));
        assertCommandSuccess("find " + keyword,
                String.format(Messages.MESSAGE_SEARCH_RESULTS, mySet.toString(),
                        expectedList.size()),
                expectedUT, expectedList);
    }

    @Test
    public void execute_find_matchesIfAnyKeywordPresent() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task pTarget1 = helper.generateTaskWithName("bla bla KEY bla");
        Task pTarget2 = helper.generateTaskWithName("bla rAnDoM bla bceofeia");
        Task pTarget3 = helper.generateTaskWithName("key key");
        Task p1 = helper.generateTaskWithName("sduauo");

        List<Task> fourTasks = helper.generateTaskList(pTarget1, p1, pTarget2,
                pTarget3);
        UTask expectedUT = helper.generateUTask(fourTasks);
        List<Task> expectedList = helper.generateTaskList(pTarget1, pTarget2,
                pTarget3);
        helper.addToModel(model, fourTasks);

        String keyword = "key rAnDoM";
        Set<String> mySet = new HashSet<String>(
                Arrays.asList(keyword.trim().split("\\s+")));
        assertCommandSuccess("find " + keyword,
                String.format(Messages.MESSAGE_SEARCH_RESULTS, mySet.toString(),
                        expectedList.size()),
                expectedUT, expectedList);
    }

    @Test
    public void execute_sortinfind_invalidOrder() throws CommandException {
        logic.execute("find incomplete");
        assertCommandFailure("sort a abc", SortInFindCommand.MESSAGE_USAGE);
    }

    @Test
    public void execute_sortinfind_successful() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task pTarget1 = helper.generateTaskWithName("a");
        Task pTarget2 = helper.generateTaskWithName("b");
        Task pTarget3 = helper.generateTaskWithName("c");

        List<Task> threeTasks = helper.generateTaskList(pTarget1, pTarget2,
                pTarget3);
        UTask expectedUT = helper.generateUTask(threeTasks);
        List<Task> expectedList = helper.generateTaskList(pTarget1, pTarget2,
                pTarget3);
        helper.addToModel(model, threeTasks);

        String keyword = "incomplete";
        logic.execute("find " + keyword);
        assertCommandSuccess("sort a asc", SortInFindCommand.MESSAGE_SUCCESS,
                expectedUT, expectedList);
    }
    // @@ author

    // @@author A0138423J
    @Test
    public void execute_Done_Success() throws Exception {
        // generate Tasks tobeDone and expectedOutcome
        TestDataHelper helper = new TestDataHelper();
        Task expectedOutcome = helper.generateEventTaskWithSeed(1);
        Task tobeDone = new EventTask(expectedOutcome);
        expectedOutcome.setStatus(new Status("Complete"));

        // adding tobeDone to UTask
        List<Task> oneTask = helper.generateTaskList(tobeDone);
        helper.addToModel(model, oneTask);
        UTask expectedUT = new UTask();
        expectedUT.addTask(expectedOutcome);

        assertCommandSuccess("done 1", String
                .format(DoneCommand.MESSAGE_DONE_TASK_SUCCESS, expectedOutcome),
                expectedUT, expectedUT.getTaskList());
    }

    @Test
    public void execute_Done_FailureAlreadyDone() throws Exception {
        // generate Tasks tobeDone and expectedOutcome
        TestDataHelper helper = new TestDataHelper();
        Task expectedOutcome = helper.generateEventTaskWithSeed(1);
        expectedOutcome.setStatus(new Status("Complete"));

        List<Task> twoTasks = helper.generateTaskList(expectedOutcome);
        helper.addToModel(model, twoTasks);

        assertCommandFailure("done 1", DoneCommand.MESSAGE_DUPLICATE_STATUS);
    }

    @Test
    public void execute_Done_FailureDuplicate() throws Exception {
        // generate Tasks tobeDone and expectedOutcome
        TestDataHelper helper = new TestDataHelper();
        Task taskOne = helper.generateEventTaskWithSeed(1);
        Task taskTwo = new EventTask(taskOne);
        taskTwo.setStatus(new Status("Complete"));

        List<Task> twoTasks = helper.generateTaskList(taskOne, taskTwo);
        helper.addToModel(model, twoTasks);

        assertCommandFailure("done 1", DoneCommand.MESSAGE_DUPLICATE_TASK);
    }

    @Test
    public void execute_Undone_Success() throws Exception {
        // generate Tasks tobeUndone and expectedOutcome
        TestDataHelper helper = new TestDataHelper();
        Task expectedOutcome = helper.generateEventTaskWithSeed(1);
        Task tobeUndone = new EventTask(expectedOutcome);
        tobeUndone.setStatus(new Status("Complete"));

        // adding tobeDone to UTask
        List<Task> oneTask = helper.generateTaskList(tobeUndone);
        helper.addToModel(model, oneTask);
        UTask expectedUT = new UTask();
        expectedUT.addTask(expectedOutcome);

        assertCommandSuccess("undone 1",
                String.format(UndoneCommand.MESSAGE_UNDONE_TASK_SUCCESS,
                        expectedOutcome),
                expectedUT, expectedUT.getTaskList());
    }

    @Test
    public void execute_Undone_FailureAlreadyUndone() throws Exception {
        // generate Tasks tobeDone and expectedOutcome
        TestDataHelper helper = new TestDataHelper();
        Task expectedOutcome = helper.generateEventTaskWithSeed(1);

        List<Task> twoTasks = helper.generateTaskList(expectedOutcome);
        helper.addToModel(model, twoTasks);

        assertCommandFailure("undone 1",
                UndoneCommand.MESSAGE_DUPLICATE_STATUS);
    }

    @Test
    public void execute_Undone_FailureDuplicate() throws Exception {
        // generate Tasks tobeDone and expectedOutcome
        TestDataHelper helper = new TestDataHelper();
        Task taskOne = helper.generateEventTaskWithSeed(1);
        Task taskTwo = new EventTask(taskOne);
        taskOne.setStatus(new Status("Complete"));

        List<Task> twoTasks = helper.generateTaskList(taskOne, taskTwo);
        helper.addToModel(model, twoTasks);

        assertCommandFailure("undone 1", UndoneCommand.MESSAGE_DUPLICATE_TASK);
    }

    @Test
    public void execute_updateInvalidArgsFormat_errorMessageShown()
            throws Exception {
        assertIncorrectIndexFormatBehaviorForCommand("update",
                MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void execute_undone_invalidIndex() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeAdded = helper.generateEventTaskWithSeed(1);

        UTask expectedAB = new UTask();
        expectedAB.addTask(toBeAdded);

        // execute incomplete command
        assertCommandFailure("undone %", String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, UndoneCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_done_invalidIndex() throws Exception {
        assertCommandFailure("done $%", String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, DoneCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_update_invalidIndex() {
        assertCommandFailure("update 999 /by today",
                MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void execute_update_duplicateTask() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task task1 = helper.generateFloatingTaskWithSeed(1);
        Task task2 = helper.generateFloatingTaskWithSeed(2);
        List<Task> twoTasks = helper.generateTaskList(task1, task2);
        helper.addToModel(model, twoTasks);
        assertCommandFailure("update 1 /name Task 2 /repeat Every 2",
                UpdateCommand.MESSAGE_DUPLICATE_TASK);
    }

    @Test
    public void execute_update_eventTaskSuccess() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task tobeAdded = helper.generateEventTaskWithSeed(1);
        Task expectedTask = helper.generateEventTaskWithSeed(2);
        List<Task> oneTask = helper.generateTaskList(tobeAdded);
        UTask expectedUT = new UTask();
        expectedUT.addTask(expectedTask);
        helper.addToModel(model, oneTask);
        assertCommandSuccess(
                "update 1 /name Task 2 /by 010120 /from 0000 to 2359"
                        + " /repeat Every 2 /status incomplete",
                String.format(UpdateCommand.MESSAGE_EDIT_TASK_SUCCESS,
                        expectedTask),
                expectedUT, expectedUT.getTaskList());
    }

    @Test
    public void execute_update_deadlineTaskSuccess() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task tobeAdded = helper.generateDeadlineTaskWithSeed(1);
        Task expectedTask = helper.generateDeadlineTaskWithSeed(2);
        List<Task> oneTask = helper.generateTaskList(tobeAdded);
        UTask expectedUT = new UTask();
        expectedUT.addTask(expectedTask);
        helper.addToModel(model, oneTask);
        assertCommandSuccess(
                "update 1 /name Task 2 /by 010120"
                        + " /repeat Every 2 /status incomplete",
                String.format(UpdateCommand.MESSAGE_EDIT_TASK_SUCCESS,
                        expectedTask),
                expectedUT, expectedUT.getTaskList());
    }

    @Test
    public void execute_update_floatingTaskSuccess() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task tobeAdded = helper.generateFloatingTaskWithSeed(1);
        Task expectedTask = helper.generateFloatingTaskWithSeed(2);
        List<Task> oneTask = helper.generateTaskList(tobeAdded);
        UTask expectedUT = new UTask();
        expectedUT.addTask(expectedTask);
        helper.addToModel(model, oneTask);
        assertCommandSuccess(
                "update 1 /name Task 2" + " /repeat Every 2 /status incomplete",
                String.format(UpdateCommand.MESSAGE_EDIT_TASK_SUCCESS,
                        expectedTask),
                expectedUT, expectedUT.getTaskList());
    }

    // @Test
    public void execute_update_fromDeadlineToEvent() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task tobeAdded = helper.generateDeadlineTaskWithSeed(1);
        Task expectedTask = helper.generateEventTaskWithSeed(2);
        List<Task> oneTask = helper.generateTaskList(tobeAdded);
        UTask expectedUT = new UTask();
        expectedUT.addTask(expectedTask);
        helper.addToModel(model, oneTask);
        assertCommandSuccess(
                "update 1 /name Task 2 /from 1111 to 2222"
                        + " /repeat Every 2 /status incomplete",
                String.format(UpdateCommand.MESSAGE_EDIT_TASK_SUCCESS,
                        expectedTask),
                expectedUT, expectedUT.getTaskList());
    }

    // @@author

    @Test
    public void execute_list_showsAllPersons() throws Exception {
        // prepare expectations
        TestDataHelper helper = new TestDataHelper();
        UTask expectedUT = helper.generateAddressBook(2);
        List<? extends ReadOnlyTask> expectedList = expectedUT.getTaskList();

        // prepare UTask state
        helper.addToModel(model, 2);

        assertCommandSuccess("list", ListCommand.MESSAGE_SUCCESS, expectedUT,
                expectedList);
    }

    /**
     * Confirms the 'invalid argument index number behaviour' for the given
     * command targeting a single task in the shown list, using visible index.
     *
     * @param commandWord
     *            to test assuming it targets a single task in the last shown
     *            list based on visible index.
     */
    private void assertIncorrectIndexFormatBehaviorForCommand(
            String commandWord, String expectedMessage) throws Exception {
        assertCommandFailure(commandWord, expectedMessage); // index missing
        assertCommandFailure(commandWord + " +1", expectedMessage); // index
                                                                    // should be
                                                                    // unsigned
        assertCommandFailure(commandWord + " -1", expectedMessage); // index
                                                                    // should be
                                                                    // unsigned
        assertCommandFailure(commandWord + " 0", expectedMessage); // index
                                                                   // cannot be
                                                                   // 0
        assertCommandFailure(commandWord + " not_a_number", expectedMessage);
    }

    /**
     * Confirms the 'invalid argument index number behaviour' for the given
     * command targeting a single task in the shown list, using visible index.
     *
     * @param commandWord
     *            to test assuming it targets a single task in the last shown
     *            list based on visible index.
     */
    private void assertIndexNotFoundBehaviorForCommand(String commandWord)
            throws Exception {
        String expectedMessage = MESSAGE_INVALID_TASK_DISPLAYED_INDEX;
        TestDataHelper helper = new TestDataHelper();
        List<Task> taskList = helper.generateTaskList(2);

        // set AB state to 2 tasks
        model.resetData(new UTask());
        for (Task p : taskList) {
            model.addTask(p);
        }

        assertCommandFailure(commandWord + " 3", expectedMessage);
    }

    @Test
    public void executeSelectInvalidArgsFormatErrorMessageShown()
            throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                SelectCommand.MESSAGE_USAGE);
        assertIncorrectIndexFormatBehaviorForCommand("select", expectedMessage);
    }

    // @@author A0138493W
    @Test
    public void execute_delete_invalidArgsFormatErrorMessageShown()
            throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteCommand.MESSAGE_USAGE);
        assertIncorrectIndexFormatBehaviorForCommand("delete", expectedMessage);
        assertIncorrectIndexFormatBehaviorForCommand("delete  ",
                expectedMessage);
        assertIncorrectIndexFormatBehaviorForCommand("delete  , 1",
                expectedMessage);
        assertIncorrectIndexFormatBehaviorForCommand("delete  a, 1",
                expectedMessage);
        assertIncorrectIndexFormatBehaviorForCommand("delete  1 to a",
                expectedMessage);
    }

    @Test
    public void execute_delete_indexNotFoundErrorMessageShown()
            throws Exception {
        assertIndexNotFoundBehaviorForCommand("delete 1 to 999");
        assertIndexNotFoundBehaviorForCommand("delete 9, 10, 11");
    }

    @Test
    public void execute_delete_removesCorrectTask() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        List<Task> threeTasks = helper.generateTaskList(5);

        UTask expectedUT = helper.generateUTask(threeTasks);
        expectedUT.removeTask(threeTasks.get(1));
        expectedUT.removeTask(threeTasks.get(0));
        helper.addToModel(model, threeTasks);

        // CommandResult result = logic.execute("delete 1 to 1, 2");
        // assertEquals(DeleteCommand.MESSAGE_DELETE_TASK_SUCCESS,
        // result.feedbackToUser);
        // assertEquals(expectedUT.getTaskList(), model.getFilteredTaskList());
        // assertEquals(expectedUT, model.getUTask());
        // assertEquals(expectedUT, latestSavedUTask);

        assertCommandSuccess("delete 1 to 2",
                DeleteCommand.MESSAGE_DELETE_TASK_SUCCESS, expectedUT,
                expectedUT.getTaskList());
    }

    @Test
    public void execute_sort_default() throws Exception {
        // prepare expectations
        TestDataHelper helper = new TestDataHelper();
        List<Task> fiveTasks = helper.generateTaskList(5);
        UTask expectedUT = helper.generateUTask(fiveTasks);

        // prepare task list state
        helper.addToModel(model, fiveTasks);

        assertCommandSuccess("sort", SortCommand.MESSAGE_SUCCESS, expectedUT,
                expectedUT.getTaskList());
    }

    @Test
    public void execute_sort_ascendingAlphabeticalOrder() throws Exception {
        // prepare expectations
        TestDataHelper helper = new TestDataHelper();
        Task second = helper.generateTaskWithName("beta");
        Task first = helper.generateTaskWithName("alpha");
        Task fourth = helper.generateTaskWithName("gamma");
        Task third = helper.generateTaskWithName("deta");
        List<Task> fourTasks = helper.generateTaskList(second, first, fourth,
                third);
        List<Task> expectedList = helper.generateTaskList(first, second, third,
                fourth);
        UTask expectedUT = helper.generateUTask(expectedList);

        // prepare task list state
        helper.addToModel(model, fourTasks);
        assertCommandSuccess("sort az", SortCommand.MESSAGE_SUCCESS, expectedUT,
                expectedList);
    }

    @Test
    public void execute_sort_descendingAlphabeticalOrder() throws Exception {
        // prepare expectations
        TestDataHelper helper = new TestDataHelper();
        Task second = helper.generateTaskWithName("beta");
        Task first = helper.generateTaskWithName("alpha");
        Task fourth = helper.generateTaskWithName("gamma");
        Task third = helper.generateTaskWithName("deta");
        List<Task> fourTasks = helper.generateTaskList(second, first, fourth,
                third);
        List<Task> expectedList = helper.generateTaskList(fourth, third, second,
                first);
        UTask expectedUT = helper.generateUTask(expectedList);

        // prepare task list state
        helper.addToModel(model, fourTasks);
        assertCommandSuccess("sort za", SortCommand.MESSAGE_SUCCESS, expectedUT,
                expectedList);
    }

    @Test
    public void execute_sort_earliestDeadlineOrder() throws Exception {
        // prepare expectations
        TestDataHelper helper = new TestDataHelper();
        Task second = helper.generateDeadlineTask("task 1", "160317");
        Task first = helper.generateDeadlineTask("task 2", "150317");
        Task fourth = helper.generateDeadlineTask("task 3", "180317");
        Task third = helper.generateDeadlineTask("task 4", "170317");
        List<Task> fourTasks = helper.generateTaskList(second, first, fourth,
                third);
        List<Task> expectedList = helper.generateTaskList(first, second, third,
                fourth);
        UTask expectedUT = helper.generateUTask(expectedList);

        // prepare task list state
        helper.addToModel(model, fourTasks);
        assertCommandSuccess("sort", SortCommand.MESSAGE_SUCCESS, expectedUT,
                expectedList);
    }

    @Test
    public void execute_sort_latestDeadlineOrder() throws Exception {
        // prepare expectations
        TestDataHelper helper = new TestDataHelper();
        Task second = helper.generateDeadlineTask("task 1", "160317");
        Task first = helper.generateDeadlineTask("task 2", "150317");
        Task fourth = helper.generateDeadlineTask("task 3", "180317");
        Task third = helper.generateDeadlineTask("task 4", "170317");
        List<Task> fourTasks = helper.generateTaskList(second, first, fourth,
                third);
        List<Task> expectedList = helper.generateTaskList(fourth, third, second,
                first);
        UTask expectedUT = helper.generateUTask(expectedList);

        // prepare task list state
        helper.addToModel(model, fourTasks);
        assertCommandSuccess("sort latest", SortCommand.MESSAGE_SUCCESS,
                expectedUT, expectedList);
    }

    @Test
    public void execute_sort_tagOrder() throws Exception {
        // prepare expectations
        TestDataHelper helper = new TestDataHelper();
        Task second = helper.generateTaskwithTags("task 1",
                generateTagList("b", "c"));
        Task first = helper.generateTaskwithTags("task 2",
                generateTagList("e", "a"));
        Task fourth = helper.generateTaskwithTags("task 3",
                generateTagList("d", "h"));
        Task third = helper.generateTaskwithTags("task 4",
                generateTagList("e", "b"));
        List<Task> fourTasks = helper.generateTaskList(second, first, fourth,
                third);
        List<Task> expectedList = helper.generateTaskList(first, second, third,
                fourth);
        UTask expectedUT = helper.generateUTask(expectedList);

        // prepare task list state
        helper.addToModel(model, fourTasks);
        assertCommandSuccess("sort tag", SortCommand.MESSAGE_SUCCESS,
                expectedUT, expectedList);
    }

    /**
     * Generates a UniqueTagList with 2 tags
     *
     * @throws IllegalValueException
     */
    private UniqueTagList generateTagList(String tagName1, String tagName2)
            throws IllegalValueException {
        Tag tag1 = new Tag(new TagName(tagName1), new TagColorIndex("2"));
        Tag tag2 = new Tag(new TagName(tagName2), new TagColorIndex("8"));
        return new UniqueTagList(tag1, tag2);
    }

    // @@author
    // @@author A0138423J
    /**
     * A utility class to generate test data.
     */
    class TestDataHelper {

        private Task simpleTask() throws Exception {
            Name name = new Name("My debug task");
            Deadline deadline = new Deadline("010117");
            Timestamp timestamp = new Timestamp("010117", "1830 to 2030");
            Frequency frequency = new Frequency("Every Monday");
            Status iscompleted = new Status("incomplete");
            Tag tag1 = new Tag(new TagName("urgent"), new TagColorIndex("2"));
            Tag tag2 = new Tag(new TagName("assignment"),
                    new TagColorIndex("8"));
            UniqueTagList tags = new UniqueTagList(tag1, tag2);
            return new EventTask(name, deadline, timestamp, frequency, tags,
                    iscompleted);
        }

        /**
         * Generates a valid task using the given seed. Running this function
         * with the same parameter values guarantees the returned task will have
         * the same state. Each unique seed will generate a unique Task object.
         *
         * @param seed
         *            used to generate the task data field values
         */
        private Task generateEventTaskWithSeed(int seed) throws Exception {
            return new EventTask(new Name("Task " + seed),
                    new Deadline("010120"),
                    new Timestamp("010120", "0000 to 2359"),
                    new Frequency("Every " + seed), new UniqueTagList(),
                    new Status("incomplete"));
        }

        private Task generateDeadlineTaskWithSeed(int seed) throws Exception {
            return new DeadlineTask(new Name("Task " + seed),
                    new Deadline("010120"), new Frequency("Every " + seed),
                    new UniqueTagList(), new Status("incomplete"));
        }

        private Task generateFloatingTaskWithSeed(int seed) throws Exception {
            return new FloatingTask(new Name("Task " + seed),
                    new Frequency("Every " + seed), new UniqueTagList(),
                    new Status("incomplete"));
        }
        // @@author

        /** Generates the correct add command based on the task given */
        private String generateEventCreateCommand(Task p) {
            StringBuffer cmd = new StringBuffer();

            cmd.append("create ");

            cmd.append(p.getName().toString());
            if (!p.getDeadline().isEmpty()) {
                DateFormat fmt = new SimpleDateFormat("ddMMyy");
                String deadline = fmt.format(p.getDeadline().getDate());
                cmd.append(" /by ").append(deadline);
            }
            if (!p.getTimestamp().isEmpty()) {
                cmd.append(" /from ").append(p.getTimestamp());
            }
            if (!p.getFrequency().isEmpty()) {
                cmd.append(" /repeat ").append(p.getFrequency());
            }
            if (!p.getStatus().isEmpty()) {
                cmd.append(" /status ").append(p.getStatus());
            }
            UniqueTagList tags = p.getTags();
            for (Tag t : tags) {
                cmd.append(" /tag ").append(t.getTagName());
            }

            return cmd.toString();
        }

        /**
         * Generates an UTask with auto-generated persons.
         */
        private UTask generateAddressBook(int numGenerated) throws Exception {
            UTask uTask = new UTask();
            addToUTask(uTask, numGenerated);
            return uTask;
        }

        /**
         * Generates an UTask based on the list of Tasks given.
         */
        private UTask generateUTask(List<Task> tasks) throws Exception {
            UTask uTask = new UTask();
            addToUTask(uTask, tasks);
            return uTask;
        }

        /**
         * Adds auto-generated Task objects to the given UTask
         *
         * @param uTask
         *            The UTask to which the Task will be added
         */
        private void addToUTask(UTask uTask, int numGenerated)
                throws Exception {
            addToUTask(uTask, generateTaskList(numGenerated));
        }

        /**
         * Adds the given list of Tasks to the given UTask
         */
        private void addToUTask(UTask uTask, List<Task> tasksToAdd)
                throws Exception {
            for (Task p : tasksToAdd) {
                uTask.addTask(p);
            }
        }

        /**
         * Adds auto-generated Task objects to the given model
         *
         * @param model
         *            The model to which the Persons will be added
         */
        private void addToModel(Model model, int numGenerated)
                throws Exception {
            addToModel(model, generateTaskList(numGenerated));
        }

        /**
         * Adds the given list of Persons to the given model
         */
        private void addToModel(Model model, List<Task> personsToAdd)
                throws Exception {
            for (Task p : personsToAdd) {
                model.addTask(p);
            }
        }

        /**
         * Generates a list of Persons based on the flags.
         */
        private List<Task> generateTaskList(int numGenerated) throws Exception {
            List<Task> persons = new ArrayList<>();
            for (int i = 1; i <= numGenerated; i++) {
                persons.add(generateEventTaskWithSeed(i));
            }
            return persons;
        }

        private List<Task> generateTaskList(Task... tasks) {
            return Arrays.asList(tasks);
        }

        /**
         * Generates a Task object with given name. Other fields will have some
         * dummy values.
         */
        private Task generateTaskWithName(String name) throws Exception {
            return new EventTask(new Name(name), new Deadline("010117"),
                    new Timestamp("010117", "0000 to 1300"), new Frequency("-"),
                    new UniqueTagList(new Tag(new TagName("tag"),
                            new TagColorIndex("2"))),
                    new Status("incomplete"));
        }

        // @@author A0138493W
        /**
         * Generates a Task object with given name and deadline Other fields
         * will have some dummy values.
         */
        private Task generateDeadlineTask(String name, String deadline)
                throws Exception {
            return new EventTask(new Name(name), new Deadline(deadline),
                    new Timestamp(deadline, "0000 to 1300"), new Frequency("-"),
                    new UniqueTagList(new Tag(new TagName("tag"),
                            new TagColorIndex("2"))),
                    new Status("incomplete"));
        }

        /**
         * Generates a Task object with given name and deadline Other fields
         * will have some dummy values.
         */
        private Task generateTaskwithTags(String name, UniqueTagList tags)
                throws Exception {
            return new EventTask(new Name(name), new Deadline("150317"),
                    new Timestamp("150317", "0000 to 1300"), new Frequency("-"),
                    tags, new Status("incomplete"));
        }
    }

}

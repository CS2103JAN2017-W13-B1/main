//@@author A0139996A
package staging.guitests.handle;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import guitests.GuiRobot;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import utask.TestApp;
import utask.model.task.EventTask;
import utask.model.task.ReadOnlyTask;
import utask.testutil.TestUtil;

/**
 * Provides a handle for the panel containing the person list.
 */
public class TaskListPanelHandle extends GuiHandle {

    public static final int NOT_FOUND = -1;
    public static final String CARD_PANE_ID = "#cardPane";

    private static final String DUE_LIST_VIEW_ID = "#lstDue";
    private static final String TODAY_LIST_VIEW_ID = "#lstToday";
    private static final String TOMORROW_LIST_VIEW_ID = "#lstTomorrow";
    private static final String FUTURE_LIST_VIEW_ID = "#lstFuture";

    public TaskListPanelHandle(GuiRobot guiRobot, Stage primaryStage) {
        super(guiRobot, primaryStage, TestApp.APP_TITLE);
    }

    public List<ReadOnlyTask> getSelectedTasks() {
        ListView<ReadOnlyTask> tasks = getInSelectionListView();
        return tasks.getSelectionModel().getSelectedItems();
    }

    public ListView<ReadOnlyTask> getDueListView() {
        return getNode(DUE_LIST_VIEW_ID);
    }

    public ListView<ReadOnlyTask> getTodayListView() {
        return getNode(TODAY_LIST_VIEW_ID);
    }

    public ListView<ReadOnlyTask> getTomorrowListView() {
        return getNode(TOMORROW_LIST_VIEW_ID);
    }

    public ListView<ReadOnlyTask> getFutureListView() {
        return getNode(FUTURE_LIST_VIEW_ID);
    }

    public ListView<ReadOnlyTask> getInSelectionListView() {
        ListView<ReadOnlyTask> list;

        list = getDueListView();

        if (doesListViewHasSelection(list)) {
            return list;
        }

        list = getTodayListView();

        if (doesListViewHasSelection(list)) {
            return list;
        }

        list = getTomorrowListView();

        if (doesListViewHasSelection(list)) {
            return list;
        }

        list = getFutureListView();

        if (doesListViewHasSelection(list)) {
            return list;
        }

        return null;
    }

    private boolean doesListViewHasSelection(ListView<ReadOnlyTask> list) {
        return list.getSelectionModel().getSelectedIndex() != -1;
    }

    /**
     * Returns true if the list is showing the person details correctly and in correct order.
     * @param persons A list of person in the correct order.
     */
    public boolean isListMatching(ReadOnlyTask... persons) {
        return this.isListMatching(0, persons);
    }

    /**
     * Returns true if the list is showing the person details correctly and in correct order.
     * @param startPosition The starting position of the sub list.
     * @param persons A list of person in the correct order.
     */
    public boolean isListMatching(int startPosition, ReadOnlyTask... persons) throws IllegalArgumentException {
        if (persons.length + startPosition != getInSelectionListView().getItems().size()) {
            throw new IllegalArgumentException("List size mismatched\n" +
                    "Expected " + (getInSelectionListView().getItems().size() - 1) + " persons");
        }
        assertTrue(this.containsInOrder(startPosition, persons));
        for (int i = 0; i < persons.length; i++) {
            final int scrollTo = i + startPosition;
            guiRobot.interact(() -> getInSelectionListView().scrollTo(scrollTo));
            guiRobot.sleep(200);
//            if (!TestUtil.compareCardAndTask(getPersonCardHandle(startPosition + i), persons[i])) {
//                return false;
//            }
        }
        return true;
    }

    /**
     * Clicks on the ListView.
     */
    public void clickOnListView() {
        Point2D point = TestUtil.getScreenMidPoint(getInSelectionListView());
        guiRobot.clickOn(point.getX(), point.getY());
    }

    /**
     * Returns true if the {@code persons} appear as the sub list (in that order) at position {@code startPosition}.
     */
    public boolean containsInOrder(int startPosition, ReadOnlyTask... persons) {
        List<ReadOnlyTask> personsInList = getInSelectionListView().getItems();

        // Return false if the list in panel is too short to contain the given list
        if (startPosition + persons.length > personsInList.size()) {
            return false;
        }

        // Return false if any of the persons doesn't match
        for (int i = 0; i < persons.length; i++) {
            if (!personsInList.get(startPosition + i).getName().fullName.equals(persons[i].getName().fullName)) {
                return false;
            }
        }

        return true;
    }

    public TaskCardHandle navigateToPerson(String name) {
        guiRobot.sleep(500); //Allow a bit of time for the list to be updated
        final Optional<ReadOnlyTask> person = getInSelectionListView().getItems().stream()
                                                    .filter(p -> p.getName().fullName.equals(name))
                                                    .findAny();
        if (!person.isPresent()) {
            throw new IllegalStateException("Name not found: " + name);
        }

        return navigateToPerson(person.get());
    }

    /**
     * Navigates the listview to display and select the person.
     */
    public TaskCardHandle navigateToPerson(ReadOnlyTask person) {
        int index = getPersonIndex(person);

        guiRobot.interact(() -> {
            getInSelectionListView().scrollTo(index);
            guiRobot.sleep(150);
            getInSelectionListView().getSelectionModel().select(index);
        });
        guiRobot.sleep(100);
        return getPersonCardHandle(person);
    }


    /**
     * Returns the position of the person given, {@code NOT_FOUND} if not found in the list.
     */
    public int getPersonIndex(ReadOnlyTask targetPerson) {
        List<ReadOnlyTask> personsInList = getInSelectionListView().getItems();
        for (int i = 0; i < personsInList.size(); i++) {
            if (personsInList.get(i).getName().equals(targetPerson.getName())) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * Gets a person from the list by index
     */
    public ReadOnlyTask getPerson(int index) {
        return getInSelectionListView().getItems().get(index);
    }

    public TaskCardHandle getPersonCardHandle(int index) {
        return getPersonCardHandle(new EventTask(getInSelectionListView().getItems().get(index)));
    }

    public TaskCardHandle getPersonCardHandle(ReadOnlyTask person) {
        Set<Node> nodes = getAllCardNodes();
        Optional<Node> personCardNode = nodes.stream()
                .filter(n -> new TaskCardHandle(guiRobot, primaryStage, n).isSameCard(person))
                .findFirst();
        if (personCardNode.isPresent()) {
            return new TaskCardHandle(guiRobot, primaryStage, personCardNode.get());
        } else {
            return null;
        }
    }

    protected Set<Node> getAllCardNodes() {
        return guiRobot.lookup(CARD_PANE_ID).queryAll();
    }

    public int getNumberOfPeople() {
        return getInSelectionListView().getItems().size();
    }
}

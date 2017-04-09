package utask.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.animation.TranslateTransition;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.util.Duration;
import utask.commons.core.LogsCenter;
import utask.commons.events.ui.FindRequestEvent;
import utask.commons.events.ui.JumpToListInFindTaskOverlayEvent;
import utask.commons.events.ui.KeyboardEscapeKeyPressedEvent;
import utask.commons.events.ui.ShowTaskOfInterestInFindTaskOverlayEvent;
import utask.commons.events.ui.UpdateSortInFindTaskOverlayEvent;
import utask.commons.util.FxViewUtil;
import utask.logic.Logic;
import utask.model.task.ReadOnlyTask;

//@@author A0139996A
/*
 * FindTaskOverlay handles the UI Logic of find tableview,
 * which includes display (rendering of data to the correct column) and sort.
 * */
public class FindTaskOverlay extends UiPart<Region> {

    private static final String ASCENDING_ORDER = "asc";
    private static final String DESCENDING_ORDER = "dsc";
    private static final Logger logger = LogsCenter.getLogger(FindTaskOverlay.class);
    private static final String FXML = "FindTaskOverlay.fxml";

    private static final int SEARCHPANE_HIDDEN_X_POS = -3000;
    private final TranslateTransition openTransitionEffect = new TranslateTransition(new Duration(350), getRoot());
    private final TranslateTransition closeTransitionEffect = new TranslateTransition(new Duration(350), getRoot());

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView<ReadOnlyTask> searchTable;

    @FXML
    private TableColumn<ReadOnlyTask, Number> columnIndex;

    @FXML
    private TableColumn<ReadOnlyTask, String> columnName;

    @FXML
    private TableColumn<ReadOnlyTask, String> columnDeadline;

    @FXML
    private TableColumn<ReadOnlyTask, String> columnComplete;

    @FXML
    private TableColumn<ReadOnlyTask, String> columnTimestamp;

    @FXML
    private TableColumn<ReadOnlyTask, String> columnFrequency;

    @FXML
    private TableColumn<ReadOnlyTask, String> columnTag;

    private ObservableList<ReadOnlyTask> masterData;

    private boolean isSearchOverlayShown = false;

    private FilteredList<ReadOnlyTask> filteredData;
    private Logic logic;

    public FindTaskOverlay(Pane parent, Logic logic) {
        super(FXML);

        assert(parent != null && logic != null);
        this.logic = logic;

        masterData = logic.getFilteredTaskList();
        initialize();
        registerAsAnEventHandler(this);
        rootPane.setTranslateX(SEARCHPANE_HIDDEN_X_POS);
        FxViewUtil.applyAnchorBoundaryParameters(rootPane, 0.0, 0.0, 0.0, 0.0);
        FxViewUtil.applyAnchorBoundaryParameters(searchTable, 0.0, 0.0, 0.0, 0.0);
        parent.getChildren().add(rootPane);
    }

    private void initialize() {
        //Initialize the columns.
        addCellFactoriesToColumn();

        //Wrap the ObservableList in a FilteredList (initially display all data).
        filteredData = new FilteredList<>(masterData, p -> true);

        //Wrap the FilteredList in a SortedList.
        SortedList<ReadOnlyTask> sortedData = new SortedList<>(filteredData);

        //Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(searchTable.comparatorProperty());

        //Add sorted (and filtered) data to the table.
        searchTable.setItems(sortedData);
    }

    private void addCellFactoriesToColumn() {
        columnIndex.setCellValueFactory(cellData-> new ReadOnlyObjectWrapper<Number>(
                                    searchTable.getItems().indexOf(cellData.getValue()) + 1));
        columnName.setCellValueFactory(t -> new ReadOnlyStringWrapper(t.getValue().getName().fullName));
        columnComplete.setCellValueFactory(t -> new ReadOnlyStringWrapper(t.getValue().getStatus().toString()));
        columnDeadline.setCellValueFactory(t -> new ReadOnlyStringWrapper(t.getValue().getDeadline().toString()));
        columnTimestamp.setCellValueFactory(t -> new ReadOnlyStringWrapper(t.getValue().getTimestamp().toString()));
        columnFrequency.setCellValueFactory(t -> new ReadOnlyStringWrapper(t.getValue().getFrequency().value));
        columnTag.setCellValueFactory(t -> new ReadOnlyStringWrapper(t.getValue().getTags().getAllTagNames()));
        columnIndex.setSortable(false);
    }

    private void sort(String columnAlphabet, String orderBy) {
        TableColumn<ReadOnlyTask, String> column = getColumnToSortFromStringColumnAlphabet(columnAlphabet);
        SortType sortType = getSortTypeFromStringOrderBy(orderBy);
        sort(column, sortType);
    }

    private void sort(TableColumn<ReadOnlyTask, String> column, SortType sortOrder) {
        searchTable.getSortOrder().clear();
        column.setSortType(sortOrder);
        searchTable.getSortOrder().add(column);
    }

    /*
     * Gets the first alphabet letter in the TableView column which is in the FXML file
     * This prevent the use of magic numbers or constants
     * */
    private String getColumnAlphabetOfTableColumn(TableColumn<ReadOnlyTask, String> column) {
        String columnName = column.getText();
        assert !columnName.isEmpty();

        return column.getText().substring(0, 1); //Substring to get first letter
    }

    //Dynamically find TableColumn based on the first alphabet in the display FXML
    private TableColumn<ReadOnlyTask, String> getColumnToSortFromStringColumnAlphabet(String columnAlphabet) {

        if (columnAlphabet.equals(getColumnAlphabetOfTableColumn(columnName))) {
            return columnName;
        } else if (columnAlphabet.equals(getColumnAlphabetOfTableColumn(columnComplete))) {
            return columnComplete;
        } else if (columnAlphabet.equals(getColumnAlphabetOfTableColumn(columnDeadline))) {
            return columnDeadline;
        } else if (columnAlphabet.equals(getColumnAlphabetOfTableColumn(columnTimestamp))) {
            return columnTimestamp;
        } else if (columnAlphabet.equals(getColumnAlphabetOfTableColumn(columnFrequency))) {
            return columnFrequency;
        } else if (columnAlphabet.equals(getColumnAlphabetOfTableColumn(columnTag))) {
            return columnTag;
        }

        assert false : "Incorrect Usage. Column alphabet provided should be shown in the UI";
        return null;
    }

    private SortType getSortTypeFromStringOrderBy(String orderBy) {
        SortType sortType;

        switch (orderBy) {
        case DESCENDING_ORDER :
            sortType = SortType.DESCENDING;
            break;
        case ASCENDING_ORDER :
        default:
            sortType = SortType.ASCENDING;
            break;
        }
        return sortType;
    }

    public void openIfSearchIsNotShowing() {
        if (!isSearchOverlayShown) {
            openTransitionEffect.setToX(0);
            openTransitionEffect.play();
            isSearchOverlayShown = true;
        }
    }

    public void closeIfSearchIsShowing() {
        if (isSearchOverlayShown) {
            closeTransitionEffect.setToX(SEARCHPANE_HIDDEN_X_POS);
            closeTransitionEffect.play();
            isSearchOverlayShown = false;
            logic.setIfFindOverlayShowing(false);
        }
    }

    @Subscribe
    private void handleFindRequestEvent(FindRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        openIfSearchIsNotShowing();
    }

    @Subscribe
    private void handleKeyboardEscapeKeyPressedEvent(KeyboardEscapeKeyPressedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        closeIfSearchIsShowing();
    }

    @Subscribe
    private void handleShowTaskOfInterestInFindTaskOverlayEvent(ShowTaskOfInterestInFindTaskOverlayEvent event) {
        assert isSearchOverlayShown : "This event should only be propagated when find overlay is showing";

        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        int index = searchTable.getItems().indexOf(event.task);
        scrollTo(index);
    }

    @Subscribe
    private void handleUpdateSortInFindTaskOverlayEvent(UpdateSortInFindTaskOverlayEvent event) {
        assert isSearchOverlayShown : "This event should only be propagated when find overlay is showing";

        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        sort(event.columnAlphabet, event.orderBy);
    }

    @Subscribe
    private void handleJumpToListInFindTaskOverlayEvent(JumpToListInFindTaskOverlayEvent event) {
        assert isSearchOverlayShown : "This event should only be propagated when find overlay is showing";
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        scrollTo(event.targetIndex);
    }

    private void scrollTo(int index) {
        searchTable.scrollTo(index);
        searchTable.getSelectionModel().select(index);
    }
}

package utask.staging.ui;

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
import utask.commons.util.FxViewUtil;
import utask.logic.Logic;
import utask.model.task.ReadOnlyTask;
import utask.staging.ui.events.KeyboardEscapeKeyPressedEvent;
import utask.staging.ui.events.SearchRequestEvent;

public class UTSearchTaskOverlay extends StagingUiPart<Region> {

    private static final Logger logger = LogsCenter.getLogger(UTSearchTaskOverlay.class);
    private static final String FXML = "UTSearchTaskOverlay.fxml";

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
    private TableColumn<ReadOnlyTask, String> columnTimestamp;

    @FXML
    private TableColumn<ReadOnlyTask, String> columnFrequency;

    @FXML
    private TableColumn<ReadOnlyTask, String> columnTag;

    private ObservableList<ReadOnlyTask> masterData;

    private Pane parent;
    private boolean isSearchOverlayShown = false;

    private FilteredList<ReadOnlyTask> filteredData;
    private Logic logic;

    public UTSearchTaskOverlay(Pane parent, Logic logic) {
        super(FXML);

        assert(parent != null & logic != null);
        this.parent = parent;
        this.logic = logic;

        populateData();
        initialize();
        registerAsAnEventHandler(this);
    }

    private void populateData() {
//        masterData = FXCollections.observableArrayList();
//        masterData.add(new ReadOnlyObservableTaskTemp("Hans", "Muster"));
        masterData = logic.getFilteredTaskList();
    }

    private void initialize() {
        rootPane.setTranslateX(SEARCHPANE_HIDDEN_X_POS);

        //Initialize the columns.

        //TODO: Try bean property
        //columnName.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());

        columnIndex.setCellValueFactory(cellData-> new ReadOnlyObjectWrapper<Number>(
                searchTable.getItems().indexOf(cellData.getValue()) + 1));
        columnName.setCellValueFactory(t -> new ReadOnlyStringWrapper(t.getValue().getName().fullName));
        columnDeadline.setCellValueFactory(t -> new ReadOnlyStringWrapper(t.getValue().getDeadline().value));
        columnTimestamp.setCellValueFactory(t -> new ReadOnlyStringWrapper(t.getValue().getTimestamp().value));
        columnFrequency.setCellValueFactory(t -> new ReadOnlyStringWrapper(t.getValue().getFrequency().value));
        columnTag.setCellValueFactory(t -> new ReadOnlyStringWrapper(t.getValue().getTags().getAllTagNames()));

        //TODO: Add isDone binding after merged with master

        columnIndex.setSortable(false);

        //Wrap the ObservableList in a FilteredList (initially display all data).
        filteredData = new FilteredList<>(masterData, p -> true);

        //Wrap the FilteredList in a SortedList.
        SortedList<ReadOnlyTask> sortedData = new SortedList<>(filteredData);

        //Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(searchTable.comparatorProperty());

        //Add sorted (and filtered) data to the table.
        searchTable.setItems(sortedData);
        FxViewUtil.applyAnchorBoundaryParameters(rootPane, 0.0, 0.0, 0.0, 0.0);
        FxViewUtil.applyAnchorBoundaryParameters(searchTable, 0.0, 0.0, 0.0, 0.0);
        parent.getChildren().add(rootPane);
    }

    public void sort(TableColumn<ReadOnlyTask, String> column) {
        sort(column, SortType.ASCENDING);
    }

    public void sort(TableColumn<ReadOnlyTask, String> column, SortType sortOrder) {
        searchTable.getSortOrder().clear();
        columnName.setSortType(sortOrder);
        searchTable.getSortOrder().addAll(column);
    }

    public void filterResultsByKeywords(FilteredList<ReadOnlyTask> filteredData, String keywords) {
        filteredData.setPredicate(task -> {
            // If filter text is empty, display all persons.
            if (keywords == null || keywords.isEmpty()) {
                return true;
            }

            // Compare first name and last name of every person with filter text.
            String lowerCaseFilter = keywords.toLowerCase();

//            if (task.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
//                return true; // Filter matches first name.
//            } else if (task.getLastName().toLowerCase().contains(lowerCaseFilter)) {
//                return true; // Filter matches last name.
//            }

            //TODO: Build comprehensive search
            return task.getName().fullName.toLowerCase().contains(lowerCaseFilter) ||
            task.getTags().getAllTagNames().toLowerCase().contains(lowerCaseFilter);

            //return false; // Does not match.
        });
    }

    public void delete() {
        ReadOnlyTask remove = searchTable.getSelectionModel().getSelectedItem();

        if (remove != null) {
            masterData.remove(remove);
            //TODO: Do actual remove
        }
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
        }
    }

    @Subscribe
    private void handleSearchRequestEvent(SearchRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        filterResultsByKeywords(filteredData, event.searchKeywords.trim());
        openIfSearchIsNotShowing();
    }

    @Subscribe
    private void handleKeyboardEscapeKeyPressedEvent(KeyboardEscapeKeyPressedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        closeIfSearchIsShowing();
    }
}

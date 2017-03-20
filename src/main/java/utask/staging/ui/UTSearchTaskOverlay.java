package utask.staging.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.animation.TranslateTransition;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.util.Duration;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.FxViewUtil;
import utask.staging.ui.events.KeyboardEscapeKeyPressedEvent;
import utask.staging.ui.events.SearchRequestEvent;

public class UTSearchTaskOverlay extends StagingUiPart<Region> {

    private static final Logger logger = LogsCenter.getLogger(UTSearchTaskOverlay.class);
    private static final String FXML = "UTSearchTaskOverlay.fxml";

    private static final int SEARCHPANE_HIDDEN_Y_POS = -3000;
    private final TranslateTransition openTransitionEffect = new TranslateTransition(new Duration(350), getRoot());
    private final TranslateTransition closeTransitionEffect = new TranslateTransition(new Duration(350), getRoot());

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField filterField;
    @FXML
    private TableView<ReadOnlySearchTask> personTable;
    @FXML
    private TableColumn<ReadOnlySearchTask, Number> indexColumn;
    @FXML
    private TableColumn<ReadOnlySearchTask, String> firstNameColumn;
    @FXML
    private TableColumn<ReadOnlySearchTask, String> lastNameColumn;

    private ObservableList<ReadOnlySearchTask> masterData;

    private Pane parent;
    private boolean isSearchOverlayShown = false;

//    private static UTSearchTaskOverlay instance;

    private FilteredList<ReadOnlySearchTask> filteredData;

    public UTSearchTaskOverlay(Pane parent) {
        super(FXML);
        this.parent = parent;

        populateData();
        initialize();
        registerAsAnEventHandler(this);
    }

    private void populateData() {
        masterData = FXCollections.observableArrayList();
        masterData.add(new ReadOnlySearchTask("Hans", "Muster"));
        masterData.add(new ReadOnlySearchTask("Ruth", "Mueller"));
        masterData.add(new ReadOnlySearchTask("Heinz", "Kurz"));
        masterData.add(new ReadOnlySearchTask("Cornelia", "Meier"));
        masterData.add(new ReadOnlySearchTask("Werner", "Meyer"));
        masterData.add(new ReadOnlySearchTask("Lydia", "Kunz"));
        masterData.add(new ReadOnlySearchTask("Anna", "Best"));
        masterData.add(new ReadOnlySearchTask("Stefan", "Meier"));
        masterData.add(new ReadOnlySearchTask("Martin", "Mueller"));
    }

//    public static UTSearchTaskOverlay getInstance(Pane parent) {
//        if (instance == null) {
//            instance = new UTSearchTaskOverlay(parent);
//        }
//
//        return instance;
//    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     * Initializes the table columns and sets up sorting and filtering.
     */
    private void initialize() {
        rootPane.setTranslateY(SEARCHPANE_HIDDEN_Y_POS);
        // 0. Initialize the columns.
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        indexColumn.setCellValueFactory(cellData-> new ReadOnlyObjectWrapper<Number>(personTable.getItems().indexOf(
                                        cellData.getValue()) + 1));
        indexColumn.setSortable(false);

        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        filteredData = new FilteredList<>(masterData, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
//        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
//            filteredData.setPredicate(task -> {
//                // If filter text is empty, display all persons.
//                if (newValue == null || newValue.isEmpty()) {
//                    return true;
//                }
//
//                // Compare first name and last name of every person with filter text.
//                String lowerCaseFilter = newValue.toLowerCase();
//
//                if (task.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
//                    return true; // Filter matches first name.
//                } else if (task.getLastName().toLowerCase().contains(lowerCaseFilter)) {
//                    return true; // Filter matches last name.
//                }
//                return false; // Does not match.
//            });
//        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<ReadOnlySearchTask> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(personTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        personTable.setItems(sortedData);
        FxViewUtil.applyAnchorBoundaryParameters(rootPane, 0.0, 0.0, 0.0, 0.0);
        FxViewUtil.applyAnchorBoundaryParameters(personTable, 0.0, 0.0, 0.0, 0.0);
        parent.getChildren().add(rootPane);
    }

    public void sort(TableColumn<ReadOnlySearchTask, String> column) {
        sort(column, SortType.ASCENDING);
    }

    public void sort(TableColumn<ReadOnlySearchTask, String> column, SortType sortOrder) {
        personTable.getSortOrder().clear();
        firstNameColumn.setSortType(sortOrder);
        personTable.getSortOrder().addAll(column);
    }

    public void filterResultsByKeywords(FilteredList<ReadOnlySearchTask> filteredData, String keywords) {
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

            return task.getFirstName().toLowerCase().contains(lowerCaseFilter) ||
            task.getLastName().toLowerCase().contains(lowerCaseFilter);
            //return false; // Does not match.
        });
    }

    public void delete() {
        ReadOnlySearchTask remove = personTable.getSelectionModel().getSelectedItem();

        if (remove != null) {
            masterData.remove(remove);
        }
    }

    public void openIfSearchIsNotShowing() {
        if (!isSearchOverlayShown) {
            openTransitionEffect.setToY(0);
            openTransitionEffect.play();
            isSearchOverlayShown = true;
        }
    }

    public void closeIfSearchIsShowing() {
        if (isSearchOverlayShown) {
            closeTransitionEffect.setToY(SEARCHPANE_HIDDEN_Y_POS);
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

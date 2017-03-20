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
import utask.commons.core.LogsCenter;
import utask.commons.util.FxViewUtil;
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
    private TableView<ReadOnlyObservableTask> personTable;
    @FXML
    private TableColumn<ReadOnlyObservableTask, Number> indexColumn;
    @FXML
    private TableColumn<ReadOnlyObservableTask, String> firstNameColumn;
    @FXML
    private TableColumn<ReadOnlyObservableTask, String> lastNameColumn;

    private ObservableList<ReadOnlyObservableTask> masterData;

    private Pane parent;
    private boolean isSearchOverlayShown = false;

//    private static UTSearchTaskOverlay instance;

    private FilteredList<ReadOnlyObservableTask> filteredData;

    public UTSearchTaskOverlay(Pane parent) {
        super(FXML);
        this.parent = parent;

        populateData();
        initialize();
        registerAsAnEventHandler(this);
    }

    private void populateData() {
        masterData = FXCollections.observableArrayList();
        masterData.add(new ReadOnlyObservableTask("Hans", "Muster"));
    }

//    public static UTSearchTaskOverlay getInstance(Pane parent) {
//        if (instance == null) {
//            instance = new UTSearchTaskOverlay(parent);
//        }
//
//        return instance;
//    }

    private void initialize() {
        rootPane.setTranslateY(SEARCHPANE_HIDDEN_Y_POS);

        //Initialize the columns.
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        indexColumn.setCellValueFactory(cellData-> new ReadOnlyObjectWrapper<Number>(personTable.getItems().indexOf(
                                        cellData.getValue()) + 1));
        indexColumn.setSortable(false);

        //Wrap the ObservableList in a FilteredList (initially display all data).
        filteredData = new FilteredList<>(masterData, p -> true);

        //Wrap the FilteredList in a SortedList.
        SortedList<ReadOnlyObservableTask> sortedData = new SortedList<>(filteredData);

        //Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(personTable.comparatorProperty());

        //Add sorted (and filtered) data to the table.
        personTable.setItems(sortedData);
        FxViewUtil.applyAnchorBoundaryParameters(rootPane, 0.0, 0.0, 0.0, 0.0);
        FxViewUtil.applyAnchorBoundaryParameters(personTable, 0.0, 0.0, 0.0, 0.0);
        parent.getChildren().add(rootPane);
    }

    public void sort(TableColumn<ReadOnlyObservableTask, String> column) {
        sort(column, SortType.ASCENDING);
    }

    public void sort(TableColumn<ReadOnlyObservableTask, String> column, SortType sortOrder) {
        personTable.getSortOrder().clear();
        firstNameColumn.setSortType(sortOrder);
        personTable.getSortOrder().addAll(column);
    }

    public void filterResultsByKeywords(FilteredList<ReadOnlyObservableTask> filteredData, String keywords) {
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
        ReadOnlyObservableTask remove = personTable.getSelectionModel().getSelectedItem();

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

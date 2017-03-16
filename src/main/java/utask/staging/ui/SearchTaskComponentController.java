package utask.staging.ui;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.util.Callback;

public class SearchTaskComponentController extends StagingUiPart<Region> {

    private static final String FXML = "SearchTaskComponent.fxml";

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
    
//    public SearchTaskComponentController(Pane parent, Keyboard keyboard){
//        
//    }

    /**
     * Just add some sample data in the constructor.
     */
    public SearchTaskComponentController(Pane parent) {
        super(FXML);
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

        this.parent = parent;

        initialize();
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     * Initializes the table columns and sets up sorting and filtering.
     */
    private void initialize() {
        System.out.println(masterData == null);

        // 0. Initialize the columns.
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());        
        indexColumn.setCellValueFactory(cellData-> new ReadOnlyObjectWrapper<Number>(personTable.getItems().indexOf(cellData.getValue()) + 1));        
        indexColumn.setSortable(false);
        
        
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<ReadOnlySearchTask> filteredData = new FilteredList<>(masterData, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(task -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (task.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (task.getLastName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<ReadOnlySearchTask> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(personTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        personTable.setItems(sortedData);
        parent.getChildren().add(rootPane);
    }
}

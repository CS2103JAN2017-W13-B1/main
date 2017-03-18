package utask.staging.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.address.commons.core.Config;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.events.ui.ExitAppRequestEvent;
import seedu.address.commons.util.FxViewUtil;
import seedu.address.logic.Logic;
import seedu.address.model.UserPrefs;

/**
 * The Main Window. Provides the basic application layout containing a menu bar
 * and space where other JavaFX elements can be placed.
 */
public class StagingMainWindow extends StagingUiPart<Region> {

    private static final String ICON = "/images/address_book_32.png";
    private static final String FXML = "UTMainWindow.fxml";
    private static final int MIN_HEIGHT = 600;
    private static final int MIN_WIDTH = 450;

    private Stage primaryStage;
    // private Logic logic;

    // Independent Ui parts residing in this Ui container
    // private BrowserPanel browserPanel;
    // private PersonListPanel personListPanel;
    // private Config config;

    @FXML
    private JFXListView<Label> lstViewTask;

    @FXML
    private JFXTextField txtCommand;

    @FXML
    private HBox hBoxSuggestion;

    @FXML
    private Label lblSuggestion;

    @FXML
    private JFXTextArea txtAreaResults;

    @FXML
    private SplitPane topPlaceholder;

    @FXML
    private VBox personList;

    @FXML
    private AnchorPane personListPanelPlaceholder;

    @FXML
    private AnchorPane todoListPanelPlaceholder;
    
    @FXML
    void txtEventOnKeyReleased(KeyEvent event) {
        String text = txtCommand.getText();

        if (!"".equals(text)) {
            String command = (text.toLowerCase().split(" "))[0];
            switch (command) {
            case "add":
                lblSuggestion.setText("add NAME ... .. . . . .");
                break;
            case "edit":
                lblSuggestion.setText("edit INDEX . . .. . . . ");
                break;
            }
        }
        // bar.close();
        // bar.enqueue(new SnackbarEvent(keywords));
        // HBox.setHgrow(bar, Priority.ALWAYS);;
        // bar.setMinWidth(hBoxSuggestion.getWidth());
        // bar.enqueue(new SnackbarEvent("Notification Msg"))
    }

    private String previousCommand = "";
    private static final Pattern searchRegex = Pattern.compile("search\\s(?<txt>\\S+)");

    @FXML
    void txtEventOnKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.UP) {
            txtCommand.setText(previousCommand);
            lblSuggestion.setText("");
        } else if (event.getCode() == KeyCode.DOWN) {
            txtCommand.setText(lblSuggestion.getText());
            lblSuggestion.setText("");
        } else if (event.getCode() == KeyCode.ENTER) {
            String text = txtCommand.getText();

            final Matcher matcher = searchRegex.matcher(text);

            if (matcher.matches()) {
                // search.fliter(matcher.group("txt").toLowerCase());
                // search.overlay();
                stask.play();
            }
            // } else if (search.isSearchActive() &&
            // text.matches("^select\\s\\d+$")) {
            // String params = (text.toLowerCase().split(" "))[1];
            // int index = Integer.parseInt(params);
            // ReadOnlyTask task = search.selectIndex(index);
            // txtAreaResults.appendText("GOTTEN >> " + task.name + " " +
            // task.date + " " + task.tags);
            // } else if (text.matches("^sort\\s\\d+$")) {
            // String params = (text.toLowerCase().split(" "))[1];
            // int index = Integer.parseInt(params);
            // search.sort(index);
            // }

            txtAreaResults.appendText(txtCommand.getText() + "\n");
            previousCommand = txtCommand.getText();
            txtCommand.setText("");
            lblSuggestion.setText("");
        } else if (event.getCode() == KeyCode.ESCAPE) {
            // if (search.handleEscape()) {
            // task.setOverlay();
            // }
            stask.done();
        }
    }

    JFXSnackbar bar;

    private TaskListPanel task;
    private SearchResultsAnchorPane search;
    private SearchTaskComponentController stask;

    public StagingMainWindow(Stage primaryStage, Config config, UserPrefs prefs, Logic logic) {
        super(FXML);

        // Set dependencies
        this.primaryStage = primaryStage;
        // this.logic = logic;
        // this.config = config;

        // Configure the UI
        setTitle(config.getAppTitle());
        setIcon(ICON);
        setWindowMinSize();
        setWindowDefaultSize(prefs);
        // Scene scene = new Scene(getRoot());
        // primaryStage.setScene(new Scene(getRoot()));

        // TODO!!!

        boolean isMacOS = System.getProperty("os.name") == "Mac";

        JFXDecorator decorator = new JFXDecorator(this.primaryStage, getRoot(), isMacOS, true, true);
        decorator.setPrefSize(800, 600);
        decorator.setCustomMaximize(true);

        Scene scene = new Scene(decorator);
        scene.getStylesheets().add(StagingMainWindow.class.getResource("/css/jfoenix-fonts.css").toExternalForm());
        scene.getStylesheets().add(StagingMainWindow.class.getResource("/css/jfoenix-design.css").toExternalForm());
        scene.getStylesheets().add(StagingMainWindow.class.getResource("/css/jfoenix-main-demo.css").toExternalForm());

        // search = new SearchResultsAnchorPane(topPlaceholder);

        // stask = new SearchTaskComponentController(topPlaceholder);
        this.primaryStage.setMinWidth(700);
        this.primaryStage.setMinHeight(800);
        this.primaryStage.setScene(scene);
        this.primaryStage.show();
        // decorator.setCustomMaximize(true);
        // this.primaryStage.setMinWidth(1000);
        // this.primaryStage.setMinHeight(800);
        // setAccelerators();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    // private void setAccelerators() {
    // setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    // }

    /**
     * Sets the accelerator of a MenuItem.
     * 
     * @param keyCombination
     *            the KeyCombination value of the accelerator
     */
    // private void setAccelerator(MenuItem menuItem, KeyCombination
    // keyCombination) {
    // menuItem.setAccelerator(keyCombination);
    //
    // /*
    // * TODO: the code below can be removed once the bug reported here
    // * https://bugs.openjdk.java.net/browse/JDK-8131666
    // * is fixed in later version of SDK.
    // *
    // * According to the bug report, TextInputControl (TextField, TextArea)
    // will
    // * consume function-key events. Because CommandBox contains a TextField,
    // and
    // * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
    // * not work when the focus is in them because the key event is consumed by
    // * the TextInputControl(s).
    // *
    // * For now, we add following event filter to capture such key events and
    // open
    // * help window purposely so to support accelerators even when focus is
    // * in CommandBox or ResultDisplay.
    // */
    // getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
    // if (event.getTarget() instanceof TextInputControl &&
    // keyCombination.match(event)) {
    // menuItem.getOnAction().handle(new ActionEvent());
    // event.consume();
    // }
    // });
    // }

    void fillInnerParts() {
        task = new TaskListPanel(personListPanelPlaceholder);
        new TodoListPanel(todoListPanelPlaceholder);
        
        // browserPanel = new BrowserPanel(browserPlaceholder);
        // personListPanel = new PersonListPanel(getPersonListPlaceholder(),
        // logic.getFilteredPersonList());
        // new ResultDisplay(getResultDisplayPlaceholder());
        // new StatusBarFooter(getStatusbarPlaceholder(),
        // config.getAddressBookFilePath());
        // new CommandBox(getCommandBoxPlaceholder(), logic);
    }

    // private AnchorPane getCommandBoxPlaceholder() {
    // return commandBoxPlaceholder;
    // }
    //
    // private AnchorPane getStatusbarPlaceholder() {
    // return statusbarPlaceholder;
    // }
    //
    // private AnchorPane getResultDisplayPlaceholder() {
    // return resultDisplayPlaceholder;
    // }
    //
    // private AnchorPane getPersonListPlaceholder() {
    // return personListPanelPlaceholder;
    // }

    void hide() {
        primaryStage.hide();
    }

    private void setTitle(String appTitle) {
        primaryStage.setTitle(appTitle);
    }

    /**
     * Sets the given image as the icon of the main window.
     * 
     * @param iconSource
     *            e.g. {@code "/images/help_icon.png"}
     */
    private void setIcon(String iconSource) {
        FxViewUtil.setStageIcon(primaryStage, iconSource);
    }

    /**
     * Sets the default size based on user preferences.
     */
    private void setWindowDefaultSize(UserPrefs prefs) {
        primaryStage.setHeight(prefs.getGuiSettings().getWindowHeight());
        primaryStage.setWidth(prefs.getGuiSettings().getWindowWidth());
        if (prefs.getGuiSettings().getWindowCoordinates() != null) {
            primaryStage.setX(prefs.getGuiSettings().getWindowCoordinates().getX());
            primaryStage.setY(prefs.getGuiSettings().getWindowCoordinates().getY());
        }
    }

    private void setWindowMinSize() {
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMinWidth(MIN_WIDTH);
    }

    /**
     * Returns the current size and the position of the main Window.
     */
    GuiSettings getCurrentGuiSetting() {
        return new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(), (int) primaryStage.getX(),
                (int) primaryStage.getY());
    }

    // @FXML
    // public void handleHelp() {
    // HelpWindow helpWindow = new HelpWindow();
    // helpWindow.show();
    // }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        raise(new ExitAppRequestEvent());
    }

    // public PersonListPanel getPersonListPanel() {
    // return this.personListPanel;
    // }
    //
    // void loadPersonPage(ReadOnlyTask person) {
    // browserPanel.loadPersonPage(person);
    // }
    //
    // void releaseResources() {
    // browserPanel.freeResources();
    // }

}

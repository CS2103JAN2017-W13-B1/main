package utask.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;
import com.jfoenix.controls.JFXDecorator;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import utask.commons.core.Config;
import utask.commons.core.EventsCenter;
import utask.commons.core.GuiSettings;
import utask.commons.core.LogsCenter;
import utask.commons.events.ui.ExitAppRequestEvent;
import utask.commons.events.ui.UIShowAliasDialogEvent;
import utask.commons.events.ui.UIShowMessageDialogEvent;
import utask.commons.events.ui.UIShowTagColorDialogEvent;
import utask.commons.util.FxViewUtil;
import utask.logic.Logic;
import utask.model.UserPrefs;
import utask.ui.dialogs.UTAliasDialog;
import utask.ui.dialogs.UTMessageDialog;
import utask.ui.dialogs.UTTagColorDialog;

/**
 * The Main Window. Provides the basic application layout containing a menu bar
 * and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Region> {
    private static final Logger logger = LogsCenter.getLogger(MainWindow.class);
    private static final String ICON = "/images/utask.png";
    private static final String FXML = "MainWindow.fxml";
    private static final int MIN_HEIGHT = 650;
    private static final int MIN_WIDTH = 620;

    private Stage primaryStage;
    private Logic logic;
    private Config config;

    private TodoListPanel todoListPanel;

    //Independent Ui parts residing in this Ui container
    @FXML
    private StackPane rootPane;

    @FXML
    private Pane topPlaceholder;

    @FXML
    private Pane personListPanelPlaceholder;

    @FXML
    private Pane todoListPanelPlaceholder;

    @FXML
    private Pane commandBoxPlaceholder;

    @FXML
    private Pane resultDisplayPlaceholder;

    @FXML
    private Pane statusbarPlaceholder;

    @FXML
    private Button btnHelp;

    //@@author A0139996A
    public MainWindow(Stage primaryStage, Config config, UserPrefs prefs, Logic logic) {
        super(FXML);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;
        this.config = config;

        // Configure the UI
        setTitle(config.getAppTitle());
        setIcon(ICON);

        setWindowDefaultSize(prefs);
        setWindowMinSize();

        Scene scene = createScene();
        setStyleSheets(scene);
        this.primaryStage.setScene(scene);
        setEventHandlers();
        setAccelerators();

        EventsCenter.getInstance().registerHandler(this);
    }

    /*
     * Creates Scene
     * JFXDecorator is used to create 'material' styled window frame
     * */
    private Scene createScene() {
        JFXDecorator decorator = new JFXDecorator(this.primaryStage, getRoot(), false, true, true);
        decorator.setPrefSize(MIN_WIDTH, MIN_HEIGHT);
        decorator.setCustomMaximize(true);
        Scene scene = new Scene(decorator);
        return scene;
    }

    /*
     * Sets required CSS Stylesheets to a scene
     *
     * Note that fonts CSS has to be set first
     * */
    private void setStyleSheets(Scene scene) {
        scene.getStylesheets().add(MainWindow.class.getResource("/css/jfoenix-fonts.css").toExternalForm());
        scene.getStylesheets().add(MainWindow.class.getResource("/css/jfoenix-design.css").toExternalForm());
        scene.getStylesheets().add(MainWindow.class.getResource("/css/utask.css").toExternalForm());
    }

    /**
     * Sets the accelerator of a Button
     *
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(Button control, KeyCombination keyCombination) {
        control.getScene().getAccelerators().put(keyCombination, new Runnable() {
            @Override
            public void run() {
                handleHelp();
            }
        });
    }

    private void setAccelerators() {
        setAccelerator(btnHelp, KeyCombination.valueOf("F1"));
    }

    private void setEventHandlers() {
        btnHelp.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                handleHelp();
            }
        });
    }

    void fillInnerParts() {
        new TaskListPanel(personListPanelPlaceholder, logic);
        todoListPanel = new TodoListPanel(todoListPanelPlaceholder, logic);
        new ResultDisplay(resultDisplayPlaceholder);
        new StatusBarFooter(statusbarPlaceholder, config.getUTaskFilePath());
        new CommandBox(commandBoxPlaceholder, logic);
        new FindTaskOverlay(topPlaceholder, logic);
    }

    public TodoListPanel getTodoListPanel() {
        return todoListPanel;
    }

    //@@author
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    void hide() {
        primaryStage.hide();
    }

    private void setTitle(String appTitle) {
        primaryStage.setTitle(appTitle);
    }

    /**
     * Sets the given image as the icon of the main window.
     * @param iconSource e.g. {@code "/images/help_icon.png"}
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

    @FXML
    public void handleHelp() {
        HelpWindow helpWindow = new HelpWindow();
        helpWindow.show();
    }

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

    @Subscribe
    private void handleUIShowTagColorDialogEvent(UIShowTagColorDialogEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        new UTTagColorDialog(rootPane).show(event.tags);
    }

    @Subscribe
    private void handleUIShowAliasDialogEvent(UIShowAliasDialogEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        new UTAliasDialog(rootPane).show(event.map);
    }

    @Subscribe
    private void handleUIShowMessageDialogEvent(UIShowMessageDialogEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        new UTMessageDialog(rootPane).show(event.headingText, event.contentText);
    }
}

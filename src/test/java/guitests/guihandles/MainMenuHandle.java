package guitests.guihandles;

import guitests.working.GuiRobot;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import utask.TestApp;

/**
 * Provides a handle to the main menu of the app.
 */
public class MainMenuHandle extends GuiHandle {
    public static final String HELP_BUTTON_ID = "#btnHelp";

    public MainMenuHandle(GuiRobot guiRobot, Stage primaryStage) {
        super(guiRobot, primaryStage, TestApp.APP_TITLE);
    }

//    public GuiHandle clickOn(String menuText) {
//        guiRobot.clickOn(menuItem);
//        return this;
//    }
//
//    public HelpWindowHandle openHelpWindowUsingMenu() {
//        clickOn("Help", "F1");
//        return new HelpWindowHandle(guiRobot, primaryStage);
//    }

    public HelpWindowHandle clickOnHelpButton() {
        guiRobot.clickOn(HELP_BUTTON_ID);
        return new HelpWindowHandle(guiRobot, primaryStage);
    }

    public HelpWindowHandle openHelpWindowUsingAccelerator() {
        useF1Accelerator();
        return new HelpWindowHandle(guiRobot, primaryStage);
    }

    private void useF1Accelerator() {
        guiRobot.push(KeyCode.F1);
        guiRobot.sleep(500);
    }
}

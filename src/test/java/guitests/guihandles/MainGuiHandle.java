package guitests.guihandles;

import guitests.working.GuiRobot;
import javafx.stage.Stage;
import utask.TestApp;

/**
 * Provides a handle for the main GUI.
 */
public class MainGuiHandle extends GuiHandle {

    public MainGuiHandle(GuiRobot guiRobot, Stage primaryStage) {
        super(guiRobot, primaryStage, TestApp.APP_TITLE);
    }

//    public TodoListPanelHandle getTodoListPanel() {
//        return new TodoListPanelHandle(guiRobot, primaryStage);
//    }
//
//    public TaskListPanelHandle getTaskListPanel() {
//        return new TaskListPanelHandle(guiRobot, primaryStage);
//    }

    public ListPanelHandle getListPanelHandle() {
        return new ListPanelHandle(guiRobot, primaryStage);
    }

    public ResultDisplayHandle getResultDisplay() {
        return new ResultDisplayHandle(guiRobot, primaryStage);
    }

    public CommandBoxHandle getCommandBox() {
        return new CommandBoxHandle(guiRobot, primaryStage, TestApp.APP_TITLE);
    }

    public MainMenuHandle getMainMenu() {
        return new MainMenuHandle(guiRobot, primaryStage);
    }

    public FindTaskOverlayHandle getfindOverlayList() {
        return new FindTaskOverlayHandle(guiRobot, primaryStage);
    }

    public AlertDialogHandle getAlertDialog(String title) {
        guiRobot.sleep(1000);
        return new AlertDialogHandle(guiRobot, primaryStage, title);
    }
}

package utask.ui;

import javafx.stage.Stage;
import utask.commons.core.Config;
import utask.logic.Logic;
import utask.model.UserPrefs;

//@@author A0139996A
/*
 * As JFoenix wrap Scene with a JFXDecorator, which breaks testing.
 * TestUIManager is created so that a plain scene can be loaded for testing.
 *
 * This demonstrates the use of Strategy pattern, where subclass can supply different algorithm during runtime.
 * In this case, the algorithm is how to create a Scene
 * */
public class TestUiManager extends UiManager {

    public TestUiManager(Logic logic, Config config, UserPrefs prefs) {
        super(logic, config, prefs);
    }

    @Override
    protected MainWindow createMainWindow(Stage primaryStage) {
        return new TestWindow(primaryStage, config, prefs, logic);
    }
}

package utask.ui;

import javafx.stage.Stage;
import utask.commons.core.Config;
import utask.logic.Logic;
import utask.model.UserPrefs;

public class TestUiManager extends UiManager {

    public TestUiManager(Logic logic, Config config, UserPrefs prefs) {
        super(logic, config, prefs);
    }

    @Override
    protected MainWindow createMainWindow(Stage primaryStage) {
        return new TestWindow(primaryStage, config, prefs, logic);
    }
}

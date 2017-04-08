package utask.ui;

import javafx.scene.Scene;
import javafx.stage.Stage;
import utask.commons.core.Config;
import utask.logic.Logic;
import utask.model.UserPrefs;

public class TestWindow extends MainWindow {

    public TestWindow(Stage primaryStage, Config config, UserPrefs prefs, Logic logic) {
        super(primaryStage, config, prefs, logic);
    }

    @Override
    protected Scene createScene() {
        Scene scene = new Scene(getRoot());
        primaryStage.setScene(scene);
        return scene;
    }
}

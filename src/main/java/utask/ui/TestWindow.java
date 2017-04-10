package utask.ui;

import javafx.scene.Scene;
import javafx.stage.Stage;
import utask.commons.core.Config;
import utask.logic.Logic;
import utask.model.UserPrefs;

//@@author A0139996A
/*
* As JFoenix wrap Scene with a JFXDecorator, which breaks testing.
* TestWindow is created so that a plain scene can be loaded for testing.
*
* This demonstrates the use of Strategy pattern, where subclass can supply different algorithm during runtime.
* In this case, the algorithm is how to create a Scene
* */
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

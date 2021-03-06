package utask.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import utask.commons.core.LogsCenter;
import utask.commons.util.FxViewUtil;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Region> {

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String ICON = "/images/help_icon.png";
    private static final String FXML = "HelpWindow.fxml";
    private static final String TITLE = "Help";
    private static final String USERGUIDE_URL =
            "https://cs2103jan2017-w13-b1.github.io/main/docs/UserGuide.html";

    @FXML
    private WebView browser;

    private final Stage dialogStage;

    public HelpWindow(Stage parentStage) {
        super(FXML);
        Scene scene = new Scene(getRoot());
        //As helpwindow may block events from propagating
        //parentStage passed as the parent stage to make it modal.
        dialogStage = createDialogStage(TITLE, parentStage, scene);
        dialogStage.setMaximized(true); //TODO: set a more appropriate initial size
        FxViewUtil.setStageIcon(dialogStage, ICON);

        browser.getEngine().load(USERGUIDE_URL);
        FxViewUtil.applyAnchorBoundaryParameters(browser, 0.0, 0.0, 0.0, 0.0);
    }

    public void show() {
        logger.fine("Showing help page about the application.");
        dialogStage.showAndWait();
    }
}

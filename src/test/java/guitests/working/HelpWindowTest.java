package guitests.working;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import guitests.UTaskGuiTest;
import guitests.guihandles.HelpWindowHandle;

public class HelpWindowTest extends UTaskGuiTest {

    @Test
    public void openHelpWindow() {
        //use accelerator
        commandBox.clickOnTextField();
        assertHelpWindowOpen(mainMenu.openHelpWindowUsingAccelerator());

        resultDisplay.clickOnTextArea();
        assertHelpWindowOpen(mainMenu.openHelpWindowUsingAccelerator());

        //click on help button
        assertHelpWindowOpen(mainMenu.clickOnHelpButton());

        //use command
        assertHelpWindowOpen(commandBox.runHelpCommand());
    }

    private void assertHelpWindowOpen(HelpWindowHandle helpWindowHandle) {
        assertTrue(helpWindowHandle.isWindowOpen());
        helpWindowHandle.closeWindow();
    }
}

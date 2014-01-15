package yadf.ui.gdx.screen.game.window;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

/**
 * The dialog window manager.
 */
public interface IDialogWindowManager {

    /**
     * Set the current dialog window.
     * @param dialogWindow the dialog window
     */
    void setWindow(Window dialogWindow);

    /**
     * Close the dialog window.
     * @param dialogWindow the dialog window
     */
    void closeWindow(Window dialogWindow);

    Skin getSkin();
}

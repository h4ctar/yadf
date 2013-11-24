package yadf.ui.gdx.screen.game.dialogwindow;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;

/**
 * The dialog window manager.
 */
public interface IDialogWindowManager {

    /**
     * Set the current dialog window.
     * @param dialogWindow the dialog window
     */
    void setDialogWindow(Dialog dialogWindow);

    /**
     * Close the dialog window.
     * @param dialogWindow the dialog window
     */
    void closeDialogWindow(Dialog dialogWindow);
}

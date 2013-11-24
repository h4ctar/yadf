package yadf.ui.gdx.screen.game;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * The controls controller.
 */
public interface IControlsController {

    /**
     * Sets the current controls.
     * @param controlls the current controls
     */
    void setCurrentControls(Actor controlls);

    /**
     * Cancel the current controls.
     */
    void cancelCurrentControls();
}

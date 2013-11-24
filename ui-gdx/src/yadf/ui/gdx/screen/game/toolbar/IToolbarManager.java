package yadf.ui.gdx.screen.game.toolbar;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * The controls controller.
 */
public interface IToolbarManager {

    /**
     * Sets the current controls.
     * @param toolbar the toolbar
     */
    void setToolbar(Actor toolbar);

    /**
     * Cancel the current controls.
     */
    void closeToolbar();
}

package yadf.ui.gdx.screen.game;

import com.badlogic.gdx.scenes.scene2d.Actor;

public interface IControlsController {

    void setCurrentControls(Actor controlls);

    void cancelCurrentControls();
}
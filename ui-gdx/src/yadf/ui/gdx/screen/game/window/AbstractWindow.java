package yadf.ui.gdx.screen.game.window;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public abstract class AbstractWindow extends Window {

    // TODO: inject the WindowManager to get the skin from
    
    public AbstractWindow(String title, Skin skin) {
        super(title, skin);
    }
}

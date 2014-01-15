package yadf.ui.gdx.screen.game.window;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class RoomWindow extends Dialog {

    public RoomWindow(Skin skin) {
        super("Room", skin);
        
        button("Delete");
        button("Close");
    }
}

package yadf.ui.gdx.screen.game.window;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class StockpileWindow extends Dialog {

    public StockpileWindow(Skin skin) {
        super("Stockpile", skin);
        
        button("Delete");
        button("Close");
    }
}

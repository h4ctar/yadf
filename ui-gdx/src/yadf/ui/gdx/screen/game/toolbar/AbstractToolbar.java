package yadf.ui.gdx.screen.game.toolbar;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public abstract class AbstractToolbar extends Table {

    protected Button addButton(String text, Skin skin) {
        Button button = new TextButton(text, skin);
        add(button).spaceBottom(10);
        row();
        return button;
    }
}

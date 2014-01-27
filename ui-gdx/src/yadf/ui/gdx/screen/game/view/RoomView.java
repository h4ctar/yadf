package yadf.ui.gdx.screen.game.view;

import yadf.simulation.room.Room;
import yadf.ui.gdx.screen.game.window.IDialogWindowManager;
import yadf.ui.gdx.screen.game.window.RoomWindow;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class RoomView extends EntityImageView<Room> {

    private IDialogWindowManager dialogWindowManager;

    public RoomView(Room room, TextureAtlas textureAtlas, IDialogWindowManager dialogWindowManagerTmp) {
        super(room, textureAtlas, "room");
        dialogWindowManager = dialogWindowManagerTmp;
        addListener(new RoomClickListener());
    }

    private class RoomClickListener extends ClickListener {

        @Override
        public void clicked(InputEvent event, float x, float y) {
            dialogWindowManager.setWindow(new RoomWindow(dialogWindowManager.getSkin()));
        }
    }
}

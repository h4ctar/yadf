package yadf.ui.gdx.screen.game.view;

import yadf.simulation.room.Room;
import yadf.ui.gdx.screen.game.window.IDialogWindowManager;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class RoomViewController extends AbstractViewController<Room> {

    private IDialogWindowManager dialogWindowManager;

    /** The texture atlas. */
    private TextureAtlas textureAtlas;

    public RoomViewController(final TextureAtlas textureAtlasTmp, Stage gameStage, IDialogWindowManager dialogWindowManagerTmp) {
        super(gameStage);
        textureAtlas = textureAtlasTmp;
        dialogWindowManager = dialogWindowManagerTmp;
    }

    @Override
    protected Actor createView(Room room) {
        return new RoomView(room, textureAtlas, dialogWindowManager);
    }
}

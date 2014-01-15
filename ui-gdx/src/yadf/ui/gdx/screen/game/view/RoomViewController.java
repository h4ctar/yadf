package yadf.ui.gdx.screen.game.view;

import yadf.simulation.room.Room;
import yadf.ui.gdx.screen.game.window.IDialogWindowManager;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class RoomViewController extends AbstractViewController<Room> {

    private IDialogWindowManager dialogWindowManager;

    public RoomViewController(Stage gameStage, IDialogWindowManager dialogWindowManagerTmp) {
        super(gameStage);
        dialogWindowManager = dialogWindowManagerTmp;
    }

    @Override
    protected Actor createView(Room room) {
        return new RoomView(room, dialogWindowManager);
    }
}

package yadf.ui.gdx.screen.game.view;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import yadf.simulation.room.Room;
import yadf.ui.gdx.screen.game.window.IDialogWindowManager;
import yadf.ui.gdx.screen.game.window.RoomWindow;

public class RoomView extends EntitySolidView<Room> {

    private IDialogWindowManager dialogWindowManager;

    public RoomView(Room room, IDialogWindowManager dialogWindowManagerTmp) {
        super(room);
        dialogWindowManager = dialogWindowManagerTmp;
        addListener(new RoomClickListener());
    }

    private class RoomClickListener extends ClickListener {

        @Override
        public void clicked(InputEvent event, float x, float y) {
            System.out.println("asdf");
            dialogWindowManager.setWindow(new RoomWindow(dialogWindowManager.getSkin()));
        }
    }
}

package yadf.ui.gdx.screen.game.toolbar;

import yadf.controller.AbstractController;
import yadf.simulation.IPlayer;
import yadf.simulation.room.RoomTypeManager;
import yadf.ui.gdx.screen.TileCamera;
import yadf.ui.gdx.screen.game.interactor.CreateRoomInteractor;
import yadf.ui.gdx.screen.game.interactor.IInteractor;
import yadf.ui.gdx.screen.game.interactor.IInteractorListener;
import yadf.ui.gdx.screen.game.interactor.IInteractorManager;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * The toolbar for the create room buttons.
 */
public class CreateRoomToolbar extends Table implements IInteractorListener {

    /** The controls controller. */
    private IToolbarManager controlsController;

    /** The interactor manager. */
    private IInteractorManager interactorManager;

    /** The player. */
    private IPlayer player;

    /** The camera. */
    private TileCamera camera;

    /** The controller. */
    private AbstractController controller;

    /**
     * Constructor.
     * @param skin the skin for the buttons
     * @param controlsControllerTmp the controls controller
     * @param interactorManagerTmp the interactor manager
     * @param playerTmp the player
     * @param cameraTmp the camera
     * @param controllerTmp the controller
     */
    public CreateRoomToolbar(final Skin skin, final IToolbarManager controlsControllerTmp,
            final IInteractorManager interactorManagerTmp, final IPlayer playerTmp, final TileCamera cameraTmp,
            final AbstractController controllerTmp) {
        controlsController = controlsControllerTmp;
        interactorManager = interactorManagerTmp;
        player = playerTmp;
        camera = cameraTmp;
        controller = controllerTmp;

        setFillParent(true);
        align(Align.top | Align.left);
        pad(10);

        for (String roomType : RoomTypeManager.getInstance().getRoomTypes()) {
            TextButton roomButton = new TextButton(roomType, skin);
            roomButton.addListener(new RoomButtonListener(roomType));
            add(roomButton).width(140).spaceBottom(10);
            row();
        }

        TextButton cancelButton = new TextButton("Cancel", skin);
        cancelButton.addListener(new CancelButtonListener());
        add(cancelButton).width(140);
    }

    @Override
    public void interactionDone(final IInteractor interactor) {
        controlsController.closeToolbar();
    }

    /**
     * The listener for all of the create room buttons.
     */
    private final class RoomButtonListener extends ClickListener {

        /** The type of the room. */
        private String roomType;

        /**
         * Constructor.
         * @param roomTypeTmp the type of the room
         */
        public RoomButtonListener(final String roomTypeTmp) {
            roomType = roomTypeTmp;
        }

        @Override
        public void clicked(final InputEvent event, final float x, final float y) {
            CreateRoomInteractor interactor = new CreateRoomInteractor(roomType, player, camera, controller);
            interactor.addListener(CreateRoomToolbar.this);
            interactorManager.installInteractor(interactor);
        }
    }

    /**
     * The listener for the cancel button.
     */
    private final class CancelButtonListener extends ClickListener {

        @Override
        public void clicked(final InputEvent event, final float x, final float y) {
            controlsController.closeToolbar();
        }
    }
}
